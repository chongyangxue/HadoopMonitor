package com.hadoopmonitor.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.mapred.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hadoopmonitor.dao.NameNodeInfoDao;
import com.hadoopmonitor.model.NameNodeInfo;

@Service
public class NameNodeService {
	@Autowired
	private JobClientLoader jobClientLoader;
	
	@Autowired
	private NameNodeInfoDao dao;

	public NameNodeInfo getNodesInfo() {
		JobClient client = jobClientLoader.getJobClient();
		FileSystem fs;
		NameNodeInfo namenodeInfo = new NameNodeInfo();
		try {
			fs = client.getFs();
			FsStatus fsStatus = fs.getStatus();
			float remain = (float)fsStatus.getRemaining() / (float)fsStatus.getCapacity();
			DecimalFormat df = new DecimalFormat("#.00");  
			float remainPercent = Float.parseFloat(df.format(remain*100)); 
			namenodeInfo.setCapacity((float)fsStatus.getCapacity()/1024/1024/1024);
			namenodeInfo.setDfsUsed((float)fsStatus.getUsed()/1024/1024/1024);
			namenodeInfo.setNonDfsUsed((float)(fsStatus.getCapacity() - fsStatus.getUsed())/1024/1024/1024);
			namenodeInfo.setRemaining((float)fsStatus.getRemaining()/1024/1024/1024);
			namenodeInfo.setRemainPercent(remainPercent);

			DistributedFileSystem hdfs = (DistributedFileSystem) fs;
			DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();
		    long heartbeatRecheckInterval = client.getConf().
		    		getInt("heartbeat.recheck.interval", 5 * 60 * 1000); // 5 minutes
		    long heartbeatInterval = client.getConf().getLong("dfs.heartbeat.interval", 3) * 1000;
		    long heartbeatExpireInterval = 2 * heartbeatRecheckInterval + 10 * heartbeatInterval;
		    Integer live = 0;
		    Integer dead = 0;
			for(DatanodeInfo dataNodeInfo : dataNodeStats) {
				if(dataNodeInfo.getLastUpdate() < 
						(System.currentTimeMillis() - heartbeatExpireInterval))
					dead ++;
				else
					live ++;
			}
			namenodeInfo.setLiveNodes(live);
			namenodeInfo.setDeadNodes(dead);
			namenodeInfo.setCheckTime(new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return namenodeInfo;
	}
	
	public void save(NameNodeInfo t) {
		dao.save(t);
	}

	public void saveOrUpdate(NameNodeInfo t) {
		dao.save(t);
	}
	
	public void update(NameNodeInfo t) {
		dao.update(t);
	}
}
