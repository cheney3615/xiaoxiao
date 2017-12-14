package cn.itcast.core.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 静态化服务器实现类
 * 
 * @author Administrator
 *
 */
@Service("staticPageService")
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Override
	public void staticProductPage(Map<String, Object> map, String id) throws IOException, TemplateException {
		
//		// 配置器
//		Configuration configuration = new Configuration();
//		
//		// 模版文件目录
//		String mPath = servletContext.getRealPath("/WEB-INF/ftl");
//		System.out.println(mPath);
//		
//		//设置模版目录
//		configuration.setDirectoryForTemplateLoading(new File(mPath));
		
		
		//使用spring的Freemarker配置获得标准的Freemark配置器
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		
		
		//生成的文件的位置
		String hPath = servletContext.getRealPath("/html/product/"+id+".html");
		System.out.println(hPath);
		
		// 获得最终文件的父文件（目录）
		File file = new File(hPath);
		File parentFile = file.getParentFile();
		
		// 如果父目录不存在，则进行创建
		if(!parentFile.exists())
		{
			parentFile.mkdir();
		}
		
		//加载模版文件
		Template template = configuration.getTemplate("product.html");
		
		// 设置输出文件的位置
		FileWriter fileWriter = new FileWriter(new File(hPath));
		
		// 开始输出
		template.process(map, fileWriter);
		
	}
	
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}
}
