

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.DefaultJobHistoryParser;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobHistory;
import org.apache.hadoop.mapred.JobHistory.Keys;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.StringUtils;

public class testLocal {
	protected static enum MyCounter {
		TOTAL_LAUNCHED_MAPS,
		TOTAL_LAUNCHED_REDUCES
	}
	
	
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addResource(new Path("/opt/sohuhadoop/conf/core-site.xml"));
		conf.addResource(new Path("/opt/sohuhadoop/conf/hdfs-site.xml"));
		conf.addResource(new Path("/opt/sohuhadoop/conf/mapred-site.xml"));
		
		InetSocketAddress jobTrackAddr = new InetSocketAddress("10.10.70.155", 9001);
		JobClient client;
		try {
			UserGroupInformation.setConfiguration(conf);
			SecurityUtil.login(conf, "mapreduce.jobtracker.keytab.file",
					"mapreduce.jobtracker.kerberos.principal");
			
			client = new JobClient(jobTrackAddr, conf);
			client.setConf(conf);

			ClusterStatus clusterStatus = client.getClusterStatus(true);
			System.out.println("running map task: "	+ clusterStatus.getMapTasks());
			System.out.println("running reduce task: " + clusterStatus.getReduceTasks());
			System.out.println("Nodes:" + clusterStatus.getTaskTrackers());
			System.out.println("Blacklisted Nodes: " + clusterStatus.getBlacklistedTrackers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
