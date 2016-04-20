package com.hydrogen.flume;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.Entity;
import com.hydrogen.model.Source;
import com.hydrogen.stage.Ingestion;
import com.hydrogen.stage.Stage;

public class FlumeInterceptor implements Interceptor {

	Log log = LogFactory.getLog(FlumeInterceptor.class);

	private Source master = null;
	private String home = null;
	private List<Entity> entitylist = null;

	public FlumeInterceptor(Context context, Source master, List<Entity> entitylist) {
		home = System.getProperty("user.home");
		this.entitylist = entitylist;
		this.master = master;
	}

	public void close() {
		System.out.println("*************** closed");

	}

	private Expression exp = null;

	public void initialize() {
		log.info("Init----- " + master.getPath() + " entities " + master.getEntities());

		for (Entity entity : entitylist) {
			log.info("Init----- " + entity.getPath() + " entity pattern = " + entity.getPattern());
		}
		ExpressionParser parser = new SpelExpressionParser();
		exp = parser.parseExpression(master.getGroup_filter());
	}

	public Event intercept(Event event) {

		List<Event> events = new ArrayList<Event>();

		events.add(event);

		List<Source> evlist = process(events);

		return event;

	}

	Set<String> keylist = new HashSet<String>();

	public List<Event> intercept(List<Event> events) {

		List<Source> evlist = process(events);
		// checkToTriggerOozie(evlist);
		return events;
	}

	private synchronized List<Source> process(List<Event> events) {
		String primary_key = null;
		List<Source> evlist = new ArrayList<Source>();
		for (Event event : events) {

			Map<String, String> map = event.getHeaders();
			String filename = map.get("basename");
			String eventId = map.get("timestamp");

			log.info("Init----- event header : " + map);
			log.info("Init----- content : " + event.getBody());
			log.info("Init----- entitylist : " + entitylist);

			Ingestion stage = new Ingestion();
			stage.setRefId(eventId);
			stage.setDetail(filename);
			stage.setStatus(Stage.STATUS.INIT);
			stage.setSource(master);
			stage.setPhase(Stage.PHASE.FLUME);

			for (Entity entity : entitylist) {

				stage.setEntity(entity);
				/*
				 * if (evlist.contains(detail)) {
				 * event.getHeaders().put("entity",
				 * entity.getEntity_id().toLowerCase()); log.info(
				 * "Init----- event header : "+map); continue; }
				 */

				try {

					Pattern pattern = Pattern.compile(entity.getPattern());
					Matcher matcher = pattern.matcher(filename);

					log.info("Init----- header check : " + entity.getPattern() + " --> " + filename);

					if (matcher.matches()) {
						StandardEvaluationContext fileContext = new StandardEvaluationContext(stage);
						primary_key = exp.getValue(fileContext, String.class);
						stage.setGroupKey(primary_key);
						event.getHeaders().put("entity", entity.getName().toLowerCase());

						log.info("Init----- header matched : " + primary_key);

						stage.setStatus(Stage.STATUS.RUNNING);

					}

				} catch (Exception e) {
					e.printStackTrace();
					stage.setErrors(e.getMessage());
					stage.setStatus(Stage.STATUS.ERROR);
					stage.setLog(e.toString());
				} finally {
					DBUtil.persist(stage);
				}
			}

		}

		return evlist;
	}

	public static class Builder implements Interceptor.Builder {

		private Context context;
		private Source master = null;
		private List<Entity> entitylist = null;

		public void configure(Context context) {
			this.context = context;
			String id = context.getString("sourceId");

			master = (Source) DBUtil.find(Source.class, Long.parseLong(id));
			entitylist = master.getEntities();
			master.setStatus("RUNNING");
			DBUtil.merge(master);
		}

		public Interceptor build() {
			return new FlumeInterceptor(context, master, entitylist);
		}
	}

	public static void main(String[] args) {

		// EventJDBCHelper.insertEvent("test.txt", 100, "test", "INIT");

	}

}