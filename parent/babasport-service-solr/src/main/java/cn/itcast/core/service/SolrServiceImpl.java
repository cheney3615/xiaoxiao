package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务实现类
 * 
 * @author Administrator
 *
 */
@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private HttpSolrServer solrServer;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private SkuDAO skuDAO;

	@Override // price asc price desc
	public Page<SuperPojo> findProductByKeyword(String keyword, String sort,
			Integer pageNum, Integer pageSize, Long brandId, Float pa, Float pb)
			throws SolrServerException {

		ArrayList<SuperPojo> arrayList = new ArrayList<SuperPojo>();

		// 创建solr搜索对象，并加载搜索条件
		SolrQuery solrQuery = new SolrQuery("name_ik:" + keyword);

		// 过滤品牌
		if (brandId != null) {
			// 添加过滤条件
			solrQuery.addFilterQuery("brandId:" + brandId);
		}

		// 过滤价格
		if (pa != null && pb != null) {
			if (pb == -1) {
				solrQuery.addFilterQuery("price:[" + pa + " TO *]");
			} else {
				solrQuery.addFilterQuery("price:[" + pa + " TO " + pb + "]");
			}
		}

		if (sort != null && sort.length() > 0) {
			// 设置排序
			solrQuery.addSort(
					new SortClause(sort.split(" ")[0], sort.split(" ")[1]));
		}

		// 设置高亮
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		solrQuery.addHighlightField("name_ik");

		// 分页

		Page page = new Page(pageNum, pageSize);

		solrQuery.setStart(page.getStartRow());
		solrQuery.setRows(page.getPageSize());

		// 开始搜索
		QueryResponse response = solrServer.query(solrQuery);

		// 遍历搜索结果
		SolrDocumentList results = response.getResults();

		// 总数量
		long numFound = results.getNumFound();
		page.setTotal(numFound);

		// 获得高亮的数据 大map的key=id 小map的 高亮字段名
		Map<String, Map<String, List<String>>> highlighting = response
				.getHighlighting();

		for (SolrDocument solrDocument : results) {

			// 将搜索结果装入到superpojo中
			String id = (String) solrDocument.get("id");
			String name = (String) solrDocument.get("name_ik");
			Float price = (Float) solrDocument.get("price");
			String url = (String) solrDocument.get("url");
			Long brandId2 = (Long) solrDocument.get("brandId");

			// 将结果集中的信息封装到商品对象中
			// 注意：由于原商品对象中并没有价格属性，而价格属性本应该是在商品对象的子对象库存对象中，
			// 而本次设计并不打算使用类似于hibernate的在pojo中做对象的相应关联，所以这里，我们可以使用万能对象来装载数据
			// 一个万能对象就可以等同于从数据库查询（包括连接查询）出的结果表中的一条数据
			SuperPojo superPojo = new SuperPojo();
			superPojo.setProperty("id", id);

			// superPojo.setProperty("name", name);

			// 填充高亮后的name
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			String name2 = list.get(0);

			superPojo.setProperty("name", name2);

			superPojo.setProperty("price", price);
			superPojo.setProperty("url", url);
			superPojo.setProperty("brandId", brandId2);

			// 将万能商品对象添加到集合中
			arrayList.add(superPojo);

		}
		// 将查询出来的数据封装到page里面的result中
		page.setResult(arrayList);

		return page;
	}

	@Override
	public void addProduct(String ids) throws SolrServerException, IOException {

		// ids的list
		List arrayList = new ArrayList();

		// 切割ids
		String[] split = ids.split(",");
		
		for (String string : split) {
			Long id = Long.parseLong(string);
			arrayList.add(id);
		}

		// 1、保存上架的商品信息 到solr索引库中
		// 查询ids中的所有商品
		Example example = new Example(Product.class);
		example.createCriteria().andIn("id", arrayList);
		List<Product> products = productDAO.selectByExample(example);

		System.out.println("psize:" + products.size());

		for (Product product2 : products) {

			// 创建文档对象
			SolrInputDocument document = new SolrInputDocument();

			// 商品id
			Long id = product2.getId();
			System.out.println("id:" + id);
			document.addField("id", id);

			// 商品名称
			String name = product2.getName();
			document.addField("name_ik", name);

			// 商品图片
			String iu = product2.getImgUrl().split(",")[0];
			document.addField("url", iu);

			// 品牌id
			Long brandId = product2.getBrandId();
			document.addField("brandId", brandId);

			// 商品最低价格
			Example example2 = new Example(Sku.class);
			example2.createCriteria().andEqualTo("productId", product2.getId());
			example2.setOrderByClause("price asc");

			PageHelper.startPage(1, 1);// 开始分页 limit
			skuDAO.selectByExample(example2);
			Page endPage = PageHelper.endPage();// 结束分页

			List result = endPage.getResult();
			Sku sku = (Sku) result.get(0);

			// 该商品的最低价格
			Float price = sku.getPrice();
			document.addField("price", price);

			solrServer.add(document);
			solrServer.commit();
		}

	}

}
