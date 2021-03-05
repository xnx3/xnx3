package com.xnx3.language;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xnx3.Language;
import com.xnx3.j2ee.util.CookieUtil;

/**
 * 语言包{@link Language}的扩展，在java web 项目中使用。
 * @author 管雷鸣
 */
public class LanguageWeb extends Language{
	/**
	 * 设置当前使用的语言包
	 * <br/>语言包会存储到客户浏览器的session、cookie中，调取显示的时候，会直接从session、cookie中取到设置的语言包是哪个，进而显示出对应语言的文字
	 * <br/>语言包位于 web项目的src 下的 language.xml 文件，可以在其中随意增加其他语言，格式同 chinese 节点
	 * <br/>手动设置后，会自动将其加入Session、Cookies，下次打开页面时，会自动将此语言包设为默认语言包
	 * @param languagePackageName 语言包名字，位于 language.xml 的一级节点名字，如现有的 chinese 、 english
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param <ul>
	 * 				<li>true：设置成功</li>
	 * 				<li>false：设置失败，语言包中没有这个语种</li>
	 * 			</ul>
	 */
	public static boolean setCurrentLanguagePackageName(HttpServletRequest request, HttpServletResponse response, String languagePackageName) {
		//判断 language.xml 中是否有这个语言包
		if(languageMap.get(languagePackageName) != null){
			request.getSession().setAttribute(cacheLanguagePackageName, languagePackageName);
			CookieUtil cookie = new CookieUtil(request, response, 999999);
			cookie.addCookie(cacheLanguagePackageName, languagePackageName);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取当前的语言包名
	 * <br/>此方法主要是在 {@link #show(HttpServletRequest, HttpServletResponse, String)}中会用到
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return 语言包名字，获取到的字符串如： chinese、 english
	 */
	public static String getCurrentLanguagePackageName(HttpServletRequest request, HttpServletResponse response){
		String packageName = null;	//当前使用的语言包
		
		//先判断Session中是否有
		if(request.getSession() != null){
			Object obj = request.getSession().getAttribute(cacheLanguagePackageName);
			if(obj != null){
				packageName = (String) obj;
			}
		}
		
		//Session中没有，再从Cookie中取
		if(packageName == null){
			CookieUtil cookieUtil = new CookieUtil(request, response, 999999);
			Cookie cookie = cookieUtil.getCookie(cacheLanguagePackageName);
			if(cookie != null){
				packageName = cookie.getValue();
			}
		}
		
		//如果session、cookie中都没有，那么就用默认语言包
		if(packageName == null){
			packageName = Language.language_default;
		}
		
		return packageName;
	}
	
	/**
	 * 获取语言包(language.xml)中的描述文字显示出来。如果没有找到返回空字符""
	 * @param key 要调用文字的代码
	 * @return 显示出来的文字
	 */
	public static String show(HttpServletRequest request, HttpServletResponse response, String key) {
		return Language.show(getCurrentLanguagePackageName(request, response), key);
	}
	
}