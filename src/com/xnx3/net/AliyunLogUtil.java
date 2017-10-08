package com.xnx3.net;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.*;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.*;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;

/**
 * 阿里云日志服务，基础操作
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
 * @author 管雷鸣
 */
public class AliyunLogUtil {
	private String project = ""; // 上面步骤创建的项目名称
	private String logstore = ""; // 上面步骤创建的日志库名称
	
	/**
	 * 提交日志的累计条数，当 {@link #logGroup}内的日志条数累计到这里指定的条数时，才回提交到阿里云日志服务中去
	 */
	public Vector<LogItem> logGroupCache;	//日志组，执行单条日志插入时，累计到多少条后便会自动推送到阿里云日志服务中去
	public int cacheLogMaxNumber = 100;		//缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
	//缓存日志的最大时间，单位为秒。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后清空（ {@link #logGroup} ）重新开始累计。数据由 systemConfig.xml的log.logService.log_cache_max_time提供
	public int cacheLogMaxTime = 600;
	//Thread.currentThread().getStackTrace()[3] 追溯调用此方法的函数，数字越大追溯位置越考前,默认为0，不启用
	public int stack_trace_deep = 0;
	//最后一次提交日志的时间 （ {@link #logGroup} ）动态时间，每次提交日志后，都会重新刷新此时间
	public int lastSubmitTime = DateUtil.timeForUnix10();
	
	// 构建一个客户端实例
	private Client client;
	
	/**
	 * 创建阿里云日志服务工具类
	 * <br/>阿里云日志服务控制台： <a href="https://sls.console.aliyun.com">https://sls.console.aliyun.com</a>
	 * @param endpoint 如 cn-hongkong.log.aliyuncs.com
	 * @param accessKeyId 阿里云访问密钥 AccessKeyId
	 * @param accessKeySecret 阿里云 AccessKeySecret
	 * @param project 日志服务中，创建的项目名称
	 * @param logstore 日志服务中，创建的项目下的日志库名称
	 */
	public AliyunLogUtil(String endpoint, String accessKeyId, String accessKeySecret, String project, String logstore) {
		this.project = project;
		this.logstore = logstore;
		client = new Client(endpoint, accessKeyId, accessKeySecret);
		
		logGroupCache = new Vector<LogItem>();
	}
	
	/**
	 * 建议使用  {@link #setCacheAutoSubmit(int, int)}
	 * <br/>设置缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为1，即不缓存。最大支持4096
	 * <br/>配合 {@link #save(String, String, LogItem)} 使用，当调用save方法保存的缓存池中的日志数量达到多少条时触发，提交缓存池中的日志到阿里云日志服务中。
	 * 		<ul>
	 * 			<li>当不调用此方法设置时，默认为缓存20条</li>
	 * 			<li>当设置为大于1的数时，启用缓存。当数量达到这里设定的数量时，才会提交到阿里云日志服务中。</li>
	 * 		</ul>
	 * @param cacheLogMaxNumber 要缓存的日志条数
	 * @deprecated 
	 */
	public void setCacheLogMaxNumber(int cacheLogMaxNumber) {
		this.cacheLogMaxNumber = cacheLogMaxNumber;
	}
	

	/**
	 * 设置日志缓存自动提交服务器的临界点，可以通过此来设置。若不设置，默认是最大100条、最长缓存时间600秒。
	 * <br/><b>注意，这两项是同时生效的，最大支持4096条，10MB的数据，一般设置为几百条或者默认即可</b>
	 * <br/>1.当日志缓存条数达到多少条时，自动提交日志，并清空日志缓存重新记录
	 * <br/>2.当日志缓存时间达到多少秒没有提交时，超过指定秒数后，再此写入日志时，会触发，使其自动提交日志，并清空日志缓存重新记录
	 * @param maxNumber (单位:条)当日志缓存条数达到多少条时，自动提交日志，并清空日志缓存重新记录。
	 * 				<br/>若设为1，则不缓存，插入就立即提交。
	 * @param maxTime (单位:秒)当日志缓存时间达到多少秒没有提交时，超过指定秒数后，再此写入日志时，会触发，使其自动提交日志，并清空日志缓存重新记录
	 */
	public void setCacheAutoSubmit(int maxNumber, int maxTime){
		this.cacheLogMaxNumber = maxNumber;
		this.cacheLogMaxTime = maxTime;
	}
	
	/**
	 * Thread.currentThread().getStackTrace()[3] 追溯调用此方法的函数，数字越大追溯位置越靠外
	 * <br/>若不设置，默认为0，不启用程序具体执行类及函数的记录
	 * <br/>此方法主要用于，当扩展此类，或继承等，日志中记录的执行类及执行方法，便是依据此，来自动获得并记入日志的
	 * @param deep 追溯级别，数字越大追溯位置越靠外，可以从1开始挨个变大，设置几个数字挨个试试。
	 */
	public void setStackTraceDeep(int deep){
		stack_trace_deep = deep;
	}
	
	/**
	 * 列出当前日志服务下的所有日志库名称
	 * @return 返回日志库名称数组
	 * @throws LogException
	 */
	public ArrayList<String> getLogStore() throws LogException{
		int offset = 0;
        int size = 100;
        String logStoreSubName = "";
		ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logStoreSubName);
		ArrayList<String> logStores;
		logStores = client.ListLogStores(req1).GetLogStores();
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
	 * @param logItem 要保存的日志，可用 {@link AliyunLogUtil#newLogItem()} 获取创建 {@link LogItem} 对象，然后将要记录的日志类似JSON， {@link LogItem#PushBack(String, String)} 加入
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
	 * 缓存日志。缓存的并不会立即联网提交到阿里云日志服务，而是达到一定条数 {@link #setCacheLogMaxNumber(int)} 达到这里设置的条数后才会触发 {@link #cacheCommit()} 提交到阿里云日志服务
	 * <br/>这里缓存的日志，没提供设置topic、source，可以将其记录到日志本身中去 
	 * @param logItem 要缓存的日志，可用 {@link AliyunLogUtil#newLogItem()} 获取创建 {@link LogItem} 对象，然后将要记录的日志类似JSON， {@link LogItem#PushBack(String, String)} 加入
	 * @throws LogException
	 */
	public void cacheLog(LogItem logItem) throws LogException{
		if(logItem == null){
			return;
		}
		
		/*使用的类的信息，来源位置*/
		if(stack_trace_deep > 0){
			StackTraceElement st = Thread.currentThread().getStackTrace()[stack_trace_deep];
			logItem.PushBack("className", st.getClassName());
			logItem.PushBack("methodName", st.getMethodName());
			logItem.PushBack("fileName", st.getFileName());
		}
		
		logGroupCache.add(logItem);
		
		boolean submit = false;	//提交日志
		if(logGroupCache.size() > cacheLogMaxNumber){
			//超过定义的缓存最大值，那么将缓存中的日志数据提交到阿里日志服务中去
			submit = true;
		}else{
			int currentTime = DateUtil.timeForUnix10();	//当前时间
			if(lastSubmitTime + cacheLogMaxTime < currentTime){
				submit = true;
				lastSubmitTime = currentTime;
			}
		}
		
		if(submit){
			cacheCommit();
		}
	}
	
	/**
	 * 手动将 {@link #cacheLog(LogItem)} 缓存中的数据提交到阿里云日志服务中去。若提交成功，便清空缓存。
	 * <br/>此方法会在 {@link #cacheLog(LogItem)} 向缓存中添加日志时，当达到 {@link #setCacheLogMaxNumber(int)} 设置的缓存最大条数后会自动触发。
	 * <br/>当然，比如Tomcat停止时，或者其他意外情况，也可以手动调用此处方法触发提交到阿里云日志服务中的命令
	 * @return {@link PutLogsResponse} 提交返回的结果
	 * @throws LogException
	 */
	public PutLogsResponse cacheCommit() throws LogException{
		PutLogsResponse r = saveByGroup("", "", logGroupCache);
		if(r != null && r.GetRequestId() != null && r.GetRequestId().length() > 0){
			//数据提交成功

			//清空日志缓存及条数
			logGroupCache.clear();
			//同步最后提交时间
			int currentTime = DateUtil.timeForUnix10();	//当前时间
			if(lastSubmitTime < currentTime){
				lastSubmitTime = currentTime;
			}
		}
        return r;
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
	 * @return 自动加入了当前时间的 {@link LogItem}
	 */
	public LogItem newLogItem(){
		return new LogItem((int) (new Date().getTime() / 1000));
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
	 * @return {@link QueriedLog}数组，将每条日志内容都详细列出来。若返回null，则是读取失败
	 * @throws LogException 
	 */
	public ArrayList<QueriedLog> queryList(String query, String topic, int startTime, int endTime, int offset, int line, boolean reverse) throws LogException{
    	GetLogsResponse res4 = null;
    	//对于每个 log offset,一次读取 10 行 log，如果读取失败，最多重复读取 3 次。
    	for (int retry_time = 0; retry_time < 3; retry_time++) {
    		GetLogsRequest req4 = new GetLogsRequest(project, logstore, startTime, endTime, topic, query, offset, line, reverse);
    		res4 = client.GetLogs(req4);
    		if (res4 != null && res4.IsCompleted()) {
    			break;
    		}
    	}
    	if(res4 == null){
    		return null;
    	}
        return res4.GetLogs();
	}
	
	public static void main(String args[]) throws LogException, InterruptedException, NotReturnValueException {
	        AliyunLogUtil aliyunLogUtil = new AliyunLogUtil("cn-hongkong.log.aliyuncs.com", "121212", "121212", "requestlog", "fangwen");
		
	        ArrayList<QueriedLog> qlList = aliyunLogUtil.queryList("*", "leiwen.wang.market", DateUtil.timeForUnix10()-10000000, DateUtil.timeForUnix10(), 0, 100, true);
        	for (int i = 0; i < qlList.size(); i++) {
        		System.out.println(i);
        		QueriedLog ll = qlList.get(i);
        		LogItem li = ll.GetLogItem();
        		System.out.println(li.GetLogContents().toString());
        	} 
        	System.out.println("qlList  :  "+qlList.size());
	    }

	
	
}
