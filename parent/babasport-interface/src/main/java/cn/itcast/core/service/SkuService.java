package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存管理服务接口
 * 
 * @author Administrator
 *
 */
public interface SkuService {

	/**
	 * 根据商品id查询该商品的库存信息
	 * 
	 * @param productId
	 * @return
	 */
	public List<Sku> findByProductId(Long productId);
	
	/**
	 * 修改库存信息
	 * @param sku
	 */
	public void update(Sku sku);
	
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
