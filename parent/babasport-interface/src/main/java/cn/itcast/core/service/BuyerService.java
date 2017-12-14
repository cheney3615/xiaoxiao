package cn.itcast.core.service;

import cn.itcast.core.pojo.Buyer;

/**
 * 用户（买家）服务类接口
 * @author Administrator
 *
 */
public interface BuyerService {
	
	/**
	 * 根据用户名查询用户对象
	 * @param username
	 * @return
	 */
	public Buyer findByUsername(String username);

}
