package com.book.dao.user;

import org.apache.ibatis.annotations.Param;

import com.book.pojo.BookUser;

public interface BookUserMapper {
	BookUser loginValidate(@Param("userId") String userId,@Param("userPsw") String userPsw);
	int saveUser(BookUser user);
}
