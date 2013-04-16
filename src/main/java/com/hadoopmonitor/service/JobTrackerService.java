package com.hadoopmonitor.service;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hadoopmonitor.dao.JobTrackerInfoDao;
import com.hadoopmonitor.model.JobTrackerInfo;

@Service
public class JobTrackerService {
	@Autowired
	private JobClientLoader jobClientLoader;
	
	@Autowired
	private JobTrackerInfoDao dao;

	public JobTrackerInfo getJobtrackerInfo() {
		JobClient client = jobClientLoader.getJobClient();
		ClusterStatus clusterStatus;
		JobTrackerInfo info = new JobTrackerInfo();
		try {
			clusterStatus = client.getClusterStatus(true);
			info.setRunningMap(clusterStatus.getMapTasks());
			info.setRunningReduce(clusterStatus.getReduceTasks());
			info.setNodes(clusterStatus.getTaskTrackers());
			info.setBlacklistNodes(clusterStatus.getBlacklistedTrackers());
			info.setCheckTime(new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public void saveOrUpdate(JobTrackerInfo t) {
		dao.saveOrUpdate(t);
	}

	public void save(JobTrackerInfo t) {
		dao.save(t);
	}

	public void update(JobTrackerInfo t) {
		dao.update(t);
	}
}
