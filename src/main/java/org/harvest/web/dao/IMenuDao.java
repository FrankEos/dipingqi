package org.harvest.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.Menu;

public interface IMenuDao {

	@Select("select id,pid,name,link,show_flag,order_by,status from harvest_sys_menu where status='1' and pid = #{pid} order by order_by")
	public List<Menu> queryMenusByPid(int pid);

	@Select("select count(0) c from harvest_sys_menu where status='1'")
	public Integer queryMenuCount();

	@Select("select id,pid,name,link,show_flag,order_by,status from harvest_sys_menu where status='1' limit #{limited} ,#{rows}")
	public List<Menu> queryMenus(@Param("limited") int limited,
			@Param("rows") int rows);

	@Insert("insert into harvest_sys_menu (pid,name,link,order_by,status,show_flag) values (#{pid},#{name},#{link},#{order_by},#{status},#{show_flag})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public Integer addMenus(Menu menu);

	@Update("update harvest_sys_menu set pid = #{pid},name = #{name},link = #{link},order_by = #{order_by},status = #{status},show_flag = #{show_flag} where id = #{id}")
	public Integer updateMenu(Menu menu);

	@Delete("delete from harvest_sys_menu where id=#{id}")
	public Integer deleteMenu(Integer id);
	
	@Insert("insert into harvest_sys_menu(pid,name,link,order_by,status,show_flag) select pid,name,link,order_by,status,show_flag from harvest_sys_menu where id = #{id}")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public Integer copyMenu(Integer id);

	
	@Select("select id,pid,name,link,show_flag,order_by,status from harvest_sys_menu where status='1' and name=#{name} limit #{limited} ,#{rows}")
	public List<Menu> queryContionMenus(@Param("limited") int limited,
			@Param("rows") int rows,@Param("name") String name);
	
	@Select("select count(0) c from harvest_sys_menu where status='1' and name=#{name}")
	public Integer queryMenuContionCount(@Param("name") String name);

}
