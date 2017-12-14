package cn.itcast.core.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.service.StaticPageService;
import freemarker.template.TemplateException;

public class MyMessageListener implements MessageListener {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private StaticPageService staticPageService;

	// 当监听到消息后，会进行调用
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;
		try {

			String ids = amessage.getText();
			System.out.println("cms的 productIds:" + ids);

			// 准备开始静态化
			String[] split = ids.split(",");

			for (String string : split) {
				Long productId = Long.parseLong(string);// 商品id
				System.out.println("productId:" + productId);

				// 根据商品id查询该商品的信息，以及该商品的库存
				SuperPojo superPojo = productService.findById(productId);

				// 去除颜色重复
				List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");

				//能让Freemarker识别
				HashSet<Color> hashSet = new HashSet<Color>();
				
				for (SuperPojo superPojo2 : skus) {
					
					Color color = new Color();
					
					Long colorId = (Long) superPojo2.get("color_id");
					String colorName = (String)superPojo2.get("colorName");
					
					color.setId(colorId);
					color.setName(colorName);
					
					hashSet.add(color);
				}

				HashMap hashMap = new HashMap();
				hashMap.put("colors", hashSet);
				hashMap.put("superPojo", superPojo);
				
				//进行静态化
				staticPageService.staticProductPage(hashMap, String.valueOf(productId));

			}

		} catch (JMSException | IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
