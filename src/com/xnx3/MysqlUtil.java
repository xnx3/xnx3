package com.xnx3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql 简单操作。
 * @author 管雷鸣
 *
 */
public class MysqlUtil {
	public String driver = "com.mysql.jdbc.Driver";
	public String url = "jdbc:mysql://localhost:3306/dashixiong?useUnicode=true&characterEncoding=utf-8";
	public String username = "root";
	public String password = "";
	public Connection conn = null;
	
	/**
	 * 设置数据库连接。只设置一次即可
	 * @param url 如： "jdbc:mysql://localhost:3306/dashixiong?useUnicode=true&characterEncoding=utf-8"
	 * @param username 数据库登陆用户名
	 * @param password 数据库登陆的密码
	 */
	public MysqlUtil(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public static void main(String[] args) {
		MysqlUtil mysql = new MysqlUtil("jdbc:mysql://localhost:3306/dashixiong?useUnicode=true&characterEncoding=utf-8", "root", "");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("ctitle", "标题");
		map.put("canswer", "答案");
		mysql.insert("ti", map);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mysql.insert("ti", map);
//		List<Map<String,Object>> list = select("SELECT * FROM user LIMIT 2,6");
	}
	
	public Connection getConn() {
		if(conn == null){
			try {
				Class.forName(driver); //classLoader,加载对应驱动
				conn = (Connection) DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
		        e.printStackTrace();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	    return conn;
	}
	
	/**
	 * 插入语句 。 使用示例：
	 * <pre>
	 * 		Map<String, String> map = new HashMap<String, String>();
	 *		map.put("name", "管雷鸣");
	 *		map.put("age", "27");
	 *		MysqlUtil.insert("user", map);
	 * </pre>
	 * @param tableName 要插入的数据表的表名
	 * @param map 要插入的数据。
	 * 		<br/>&nbsp;&nbsp;&nbsp;&nbsp; key: 列名，   value:值
	 * 		<br/>&nbsp;&nbsp;&nbsp;&nbsp;使用如：  map.put("username", "管雷鸣");
	 * @return  java.sql.PreparedStatement.executeUpdate() 的执行返回值
	 */
	public int insert(String tableName, Map<String, String> map) {
	    Connection conn = getConn();
	    
	    int i = 0;
	    PreparedStatement pstmt = null;
	    try {
	    	String column = "";
	    	String values = "";
	    	List<String> paramList = new ArrayList<String>(); 
	    	for (Map.Entry<String, String> entry : map.entrySet()) {
	    		if(column != ""){
	    			column = column + ", ";
	    			values = values + ", ";
	    		}
	    		column = column+entry.getKey();
	    		values = values + "?";
	    		paramList.add(entry.getValue());
	    	}
	    	String sql = "INSERT INTO "+tableName+"("+column+") VALUES("+values+")";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        
	        for (int j = 1; j <= paramList.size(); j++) {
	        	pstmt.setString(j, paramList.get(j-1));
			}
	        i = pstmt.executeUpdate();
	        
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }finally{
	    	try {
	    		if(pstmt != null && pstmt.isClosed() == false){
	    			pstmt.close();
	    		}
//				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    return i;
	}
	
	/**
	 * 关闭数据库连接 conn
	 */
	public void closeConnect(){
		try {
			if(conn != null && conn.isClosed() == false){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行 SELECT 语句，将每条结果封装为Map，以List方式将查询到的所有结果返回 。 使用示例：
	 * <pre>
	 * 		List<Map<String,Object>> list = select("SELECT * FROM user LIMIT 2,6");
	 * </pre>
	 * @param sql 查询语句，如 SELECT * FROM user WHERE userid > 10 ORDER BY id DESC LIMIT 2,6
	 * @return 查询结果。可使用 list.get(0).get("列名")   获取某一行中，某列名的值
	 */
	public List<Map<String,Object>> select(String sql){
		Connection conn = getConn();
	    PreparedStatement pstmt;
	    List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        ResultSetMetaData rsmd = rs.getMetaData();  
    	    int colCount=rsmd.getColumnCount();
    	    List<String> colNameList=new ArrayList<String>();
    	    for(int i=0;i<colCount;i++){
    	    	colNameList.add(rsmd.getColumnName(i+1));
    	    }
    	    while(rs.next()){
    	    	Map<String, Object> map=new HashMap<String, Object>();
    	    	for(int i=0;i<colCount;i++){
    	    		String key=colNameList.get(i);
    	    		Object value=rs.getString(colNameList.get(i));
    	    		map.put(key, value); 
    	    	}
    	    	resultList.add(map);
    	    }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return resultList;
	}
	
	
	private Integer getAll() {
	    Connection conn = getConn();
	    String sql = "select * from students";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        int col = rs.getMetaData().getColumnCount();
	        while (rs.next()) {
	            for (int i = 1; i <= col; i++) {
	                if ((i == 2) && (rs.getString(i).length() < 8)) {
	                }
	             }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	
	public int update(String sql) {
	    Connection conn = getConn();
	    int i = 0;
//	    String sql = "update students set Age=1 where Name='123'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        pstmt.close();
//	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	
	public int delete(String name) {
	    Connection conn = getConn();
	    int i = 0;
	    String sql = "delete from students where Name='" + name + "'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        pstmt.close();
//	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	
}
