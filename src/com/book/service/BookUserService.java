package com.book.service;

import com.book.pojo.BookUser;

public interface BookUserService {
		BookUser loginValidate( String userId,String userPsw);
		boolean saveUser(BookUser user);
}
