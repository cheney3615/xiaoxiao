package cn.itcast.core.tools;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义session工具类 主要是分配maosessionid
 * 
 * @author Administrator
 *
 */
public class SessionTool {

	/**
	 * 获得maosessionid  如果没有就创建，并写入到浏览器的cookie中，如果有，就直接返回
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request,
			HttpServletResponse response) {
		//判断cookie中有没有maosid
		Cookie[] cookies = request.getCookies();
		
		if(cookies!=null)
		{
			for (Cookie cookie : cookies) {
				//cookie中有maosessionid
				if(cookie.getName().equals("maosessionid"))
				{
					return cookie.getValue();
				}
			}
		}
		
		//cookie没有maosessionid ，我们要创建maosessionid
		String maosessionid=UUID.randomUUID().toString().replaceAll("-", "");
		
		//将新建maosessionid写入到cookie中
		Cookie cookie = new Cookie("maosessionid", maosessionid);
		
		// 设置cookie存活时间
		cookie.setMaxAge(-1);
		
		// 设置路径 如果不设置，端口后面的目录名称（xxx:8080/login）会对cookie存储照成影响
		cookie.setPath("/");
		
		// 设置二级跨域，由于本次课程中都是localhost，所有无需设置，但是项目正式上限后需要设置该项
		// cookie.setDomain(".babasport.com");
		
		response.addCookie(cookie);
		
		// 把cookie写回浏览器客户端
		return maosessionid;
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
