package org.harvest.web.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.User;

public interface IUserDao {

	@Select("select id,userName,password from harvest_sys_user where userName = #{userName} and password=#{password}")
	public User findUser(@Param("userName")String userName,@Param("password")String password);

	@Update("update harvest_sys_user set password=#{password} where userName = #{userName}")
	public Integer updatePasswd(@Param("userName")String userName,@Param("password")String password);


}
