package org.harvest.web.service;

import org.harvest.web.bean.User;

public interface IUserService {

	/**
	 * 用户登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public User login(String userName, String password);

	/**
	 * 用户密码修改
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Integer updatePasswd(String userName,String password) throws Exception;

}
