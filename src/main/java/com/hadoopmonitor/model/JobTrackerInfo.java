package com.hadoopmonitor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hadoop_jobtracker", catalog = "appmonitor")
public class JobTrackerInfo {
	private Long id;
	private Integer runningMap;
	private Integer runningReduce;
	private Integer nodes;
	private Integer blacklistNodes;
	private Date checkTime;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "running_map")
	public Integer getRunningMap() {
		return runningMap;
	}

	public void setRunningMap(Integer runningMap) {
		this.runningMap = runningMap;
	}

	@Column(name = "running_reduce")
	public Integer getRunningReduce() {
		return runningReduce;
	}

	public void setRunningReduce(Integer runningReduce) {
		this.runningReduce = runningReduce;
	}

	@Column(name = "nodes")
	public Integer getNodes() {
		return nodes;
	}

	public void setNodes(Integer nodes) {
		this.nodes = nodes;
	}

	@Column(name = "blacklist_nodes")
	public Integer getBlacklistNodes() {
		return blacklistNodes;
	}

	public void setBlacklistNodes(Integer blacklistNodes) {
		this.blacklistNodes = blacklistNodes;
	}

	@Column(name = "check_time")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
}
