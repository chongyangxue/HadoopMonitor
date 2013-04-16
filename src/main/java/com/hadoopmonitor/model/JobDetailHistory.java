package com.hadoopmonitor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hadoop_job_detail_history", catalog = "appmonitor")
public class JobDetailHistory {
	private String jobId;
	private String jobName;
	private String user;
	private Date submitTime;
	private Date launchTime;
	private Date finishTime;
	private String status;
	private Integer successMaps;
	private Integer successReduces;
	private Integer failedMaps;
	private Integer failedReduces;
	private Integer killedMaps;
	private Integer killedReduces;

	@Id
	@Column(name = "job_id", length = 45)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Column(name = "job_name", length = 45)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "user", length = 20)
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Column(name = "submit_time")
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	@Column(name = "launch_time")
	public Date getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}

	@Column(name = "finish_time")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "success_maps")
	public Integer getSuccessMaps() {
		return successMaps;
	}

	public void setSuccessMaps(Integer successMaps) {
		this.successMaps = successMaps;
	}

	@Column(name = "success_reduces")
	public Integer getSuccessReduces() {
		return successReduces;
	}

	public void setSuccessReduces(Integer successReduces) {
		this.successReduces = successReduces;
	}

	@Column(name = "failed_maps")
	public Integer getFailedMaps() {
		return failedMaps;
	}

	public void setFailedMaps(Integer failedMaps) {
		this.failedMaps = failedMaps;
	}

	@Column(name = "failed_reduces")
	public Integer getFailedReduces() {
		return failedReduces;
	}

	public void setFailedReduces(Integer failedReduces) {
		this.failedReduces = failedReduces;
	}

	@Column(name = "killed_maps")
	public Integer getKilledMaps() {
		return killedMaps;
	}

	public void setKilledMaps(Integer killedMaps) {
		this.killedMaps = killedMaps;
	}

	@Column(name = "killed_reduces")
	public Integer getKilledReduces() {
		return killedReduces;
	}

	public void setKilledReduces(Integer killedReduces) {
		this.killedReduces = killedReduces;
	}
}
