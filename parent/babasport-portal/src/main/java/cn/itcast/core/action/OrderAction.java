package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.Order;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 订单管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class OrderAction {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	// 去结算：显示结算页面
	// 结算 skuIds表示用户从购物车中选择的商品，本次课程先不做该功能
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(Model model, Long[] skuIds,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {

		// 获得登录的用户名
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 通过用户名获得该用户购物车 (简单购物车)
		Cart cart = cartService.getCartFormRedis(username);

		// 有货无货的标识
		boolean flag = true;

		// 判断redis里的购物车不能为空，也不能是空车子
		if (cart != null && cart.getItems().size() > 0) {
			// 填充购物车
			cart = cartService.fillItemsSkus(cart);

			// 判断库存够不够
			List<Item> items = cart.getItems();
			for (Item item : items) {
				// 该商品库存不够
				if (item.getAmount() > Integer
						.parseInt(item.getSku().get("stock").toString())) {
					flag = false;
					item.setIsHave(flag);// 标识该商品无货

				}
			}

			// 至少有一款商品无货
			if (!flag) {
				model.addAttribute("cart", cart);
				return "cart";
			}

		}
		return "order";
	}

	// 提交订单
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("提交订单");
		System.out.println(order);

		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 将订单信息添加到订单表及订单详情表中
		orderService.addOrderAndDetail(order, username);

		return "success";
	}

}
