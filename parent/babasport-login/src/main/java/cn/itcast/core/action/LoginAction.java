package cn.itcast.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.service.BuyerService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.Encryption;
import cn.itcast.core.tools.SessionTool;

/**
 * 登录管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginAction {

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SessionService sessionService;

	// 显示登录
	@RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
	public String showLogin(Model model, String returnUrl) {
		System.out.println("显示登录");
		model.addAttribute("returnUrl", returnUrl);
		return "login";
	}

	// 执行登录
	@RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
	public String doLogin(Model model, String username, String password,
			String returnUrl, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("执行登录");
		System.out.println(username);
		System.out.println(password);
		System.out.println(returnUrl);

		// 如果用户直接打开登录页面，则登录后返回首页
		if (returnUrl == null || returnUrl.length() < 1) {
			returnUrl = "http://localhost:8082/";
		}

		if (username != null) {
			if (password != null) {
				// 将username放到数据库中去查
				Buyer buyer = buyerService.findByUsername(username);

				if (buyer != null) {
					// 用户是正确、验证密码

					if (Encryption.encrypt(password)
							.equals(buyer.getPassword())) {
						// 密码也正确，做登录的事情
						System.out.println("密码也正确");

						// 登录成功

						// 获得maosid,并将它和username一起存入到redis中
						sessionService.addUsernameToRedis(
								SessionTool.getSessionID(request, response),
								username);

						return "redirect:" + returnUrl;

					} else {
						model.addAttribute("error", "密码错误");
					}

				} else {
					model.addAttribute("error", "用户名不存在");
				}

			} else {
				model.addAttribute("error", "密码不能为空");
			}
		} else {
			// 用户名为空，必需填写
			model.addAttribute("error", "用户名不能为空");
		}

		model.addAttribute("returnUrl", returnUrl);

		return "login";
	}

	// 验证用户是否登录，并返回登录的用户名
	@RequestMapping(value = "/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback,HttpServletRequest request,
			HttpServletResponse response) {
		
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));
		
		System.out.println("登录的username:"+username);
		
		System.out.println("callback:"+callback);
		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(username);
		mappingJacksonValue.setJsonpFunction(callback);
		
		System.out.println(mappingJacksonValue.toString());
		System.out.println(mappingJacksonValue.getValue());
		
		
		return mappingJacksonValue;
	}
}
