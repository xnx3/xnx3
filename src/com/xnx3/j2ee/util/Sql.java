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
	private String tableName;	//当前查询的数据表名
	
	/**
	 * 查询字段支持的运算符
	 */
	final static String[] COLUMN_GROUP = {">=","<=","=",">","<"};
	
	/**
	 * 防止SQL注入的关键字
	 */
	final static String[] INJECT_KEYWORD = {"AND","EXEC","INSERT","SELECT","DELETE","UPDATE","COUNT","MASTER","TRUNCATE","CHAR","DECLARE","OR"};
	
	/**
	 * 组合sql查询的where 条件。返回如：  WHERE a = '1' AND b = '2'
	 * @param request HttpServletRequest
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
	 * @param appendWhere 附加的查询条件，如  a=1 AND b='2' AND c='3' ORDER BY addtime DESC
	 * 						<li>没有其他附加查询条件，则为null
	 * @return 如：WHERE a = '1' AND b = '2'，若没有，则返回 "" 空字符串
	 */
	public String generateWhere(HttpServletRequest request,String[] column,String appendWhere){
		return generateWhere(request, column, appendWhere, "");
	}
	
	/**
	 * 组合sql查询的where 条件。返回如：  WHERE a = '1' AND b = '2'
	 * @param request HttpServletRequest
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
	 * @param appendWhere 附加的查询条件，如  a=1 AND b='2' AND c='3' ORDER BY addtime DESC
	 * 						<li>没有其他附加查询条件，则为null
	 * @param tableName 表名，若传入，sql查询组合时会加上表名。多表查询时可用到。
	 * @return 如：WHERE a = '1' AND b = '2'，若没有，则返回 "" 空字符串
	 */
	public String generateWhere(HttpServletRequest request,String[] column,String appendWhere,String tableName){
		this.tableName = tableName;
		String where = "";
		if(appendWhere==null){
			appendWhere = "";
		}
		
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
								where = where +getTableName()+sqlColumn.getColumnName()+" LIKE '%"+value+"%'";
							}else{
								where = where + getTableName()+sqlColumn.getColumnName()+" "+sqlColumn.getOperators()+" "+value;
							}
						}
					}
				}
			}
		}
		
		if(appendWhere.length()==0){
			return where;
		}else{
			if(where.indexOf("WHERE")>0){
				return where + " AND "+appendWhere;
			}else{
				return " WHERE "+appendWhere;
			}
		}
	}
	
	/**
	 * 获取sql查询组合时的tableName，若为空，则返回空字符串。若不为空，则返回 tableName+"."
	 * @return
	 */
	private String getTableName(){
		if(this.tableName.equals("")){
			return "";
		}else{
			return tableName+".";
		}
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
