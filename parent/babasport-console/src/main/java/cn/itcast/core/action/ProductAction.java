package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	// product 产品 通用
	@RequestMapping(value = "/console/product/{pageName}.do")
	public String consoleProductShow(
			@PathVariable(value = "pageName") String pageName) {
		System.out.println("pageName" + pageName);
		return "product/" + pageName;
	}

	// 显示商品列表
	@RequestMapping(value = "/console/product/list.do")
	public String consoleProductShowList(Model model, String name, Long brandId,
			Integer isShow, Integer pageNum, Integer pageSize) {
		name = Encoding.encodeGetRequest(name);
		System.out.println("name:" + name);

		// 封装查询条件
		Product product = new Product();
		product.setName(name);

		Page<Product> pageProducts = productService.findByExample(product,
				pageNum, pageSize);

		System.out.println("size:" + pageProducts.getResult().size());

		model.addAttribute("pageProducts", pageProducts);

		// 回显查询条件到页面
		model.addAttribute("name", name);

		return "product/list";
	}

	// 显示商品添加页面
	@RequestMapping(value = "/console/product/showAdd.do")
	public String consoleProductShowAdd(Model model, Long brandId) {

		System.out.println("显示商品的添加页面");

		List<Color> colors = productService.findEnableColors();
		model.addAttribute("colors", colors);

		return "product/add";
	}

	// 执行商品添加
	@RequestMapping(value = "/console/product/doAdd.do")
	public String consoleProductDoAdd(Product product) {
		System.out.println("执行商品添加");
		System.out.println(product);
		productService.add(product);

		return "redirect:list.do";
	}
	
	//商品的上下架
	@RequestMapping(value = "/console/product/isShow.do")
	public String consoleProductIsShow(String ids,Integer flag) throws SolrServerException, IOException {
		
		//封装条件
		Product product = new Product();
		product.setIsShow(flag);
		
		productService.update(product, ids);

		return "redirect:list.do";
	}

}
