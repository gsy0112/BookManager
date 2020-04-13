package com.book.service;

import java.util.List;

import com.book.pojo.BookInfo;

public interface BookInfoService {
	int getcount(String bookName);
	
	List<BookInfo> getBookList(String bookName,int from,int pageSize);
	boolean save(BookInfo book);

	boolean delete(int id);
	
	BookInfo findByid(int id);
	boolean update(BookInfo bookinfo);
}
