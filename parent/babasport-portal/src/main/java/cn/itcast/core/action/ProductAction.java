package cn.itcast.core.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;

/**
 * 商品控制器
 * @author Administrator
 *
 */
@Controller
public class ProductAction {
	
	@Autowired
	private ProductService productService;

	// 显示单个商品页面
	@RequestMapping(value = "/product/detail")
	public String showSingleProduct(Model model,Long productId)
	{
		System.out.println("productId:"+productId);
		
		//根据商品id查询该商品的信息，以及该商品的库存
		 SuperPojo superPojo = productService.findById(productId);
		 
		//去除颜色重复
		List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");
		
		HashMap<Long, String> map = new HashMap<Long, String>();
		
		for (SuperPojo superPojo2 : skus) {
			Long colorId = (Long) superPojo2.get("color_id");
			String colorName = (String)superPojo2.get("colorName");
			map.put(colorId, colorName);
		}
		
		model.addAttribute("colors", map);
		
		model.addAttribute("superPojo", superPojo);
		
		return "product";
	}
	
}
