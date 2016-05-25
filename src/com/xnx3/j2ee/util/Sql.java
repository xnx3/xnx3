package com.xnx3.j2ee.util;
import java.text.ParseException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.DateUtil;
import com.xnx3.Lang;

/**
 * sql查询相关
 * @author 管雷鸣
 *
 */
public class Sql {
	private String tableName = "";	//当前查询的数据表名
	
	/**
	 * 查询字段支持的运算符
	 */
	final static String[] COLUMN_GROUP = {">=","<=","=",">","<"};
	
	/**
	 * 防止SQL注入的关键字
	 */
	final static String[] INJECT_KEYWORD = {"AND","EXEC","INSERT","SELECT","DELETE","UPDATE","COUNT","MASTER","TRUNCATE","CHAR","DECLARE","OR"};
	
	private String where = "";		//当前的SQL查询中的 WHERE条件 
	private String orderBy = "";	//排序规则
	private String selectFrom = "";	//如 SELECT * FROM user ，只有SELECT 跟 FROM 
	private Page page;
	private HttpServletRequest request;
	private String groupBy = "";	//GROUP BY
	
	public Sql(HttpServletRequest request) {
		this.request = request;
		String ob = request.getParameter("orderBy");
		if(ob != null && ob.length()>0){
			if(ob.indexOf("_ASC")>0){
				orderBy = " ORDER BY "+ob.replace("_ASC", "")+" ASC";
			}else if (ob.indexOf("_DESC")>0) {
				orderBy = " ORDER BY "+ob.replace("_DESC", "")+" DESC";
			}
		}
	}
	
	/**
	 * 设置搜索的数据表列，组合WHERE
	 * <br/>如  sql.setSearchColumn(new String[]{"city=","area=","type=","id>"});
	 * @param column 列名数组。只要在数组中的都会自动从request取出来加入where。
	 * 						<b>数据表的字段名需要跟get/post传入的名字相同</b>
	 * 						如列名为createTime，可为：createTime>  。如果只传入createTime，则会使用默认的LIKE模糊搜索
	 * 						<li>支持的运算符：>=、<=、＝、>、<
	 * 						<li>如果以下只是参数名字，则默认使用LIKE模糊搜索，如"username"，组合出来的SQL为：username LIKE '%value%'
	 * 						<li>如果以下使用 "id="  组合出来的SQL为：id = 1234
	 * 						<li>支持将时间（2016-02-18 00:00:00）自动转化为10位时间戳，列前需要加转换标示,如列为regtime，让其自动转换为10位时间戳参与SQL查询，则为regtime(date:yyyy-MM-dd hh:mm:ss)，后面的yyyy-MM-dd hh:mm:ss为get/post值传入的格式
	 * 							<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如传入list.do?regtime=2016-02-18 00:00:00 ，则组合出的SQL为： regtime > 1455724800
	 * 						<li>若为null，则直接忽略request跟column这两个参数
	 * 						<br/><br/>如：<i>String[] column = {"username","email","nickname","phone","id=","regtime(date:yyyy-MM-dd hh:mm:ss)>"};</i>
	 * @return 返回组合好的 where语句，如 WHERE a = '1' AND b = '2'，若没有，则返回 "" 空字符串。
	 */
	public String setSearchColumn(String[] column){
		if(column!=null){
			Enumeration<String> p = request.getParameterNames();
			while(p.hasMoreElements()){
				String name = p.nextElement();
				for (int i = 0; i < column.length; i++) {
					SqlColumn sqlColumn = new SqlColumn(column[i]);
					
					if(sqlColumn.getColumnName().equals(name)){
						String value = inject(request.getParameter(name));
						if(value.length()>0){
							if(sqlColumn.getDateFormat()!=null){
								//将value转换为10位的时间戳
								try {
									value = ""+new DateUtil().StringToInt(value, sqlColumn.getDateFormat());
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							
							if(where.equals("")){
								where=" WHERE ";
							}else{
								where = where + " AND ";
							}
							
							if(sqlColumn.getOperators() == null){
								where = where +getSearchColumnTableName()+sqlColumn.getColumnName()+" LIKE '%"+value+"%'";
							}else{
								where = where + getSearchColumnTableName()+sqlColumn.getColumnName()+" "+sqlColumn.getOperators()+" '"+value+"' ";
							}
						}
					}
				}
			}
		}
		return where;
	}
	
	private String getSearchColumnTableName(){
		if(this.tableName.length()>0){
			return tableName+".";
		}else{
			return "";
		}
	}
	
	/**
	 * 创建生成的SQL
	 * @param selectFrom 如 SELECT * FROM user
	 * @param page {@link Page} 自动分页模块，LIMIT分页
	 * @return 完整的SQL语句
	 */
	public String setSelectFromAndPage(String selectFrom, Page page){
		this.selectFrom = selectFrom;
		this.page = page;
		return selectFrom+where+groupBy+orderBy+" LIMIT "+page.getLimitStart()+","+page.getEveryNumber();
	}
	
	/**
	 * 获取当前组合好的WHERE查询条件
	 * @return 如"WHERE status = 2"，若没有条件，返回""空字符串
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * 获取生成的SQL语句，同 {@link Sql#generateSql(String, Page)}生成的SQL语句
	 * @return SQL语句
	 */
	public String getSql() {
		if(page == null){
			return selectFrom+where+groupBy+orderBy;
		}else{
			return selectFrom+where+groupBy+orderBy+" LIMIT "+page.getLimitStart()+","+page.getEveryNumber();
		}
	}
	
	/**
	 * 设置 {@link #setSearchColumn(String[])} 搜索的数据表。是搜索哪个数据表里的字段。若sql只是查一个表，可不用设置此处。忽略即可
	 * <br/>只对 column设定的字段有效
	 * @param tableName
	 */
	public void setSearchTable(String tableName){
		this.tableName = tableName;
	}
	
	/**
	 * 防sql注入
	 * @param content 检查的内容
	 * @return 防注入检测字符串完毕后返回的内容。若检测到敏感词出现，返回空字符串""
	 */
	public String inject(String content){
		for (int i = 0; i < INJECT_KEYWORD.length; i++) {
			if(content.toUpperCase().indexOf(INJECT_KEYWORD[i])!=-1){
				return "";
			}
		}
		return content;
	}
	
	/**
	 * 附加WHERE查询条件，如 "status=2"。（此无防注入拦截）
	 * @return WHERE a = '1' AND b = '2'，若没有，则返回 "" 空字符串
	 */
	public String appendWhere(String appendWhere){
		if(where.indexOf("WHERE")>0){
			where = where + " AND "+appendWhere;
		}else{
			where = " WHERE "+appendWhere;
		}
		return where;
	}


	/**
	 * 获取当前计算好要使用的排序规则
	 * @return 里面数值如：  user.id DESC
	 */
	public String getOrderBy() {
		return orderBy;
	}
	
	/**
	 * 排序规则，传入的数值如： user.id DESC
	 * <br/>若设置了此项，get传入的排序方式则不起作用
	 * @param orderBy 如： user.id DESC
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = " ORDER BY "+orderBy;
	}

	/**
	 * 默认排序规则。当网址里没有传递指定排序规则时，并且此项有值，则会使用此项排序规则
	 * <br/>不会覆盖get传入的指定排序规则。
	 * @param defaultOrderBy 如user.id DESC
	 */
	public void setDefaultOrderBy(String defaultOrderBy) {
		if(orderBy == null || orderBy.length()==0){
			orderBy = " ORDER BY "+defaultOrderBy;
		}
	}
	
	/**
	 * 设置 GROUP BY 条件。
	 * @param groupBy 传入字段名如： user.id
	 */
	public void setGroupBy(String groupBy){
		this.groupBy = " GROUP BY " + groupBy;
	}
}

/**
 * 将组合的运算符跟列名拆分
 * @author 管雷鸣
 *
 */
class SqlColumn{
	private String operators;	//运算符
	private String columnName;	//列名
	private String dateFormat;	//时间格式化参数,如 yyyy-MM-dd hh:mm:ss 只针对有(date:yyy-MM-dd...)标注的有效。如果为null，则没有date标注
	
	/**
	 * 传入组合的列名，如 >create_date  或 =id
	 * @param groupColumn
	 */
	public SqlColumn(String groupColumn){
		for (int i = 0; i < Sql.COLUMN_GROUP.length; i++) {
			if(groupColumn.indexOf(Sql.COLUMN_GROUP[i])>0){
				this.operators = Sql.COLUMN_GROUP[i];
				this.columnName = groupColumn.replace(this.operators, "");
				break;
			}
		}
		
		if(this.operators==null){
			this.columnName = groupColumn;
		}
		
		/**
		 * 时间判断筛选
		 */
		boolean isDateToInt = this.columnName.indexOf("date")>0;	//判断是否需要进行时间戳转换
		if(isDateToInt){
			this.dateFormat = Lang.subString(columnName, "(date:", ")",2);
			this.columnName = this.columnName.replace("(date:"+this.dateFormat+")", "");
		}
	}
	
	/**
	 * 如果没有传入运算符，则返回null
	 * @return
	 */
	public String getOperators() {
		return operators;
	}
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}
