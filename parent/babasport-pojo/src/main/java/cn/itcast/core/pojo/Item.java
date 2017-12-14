package cn.itcast.core.pojo;

import java.io.Serializable;

/**
 * 购物车中的每一条项
 * @author Administrator
 *
 */
public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SuperPojo sku;//复合型的sku
	
	private Long skuId;//库存id （单独提出来方便操作）
	
	private Integer amount;//购买数量
	
	//有货无货的标识属性
	private Boolean isHave = true;
	
	
	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public SuperPojo getSku() {
		return sku;
	}

	public void setSku(SuperPojo sku) {
		this.sku = sku;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
