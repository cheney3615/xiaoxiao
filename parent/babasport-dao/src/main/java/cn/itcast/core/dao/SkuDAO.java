package cn.itcast.core.dao;

import java.util.List;

import com.github.abel533.mapper.Mapper;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

public interface SkuDAO extends Mapper<Sku>{

	/**
	 * 根据商品id查询该商品的库存信息,并且将颜色名称也带出来
	 * 
	 * @param productId
	 * @return
	 */
	public List<SuperPojo> findSkuAndColorByProductId(Long productId);
	
	/**
	 * 根据库存id查询出库存的复合对象（商品名称、颜色的名称、商品的图片）
	 * @param skuId
	 * @return
	 */
	public SuperPojo findSkuAndColorAndProductBySkuId(Long skuId);
	
}
