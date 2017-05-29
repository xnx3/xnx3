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
	 * 默认向上列出3页跳转、向下列出3页跳转
	 * {@link #getUpList()} {@link #getNextList()}会用到
	 */
	private int listNumber = 3;
	
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
	private int upPageNumber;	//上一页的编号
	private int nextPageNumber;	//上一页的编号
	private List<TagA> upList;	//向上的list分页标签
	private List<TagA> nextList;	//向下的分页list标签
	
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
			this.upPageNumber = currentPage-1;
			this.upPage = generateUrl(this.upPageNumber);
		}else{
			this.upPageNumber = 1;
			this.upPage = this.firstPage;
		}
		
		//生成下一页的URL
		if(currentPage<lastPageNumber){
			this.nextPageNumber = currentPage+1;
			this.nextPage=generateUrl(this.nextPageNumber);
		}else{
			this.nextPage=this.lastPage;
			this.nextPageNumber = this.lastPageNumber;
		}
		
		this.haveNextPage = currentPage<lastPageNumber;	//是否还有下一页
		this.haveUpPage = currentPage>1;		//是否还有上一页
		
		this.currentFirstPage = currentPage == 1;		//当前页是否是第一页
		this.currentLastPage = currentPage == this.lastPageNumber;		//当前页是否是最后一页
	}
	
	/**
	 * 创建Page对象，如果不是B/S，可以使用此创建
	 * @param allRecordNumber 共多少条
	 * @param everyNumber 每页多少条
	 * @return
	 */
	public Page(int allRecordNumber , int everyNumber){
		upList = new ArrayList<TagA>();
		nextList = new ArrayList<TagA>();
		this.allRecordNumber = allRecordNumber;
		this.everyNumber = everyNumber;
		
		//计算一共有多少页，最后一页是第几页
		if(allRecordNumber == 0){
			this.lastPageNumber = 1;
		}else{
			this.lastPageNumber = (int) Math.ceil((float)allRecordNumber/everyNumber);
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
		if(request == null){
			this.url = "";
		}
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
		
		setUrlByStringUrl(url);
	}
	
	/**
	 * 传入当前页面的完整url（会自动过滤掉当前第几页的参数，以方便生成上一页、下一页等等链接）
	 * <br/>若是bs应用，可直接使用 {@link #setUrl(HttpServletRequest)} 传入request，会全自动生成
	 * @param url 生成的首页、上一页等链接地址的URL前缀，如 http://aaa.xnx3.com/index.php?id=3&pageCurrent=5&status=4
	 */
	public void setUrlByStringUrl(String url) {
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
	 * 设置当前第几页
	 * 此必须是设置了 {@link #setUrlByStringUrl(String)}之后才可以调用此方法
	 * @return
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
		if(this.currentPageNumber == 0){
			this.currentPageNumber = 1;
		}
		
		this.limitStart = (this.currentPageNumber-1)*this.everyNumber;	//开始的limit
		
		this.lastPage=generateUrl(lastPageNumber);	//生成尾页url
		this.firstPage=generateUrl(1);	//生成首页，第一页URL
		
		//上一页的链接URL
		if(this.currentPageNumber>1){
			this.upPageNumber = this.currentPageNumber-1;
			this.upPage = generateUrl(this.upPageNumber);
		}else{
			this.upPage = this.firstPage;
			this.upPageNumber = 1;
		}
		
		//生成下一页的URL
		if(this.currentPageNumber<lastPageNumber){
			this.nextPageNumber = this.currentPageNumber+1;
			this.nextPage=generateUrl(this.nextPageNumber);
		}else{
			this.nextPage=this.lastPage;
			this.nextPageNumber = this.lastPageNumber;
		}
		
		this.haveNextPage = currentPageNumber<lastPageNumber;	//是否还有下一页
		this.haveUpPage = currentPageNumber>1;		//是否还有上一页
		
		this.currentFirstPage = currentPageNumber == 1;		//当前页是否是第一页
		this.currentLastPage = currentPageNumber == this.lastPageNumber;		//当前页是否是最后一页
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
				a.setPageNumber(pageNum);
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
				a.setPageNumber(pageNum);
				upList.add(0,a);
			}else{
				break;
			}
		}
		
		return upList;
	}
	
	/**
	 * 获取上一页的页面编号
	 * @return
	 */
	public int getUpPageNumber() {
		return upPageNumber;
	}

	/**
	 * 获取下一页的页面编号
	 * @return
	 */
	public int getNextPageNumber() {
		return nextPageNumber;
	}


	@Override
	public String toString() {
		return "Page [limitStart=" + limitStart + ", url=" + url
				+ ", allRecordNumber=" + allRecordNumber
				+ ", currentPageNumber=" + currentPageNumber + ", everyNumber="
				+ everyNumber + ", lastPageNumber=" + lastPageNumber
				+ ", nextPage=" + nextPage + ", upPage=" + upPage
				+ ", lastPage=" + lastPage + ", firstPage=" + firstPage
				+ ", haveNextPage=" + haveNextPage + ", haveUpPage="
				+ haveUpPage + ", currentLastPage=" + currentLastPage
				+ ", currentFirstPage=" + currentFirstPage + ", upPageNumber="
				+ upPageNumber + ", nextPageNumber=" + nextPageNumber
				+ ", upList=" + upList + ", nextList=" + nextList
				+ ", getLastPage()=" + getLastPage() + ", getFirstPage()="
				+ getFirstPage() + ", getNextPage()=" + getNextPage()
				+ ", isHaveNextPage()=" + isHaveNextPage()
				+ ", isHaveUpPage()=" + isHaveUpPage() + ", getUpPage()="
				+ getUpPage() + ", getLimitStart()=" + getLimitStart()
				+ ", getAllRecordNumber()=" + getAllRecordNumber()
				+ ", getCurrentPageNumber()=" + getCurrentPageNumber()
				+ ", getEveryNumber()=" + getEveryNumber()
				+ ", getLastPageNumber()=" + getLastPageNumber()
				+ ", isCurrentLastPage()=" + isCurrentLastPage()
				+ ", isCurrentFirstPage()=" + isCurrentFirstPage()
				+ ", getNextList()=" + getNextList() + ", getUpList()="
				+ getUpList() + ", getUpPageNumber()=" + getUpPageNumber()
				+ ", getNextPageNumber()=" + getNextPageNumber() + "]";
	}

	public static void main(String[] args) {
		Page page = new Page(110, 10);
		page.setUrlByStringUrl("");
		page.setCurrentPageNumber(6);
		
		System.out.println(page.toString());
	}

	/**
	 * 使用 {@link #getUpList()} 、 {@link #getNextList()} 获取上几页、下几页得跳转时用到，设置出现得上几页、下几页跳转按钮的个数。若不设置为默认向上列出3页跳转、向下列出3页跳转
	 * @param listNumber 设置出现得上几页、下几页跳转按钮的个数
	 */
	public void setListNumber(int listNumber) {
		this.listNumber = listNumber;
	}
	
}
