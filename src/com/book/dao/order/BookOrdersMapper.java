package com.book.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.book.pojo.BookOrders;


public interface BookOrdersMapper {
	
	int saveOrder(BookOrders order);
	
	List<BookOrders> getOrderList(@Param("uid") String uid);

	int updateOrders(@Param("oid") String oid,@Param("count") int count,@Param("curPrice") double curPrice);
}
