package cn.itcast.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车对象类，里面包括用户选择的多个库存商品，以及结算等相关信息
 * 
 * @author Administrator
 *
 */
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Item> items = new ArrayList<Item>();// 购物项的集合

	/**
	 * 添加单个购物项到购物车中的item集合里面
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		// 判断新添加的购物项，在原有购物项的集合中是否已经有了，如果有，叠加购买数量，如果没有，新加一个购物项在items中
		for (Item item2 : items) {
			// 购物项已经有了 叠加购买数量
			if (item.getSkuId().equals(item2.getSkuId())) {
				item2.setAmount(item2.getAmount() + item.getAmount());
				return;
			}
		}

		// 如果没有该购物项 就新建购物项到集合中
		this.items.add(item);

	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	// 总的结算相关信息、运费、价格、。。。。。

	/**
	 * 商品总数量
	 * 
	 * @return
	 */
	@JsonIgnore
	public Integer getProductAmount() {
		Integer result = 0;
		for (Item item : items) {
			result = result + item.getAmount();
		}
		return result;
	}

	/**
	 * 商品的总价格
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getProductPrice() {
		Float result = 0f;

		for (Item item : items) {
			result = result + item.getAmount()
					* Float.parseFloat(item.getSku().get("price").toString());
		}

		return result;
	}

	/**
	 * 运费
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		// 如果商品总金额小于79就收个10块钱，否则就不要钱
		if (this.getProductPrice() < 79) {
			result = 10f;
		}
		return result;
	}

	/**
	 * 计算总价格
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getTotalPrice() {
		return this.getProductPrice() + this.getFee();
	}

}
