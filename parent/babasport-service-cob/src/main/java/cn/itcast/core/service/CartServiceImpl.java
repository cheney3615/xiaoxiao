package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import redis.clients.jedis.Jedis;

/**
 * 购物车服务实现类
 * 
 * @author Administrator
 *
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

	@Autowired
	private Jedis jedis;
	
	@Autowired
	private SkuService skuService;

	@Override
	public Cart getCartFormRedis(String username)
			throws JsonParseException, JsonMappingException, IOException {

		// 从redis中根据用户名获得cart json字符串
		String cartJson = jedis.get("cart:" + username);

		if (cartJson == null) {
			return null;
		}

		// 将cart json转成cart对象
		ObjectMapper om = new ObjectMapper();
		Cart cart = om.readValue(cartJson, Cart.class);

		return cart;
	}

	@Override
	public void addCartToRedis(String username, Cart cart)
			throws JsonProcessingException {

		// 将cart对象转成json
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		String cartJson = om.writeValueAsString(cart);

		// 将json配合用户名存入到redis中
		jedis.set("cart:" + username, cartJson);

	}

	@Override
	public Cart fillItemsSkus(Cart cart) {

		if (cart != null) {
			// 填充cart中的items的superpojo（sku）
			List<Item> items = cart.getItems();
			for (Item item : items) {
				Long skuId = item.getSkuId();
				SuperPojo sku = skuService
						.findSkuAndColorAndProductBySkuId(skuId);
				item.setSku(sku);
			}

			System.out.println("itemsize:" + cart.getItems().size());
		}
		return cart;
	}

}
