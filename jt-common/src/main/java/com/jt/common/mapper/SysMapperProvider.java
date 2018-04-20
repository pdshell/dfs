package com.jt.common.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;

import com.github.abel533.mapper.MapperProvider;
import com.github.abel533.mapperhelper.EntityHelper;
import com.github.abel533.mapperhelper.MapperHelper;
//当前类是通用Mapper的扩展类
public class SysMapperProvider extends MapperProvider {

    public SysMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode deleteByIDS(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        Set<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        EntityHelper.EntityColumn column = null;
        for (EntityHelper.EntityColumn entityColumn : entityColumns) {
            column = entityColumn;
            break;
        }
        
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        // 开始拼sql
        BEGIN();
        // delete from table
        DELETE_FROM(tableName(entityClass));
        // 得到sql
        String sql = SQL();
        // 静态SQL部分
        sqlNodes.add(new StaticTextSqlNode(sql + " WHERE " + column.getColumn() + " IN "));
        // 构造foreach sql
        SqlNode foreach = new ForEachSqlNode(ms.getConfiguration(), new StaticTextSqlNode("#{"
                + column.getProperty() + "}"), "ids", "index", column.getProperty(), "(", ")", ",");
        sqlNodes.add(foreach);
        return new MixedSqlNode(sqlNodes);
    }
    
    
    /**
     * 目的:获取表的名称
     * 1.获取方法的执行的路径
     * 2.获取Mapper的接口路径
     * 3.通过反射的机制获取Mapper的类型
     * 4.通过Mapper的类型获取父级接口类型
     * 	 1.判断父级接口是否为泛型
     * 5.获取泛型中的类型 Item
     * 6.获取item类型的注解
     * 7.获取表的名称
     * @param ms
     * @return
     */
    public SqlNode findCount(MappedStatement ms){
    	
    	//1.获取当前正在执行的Mapper的方法路径
    	//com.jt.manage.mapper.itemMapper.findCount()
    	try {
        	//1.获取客户端调用的方法 
    		//com.jt.manage.mapper.ItemMapper.findCount()
        	String methodPath = ms.getId();
        	
        	//2.获取ItemMapper的字符串  com.jt.manage.mapper.ItemMapper
        	String targetPath = methodPath.substring(0, methodPath.lastIndexOf("."));
        	
        	//3.获取ItemMapper对象   ItemMapper.Class
        	Class<?> targetClass = Class.forName(targetPath);
        	
        	//4.获取ItemMapper的父级接口 由于接口是可以多继承的
        	Type[] types = targetClass.getGenericInterfaces();
        	
        	//5.获取SysMapper
        	Type targetType = types[0];
        	
        	//判断该类型是否为泛型 	SysMapper<Item> 
        	if(targetType instanceof ParameterizedType){
        		//表示当前接口是一个泛型,并且获取泛型参数  SysMapper<Item>
        		ParameterizedType parameterizedType = (ParameterizedType) targetType;
        		
        		//SysMapper<T,V,K>   获取泛型的全部参数  Item Type
        		Type[] supers =  parameterizedType.getActualTypeArguments();
        		
        		//表示成功获取第一个参数   Item.class
        		Class<?> targetMethodClass = (Class<?>) supers[0];
        		
        		//判断Class不能为空
        		if(targetMethodClass !=null){
        			
        			//判断该类中是否含有注解
        			if(targetMethodClass.isAnnotationPresent(Table.class)){
        				//获取目标对象的注解
        				Table table = targetMethodClass.getAnnotation(Table.class);
        				
        				//获取表名
        				String tableName = table.name();
        				
        				//定义查询sql语句
        				String sql = "select count(*) from "+tableName;
        				
        				//定义sqlNode对象
        				SqlNode sqlNode = new StaticTextSqlNode(sql);
        				
        				return sqlNode;
        			}	
        		}
        	}
        	
    		} catch (ClassNotFoundException e) {
    			
    			e.printStackTrace();
    		}
        	
        	return null;
        	
        }

}
