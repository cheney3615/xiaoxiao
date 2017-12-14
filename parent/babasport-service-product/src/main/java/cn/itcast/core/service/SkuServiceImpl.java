package cn.itcast.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存服务实现类
 * 
 * @author Administrator
 *
 */
@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDAO skuDAO;

	@Override
	public List<Sku> findByProductId(Long productId) {

		//设置查询条件
		Example example = new Example(Sku.class);
		example.createCriteria().andEqualTo("productId", productId);
		
		List<Sku> selectByExample = skuDAO.selectByExample(example);

		return selectByExample;
	}

	@Override
	public void update(Sku sku) {
		//只修改sku对象非null的信息
		skuDAO.updateByPrimaryKeySelective(sku);
	}

	@Override
	public List<SuperPojo> findSkuAndColorByProductId(Long productId) {
		
		List<SuperPojo> skus = skuDAO.findSkuAndColorByProductId(productId);
		return skus;
	}

	@Override
	public SuperPojo findSkuAndColorAndProductBySkuId(Long skuId) {
		
		SuperPojo sku = skuDAO.findSkuAndColorAndProductBySkuId(skuId);
		
		return sku;
	}
	

}
