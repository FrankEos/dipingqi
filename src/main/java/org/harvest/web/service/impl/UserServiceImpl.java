package org.harvest.web.service.impl;

import org.harvest.web.bean.User;
import org.harvest.web.dao.IUserDao;
import org.harvest.web.service.IUserService;
import org.harvest.web.util.MD5Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao mUserDao;

	public User login(String userName, String password) {
		password = MD5Tool.calcMD5(password);
		return this.mUserDao.findUser(userName,password);
	}


	public Integer updatePasswd(String userName,String password) throws Exception {
		password = MD5Tool.calcMD5(password);
		return this.mUserDao.updatePasswd(userName,password);
	}

}
