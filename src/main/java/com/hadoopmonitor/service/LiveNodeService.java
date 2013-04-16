package com.hadoopmonitor.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.mapred.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hadoopmonitor.dao.LiveNodeInfoDao;
import com.hadoopmonitor.model.LiveNodeInfo;

@Service
public class LiveNodeService {
	@Autowired
	private JobClientLoader jobClientLoader;
	
	@Autowired
	private LiveNodeInfoDao dao;

	public List<LiveNodeInfo> getLiveNodes() {
		JobClient client = jobClientLoader.getJobClient();
		FileSystem fs;
		try{
			fs = client.getFs();
			DistributedFileSystem hdfs = (DistributedFileSystem)fs;
			DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();

			long heartbeatRecheckInterval = client.getConf().getInt(
					"heartbeat.recheck.interval", 5 * 60 * 1000); // 5 minutes
			long heartbeatInterval = client.getConf().getLong(
					"dfs.heartbeat.interval", 3) * 1000;
			long heartbeatExpireInterval = 2 * heartbeatRecheckInterval + 10
					* heartbeatInterval;
			List<LiveNodeInfo> liveNodeList = new ArrayList<LiveNodeInfo>();
			for(DatanodeInfo dataNodeInfo : dataNodeStats){
				LiveNodeInfo liveNode = new LiveNodeInfo();
				if(dataNodeInfo.getLastUpdate() > (System.currentTimeMillis() - heartbeatExpireInterval)){
					liveNode.setHost(dataNodeInfo.getHost() + ":" + dataNodeInfo.getPort());
					liveNode.setHostName(dataNodeInfo.getHostName());
					liveNode.setRemain((float)dataNodeInfo.getRemaining() / 1024 / 1024 / 1024);
					DecimalFormat df = new DecimalFormat("#.00");  
					float remainPercent = Float.parseFloat(df.format(dataNodeInfo.getDfsUsedPercent()));
					liveNode.setUsedPercent(remainPercent);
					liveNodeList.add(liveNode);
				}
			}
			return liveNodeList;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}

	public void save(LiveNodeInfo t) {
		dao.save(t);
	}

	public void saveOrUpdate(LiveNodeInfo t) {
		dao.saveOrUpdate(t);
	}
	
	public void update(LiveNodeInfo t) {
		dao.update(t);
	}
}
