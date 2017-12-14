package cn.itcast.core.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.SkuService;

/**
 * 库存管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class SkuAction {

	@Autowired
	private SkuService skuService;

	// sku 库存 通用
	@RequestMapping(value = "/console/sku/{pageName}.do")
	public String consoleSkuShow(
			@PathVariable(value = "pageName") String pageName) {
		System.out.println("pageName" + pageName);
		return "sku/" + pageName;
	}

	// 显示某个商品的库存列表
	@RequestMapping(value = "/console/sku/list.do")
	public String consoleSkuShowList(Model model, Long productId) {
		System.out.println("productId:" + productId);
		// 根据商品id去查询该商品的库存信息
		//List<Sku> skus = skuService.findByProductId(productId);
		
		List<SuperPojo> skus = skuService.findSkuAndColorByProductId(productId);
		
		System.out.println("skus数量：" + skus.size());
		model.addAttribute("skus", skus);

		return "sku/list";
	}

	// 执行库存修改
	@RequestMapping(value = "/console/sku/update.do")
	@ResponseBody
	public String consoleSkuDoUpdate(Sku sku) {
		System.out.println(sku);
		skuService.update(sku);
		
		return "";
	}
}
