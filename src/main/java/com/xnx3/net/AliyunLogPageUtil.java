package com.xnx3.net;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.util.Page;

/**
 * 阿里云日志服务，扩展操作，加入分页功能，在实际的项目中的调出分页展示
 * @author 管雷鸣
 */
public class AliyunLogPageUtil {
	AliyunLogUtil aliyunLogUtil;
	Page page;	//当前分页相关
	
	/**
	 * 创建阿里云日志服务分页的对象，这里可以传入已创建的 {@link AliyunLogUtil}对象
	 * @param aliyunLogUtil 已经实例化的阿里云日志服务对象
	 */
	public AliyunLogPageUtil(AliyunLogUtil aliyunLogUtil) {
		this.aliyunLogUtil = aliyunLogUtil;
	}
	
	/**
	 * 创建阿里云日志服务分页的对象，这里传入阿里云日志服务相关的配置参数
	 * <br/>阿里云日志服务控制台： <a href="https://sls.console.aliyun.com">https://sls.console.aliyun.com</a>
	 * @param endpoint 如 cn-hongkong.log.aliyuncs.com
	 * @param accessKeyId 阿里云访问密钥 AccessKeyId
	 * @param accessKeySecret 阿里云 AccessKeySecret
	 * @param project 日志服务中，创建的项目名称
	 * @param logstore 日志服务中，创建的项目下的日志库名称
	 * @see AliyunLogUtil#AliyunLogUtil(String, String, String, String, String)
	 */
	public AliyunLogPageUtil(String endpoint, String accessKeyId, String accessKeySecret, String project, String logstore) {
		this.aliyunLogUtil = new AliyunLogUtil(endpoint, accessKeyId, accessKeySecret, project, logstore);
	}
	
	/**
	 * 列出日志服务的列表记录
	 * <br/>获取当前生成得分页信息可用 {@link #getPage()} 得到
	 * @param query 查询条件。若divQuery为true，开启用户自定义检索，这里会先将query的查询条件作为首要查询。总之查询时无论是用户传入什么条件，都会加上此指定的查询条件一并 and 查询。(<a href="https://help.aliyun.com/document_detail/29060.html">查询语法参考</a>)
	 * @param topic topic主题，若不指定查询主题，可为空字符串""
	 * @param divQuery 是否开启用户自定义检索。若是开启，则用户可以通过<a href="https://help.aliyun.com/document_detail/29060.html">查询语法</a>进行自定义查询。GET或者POST传入queryString=查询条件
	 * @param startTime 筛选日志的开始时间，10位时间戳，若为0，则为结束时间以前1000天的时刻
	 * @param endTime 筛选日志的结束时间，10位时间戳，若为0，则为当前时间
	 * @param everyPageNumber 分页列表每页显示的条数
	 * @param request 主要用于获取当前url中的get传递的参数。分页数都是通过get传递的。同样，分页的参数也是自动根据这个计算出来的
	 * @return 返回查询的结果。
	 * @throws LogException
	 */
	public ArrayList<QueriedLog> list(String query, String topic, boolean divQuery, int startTime, int endTime, int everyPageNumber, HttpServletRequest request) throws LogException{
		if(endTime == 0){
			endTime = DateUtil.timeForUnix10();
		}
		if(startTime == 0){
			startTime = DateUtil.getDateZeroTime(endTime - 86400000);	
		}
		
		if(divQuery){
			String queryStringRequest = request.getParameter("queryString");	//查询的关键词,可模糊查询
			if(queryStringRequest == null || queryStringRequest.length() == 0){
			}else{
				if(query != null && query.length() > 0){
					query = query + " and "+queryStringRequest;
				}else{
					query = queryStringRequest;
				}
			}
		}
		
		//统计1000天内的符合条件的总数量
		int count = (int) aliyunLogUtil.queryCount(query, "", startTime, endTime);
		
		page = new Page(count, everyPageNumber, request);
		
		//最近三个月访问量统计
		ArrayList<QueriedLog> logList = aliyunLogUtil.queryList(query, "", startTime, endTime, page.getLimitStart(), page.getEveryNumber(), true);
		return logList;
	}
	
	/**
	 * 获得当前分页的信息，比如上一页、当前第几页、下一页等
	 * <br/>需要在 list() 方法之后调用，这里是 list() 所生成的分页数据 
	 * @return
	 */
	public Page getPage(){
		return page;
	}
	

	/**
	 * 获取当前页面的JSONArray的数据。该数据可以直接在Spring MVC的view中通过c:forEach遍历出后，直接进行listitem.logtime这种形式调出
	 * <br/>获取当前生成得分页信息可用 {@link #getPage()} 得到
	 * @param query 查询条件。若divQuery为true，开启用户自定义检索，这里会先将query的查询条件作为首要查询。总之查询时无论是用户传入什么条件，都会加上此指定的查询条件一并 and 查询。(<a href="https://help.aliyun.com/document_detail/29060.html">查询语法参考</a>)
	 * @param topic topic主题，若不指定查询主题，可为空字符串""
	 * @param divQuery 是否开启用户自定义检索。若是开启，则用户可以通过<a href="https://help.aliyun.com/document_detail/29060.html">查询语法</a>进行自定义查询。GET或者POST传入queryString=查询条件
	 * @param startTime 筛选日志的开始时间，10位时间戳，若为0，则为结束时间以前1000天的时刻
	 * @param endTime 筛选日志的结束时间，10位时间戳，若为0，则为当前时间
	 * @param everyPageNumber 分页列表每页显示的条数
	 * @param request 主要用于获取当前url中的get传递的参数。分页数都是通过get传递的。同样，分页的参数也是自动根据这个计算出来的
	 * @return 返回查询的结果。
	 * @throws LogException
	 */
	public JSONArray listForJSONArray(String query, String topic, boolean divQuery, int startTime, int endTime, int everyPageNumber, HttpServletRequest request) throws LogException{
		//super.list(query, topic, divQuery, startTime, endTime, everyPageNumber, request)
		ArrayList<QueriedLog> logList = list(query, topic, divQuery, startTime, endTime, everyPageNumber, request);
		JSONArray jsonArray = new JSONArray();	//访问量，pv
		for (int i = 0; i < logList.size(); i++) {
			LogItem li = logList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			jsonArray.add(json);
		}
		
		return jsonArray;
	}
	
	
	/**
	 * 获取当前页面的JSONArray的数据。该数据可以直接在Spring MVC的view中通过c:forEach遍历出后，直接进行listitem.logtime这种形式调出
	 * <br/>相比于 {@link #listForJSONArray(String, String, boolean, int, int, int, HttpServletRequest)}，这个方法默认的开始时间是100天以前，结束时间是当前时间
	 * <br/>获取当前生成得分页信息可用 {@link #getPage()} 得到
	 * @param query 查询条件。若divQuery为true，开启用户自定义检索，这里会先将query的查询条件作为首要查询。总之查询时无论是用户传入什么条件，都会加上此指定的查询条件一并 and 查询。(<a href="https://help.aliyun.com/document_detail/29060.html">查询语法参考</a>)
	 * @param topic topic主题，若不指定查询主题，可为空字符串""
	 * @param divQuery 是否开启用户自定义检索。若是开启，则用户可以通过<a href="https://help.aliyun.com/document_detail/29060.html">查询语法</a>进行自定义查询。GET或者POST传入queryString=查询条件
	 * @param everyPageNumber 分页列表每页显示的条数
	 * @param request 主要用于获取当前url中的get传递的参数。分页数都是通过get传递的。同样，分页的参数也是自动根据这个计算出来的
	 * @return 返回查询的结果。
	 * @throws LogException
	 */
	public JSONArray list(String query, String topic, boolean divQuery, int everyPageNumber, HttpServletRequest request) throws LogException{
		return listForJSONArray(query, topic, divQuery, 0, 0, everyPageNumber, request);
	}
	
}
