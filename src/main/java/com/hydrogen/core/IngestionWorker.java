package com.hydrogen.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.Entity;
import com.hydrogen.model.Source;
import com.hydrogen.stage.Ingestion;
import com.hydrogen.stage.Stage;
import com.hydrogen.stage.Stage.TYPE;

public class IngestionWorker extends Worker {

	private StageManager manager = null;
	private Map<String, Object> where = null;

	public IngestionWorker(StageManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.RUNNING);
	}

	public void work() {
		List<Ingestion> running = DBUtil.findAll(Ingestion.class, where);
		if (running != null && running.size() > 0)
			checkToTriggerWorkflow(running);
	}

	private void checkToTriggerWorkflow(List<Ingestion> ilist) {
		Map<String, List<Ingestion>> groupMap = new HashMap<String, List<Ingestion>>();
		for (Ingestion ingestion : ilist) {
			if (ingestion.getGroupKey() != null) {
				if (groupMap.containsKey(ingestion.getGroupKey())) {
					groupMap.get(ingestion.getGroupKey()).add(ingestion);
				} else {
					groupMap.put(ingestion.getGroupKey(), Arrays.asList(ingestion));
				}
			} else {
				groupMap.put(Math.random() + "", Arrays.asList(ingestion));
			}
		}

		for (

		String gk : groupMap.keySet()) {
			List<Ingestion> list = groupMap.get(gk);
			Ingestion first = list.get(0);
			Source src = first.getSource();

			if (list.size() >= src.getEntities().size()) {
				List<Stage> dataset = new ArrayList<Stage>();
				for (Entity e : src.getEntities()) {
					Pattern pattern = Pattern.compile(e.getPattern());
					for (Ingestion ingestion : list) {
						Matcher matcher = pattern.matcher(ingestion.getName());
						if (matcher.matches()) {
							dataset.add(ingestion);
						}
					}

				}
				if (dataset.size() == src.getEntities().size()) {
					manager.nextPhase(TYPE.INGESTION, dataset);
				}
			}

		}

	}

}
