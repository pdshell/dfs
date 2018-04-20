package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.ItemCatResult;
import com.jt.manage.pojo.ItemCat;

public interface ItemCatService {

	List<ItemCat> findItemCat();

	List<ItemCat> findItemCatByParentId(Long parentId);
	
	//查询商品分类的全部信息
	ItemCatResult findItemCatAll();

	ItemCatResult findCacheItemCatAll();
	
}
