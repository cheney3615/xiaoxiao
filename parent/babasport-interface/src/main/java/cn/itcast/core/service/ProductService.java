package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 产品服务接口
 * @author Administrator
 *
 */
public interface ProductService {
	
	/**
	 * 根据条件查询
	 *
	 * @param product
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Product> findByExample(Product product,Integer pageNum,Integer pageSize);

	/**
	 * 查询所有可用颜色（颜色的父id不为0）
	 * 
	 * @return
	 */
	public List<Color> findEnableColors();
	
	/**
	 * 添加商品
	 * @param product
	 */
	public void add(Product product);
	
	
	/**
	 * 修改多个商品的统一信息(一样)
	 * @param product
	 * @param ids
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void update(Product product,String ids) throws SolrServerException, IOException;
	
	
	/**
	 * 根据商品id，查询单个商品信息 ,和该商品的库存信息
	 * @param productId 
	 * @return
	 */
	public SuperPojo findById(Long productId);
}
