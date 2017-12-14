package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌管理服务接口
 * 
 * @author Administrator
 *
 */
public interface BrandService {

	/**
	 * 根据条件查询
	 * @param brand 
	 * @param pageNum 当前页
	 * @param pageSize 每页显示数量
	 * @return
	 */
	public Page<Brand> findByExample(Brand brand,Integer pageNum,Integer pageSize);

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
	
	/**
	 * 从redis中获得所有品牌
	 * @return
	 */
	public List<Brand> findAllFromRedis();
	
}
