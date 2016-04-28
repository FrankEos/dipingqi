package org.harvest.web.service.impl;

import java.util.List;

import org.harvest.web.bean.Menu;
import org.harvest.web.dao.IMenuDao;
import org.harvest.web.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements IMenuService {

	@Autowired
	private IMenuDao mMenuDao;

	public List<Menu> queryAllMenus() {
		List<Menu> rootMenus = this.mMenuDao.queryMenusByPid(0);
		for (Menu root : rootMenus) {
			root.setSonMenus(this.mMenuDao.queryMenusByPid(root.getId()));
		}
		return rootMenus;
	}

	public Integer queryMenuCount() {
		return this.mMenuDao.queryMenuCount();
	}

	public List<Menu> queryMenus(int page, int rows) {
		return this.mMenuDao.queryMenus((page - 1) * rows, rows);
	}

	public Integer addMenu(Menu menu) throws Exception {
		return this.mMenuDao.addMenus(menu);
	}

	public Integer updateMenu(Menu menu) throws Exception {
		return this.mMenuDao.updateMenu(menu);
	}

	public Integer deleteMenu(List<Integer> ids) throws Exception {
		Integer result = 0;
		for (Integer id : ids) {
			result += this.mMenuDao.deleteMenu(id);
		}
//		result += this.mMenuDao.deleteMenu(ids);
//		this.mMenuDao.deleteMenu(ids);
		return result;
	}
	
	public Integer copyMenu(List<Integer> ids) throws Exception {
		Integer result = 0;
		for (Integer id : ids) {
			result += this.mMenuDao.copyMenu(id);
		}
		return result;
	}

	public List<Menu> queryContionMenus(int page, int rows, String name) {
		return this.mMenuDao.queryContionMenus((page - 1) * rows, rows,name);
	}

	public Integer queryMenuContionCount(String name) {
		return this.mMenuDao.queryMenuContionCount(name);
	}
}
