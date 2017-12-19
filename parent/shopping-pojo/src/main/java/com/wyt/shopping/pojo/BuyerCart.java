package com.wyt.shopping.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BuyerCart implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<BuyerItem> buyerItems = new ArrayList<>();

	// 订单项装入购物车
	public void addBuyerItem(BuyerItem buyerItem) {
		if(buyerItems.contains(buyerItem)) {
			for (BuyerItem item : buyerItems) {
				if(item.equals(buyerItem)) {
					// 同类商品合并数量
					item.setAmount(buyerItem.getAmount() + item.getAmount());
				}
			}
		} else {
			// 新商品，加入购物车
			buyerItems.add(buyerItem);
		}
	}
	
	public List<BuyerItem> getBuyerItems() {
		return buyerItems;
	}

	public void setBuyerItems(List<BuyerItem> buyerItems) {
		this.buyerItems = buyerItems;
	}
	
	@JsonIgnore
	public Float getProductPrice() {
		Float price = 0f;
		for (BuyerItem buyerItem : buyerItems) {
			price += buyerItem.getAmount() * buyerItem.getSku().getPrice();
		}
		return price;
	}
	
	// 商品价格
	@JsonIgnore
	public Float getProductProice() {
		Float productPrice = 0f;
		for (BuyerItem buyerItem : buyerItems) {
			productPrice += buyerItem.getSku().getPrice() * buyerItem.getAmount();
		}
		return productPrice;
	}
	
	// 商品运费
	@JsonIgnore
	public Float getFee() {
		Float fee = 0f;
		if (this.getProductPrice() < 99f) {
			fee = 10f;
		}
		return fee;
	}
	
	// 商品数量
	@JsonIgnore
	public Integer getProductAmount() {
		Integer productAmount = 0;
		for (BuyerItem buyerItem : buyerItems) {
			productAmount += buyerItem.getAmount();
		}
		return productAmount;
	}
	
	// 商品总价格（运费+商品价格）
	@JsonIgnore
	public Float getTotalPrice() {
		return this.getFee() + this.getProductPrice();
	}
	
}
