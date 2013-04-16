package com.hadoopmonitor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hadoop_namenode_stat")
public class NameNodeInfo {
	private int id;
	private Float capacity;
	private Float dfsUsed;
	private Float nonDfsUsed;
	private Float remaining;
	private Float remainPercent;
	private Integer liveNodes;
	private Integer deadNodes;
	private Date checkTime;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getCapacity() {
		return capacity;
	}

	public void setCapacity(Float capacity) {
		this.capacity = capacity;
	}

	@Column(name = "dfs_used")
	public Float getDfsUsed() {
		return dfsUsed;
	}

	public void setDfsUsed(Float dfsUsed) {
		this.dfsUsed = dfsUsed;
	}

	@Column(name = "non_dfs_used")
	public Float getNonDfsUsed() {
		return nonDfsUsed;
	}

	public void setNonDfsUsed(Float nonDfsUsed) {
		this.nonDfsUsed = nonDfsUsed;
	}

	public Float getRemaining() {
		return remaining;
	}

	public void setRemaining(Float remaining) {
		this.remaining = remaining;
	}

	@Column(name = "remain_percent")
	public Float getRemainPercent() {
		return remainPercent;
	}

	public void setRemainPercent(Float remainPercent) {
		this.remainPercent = remainPercent;
	}

	@Column(name = "live_nodes")
	public Integer getLiveNodes() {
		return liveNodes;
	}

	public void setLiveNodes(Integer liveNodes) {
		this.liveNodes = liveNodes;
	}

	@Column(name = "dead_nodes")
	public Integer getDeadNodes() {
		return deadNodes;
	}

	public void setDeadNodes(Integer deadNodes) {
		this.deadNodes = deadNodes;
	}

	@Column(name = "check_time")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
}
