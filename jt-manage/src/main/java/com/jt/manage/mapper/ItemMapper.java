package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	
	/**
	 * Mybatis的接口中可以添加注解,完成特定的操作
	 * 说明:
	 * 	Mybatis中的直接根据映射标签后期开发的.
	 *  功能上与映射文件一致.
	 * @return
	 */
	//@Select(value="select * from item")
	//@Insert("")
	//@Delete("")
	//@Update("")
	//List<Item> selectAll();
	
	
	
	
	
	
	
	
	
	List<Item> findItemAll();
	//查询记录总数
	int findItemCount();
	
	//根据分页实现数据的查询
	/**
	 * 说明:Mybatis原生不支持多值的传递,如果需要进行多值的传递需要
	 * 进行数据的封装.
	 * 1.使用对象进行数据的封装      一般做新增或修改操作时使用
	 * 2.使用Map<key,value>进行数据的封装       其他都使用map进行数据封装  
 	 * @param begin
	 * @param rows
	 * @return
	 */
	List<Item> findItemByPage
	(@Param("begin")int begin,@Param("rows")Integer rows);
	
	//查询商品分类的名称
	String findItemCatName(Long itemCatId);
	
	//批量实现数据修改
	void updateStatus(@Param("status")int status,@Param("ids") Long[] ids);
	
	
	
	
	
	
	
	
}
