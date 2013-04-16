package com.hadoopmonitor.service;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.security.UserGroupInformation;

public class JobClientLoader {
	private JobClient jobClient;
	private String masterAddress;
	private String user;
	private String keytab;

	public JobClient getJobClient() {
		if(this.jobClient != null) {
			return jobClient;
		} else {
			Configuration conf = new Configuration();
			conf.addResource(new Path("/opt/sohuhadoop/conf/core-site.xml"));
			conf.addResource(new Path("/opt/sohuhadoop/conf/hdfs-site.xml"));
			conf.addResource(new Path("/opt/sohuhadoop/conf/mapred-site.xml"));
			InetSocketAddress nodeAddr = new InetSocketAddress(masterAddress, 9001);
			try{
				conf.set("hadoop.security.authentication", "kerberos");
				conf.set("hadoop.kerberos.kinit", "/usr/kerberos/bin/kinit");
				UserGroupInformation.setConfiguration(conf);
				UserGroupInformation.loginUserFromKeytab(user, keytab);
				jobClient = new JobClient(nodeAddr, conf);
				jobClient.setConf(conf);
			}catch (Exception e){
				e.printStackTrace();
			}
			return jobClient;
		}
	}

	public void setJobClient(JobClient jobClient) {
		this.jobClient = jobClient;
	}

	public String getMasterAddress() {
		return masterAddress;
	}

	public void setMasterAddress(String masterAddress) {
		this.masterAddress = masterAddress;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getKeytab() {
		return keytab;
	}

	public void setKeytab(String keytab) {
		this.keytab = keytab;
	}
}
