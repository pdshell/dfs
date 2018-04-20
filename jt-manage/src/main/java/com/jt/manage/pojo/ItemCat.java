package com.jt.manage.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

/**
 * 通过通用Mapper的形式操作数据库
 * 说明:
 * 	1.需要将pojo对象与数据表一一对应
 *  2.定义数据表的主键
 *  3.如果有自增需要设定
 * @author LYJ
 *
 */
@Table(name="tb_item_cat")
@JsonIgnoreProperties(ignoreUnknown=true) //忽略未知字段
public class ItemCat extends BasePojo{
	@Id	//关联了数据表的主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //主键自增
	private Long id;	    //商品分类的id
	
	//@Column(name="parent_id")
	private Long parentId;	//上级id
	private String name;	//商品分类的名称
	private Integer status;	//1正常，2删除
	private Integer sortOrder;	//排序号
	private Boolean isParent;   //判断是否为父级菜单
	
	/**
	 * 为了满足树形结构添加get方法
	 * Text和state
	 * state的属性如果是closed，表示这个是父节点，
	 * 它还有子节点。open代表子节点
	 */
	public String getText(){
		
		return name;
	}
	
	//根据菜单的级别不同   如果是父级菜单 用closed  如果不是父级菜单open
	public String getState(){
		
		return isParent ? "closed" : "open";
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
}
