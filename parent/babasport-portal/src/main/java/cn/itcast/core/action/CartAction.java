package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.service.SkuService;
import cn.itcast.core.tools.SessionTool;

/**
 * 购物车控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CartAction {

	@Autowired
	private SkuService skuService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CartService cartService;

	// 显示购物车
	@RequestMapping(value = "/cart")
	public String showCart(Model model, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {

		Cart cart = null;// 总cart
		Cart cart1 = null;// cookie中的cart
		Cart cart2 = null;// redis中cart

		// 从cookie中取出购物车
		cart1 = this.getCartFormCookies(request);

		// 进行用户登录判断
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 如果用户已经登录，就从redis中取出购物车
		if (username != null) {
			cart2 = cartService.getCartFormRedis(username);
		}

		// 合并购物车
		cart = this.mergeCart(cart1, cart2);

		// 如果用户已经登录，
		if (username != null && cart != null) {
			// 将总cart放入到redis中
			cartService.addCartToRedis(username, cart);

			// 删除cookie中原有的购物车
			this.delCartFormCookies(request, response);
		}

		// System.out.println("cart:" + cart);

		//填充购物车
		cart=cartService.fillItemsSkus(cart);

		model.addAttribute("cart", cart);

		return "cart";
	}

	// 加入购物车
	@RequestMapping(value = "/addCart")
	public String addCart(Long skuId, Integer amount,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("加入购物车");
		System.out.println(skuId);
		System.out.println(amount);

		Cart cart = null;// 购物车

		// 从cookie中取出购物车
		cart = this.getCartFormCookies(request);

		// 进行用户登录判断
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 如果用户已经登录，就从redis中取出购物车
		if (username != null) {
			cart = cartService.getCartFormRedis(username);// 纯redis的购物车
		}

		// 如果cookie中的购物车为空，就为用户新建一个
		if (cart == null) {
			cart = new Cart();
		}

		// 创建购买项
		Item item = new Item();
		item.setSkuId(skuId);
		item.setAmount(amount);

		// 将购买项添加到购物车对象中
		cart.addItem(item);

		// 如果用户已经登录 cart反存到redis中
		if (username != null) {
			cartService.addCartToRedis(username, cart);
		} else {
			// 将cart存入cookie中
			this.addCartToCookies(cart, response);
		}
		return "redirect:/cart";
	}

	/**
	 * 从cookie中取出购物车
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Cart getCartFormCookies(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {

		// 获得cookie
		Cookie[] cookies = request.getCookies();

		// 如果cookie不是空
		if (cookies != null) {
			// 从cookie找出cart对应的json
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart")) {
					String jsonCart = cookie.getValue();
					ObjectMapper om = new ObjectMapper();
					Cart cart = om.readValue(jsonCart, Cart.class);
					return cart;
				}
			}
		}
		return null;
	}

	/**
	 * 添加购物车到cookie中（cookie中原有的会被替换）
	 * 
	 * @param cart
	 * @param response
	 * @throws JsonProcessingException
	 */
	public void addCartToCookies(Cart cart, HttpServletResponse response)
			throws JsonProcessingException {

		// 将cart对象转成json字符串
		ObjectMapper om = new ObjectMapper();
		String cartJson = om.writeValueAsString(cart);

		// 将json字符串存入到cookie中
		Cookie cookie = new Cookie("cart", cartJson);
		cookie.setMaxAge(60 * 60 * 24 * 7);
		cookie.setPath("/");

		response.addCookie(cookie);
	}

	/**
	 * 从cookie中删除购物车
	 * 
	 * @param request
	 * @param response
	 */
	public void delCartFormCookies(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}
		}

	}

	/**
	 * 合并购物车
	 * 
	 * @param cart1
	 * @param cart2
	 * @return
	 */
	public Cart mergeCart(Cart cart1, Cart cart2) {
		if (cart1 == null) {
			return cart2;
		} else if (cart2 == null) {
			return cart1;
		} else {

			List<Item> items = cart2.getItems();

			for (Item item : items) {
				cart1.addItem(item);
			}
			return cart1;
		}
	}
}
