package com.wyt.shopping.pojo;

import java.io.Serializable;

import com.wyt.shopping.utils.MyConstants;

/**
 * 
 * @ClassName: Brand
 * @Description: 品牌信息
 */
@SuppressWarnings("serial")
public class Brand implements Serializable{

		private Long id; 			// 品牌ID  bigint
		private String name; 		// 品牌名称
		private String description; // 描述
		private String imgUrl; 		// 图片URL
		private Integer sort; 		// 排序  越大越靠前   
		private Integer isDisplay; 	// 是否可用   0 不可用 1 可用
		
		
		// 用与图片回显
		public String getAllUrl() {
			if(this.imgUrl != null) {
				return MyConstants.IMG_URL + this.imgUrl;
			}
			return null;
		}
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		public Integer getSort() {
			return sort;
		}
		public void setSort(Integer sort) {
			this.sort = sort;
		}
		public Integer getIsDisplay() {
			return isDisplay;
		}
		public void setIsDisplay(Integer isDisplay) {
			this.isDisplay = isDisplay;
		}
		
}
