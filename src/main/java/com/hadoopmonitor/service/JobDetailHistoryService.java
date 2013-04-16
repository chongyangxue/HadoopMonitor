package com.hadoopmonitor.service;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.DefaultJobHistoryParser;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobHistory;
import org.apache.hadoop.mapred.JobHistory.Keys;
import org.apache.hadoop.mapred.JobHistory.TaskAttempt;
import org.apache.hadoop.mapred.JobHistory.Values;
import org.apache.hadoop.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hadoopmonitor.dao.JobDetailHistoryDao;
import com.hadoopmonitor.model.JobDetailHistory;

@Service
public class JobDetailHistoryService {
	@Autowired
	private JobClientLoader jobClientLoader;
	
	@Autowired
	private JobDetailHistoryDao dao;

	private int numFailedMaps = 0;
	private int numKilledMaps = 0;
	private int numFailedReduces = 0;
	private int numKilledReduces = 0;
	
	public JobDetailHistory getJobDetailHistory(String jobId, String logFilePath) {
		JobClient client = jobClientLoader.getJobClient();
		JobHistory.JobInfo jobInfo = new JobHistory.JobInfo(jobId);
		JobDetailHistory jobHistory = new JobDetailHistory();
		try {
			DefaultJobHistoryParser.parseJobTasks(logFilePath, jobInfo, client.getFs());
			client.getFs().delete(new Path(logFilePath), true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		countTasks(jobInfo);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		jobHistory.setJobId(jobId);
		jobHistory.setUser(jobInfo.get(Keys.USER));
		jobHistory.setJobName(jobInfo.get(Keys.JOBNAME));
		try {
			jobHistory.setSubmitTime(dateFormat.parse(
					StringUtils.getFormattedTimeWithDiff(dateFormat, 
							jobInfo.getLong(Keys.SUBMIT_TIME), 0)));
			jobHistory.setLaunchTime(dateFormat.parse(
					StringUtils.getFormattedTimeWithDiff(dateFormat, 
							jobInfo.getLong(Keys.LAUNCH_TIME), 
							jobInfo.getLong(Keys.SUBMIT_TIME))));
			jobHistory.setFinishTime(dateFormat.parse(
					StringUtils.getFormattedTimeWithDiff(dateFormat,
							jobInfo.getLong(Keys.FINISH_TIME),
							jobInfo.getLong(Keys.LAUNCH_TIME))));
			
			if (jobInfo.get(Keys.JOB_STATUS) == "")
				jobHistory.setStatus("Incomplete");
			else
				jobHistory.setStatus(jobInfo.get(Keys.JOB_STATUS));
			jobHistory.setSuccessMaps(jobInfo.getInt(Keys.FINISHED_MAPS));
			jobHistory.setSuccessReduces(jobInfo.getInt(Keys.FINISHED_REDUCES));
			jobHistory.setFailedMaps(numFailedMaps);
			jobHistory.setFailedReduces(numFailedReduces);
			jobHistory.setKilledMaps(numKilledMaps);
			jobHistory.setKilledReduces(numKilledReduces);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jobHistory;
	}

	private void countTasks(JobHistory.JobInfo jobInfo) {

		Map<String, JobHistory.Task> tasks = jobInfo.getAllTasks();
		Map<String, String> allHosts = new TreeMap<String, String>();
		for (JobHistory.Task task : tasks.values()) {
			Map<String, TaskAttempt> attempts = task.getTaskAttempts();
			allHosts.put(task.get(Keys.HOSTNAME), "");
			for (TaskAttempt attempt : attempts.values()) {
				if (Values.MAP.name().equals(task.get(Keys.TASK_TYPE))) {
					if (Values.FAILED.name().equals(
							attempt.get(Keys.TASK_STATUS))) {
						numFailedMaps++;
					} else if (Values.KILLED.name().equals(
							attempt.get(Keys.TASK_STATUS))) {
						numKilledMaps++;
					}
				} else if (Values.REDUCE.name()
						.equals(task.get(Keys.TASK_TYPE))) {
					if (Values.FAILED.name().equals(
							attempt.get(Keys.TASK_STATUS))) {
						numFailedReduces++;
					} else if (Values.KILLED.name().equals(
							attempt.get(Keys.TASK_STATUS))) {
						numKilledReduces++;
					}
				}
			}
		}
	}

	public Path[] getFilePaths(String logPath) {
		JobClient client = jobClientLoader.getJobClient();
		try{
			FileSystem fs = FileSystem.get(URI.create(logPath), client.getConf());
			Path path = new Path(logPath);
			FileStatus[] status = fs.listStatus(path);
			Path[] listedPaths = FileUtil.stat2Paths(status);
			return listedPaths;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}

	public List<JobDetailHistory> getJobDetailList(String logPath) {
		Path[] filePaths = getFilePaths(logPath);
		List<JobDetailHistory> jobDetailList = new ArrayList<JobDetailHistory>();
		for(Path path : filePaths) {
			Pattern jobIdPattern = Pattern.compile("job_\\d+_\\d+");
			Pattern filePathPattern = Pattern.compile(logPath + "/.+");
			Matcher jobIdMatcher = jobIdPattern.matcher(path.toString());
			Matcher filePathMatcher = filePathPattern.matcher(path.toString());
			if(jobIdMatcher.find() && filePathMatcher.find()){
				String jobId = jobIdMatcher.group(0);
				String filePath = filePathMatcher.group(0);
				JobDetailHistory jobDetailHistory = getJobDetailHistory(jobId, filePath);
				jobDetailList.add(jobDetailHistory);
				
			}
		}
		return jobDetailList;
	}

	public void saveOrUpdate(JobDetailHistory t) {
		dao.saveOrUpdate(t);
	}

	public void save(JobDetailHistory t) {
		dao.save(t);
	}

	public void update(JobDetailHistory t) {
		dao.update(t);
	}
}
