package com.xnx3.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import net.sf.json.JSONObject;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.*;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.*;
import com.aliyun.openservices.log.common.Consts.CursorMode;
import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.Logs.Log;
import com.aliyun.openservices.log.common.Logs.Log.Content;
import com.aliyun.openservices.log.common.Logs.LogGroup;
import com.aliyun.openservices.log.common.QueriedLog;
import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;

/**
 * 阿里云日志服务
 * 需Jar包：
 * <br/>aliyun-log-0.6.1.jar
 * <br/>commons-codec-1.4.jar
 * <br/>commons-collections-3.2.1.jar
 * <br/>commons-digester-1.8.jar
 * <br/>commons-lang-2.6.jar
 * <br/>commons-logging-1.1.1.jar
 * <br/>commons-validator-1.4.0.jar
 * <br/>ezmorph-1.0.6.jar
 * <br/>gson-2.2.4.jar
 * <br/>hamcrest-core-1.1.jar
 * <br/>httpclient-4.5.1.jar
 * <br/>httpcore-4.1.4.jar
 * <br/>json-lib-2.4-jdk15.jar
 * <br/>lz4-1.3.0.jar
 * <br/>protobuf-java-2.5.0.jar
 * 
 * @author 管雷鸣
 */
public class AliyunLogUtil {
	private String project = ""; // 上面步骤创建的项目名称
	private String logstore = ""; // 上面步骤创建的日志库名称
	
	/**
	 * 提交日志的累计条数，当 {@link #logGroup}内的日志条数累计到这里指定的条数时，才回提交到阿里云日志服务中去
	 */
	public int logGroupSubmitNumber = 100;
	public Vector<LogItem> logGroup;	//日志组，执行单条日志插入时，累计到多少条后便会自动推送到阿里云日志服务中去
	
	// 构建一个客户端实例
	private Client client;
	
	/**
	 * 
	 * @param endpoint 如 cn-hongkong.log.aliyuncs.com
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param project 项目
	 * @param logstore 日志库
	 */
	public AliyunLogUtil(String endpoint, String accessKeyId, String accessKeySecret, String project, String logstore) {
		this.project = project;
		this.logstore = logstore;
		client = new Client(endpoint, accessKeyId, accessKeySecret);
		
		logGroup = new Vector<LogItem>();
	}
	
	// 列出当前 project 下的所有日志库名称
	public ArrayList<String> getLogStore() throws LogException{
		int offset = 0;
        int size = 100;
        String logStoreSubName = "";
		ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logStoreSubName);
		ArrayList<String> logStores;
		logStores = client.ListLogStores(req1).GetLogStores();
		System.out.println("ListLogs:" + logStores.toString() + "\n");
		return logStores;
	}
	
	/**
	 * 保存单条日志。执行此方法，会立即将传入的日志保存到阿里云日志服务中。
	 * <br/>如果保存量大、频繁，使用 {@link #saveByGroup(String, String, Vector)} 累计多条日志时一并推送
	 * <br/>使用示例：
	 * <pre>
	 * 		//创建 AliyunLogUtil 对象
	 * 		AliyunLogUtil aliyunLogUtil = new AliyunLogUtil(......);
	 * 		//创建单条日志
	 * 		LogItem logItem = aliyunLogUtil.newLogItem();
	 * 		logItem.PushBack("name", "试试");
	 * 		logItem.PushBack("url", "www.xnx3.com");
	 * 		logItem.PushBack("date", "2017.5.2");
	 * 		//刚创建的单条日志到阿里云日志服务中去
	 * 		aliyunLogUtil.save("topic1", "127.0.0.1", logItem);
	 * </pre>
	 * @param topic 用户自定义字段，用以标记一批日志（例如：访问日志根据不同的站点进行标记）。默认该字段为空字符串（空字符串也是一个有效的主题）。任意不超过 128 字节的字符串。
	 * @param source 日志的来源地，例如产生该日志机器的 IP 地址。默认该字段为空。任意不超过 128 字节的字符串。
	 * @param logItem 要保存的单条日志。可以通过  {@link #newLogItem()} 创建。如
	 * @return {@link PutLogsResponse}
	 * @throws LogException
	 */
	public PutLogsResponse save(String topic, String source, LogItem logItem) throws LogException{
        Vector<LogItem> logGroup = new Vector<LogItem>();
        logGroup.add(logItem);
        PutLogsRequest req2 = new PutLogsRequest(project, logstore, topic, source, logGroup);
        return client.PutLogs(req2);
	}
	
	/**
	 * 保存多条日志（日志组）。执行此方法，会立即将传入的日志保存到阿里云日志服务中。
	 * <br/>如：
	 * <pre>
	 * 		//创建 AliyunLogUtil 对象
	 * 		AliyunLogUtil aliyunLogUtil = new AliyunLogUtil(......);
	 * 		//创建日志组
	 * 		Vector<LogItem> logGroup = new Vector<LogItem>();
	 * 		int i = 0;
	 * 		while (++i < 100) {
	 * 			//循环向日志组内加入日志
	 * 			LogItem logItem = aliyunLogUtil.newLogItem();
	 *     		logItem.PushBack("date", "2017.5.2");
	 * 			logItem.PushBack("number", "当前次数："+i);
	 * 			logGroup.add(logItem);
	 * 		}
	 * 		//保存整个日志组的日志到阿里云日志服务中去
	 * 		aliyunLogUtil.saveByGroup("topic2", "127.0.0.1", logGroup);
	 * </pre>
	 * @param topic 用户自定义字段，用以标记一批日志（例如：访问日志根据不同的站点进行标记）。默认该字段为空字符串（空字符串也是一个有效的主题）。任意不超过 128 字节的字符串。
	 * @param source 日志的来源地，例如产生该日志机器的 IP 地址。默认该字段为空。任意不超过 128 字节的字符串。
	 * @param logGroup 日志组,限制为：最大 4096 行日志，或 10MB 空间。
	 * @return {@link PutLogsResponse}
	 * @throws LogException
	 */
	public PutLogsResponse saveByGroup(String topic, String source, Vector<LogItem> logGroup) throws LogException{
        PutLogsRequest req2 = new PutLogsRequest(project, logstore, topic, source, logGroup);
        return client.PutLogs(req2);
	}
	
	
	/**
	 * 创建一个新的 {@link LogItem} 不过相比于原本的，这里不用传入当前时间了，自动赋予当前时间戳
	 * @return
	 */
	public LogItem newLogItem(){
		return new LogItem((int) (new Date().getTime() / 1000));
	}
	
	
	public void read() throws LogException{
		// 把 0 号 shard 中，最近 1 分钟写入的数据都读取出来。
        int shard_id = 0;
        long curTimeInSec = System.currentTimeMillis() / 1000;
        GetCursorResponse cursorRes = client.GetCursor(project, logstore, shard_id, curTimeInSec - 60);
        String beginCursor = cursorRes.GetCursor();
        cursorRes = client.GetCursor(project, logstore, shard_id, CursorMode.END);
        String endCursor = cursorRes.GetCursor();
        String curCursor = beginCursor;
        while (curCursor.equals(endCursor) == false) {
            int loggroup_count = 2; // 每次读取两个 loggroup
            BatchGetLogResponse logDataRes = client.BatchGetLog(project, logstore, shard_id, loggroup_count, curCursor,
                    endCursor);
            // 读取LogGroup的List
            List<LogGroupData> logGroups = logDataRes.GetLogGroups(); 
            for (LogGroupData logGroupData : logGroups) {
                // 直接使用Protocol buffer格式的LogGroup进行
                LogGroup log_group_pb = logGroupData.GetLogGroup();  
                System.out.println("Source:" + log_group_pb.getSource());
                System.out.println("Topic:" + log_group_pb.getTopic());
                System.out.println(log_group_pb.getLogsList().size());
                for(Log log_pb: log_group_pb.getLogsList()){
                    System.out.println("LogTime:" + log_pb.getTime());
                    for(Content content: log_pb.getContentsList()) {
                        System.out.println(content.getKey() + ":" + content.getValue());
                    }
                }
            }
            String next_cursor = logDataRes.GetNextCursor();
            curCursor = next_cursor;
        }
	}
	
	/**
	 * 统计符合条件的日志的记录条数
	 * <br/><b>只有阿里云日志控制台打开索引功能，才能使用此接口</b>
	 * <br/>最新数据有1分钟延迟时间
	 * @param query 查询表达式。为空字符串""则查询所有。关于查询表达式的详细语法，请参考 <a href="https://help.aliyun.com/document_detail/29060.html">查询语法 https://help.aliyun.com/document_detail/29060.html</a>
	 * @param topic 查询日志主题。为空字符串""则查询所有。
	 * @param startTime 查询开始时间点（精度为秒，从 1970-1-1 00:00:00 UTC 计算起的秒数）。10位时间戳，可用 {@link DateUtil#timeForUnix10()} 取得
	 * @param endTime 查询结束时间点，10位时间戳，可用 {@link DateUtil#timeForUnix10()} 取得
	 * @return 符合条件的记录的条数
	 * @throws LogException 
	 */
	public long queryCount(String query, String topic, int startTime, int endTime) throws LogException{
		GetHistogramsResponse res3 = null;
		//如果读取失败，最多重复三次
		int i = 0;
		while (i++ < 3) {
			GetHistogramsRequest req3 = new GetHistogramsRequest(project, logstore, topic, query, startTime, endTime);
			res3 = client.GetHistograms(req3);
			if (res3 != null && res3.IsCompleted()){
				// IsCompleted() 返回
                // true，表示查询结果是准确的，如果返回
                // false，则重复查询
				break;
			}
		}
		return res3.GetTotalCount();
	}
	
	/**
	 * 统计符合条件的日志的记录条数
	 * <br/><b>只有阿里云日志控制台打开索引功能，才能使用此接口</b>
	 * <br/>最新数据有1分钟延迟时间
	 * <br/>使用示例：
	 * <pre>
	 * 		ArrayList<QueriedLog> qlList = aliyunLogUtil.queryList("", "", DateUtil.timeForUnix10()-10000, DateUtil.timeForUnix10(), 0, 100, true);
	 * 		for (int i = 0; i < qlList.size(); i++) {
	 * 			QueriedLog ll = qlList.get(i);
	 * 			LogItem li = ll.GetLogItem();
	 * 			JSONObject json = JSONObject.fromObject(li.ToJsonString());
	 * 			System.out.println(DateUtil.dateFormat(json.getLong("logtime"), com.xnx3.DateUtil.FORMAT_DEFAULT)+"__"+li.ToJsonString());
	 * 		}
	 * </pre>
	 * @param query 查询表达式。为空字符串""则查询所有。关于查询表达式的详细语法，请参考 <a href="https://help.aliyun.com/document_detail/29060.html">查询语法 https://help.aliyun.com/document_detail/29060.html</a>
	 * @param topic 查询日志主题。为空字符串""则查询所有。
	 * @param startTime 查询开始时间点（精度为秒，从 1970-1-1 00:00:00 UTC 计算起的秒数）。10位时间戳，可用 {@link DateUtil#timeForUnix10()} 取得
	 * @param endTime 查询结束时间点，10位时间戳，可用 {@link DateUtil#timeForUnix10()} 取得
	 * @param offset 请求返回日志的起始点。取值范围为 0 或正整数，默认值为 0。
	 * @param line 请求返回的最大日志条数。取值范围为 0~100，默认值为 100。
	 * @param reverse 是否按日志时间戳逆序返回日志。
	 * 			<ul>
	 * 				<li>true : 逆序，时间越大越靠前</li>
	 * 				<li>false : 顺序，时间越大越靠后</li>
	 * 			</ul>			
	 * @return {@link QueriedLog}数组，将每条日志内容都详细列出来
	 * @throws LogException 
	 */
	public ArrayList<QueriedLog> queryList(String query, String topic, int startTime, int endTime, int offset, int line, boolean reverse) throws LogException{
		//查询日志条数
		long total_log_lines = queryCount(query, topic, startTime, endTime);
		
		ArrayList<QueriedLog> qlList = new ArrayList<QueriedLog>();
		
        while (offset <= total_log_lines) {
        	GetLogsResponse res4 = null;
        	//对于每个 log offset,一次读取 10 行 log，如果读取失败，最多重复读取 3 次。
        	for (int retry_time = 0; retry_time < 3; retry_time++) {
        		GetLogsRequest req4 = new GetLogsRequest(project, logstore, startTime, endTime, topic, query, offset, line, reverse);
        		res4 = client.GetLogs(req4);
        		if (res4 != null && res4.IsCompleted()) {
        			break;
        		}
        	}
        	ArrayList<QueriedLog> ql = res4.GetLogs();
        	qlList.addAll(ql);
        	
        	offset += line;
        }
        
        return qlList;
	}
	
	
	public static void main(String args[]) throws LogException, InterruptedException, NotReturnValueException {
	        AliyunLogUtil aliyunLogUtil = new AliyunLogUtil("cn-hongkong.log.aliyuncs.com", "121212", "121212", "requestlog", "fangwen");

	        ArrayList<QueriedLog> qlList = aliyunLogUtil.queryList("", "leiwen.wang.market", DateUtil.timeForUnix10()-10000, DateUtil.timeForUnix10(), 0, 100, true);
        	for (int i = 0; i < qlList.size(); i++) {
        		System.out.println(i);
        		QueriedLog ll = qlList.get(i);
        		LogItem li = ll.GetLogItem();
        		JSONObject json = JSONObject.fromObject(li.ToJsonString());
        		System.out.println(DateUtil.dateFormat(json.getLong("logtime"), com.xnx3.DateUtil.FORMAT_DEFAULT)+"__"+li.ToJsonString());
        	} 
        	System.out.println("qlList  :  "+qlList.size());
	    }
	
}
