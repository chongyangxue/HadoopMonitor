import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.Path;
import org.junit.Test;



public class test {
	public void testPattern() {
		String str = "hdfs://zw-hadoop-master:9000/user/hadoopmc/logs/" +
				"zw-hadoop-master_1365420553960_job_201304081929_0097_go2maplog_" +
				"Distributed+Lzo+Indexer+%5Bresult%2Fandroid%2FrealTime%2Fb";
		Pattern pattern = Pattern.compile("/user/hadoopmc/logs" + "/.+");
		Matcher matcher = pattern.matcher(str);
		String jobId = null;
		if(matcher.find()){
			jobId = matcher.group(0);
		}
		System.out.println(jobId);
	}
	
	@Test
	public void testPath() {
		Path[] filePaths = null;
		for(Path path : filePaths) {
			System.out.println("*_*");
		}
	}
}
