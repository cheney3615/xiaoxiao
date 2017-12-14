package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ColorDAO;
import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 商品服务实现类
 * 
 * @author Administrator
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ColorDAO colorDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private HttpSolrServer solrServer;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public Page<Product> findByExample(Product product, Integer pageNum,
			Integer pageSize) {

		if (product.getName() == null) {
			product.setName("");
		}

		// 设置查询条件
		Example example = new Example(Product.class);
		example.createCriteria().andLike("name", "%" + product.getName() + "%");
		example.setOrderByClause("createTime desc");

		PageHelper.startPage(pageNum, pageSize);// 开始分页
		productDAO.selectByExample(example);
		Page endPage = PageHelper.endPage();// 结束分页

		return endPage;
	}

	@Override
	public List<Color> findEnableColors() {
		// 设置查询条件
		Example example = new Example(Color.class);
		example.createCriteria().andNotEqualTo("parentId", 0);
		List<Color> selectByExample = colorDAO.selectByExample(example);

		return selectByExample;
	}

	@Override
	public void add(Product product) {

		// 1、在商品表中添加商品信息

		// 刚添加的商品不上架
		product.setIsShow(0);
		product.setCreateTime(new Date());

		// 使用redis生成的商品id
		Long incr = jedis.incr("pno");
		product.setId(incr);

		productDAO.insert(product);

		System.out.println("productId:" + product.getId());

		// 2、根据商品的尺码和颜色的组合 插入信息到库存表中

		String colors = product.getColors();
		String sizes = product.getSizes();

		String[] splitColors = colors.split(",");
		String[] splitSizes = sizes.split(",");

		// 遍历颜色
		for (String colorString : splitColors) {
			Long colorId = Long.parseLong(colorString);

			// 遍历尺码
			for (String size : splitSizes) {

				Sku sku = new Sku();
				sku.setColorId(colorId);
				sku.setSize(size);

				sku.setProductId(product.getId());

				// 默认信息
				sku.setMarketPrice(1000.00f);
				sku.setPrice(800.00f);
				sku.setDeliveFee(20f);
				sku.setStock(0);
				sku.setUpperLimit(100);
				sku.setCreateTime(new Date());
				skuDAO.insert(sku);
			}
		}
	}

	@Override
	public void update(Product product, final String ids)
			throws SolrServerException, IOException {

		// ids的list
		List arrayList = new ArrayList();

		// 切割ids
		String[] split = ids.split(",");
		for (String string : split) {
			Long id = Long.parseLong(string);
			// 封装id到产品对象中
			product.setId(id);
			productDAO.updateByPrimaryKeySelective(product);

			arrayList.add(id);
		}

		// 如果是商品上架
		if (product.getIsShow() == 1) {
			// 发送商品的ids到mq中
			jmsTemplate.send("productIds", new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					return session.createTextMessage(ids);
				}
			});
		}
	}

	@Override
	public SuperPojo findById(Long productId) {

		SuperPojo superPojo = new SuperPojo();
		
		Product product = productDAO.selectByPrimaryKey(productId);

		// 查询该商品的库存
//		Example example = new Example(Sku.class);
//		example.createCriteria().andEqualTo("productId", productId);
//		List<Sku> skus = skuDAO.selectByExample(example);
		
		List<SuperPojo> skus = skuDAO.findSkuAndColorByProductId(productId);
		
		superPojo.setProperty("product", product);
		superPojo.setProperty("skus", skus);

		return superPojo;
	}
}
