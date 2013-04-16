package com.hadoopmonitor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hadoop_live_nodes", catalog = "appmonitor")
public class LiveNodeInfo {
	private String host;
	private String hostName;
	private Float remain;
	private Float usedPercent;

	@Id
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Column(name = "host_name", length = 45)
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Float getRemain() {
		return remain;
	}

	public void setRemain(Float remain) {
		this.remain = remain;
	}

	@Column(name = "used_percent")
	public Float getUsedPercent() {
		return usedPercent;
	}

	public void setUsedPercent(Float usedPercent) {
		this.usedPercent = usedPercent;
	}
}
