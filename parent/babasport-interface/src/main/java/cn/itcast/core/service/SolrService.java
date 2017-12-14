package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务接口
 * 
 * @author Administrator
 *
 */
public interface SolrService {

	/**
	 * 根据用户输入的关键字到solr服务上搜索具体的商品
	 * 
	 * @param keyword
	 * @throws SolrServerException
	 */
	public Page<SuperPojo> findProductByKeyword(String keyword, String sort,
			Integer pageNum, Integer pageSize, Long brandId,Float pa,Float pb)
			throws SolrServerException;
	
	/**
	 * 添加商品到solr服务器中
	 * 
	 * @param ids
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void addProduct(String ids) throws SolrServerException, IOException;

}
