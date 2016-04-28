package org.harvest.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.Portal;

public interface IPortalDao {



	/**
	 * 根据站占名称查询
	 * @param portal_name
	 * @return
	 */
	@Select("SELECT * FROM `harvest_portal` where portal_name like '%${portal_name}%' order by id desc limit #{limited},#{rows}")
	public List<Portal> queryPortalList(@Param("portal_name") String portal_name,@Param("limited") int limited,@Param("rows") int rows);
	
	@Select("SELECT count(*) FROM `harvest_portal` where portal_name like '%${portal_name}%'")
	public Integer queryPortalCount(@Param("portal_name") String portal_name);
	
	@Select("select id from `harvest_portal` where portal_name = #{portal_name} and portal_desc = #{portal_desc} and max_thread = #{max_thread} and cycle = #{cycle} and show_flag = #{show_flag} and isAllByHand = #{isAllByHand} and incrementPageCount = #{incrementPageCount}")
	public Integer queryPortalId(Portal portal);
	
	/**
	 * 修改
	 * @param portal
	 * @return
	 */
	@Update("update `harvest_portal` set portal_name = #{portal_name}, portal_desc = #{portal_desc}, max_thread = #{max_thread}, cycle = #{cycle}, show_flag = #{show_flag}, isAllByHand = #{isAllByHand}, incrementPageCount = #{incrementPageCount} where id = #{id}")
	public Integer updatePortal(Portal portal);
	
	/**
	 * 新增
	 * @param portal
	 * @return
	 */
	@Insert("insert into `harvest_portal` (portal_name,portal_desc,max_thread,cycle,show_flag,isAllByHand,incrementPageCount) values (#{portal_name}, #{portal_desc}, #{max_thread}, #{cycle}, #{show_flag}, #{isAllByHand}, #{incrementPageCount})")
	public Integer addPortal(Portal portal);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Delete("DELETE FROM `harvest_portal` where id = #{id}")
	public Integer deletePortal(Integer id);
	
	
	
	
	
	
	/***************************			crawler 部分使用			***************************/
	

	/**
	 * 获取一个站点信息
	 * 
	 * @param portalId
	 * @return
	 */
	@Select("SELECT * FROM `harvest_portal`  where id = #{portalId}")
	public Portal queryPortalById(@Param("portalId") Integer portalId);
	
	/**
	 * 获取所有有效的站点信息
	 * @return
	 */
	@Select("SELECT * FROM `harvest_portal` where show_flag = 1")
	public List<Portal> queryValidPortal();

}
