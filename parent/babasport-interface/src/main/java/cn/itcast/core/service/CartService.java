package cn.itcast.core.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;

/**
 * 购物车服务接口
 * @author Administrator
 *
 */
public interface CartService {
	
	/**
	 * 根据用户名从redis中取出购物车对象
	 * @param username
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * 将购物车对象存入到redis中
	 * @param username
	 * @param cart
	 * @throws JsonProcessingException 
	 */
	public void addCartToRedis(String username,Cart cart) throws JsonProcessingException;

	/**
	 * 填充购物车
	 * @param cart 只有skuid和amout的购物车
	 * @return 完成的购物车信息（带复合型的sku对象）
	 */
	public Cart fillItemsSkus(Cart cart);
	
}
