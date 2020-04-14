package com.book.dao.book;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.book.pojo.BookInfo;

public interface BookMapper {
	int getcount(@Param("bookName") String bookName);
	List<BookInfo> getBookList(@Param("bookName") String bookName,@Param("from") int from,@Param("pageSize") int pageSize);
	int save(BookInfo book);
	int delete(int id);
	BookInfo findByid(int id);
	int update(BookInfo bookinfo);
}
