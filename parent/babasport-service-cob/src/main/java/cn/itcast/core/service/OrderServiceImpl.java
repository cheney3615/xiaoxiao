package cn.itcast.core.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.dao.DetailDAO;
import cn.itcast.core.dao.OrderDAO;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Detail;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.Order;
import redis.clients.jedis.Jedis;

/**
 * 订单服务实现类
 * 
 * @author Administrator
 *
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private DetailDAO detailDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private CartService cartService;

	@Override
	public void addOrderAndDetail(Order order, String username)
			throws JsonParseException, JsonMappingException, IOException {

		// 获得订单id
		Long incr = jedis.incr("oid");
		order.setId(incr);

		// 获得用户的购物车(简单的购物车)
		Cart cart = cartService.getCartFormRedis(username);
		// 填充购物车
		cart = cartService.fillItemsSkus(cart);

		// 从购物车中取出相关信息放入订单对象中
		order.setDeliverFee(cart.getFee()); // 运费
		order.setTotalPrice(cart.getTotalPrice()); // 总价格
		order.setOrderPrice(cart.getProductPrice()); // 商品总价格

		// 设置订单的支付状态
		// 支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
		if (order.getPaymentWay() == 1) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}

		// 设置订单状态
		// 订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(0);

		// 设置时间
		order.setCreateDate(new Date());

		// 在用户注册的时候，将成功注册的用户名和用户id对应着放入到redis
		// 设置用户id
		Long buyerId = Long.parseLong(jedis.get(username));
		order.setBuyerId(buyerId);

		// 添加订单对象
		orderDAO.insert(order);
		
		System.out.println("oid:"+order.getId());

		// 添加订单详情
		List<Item> items = cart.getItems();
		for (Item item : items) {
			Detail detail = new Detail();
			detail.setProductId(
					Long.parseLong(item.getSku().get("product_id") + ""));
			detail.setProductName(
					String.valueOf(item.getSku().get("productName")));
			detail.setColor(item.getSku().get("colorname") + "");
			detail.setSize(item.getSku().get("size") + "");
			detail.setPrice(Float.parseFloat(item.getSku().get("price") + ""));
			detail.setAmount(item.getAmount());

			//添加订单id到订单详情对象中
			detail.setOrderId(order.getId());
			
			detailDAO.insert(detail);
			
			//删除redis中的购物车
			jedis.del("cart:"+username);
		}
	}

}
