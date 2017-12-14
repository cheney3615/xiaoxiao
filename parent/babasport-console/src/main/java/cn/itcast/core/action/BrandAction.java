package cn.itcast.core.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class BrandAction {

	@Autowired
	private BrandService brandService;

	// brand 品牌 通用
	@RequestMapping(value = "/console/brand/{pageName}.do")
	public String consoleBrandShow(Model model,
			@PathVariable(value = "pageName") String pageName, String name,
			Integer isDisplay, Integer pageNum, Integer pageSize) {
		System.out.println("pageName" + pageName);

		System.out.println("通用haha");

		return "brand/" + pageName;
	}

	// 显示品牌列表
	@RequestMapping(value = "/console/brand/list.do")
	public String consoleBrandShowList(Model model, String name,
			Integer isDisplay, Integer pageNum, Integer pageSize) {
		System.out.println("特殊xixi");

		System.out.println("显示品牌列表");

		name = Encoding.encodeGetRequest(name);

		System.out.println("name:" + name);
		System.out.println("isDisplay:" + isDisplay);

		// 封装条件到brand对象中
		Brand brand = new Brand();
		brand.setName(name);
		brand.setIsDisplay(isDisplay);

		Page<Brand> pageBrands = brandService.findByExample(brand, pageNum,
				pageSize);
		System.out.println(pageBrands.getResult().size());

		// 将它传递给页面 ，页面就可以通过jstl取得它
		model.addAttribute("pageBrands", pageBrands);

		// 将查询条件进行回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);

		return "brand/list";
	}

	// 显示品牌修改页面
	@RequestMapping(value = "/console/brand/showEdit.do")
	public String consoleBrandShowEdit(Model model, Long brandId) {

		System.out.println("brandId:" + brandId);

		Brand brand = brandService.findById(brandId);
		System.out.println(brand);
		model.addAttribute("brand", brand);

		return "brand/edit";
	}

	// 执行品牌修改
	@RequestMapping(value = "/console/brand/doEdit.do")
	public String consoleBrandDoEdit(Model model, Brand brand) {
		System.out.println(brand);

		brandService.updateById(brand);

		return "redirect:list.do";
	}

	// 执行品牌删除
	@RequestMapping(value = "/console/brand/doDelete.do")
	public String consoleBrandDoDelete(String ids) {

		System.out.println("ids:" + ids);
		brandService.deleteByIds(ids);

		return "redirect:list.do";
	}

}
