package org.harvest.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.Processor;

public interface IProcessorDao {



	/**
	 * 根据处理器名称查询
	 * @param proc_name
	 * @return
	 */
	@Select("SELECT * FROM `harvest_processor` where proc_name like '%${proc_name}%' limit #{limited},#{rows}")
	public List<Processor> queryProcessorList(@Param("proc_name") String proc_name,@Param("limited") int limited,@Param("rows") int rows);
	
	@Select("SELECT count(*) FROM `harvest_processor` where proc_name like '%${proc_name}%'")
	public Integer queryProcessorCount(@Param("proc_name") String proc_name);
	
	/**
	 * 修改
	 * @param processor
	 * @return
	 */
	@Update("update `harvest_processor` set proc_name = #{proc_name}, proc_descr = #{proc_descr} where proc_class = #{proc_class}")
	public int updateProcessor(Processor processor);
	
	/**
	 * 新增
	 * @param processor
	 * @return
	 */
	@Insert("insert into `harvest_processor` (proc_class,proc_name,proc_descr) values (#{proc_class}, #{proc_name}, #{proc_descr})")
	public int addProcessor(Processor processor);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Delete("DELETE FROM `harvest_processor` where proc_class = #{proc_class}")
	public int deleteProcessor(@Param("proc_class") String proc_class);
	

	@Select("SELECT proc_class,proc_name FROM `harvest_processor`")
	public List<Processor> queryProcessorOptions();

}
