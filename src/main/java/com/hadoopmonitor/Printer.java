package com.hadoopmonitor;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hadoopmonitor.model.JobDetailHistory;
import com.hadoopmonitor.model.JobTrackerInfo;
import com.hadoopmonitor.model.LiveNodeInfo;
import com.hadoopmonitor.model.NameNodeInfo;
import com.hadoopmonitor.service.JobDetailHistoryService;
import com.hadoopmonitor.service.JobTrackerService;
import com.hadoopmonitor.service.LiveNodeService;
import com.hadoopmonitor.service.NameNodeService;

@Component
public class Printer {
	@Autowired
	private JobTrackerService jobTrackerService;

	@Autowired
	private JobDetailHistoryService jobHistoryService;

	@Autowired
	private NameNodeService nameNodeService;

	@Autowired
	private LiveNodeService liveNodeService;

	public void printHadoopStatus(){
		try {
			System.out.println("************************ Job Info***********************");
			JobTrackerInfo jobTrackerInfo = jobTrackerService.getJobtrackerInfo();
			System.out.println("Running Map: " + jobTrackerInfo.getRunningMap());
			System.out.println("Running Reduce: " + jobTrackerInfo.getRunningReduce());
			System.out.println("Nodes: " + jobTrackerInfo.getNodes());
			System.out.println("BlackList Nodes: " + jobTrackerInfo.getBlacklistNodes());
			jobTrackerService.saveOrUpdate(jobTrackerInfo);
			
			System.out.println("************************ Job Detail***********************");
			JobDetailHistory history = jobHistoryService.
					getJobDetailHistory("job_201303251424_28851", "/user/hadoopmc/job_201303251424_28851");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("User: " + history.getUser());
			System.out.println("Job Name: " + history.getJobName());
			System.out.println("Submitted At: " + dateFormat.format(history.getSubmitTime()));
			System.out.println("Launched At: " + dateFormat.format(history.getLaunchTime()));
			System.out.println("Finished At: " + dateFormat.format(history.getFinishTime()));
			System.out.println("Status: " + history.getStatus());
			System.out.println("Successful Maps: " + history.getSuccessMaps());
			System.out.println("Successful Reduces: " + history.getSuccessReduces());
			System.out.println("Failed Maps: " + history.getFailedMaps());
			System.out.println("Failed Reduces: " + history.getFailedReduces());
			System.out.println("Killed Maps: " + history.getKilledMaps());
			System.out.println("Killed Reduces: " + history.getKilledReduces());
			jobHistoryService.saveOrUpdate(history);
			
			System.out.println("************************ Node Info***********************");
			NameNodeInfo nodeInfo = nameNodeService.getNodesInfo();
			System.out.println("Capacity: " + nodeInfo.getCapacity() + "GB");
			System.out.println("DFS Used: " + nodeInfo.getDfsUsed() + "GB");
			System.out.println("Non DFS Used: " + nodeInfo.getNonDfsUsed() + "GB");
			System.out.println("DFS Remaining: " + nodeInfo.getRemaining() + "GB");
			System.out.println("DFS Remaining%: " + nodeInfo.getRemainPercent() + "%");
			System.out.println("Live Nodes: " + nodeInfo.getLiveNodes());
			System.out.println("Dead Nodes: " + nodeInfo.getDeadNodes());
			nameNodeService.saveOrUpdate(nodeInfo);

			System.out.println("************************ Live Nodes ***********************");
			List<LiveNodeInfo> liveNodeList = liveNodeService.getLiveNodes();
			for(LiveNodeInfo live : liveNodeList) {
				System.out.println(live.getHost() + " | " 
			                     + live.getHostName() + " | "
			                     + live.getRemain() + " | "
			                     + live.getUsedPercent() + "%");
				liveNodeService.saveOrUpdate(live);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printRetiredFiles() {
		List<JobDetailHistory> jobHistoryList = jobHistoryService.getJobDetailList("/user/hadoopmc/logs");
		for(JobDetailHistory history : jobHistoryList) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("User: " + history.getUser());
			System.out.println("Job Name: " + history.getJobName());
			System.out.println("Submitted At: " + dateFormat.format(history.getSubmitTime()));
			System.out.println("Launched At: " + dateFormat.format(history.getLaunchTime()));
			System.out.println("Finished At: " + dateFormat.format(history.getFinishTime()));
			System.out.println("Status: " + history.getStatus());
			System.out.println("Successful Maps: " + history.getSuccessMaps());
			System.out.println("Successful Reduces: " + history.getSuccessReduces());
			System.out.println("Failed Maps: " + history.getFailedMaps());
			System.out.println("Failed Reduces: " + history.getFailedReduces());
			System.out.println("Killed Maps: " + history.getKilledMaps());
			System.out.println("Killed Reduces: " + history.getKilledReduces());
			try {
				jobHistoryService.save(history);
			}catch(Exception e) {
				continue;
			}
		}
	}
}
