package org.harvest.web.service;

import java.util.List;

import org.harvest.web.bean.Menu;


public interface IMenuService {

	public List<Menu> queryAllMenus();

	public List<Menu> queryMenus(int page, int rows);

	public Integer queryMenuCount();
	
	public Integer addMenu(Menu menu) throws Exception;
	
	public Integer updateMenu(Menu menu) throws Exception;
	
	public Integer deleteMenu(List<Integer> ids) throws Exception;
	
	public Integer copyMenu(List<Integer> ids) throws Exception;
	
	public List<Menu> queryContionMenus(int page, int rows,String name);
	
	public Integer queryMenuContionCount(String name);

}
