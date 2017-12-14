package cn.itcast.core.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理中心控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CenterAction {

	// 显示后台管理（通用）
	/**
	 * 将用户输入的url路径，取出关键部分，直接转发到指定jsp页面
	 * 
	 * @param pageName
	 * @return
	 */
	@RequestMapping(value = "/console/{pageName}.do")
	public String consoleShow(@PathVariable(value = "pageName") String pageName) {
		System.out.println("pageName" + pageName);
		return pageName;
	}

	// frame 框架
	@RequestMapping(value = "/console/frame/{pageName}.do")
	public String consoleFrameShow(@PathVariable(value = "pageName") String pageName) {
		System.out.println("pageName" + pageName);
		return "frame/" + pageName;
	}

	
	
}
