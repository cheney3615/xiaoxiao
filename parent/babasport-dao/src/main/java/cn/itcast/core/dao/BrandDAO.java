package cn.itcast.core.dao;

import java.util.List;

import cn.itcast.core.pojo.Brand;

/**
 * 品牌管理DAO
 * 
 * @author Administrator
 *
 */
public interface BrandDAO {

	/**
	 * 根据条件查询
	 * 
	 * @param brand
	 * @return
	 */
	public List<Brand> findByExample(Brand brand);

	/**
	 * 根据品牌id查询品牌信息
	 * 
	 * @param brandId
	 * @return
	 */
	public Brand findById(Long brandId);

	/**
	 * 根据品牌id修改品牌
	 * 
	 * @param brand
	 */
	public void updateById(Brand brand);
	
	/**
	 * 根据品牌ids删除品牌
	 * 
	 * @param brand
	 */
	public void deleteByIds(String ids);

}
