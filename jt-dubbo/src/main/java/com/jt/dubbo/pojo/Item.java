package com.jt.dubbo.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

@JsonIgnoreProperties(ignoreUnknown=true) //表示忽略未知字段
public class Item extends BasePojo{
	
	private Long id;		//商品的ID号
	private String title;	//商品的标题
	private String sellPoint;//商品的卖点信息
	private Long price;		//定义Long类型的目的是为了计算速度快
	private Integer num;	//商品的数量
	private String barcode;	//商品二维码信息
	private String image;	//商品的图片信息
	private Long cid;		//商品分类的ID
	private Integer status;	//默认值为1，可选值：1正常，2下架，3删除
	
	
	//为了满足页面图片展现添加该方法
	public String[] getImages(){
		
		return image.split(",");
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
