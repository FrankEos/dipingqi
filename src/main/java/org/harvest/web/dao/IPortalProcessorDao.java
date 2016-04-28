package org.harvest.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.PortalProcessor;

public interface IPortalProcessorDao {



	/**
	 * 查询
	 * @param portalId
	 * @return
	 */
	@Select("SELECT t1.*,t2.portal_name FROM `harvest_portal_processor` t1,`harvest_portal` t2 where t1.portal_id=t2.id and t1.portal_id = #{portalId} limit #{limited},#{rows}")
	public List<PortalProcessor> queryPortalProcessorList(@Param(value="portalId") Integer portalId,@Param("limited") int limited,@Param("rows") int rows);
	
	@Select("SELECT count(*) FROM `harvest_portal_processor` where portal_id = #{portalId}")
	public Integer queryPortalProcessorCount(@Param("portalId") Integer portalId);
	
	/**
	 * 修改
	 * @param portalProcessor
	 * @return
	 */
	@Update("update `harvest_portal_processor` set url_regx = #{url_regx}, proc_class = #{proc_class}, rule_file = #{rule_file} where portal_id = #{portal_id} and regx_id = #{regx_id}")
	public Integer updatePortalProcessor(PortalProcessor portalProcessor);
	
	/**
	 * 新增
	 * @param portalProcessor
	 * @return
	 */
	@Insert("insert into `harvest_portal_processor` (portal_id,regx_id,url_regx,proc_class,rule_file) values (#{portal_id}, #{regx_id}, #{url_regx}, #{proc_class}, #{rule_file})")
	public Integer addPortalProcessor(PortalProcessor portalProcessor);
	
	/**
	 * 删除
	 * @param regx_id
	 * @return
	 */
	@Delete("DELETE FROM `harvest_portal_processor` where portal_id = #{portalId} and regx_id = #{regx_id}")
	public Integer deletePortalProcessor(@Param(value="portalId") Integer portalId,@Param(value="regx_id") Integer regx_id);

	/**
	 * 查询站点处理器最大ID
	 * @param url_md5
	 * @return
	 */
	@Select("SELECT max(regx_id) FROM `harvest_portal_processor` where portal_id = #{portalId}")
	public Integer queryRegxMaxId(@Param(value="portalId") Integer portalId);
	
	
	
	
	
	
	/***************************			crawler 部分使用			***************************/
	
	/**
	 * 获取站点的处理器
	 * 
	 * @param portalId
	 * @return
	 */
	@Select("SELECT * FROM `harvest_portal_processor` where portal_id=#{portalId}")
	public List<PortalProcessor> queryPortalProcessorByPortalId(@Param(value="portalId") Integer portalId);

}
