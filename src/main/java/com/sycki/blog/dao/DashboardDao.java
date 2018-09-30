package com.sycki.blog.dao;

import com.sycki.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DashboardDao {
	
	@Select("select * from blog_articles where id = #{id}")
	public User selectUserById(@Param("id") int id);

	@Select("select * from blog_user where user_name = #{userName} and password = #{passwd}")
	public User login(@Param("userName") String userName, @Param("passwd") String passwd);

}
