package com.hydrogen.stage;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Stage {

	public enum STATUS {
		INIT, RUNNING, STOPPED, KILLED, ERROR, COMPLETED
	}

	public enum PHASE {
		FLUME, OOZIE, CRUNCH, HIVE, SPARK, HBASE, CACHE
	}

	public enum TYPE {
		INGESTION, VALIDATION, ANALYTICS, DIMENSION, FORMATION, PRESENTATION
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private STATUS status = null;
	private String refId = null;
	private String detail = null;
	private PHASE phase = null;
	private Timestamp startTime = null;
	private Timestamp endTime = null;
	private String errors = null;
	private String log = null;
	private String name = null;
	private Stage nextPhase = null;
	private Stage prevPhase = null;
	private transient boolean statusChanged = false;

	public Stage getPrevPhase() {
		return prevPhase;
	}

	public void setPrevPhase(Stage prevPhase) {
		this.prevPhase = prevPhase;
	}

	public Stage getNextPhase() {
		return nextPhase;
	}

	public void setNextPhase(Stage nextPhase) {
		this.nextPhase = nextPhase;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public String getErrors() {
		return errors;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public PHASE getPhase() {
		return phase;
	}

	public void setPhase(PHASE phase) {
		this.phase = phase;
	}

	public Long getId() {
		return id;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		if (this.status.equals(status))
			statusChanged = false;
		else
			statusChanged = true;
		this.status = status;
	}

	public boolean isStatusChanged() {
		return statusChanged;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
