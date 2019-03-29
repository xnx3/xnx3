package com.xnx3.j2ee.util;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
	 * <pre><>:这个符号表示在……之间</pre>
	 */
	final static String[] COLUMN_GROUP = {">=","<=","=","<>",">","<"};
	
	private String where = "";		//当前的SQL查询中的 WHERE条件 
	private String orderBy = "";	//排序规则
	private String selectFrom = "";	//如 SELECT * FROM user ，只有SELECT 跟 FROM 
	private Page page;
	private HttpServletRequest request;
	private String groupBy = "";	//GROUP BY
	private String[] orderByField = {};	//允许进行OrderBy排序的数据库字段,可通过 setOrderByField()进行设置

	public Sql(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * 设置搜索的数据表列，组合WHERE
	 * <br/>如  sql.setSearchColumn(new String[]{"city=","area=","type=","id>"});
	 * @param column 列名数组。只要在数组中的都会自动从request取出来加入where。
	 * 						<b>数据表的字段名需要跟get/post传入的名字相同</b>
	 * 						如列名为createTime，可为：createTime>  。如果只传入createTime，则会使用默认的LIKE模糊搜索
	 * 						<ul>
	 * 							<li>1.支持的运算符：>=、<=、＝、>、<、<>
	 * 							<li>2.如果以下只是参数名字，则默认使用LIKE模糊搜索，如"username"，组合出来的SQL为：username LIKE '%value%'
	 * 							<li>3.如果以下使用 "id="  组合出来的SQL为：id = 1234
	 * 							<li>4.支持将时间（2016-02-18 00:00:00）自动转化为10位时间戳，列前需要加转换标示,如列为regtime，让其自动转换为10位时间戳参与SQL查询，则为regtime(date:yyyy-MM-dd hh:mm:ss)，后面的yyyy-MM-dd hh:mm:ss为get/post值传入的格式
	 * 								<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如传入list.do?regtime=2016-02-18 00:00:00 ，则组合出的SQL为： regtime > 1455724800
	 * 							<li>5.若为null，则直接忽略request跟column这两个参数
	 * 							<li>6.支持查询某列两个值中间的数据，如查询id大于等于3且小于等于300之间的数，则setColumn("id<>")、 get传入两个参数：id_start=3 、id_end=300
	 * 							<li>7.支持值为多个，仅支持1里面的基本运算符，带有时间转换的不支持。多个值以,分割，在组合WHERE时会自动加上OR，如GET加入参数 name=a,b,c 则会组合出 name = 'a'  OR name = 'b'  OR name = 'c'  
	 * 						</ul>
	 * 						<br/><br/>如：<i>String[] column = {"username","email","nickname","phone","id=","regtime(date:yyyy-MM-dd hh:mm:ss)>"};</i>
	 * @return 返回组合好的 where语句，如 WHERE a = '1' AND b = '2'，若没有，则返回 "" 空字符串。
	 */
	public String setSearchColumn(String[] column){
		if(column != null){
			//先将自定义设置的，搜索哪些列，判断出来，放入list，以供使用
			Map<String, SqlColumn> columnMap = new HashMap<String, SqlColumn>();		//sql数据表的列名  －>  SqlColumn
			String columns = ",";
			for (int i = 0; i < column.length; i++) {
				SqlColumn sqlColumn = new SqlColumn(column[i]);
				columnMap.put(sqlColumn.getColumnName(), sqlColumn);
				columns = columns +sqlColumn.getColumnName() + ",";
			}
			
			//找出传入的参数，哪些参数是有效的，将有效的加入where组合
			Enumeration<String> p = request.getParameterNames();
			while(p.hasMoreElements()){
				String name = p.nextElement();
				String sqltable_column_name = name.replace("_start", "").replace("_end", "");	//使获取到的，跟数据表的列名一样，以便判断
				
				//判断此传入的参数名，是否是配置里面指定有效的参数名
				if(columns.indexOf(","+sqltable_column_name+",") > -1){
					SqlColumn sc = columnMap.get(sqltable_column_name);
					
					//如果是大于等于 <> ，区间运算符，查询两者之间，单独判断
					if(sc.getOperators() != null && sc.getOperators().equals("<>")){
						if(name.indexOf("_start") > -1){
							String start = request.getParameter(name);
							if(start != null && start.length() > 0){
								start = inject(start);
								if(start.length() > 0){
									if(sc.getDateFormat()!=null){
										//将value转换为10位的时间戳
										start = ""+DateUtil.StringToInt(start, sc.getDateFormat());
									}
									
									if(where.equals("")){
										where=" WHERE ";
									}else{
										where = where + " AND ";
									}
									
									where = where +getSearchColumnTableName()+sc.getColumnName()+" >= "+start.replaceAll(" ", "");
								}
							}
						}else if(name.indexOf("_end") > -1){
							String end = request.getParameter(name);
							if(end != null && end.length() > 0){
								end = inject(end);
								if(end.length() > 0){
									if(sc.getDateFormat()!=null){
										//将value转换为10位的时间戳
										end = ""+DateUtil.StringToInt(end, sc.getDateFormat());
									}
									
									if(where.equals("")){
										where=" WHERE ";
									}else{
										where = where + " AND ";
									}
									
									where = where +getSearchColumnTableName()+sc.getColumnName()+" <= "+end.replaceAll(" ", "");
								}
							}
						}
					}else if(sc.getColumnName().equals(name)){
							//正常运算符，如 <、 >、 =、 <=、 >=
							String value = inject(request.getParameter(name));
							if(value.length()>0){
								if(sc.getDateFormat()!=null){
									//将value转换为10位的时间戳
									value = ""+DateUtil.StringToInt(value, sc.getDateFormat());
								}
								
								if(where.equals("")){
									where=" WHERE ";
								}else{
									where = where + " AND ";
								}
								
								//判断其值是由一个还是由多个
								String valueArray[] = {""};
								if(value.indexOf(",") > -1){
									valueArray = value.split(",");
								}else{
									valueArray[0] = value;
								}
								
								int va = 0;
								StringBuffer appendWhere = new StringBuffer();	//通过这个参数组合成的要追加的where条件，where条件的值可能只有一个，也可能由多个，多个值之间用,分割，会自动组合加上OR
								while (valueArray.length > va) {
									String val = filter(valueArray[va]);
									if(val == null || val.length() == 0){
										va++;
										continue;
									}
									
									appendWhere.append((va == 0 ? "":" OR ") + getSearchColumnTableName()+sc.getColumnName());
									if(sc.getOperators() == null ){
										appendWhere.append(" LIKE '%"+val+"%'");
									}else{
										appendWhere.append(" "+sc.getOperators()+" '"+val+"' ");
									}
									
									va++;
								}
								
								String appendW = valueArray.length > 1 ? "( "+appendWhere.toString()+" )":appendWhere.toString();
								where = where + appendW;
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
	 * @return 防注入检测字符串完毕后返回的内容。若检测到敏感词出现，则用该关键词的全角字符替换之
	 */
	public String inject(String content){
		return filter(content);
	}
	
	
	/**
	 * 防止SQL注入的关键字
	 */
	final static String[] INJECT_KEYWORD = {"'", "sitename", "net user", "xp_cmdshell", "like'", "and", "exec", "execute", "insert", "create", "drop", "table", "from", "grant", "use", "group_concat", "column_name", "information_schema.columns", "table_schema", "union", "where", "select", "delete", "update", "order", "by", "count", "chr", "mid", "master", "truncate", "char", "declare", "or", ";", "-", "--", ",", "like", "%", "#","*","+"};
	/**
	 * 防止SQL注入的关键字对应的全角字符
	 */
	final static String[] KEYWORD_FULL_STR = {"＇", "ｓｉｔｅｎａｍｅ", "ｎｅｔ　ｕｓｅｒ", "ｘｐ＿ｃｍｄｓｈｅｌｌ", "ｌｉｋｅ＇", "ａｎｄ", "ｅｘｅｃ", "ｅｘｅｃｕｔｅ", "ｉｎｓｅｒｔ", "ｃｒｅａｔｅ", "ｄｒｏｐ", "ｔａｂｌｅ", "ｆｒｏｍ", "ｇｒａｎｔ", "ｕｓｅ", "ｇｒｏｕｐ＿ｃｏｎｃａｔ", "ｃｏｌｕｍｎ＿ｎａｍｅ", "ｉｎｆｏｒｍａｔｉｏｎ＿ｓｃｈｅｍａ．ｃｏｌｕｍｎｓ", "ｔａｂｌｅ＿ｓｃｈｅｍａ", "ｕｎｉｏｎ", "ｗｈｅｒｅ", "ｓｅｌｅｃｔ", "ｄｅｌｅｔｅ", "ｕｐｄａｔｅ", "ｏｒｄｅｒ", "ｂｙ", "ｃｏｕｎｔ", "ｃｈｒ", "ｍｉｄ", "ｍａｓｔｅｒ", "ｔｒｕｎｃａｔｅ", "ｃｈａｒ", "ｄｅｃｌａｒｅ", "ｏｒ", "；", "－", "－－", "，", "ｌｉｋｅ", "％", "＃", "＊","＋"};

	/**
	 * 过滤字符串的值，防止被sql注入
	 * @param value 要过滤的字符串
	 * @return 将有危险的字符，替换为其全角字符
	 */
	public static String filter(String value){
		if(value == null){
			return null;
		}
		//原始的，未进行全部转化为小写的字符串，每次找到风险关键词后，替换完毕每一个都会更新到此处。
		String originalValue = value;	
		
		//是否发现危险字符或者关键词，如果发现了，则为true，未发现，则为false，将value原样返回即可
		boolean find = false;	 
		
		for (int i = 0; i < INJECT_KEYWORD.length; i++) {
			//统一转为小写后进行判断
			int index = originalValue.toLowerCase().indexOf(INJECT_KEYWORD[i]);
			if(index != -1){
				//发现了风险关键词，进行字符串重组
				originalValue = originalValue.substring(0, index) + KEYWORD_FULL_STR[i] + originalValue.substring(index+INJECT_KEYWORD[i].length(), originalValue.length());
				find = true;
			}
		}
		
		return originalValue;
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
	 * <br/>注意，此项对传入值需要自行进行防注入判断！
	 * <br/>注意，此项同 {@link #setOrderByField(String[])}，若在这个之后使用，则前的{@link #setOrderByField(String[])}会被覆盖掉，反之同理。
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
	 * orderBy排序，允许用户传入哪些字段进行排序（数据库的列名）
	 * <br/>若不设置此项，则orderBy排序不起作用！
	 * <br/>设置的同时，也是自动进行将用户选择进行排序，组合排序SQL
	 * <br/>注意，在此之后前若是调用 {@link #setOrderBy(String)}，则此会把之前的{@link #setOrderBy(String)}给覆盖掉
	 * <br/>可直接使用，对数据列会自动进行SQL防注入
	 * @param orderByField 允许进行排序的字段集合，在这个数组中的字段可以进行ASC、DESC排序。传入如 new String[]{"id","addtime"}
	 * @return 返回组合好的排序SQL，如" ORDER BY id DESC"，若用户自己选择的排序不在指定的排序字段中(违法，入侵系统)时，会返回空字符串
	 * 			<br/>此可忽略，提供调试使用。执行此项后，会自动将组合好的ORDER BY 存入本Sql对象中，在调用 {@link #getSql()}时自动组合上
	 */
	public String setOrderByField(String[] orderByField) {
		String ob = request.getParameter("orderBy");
		if(ob != null && ob.length()>0){
			String sc = ""; //ASC、DESC
			String value = "";	//具体的数据列
			//取得倒序还是正序，以及排序的数据列
			if(ob.indexOf("_ASC")>0){
				sc = "ASC";
				value = ob.replace("_ASC", "");
			}else if (ob.indexOf("_DESC")>0) {
				sc = "DESC";
				value = ob.replace("_DESC", "");
			}
			
			//判断数据列是否在指定的可以进行排序的数据里中，进行过滤，只有指定可以排序的数据列，才可以进行排序
			if(orderByField.length > 0){
				for (int i = 0; i < orderByField.length; i++) {
					if(orderByField[i].length() > 0 && orderByField[i].equals(value)){
						//用户选择的排序方式符合指定的排序列，那么才会进行保存排序规则，组合排序SQL
						orderBy = " ORDER BY "+orderByField[i]+" "+sc;
						return orderBy;
					}
				}
			}
		}
		
		return "";
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
