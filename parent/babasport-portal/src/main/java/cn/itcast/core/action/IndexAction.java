package cn.itcast.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.SolrService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 首页控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexAction {

	@Autowired
	private SolrService solrService;

	@Autowired
	private BrandService brandService;

	// 显示首页
	@RequestMapping(value = "/")
	public String showIndex() {
		return "index";
	}

	// 首页搜索
	@RequestMapping(value = "/search")
	public String indexSearch(Model model, String keyword, String sort,
			Integer pageNum, Integer pageSize,Long brandId,Float pa,Float pb) throws SolrServerException {
		keyword = Encoding.encodeGetRequest(keyword);
		System.out.println("keyword:" + keyword);

		System.out.println("sort:" + sort);

		
		Page<SuperPojo> pageSuperPojos = solrService
				.findProductByKeyword(keyword, sort, pageNum, pageSize,brandId,pa,pb);

		System.out.println(
				"SuperPojos size:" + pageSuperPojos.getResult().size());

		model.addAttribute("pageSuperPojos", pageSuperPojos);

		// 回显查询的关键字
		model.addAttribute("keyword", keyword);

		// 回显页面用户上一次的排序规则
		model.addAttribute("sort2", sort);

		// 切换排序
		if (sort.equals("price asc")) {
			sort = "price desc";
		} else if (sort.equals("price desc")) {
			sort = "price asc";
		}

		model.addAttribute("sort", sort);

		// 从redis中查询所有品牌，并传给页面
		List<Brand> brands = brandService.findAllFromRedis();
		System.out.println("brands size:" + brands.size());
		model.addAttribute("brands", brands);
		
		//将brandId回传给页面
		model.addAttribute("brandId", brandId);
		
		//回传价格给页面
		model.addAttribute("pa", pa);
		model.addAttribute("pb", pb);
		
		//将用户选择的品牌名称放入map中
		Map<String, String> hashMap = new HashMap<String, String>();
		
		for (Brand brand : brands) {
			if(brand.getId()==brandId)
			{
				hashMap.put("品牌",brand.getName());
				break;
			}
		}
		
		//将用户选择的价格放入map中
		if(pa!=null&&pb!=null)
		{
			if(pb==-1)
			{
				hashMap.put("价格", pa+"以上");
			}
			else
			{
				hashMap.put("价格", pa+"-"+pb);
			}
		}
		
		model.addAttribute("map", hashMap);

		return "search";
	}

}
