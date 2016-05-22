package com.xnx3.j2ee.util;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.xnx3.Lang;
import com.xnx3.bean.TagA;

/**
 * 分页
 * @author 管雷鸣
 *
 */
public class Page{
	/**
	 * get传入要访问的列表的页面页数的名字
	 */
	public final static String CURRENTPAGENAME="currentPage";
	/**
	 * 默认向上列出5页跳转、向下列出5页跳转
	 * {@link #getUpList()} {@link #getNextList()}会用到
	 */
	public static int listNumber = 5;
	
	private int limitStart;	//limit 的开始
	private String url;	//当前列表网址，后跟随除CURRENTPAGENAME外的其他所有参数
	private int allRecordNumber;	//总条数
	private int currentPageNumber;	//当前页面，当前第几页
	private int everyNumber;	//每页显示多少条
	private int lastPageNumber;	//最后一页是编号为几的页数
	private String nextPage;	//下一页URL
	private String upPage;		//上一页URL
	private String lastPage;	//尾页URL
	private String firstPage;	//首页，第一页URL
	private boolean haveNextPage;	//是否还有下一页
	private boolean haveUpPage;		//是否还有上一页
	private boolean currentLastPage;	//当前是否是最后一页
	private boolean currentFirstPage;	//当前是否是首页，第一页
	private List<TagA> upList;	//向上的list分页标签
	private List<TagA> nextList;	//向下的分页list标签
	private String orderBy;	//排序规则
	
	/**
	 * @param allRecordNumber 共多少条
	 * @param everyNumber 每页多少条
	 * @param request HttpServletRequest
	 * 			get方式传入值：
	 * 			<ul>
	 * 				<li>currentPage：请求第几页。若不传，默认请求第一页</li>
	 * 				<li>orderBy：排序方式，如"user.id_DESC"，若不传则sql不会拼接上ORDER BY</li>
	 * 			</ul>
	 * @return
	 */
	public Page(int allRecordNumber , int everyNumber  ,HttpServletRequest request){
		upList = new ArrayList<TagA>();
		nextList = new ArrayList<TagA>();
		this.allRecordNumber = allRecordNumber;
		this.everyNumber = everyNumber;
		setUrl(request);
		
		//当前第几页
		int currentPage = 0;
		String currentPageString = request.getParameter(CURRENTPAGENAME);
		if(!(currentPageString==null || currentPageString.length()==0)){
			currentPage = Lang.stringToInt(currentPageString, 0);
		}
		if(currentPage == 0){
			currentPage = 1;
		}
		this.currentPageNumber = currentPage;
		
		//计算一共有多少页，最后一页是第几页
		if(allRecordNumber == 0){
			this.lastPageNumber = 1;
		}else{
			this.lastPageNumber = (int) Math.ceil((float)allRecordNumber/everyNumber);
		}
		
		this.limitStart = (this.currentPageNumber-1)*this.everyNumber;	//开始的limit
		
		this.lastPage=generateUrl(lastPageNumber);	//生成尾页url
		this.firstPage=generateUrl(1);	//生成首页，第一页URL
		
		//上一页的链接URL
		if(currentPage>1){
			this.upPage = generateUrl(currentPage-1);
		}else{
			this.upPage = this.firstPage;
		}
		
		//生成下一页的URL
		if(currentPage<lastPageNumber){
			this.nextPage=generateUrl(currentPage+1);
		}else{
			this.nextPage=this.lastPage;
		}
		
		this.haveNextPage = currentPage<lastPageNumber;	//是否还有下一页
		this.haveUpPage = currentPage>1;		//是否还有上一页
		
		this.currentFirstPage = currentPage == 1;		//当前页是否是第一页
		this.currentLastPage = currentPage == this.lastPageNumber;		//当前页是否是最后一页
		
		String ob = request.getParameter("orderBy");
		if(ob != null && ob.length()>0){
			if(ob.indexOf("_ASC")>0){
				orderBy = ob.replace("_ASC", "")+" ASC";
			}else if (ob.indexOf("_DESC")>0) {
				orderBy = ob.replace("_DESC", "")+" DESC";
			}
		}
	}
	
	/**
	 * 获取最后一页的链接URL
	 * @return
	 */
	public String getLastPage() {
		return this.lastPage;
	}

	/**
	 * 获取第一页的链接URL
	 * @return
	 */
	public String getFirstPage(){
		return this.firstPage;
	}
	
	/**
	 * 获取下一页的链接URL,若没有下一页，返回的是尾页
	 * @return 
	 */
	public String getNextPage(){
		return this.nextPage;
	}
	
	/**
	 * 是否还有下一页
	 * @return true:有下一页
	 */
	public boolean isHaveNextPage(){
		return this.haveNextPage;
	}
	
	/**
	 * 是否还有上一页
	 * @return true:有上一页
	 */
	public boolean isHaveUpPage(){
		return this.haveUpPage;
	}
	
	/**
	 * 获取上一页的链接URL，若是没有上一页，返回的是第一页
	 * @return
	 */
	public String getUpPage(){
		return this.upPage;
	}
	
	/**
	 * 组合url，获得跳转的链接地址
	 * @param pageNum 链接跳转的第几页
	 * @return
	 */
	private String generateUrl(int pageNum){
		return url+(this.url.indexOf("?")>0? "&":"?")+CURRENTPAGENAME+"="+pageNum;
	}

	/**
	 * 传入当前页面的完整url（会自动过滤掉当前第几页的参数，以方便生成上一页、下一页等等链接）
	 * @param url
	 */
	private void setUrl(HttpServletRequest request) {
		String url = "http://" + request.getServerName(); //服务器地址  
		int port = request.getServerPort();	//端口号
		if(port != 80){
			url += ":"+port;
		}
		url +=request.getContextPath()+request.getServletPath();      //+项目名称  、请求页面或其他地址  
		String params = request.getQueryString();	//传递的参数
		if(params==null||params.length()==0){
		}else{
			url +="?"+params;
		}
		
		String ur = null;
		if(url!=null&&url.indexOf(CURRENTPAGENAME)>0){
			//传入的参数有当前页，需要吧currentPage这个参数给过滤掉
			ur = com.xnx3.Lang.subString(url, CURRENTPAGENAME, "&", 2);
			if(ur==null||ur.length()==0){
				ur=com.xnx3.Lang.subString(url, CURRENTPAGENAME, null, 2);
			}
			if(ur==null||ur.length()==0){
				ur = url;
			}else{
				String cp = ur;
				ur = url.replace("?"+CURRENTPAGENAME+cp, "");
				ur = ur.replace("&"+CURRENTPAGENAME+cp, "");
			}
			url =ur;
		}
		this.url = url;
	}
	
	/**
	 * 获取limit查询开始的数字
	 * @return
	 */
	public int getLimitStart() {
		return limitStart;
	}
	
	/**
	 * 获取总的记录数
	 */
	public int getAllRecordNumber() {
		return allRecordNumber;
	}
	
	/**
	 * 当前第几页
	 * @return
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
	/**
	 * 每页几条记录
	 * @return
	 */
	public int getEveryNumber() {
		return this.everyNumber;
	}

	public int getLastPageNumber() {
		return lastPageNumber;
	}
	
	/**
	 * 当前页面是否是最后一页
	 * @return
	 */
	public boolean isCurrentLastPage() {
		return currentLastPage;
	}

	/**
	 * 当前页面是否是第一页
	 * @return
	 */
	public boolean isCurrentFirstPage() {
		return currentFirstPage;
	}

	/**
	 * 向上的几页分页标签
	 * @return
	 */
	public List<TagA> getNextList() {
		nextList.clear();
		for (int i = 0; i < listNumber; i++) {
			int pageNum = this.currentPageNumber+i+1;
			if(pageNum<=this.lastPageNumber){
				TagA a = new TagA();
				a.setHref(generateUrl(pageNum));
				a.setTitle(pageNum+"");
				nextList.add(a);
			}else{
				break;
			}
		}
		
		return nextList;
	}

	/**
	 * 向下的几页分页标签
	 * @return
	 */
	public List<TagA> getUpList() {
		upList.clear();
		for (int i = 0; i < listNumber; i++) {
			int pageNum = this.currentPageNumber-i-1;
			if(pageNum>0){
				TagA a = new TagA();
				a.setHref(generateUrl(pageNum));
				a.setTitle(pageNum+"");
				upList.add(0,a);
			}else{
				break;
			}
		}
		
		return upList;
	}

	/**
	 * 排序规则，里面数值如：  user.id DESC
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}
	
	/**
	 * 排序规则，里面数值如：  user.id DESC
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	
}
