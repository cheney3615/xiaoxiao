package cn.itcast.core.service;

import java.io.IOException;
import java.util.Map;

import freemarker.template.TemplateException;

/**
 * 静态化页面服务接口
 * 
 * @author Administrator
 *
 */
public interface StaticPageService {
	
	/**
	 * 静态化商品详情页面
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void staticProductPage(Map<String, Object> map,String id) throws IOException, TemplateException;

}
