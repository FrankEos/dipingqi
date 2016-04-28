package org.harvest.web.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.Statistics;

public interface IStatisticsDao {
	
	/**
	 * 为portal创建statistics表
	 * 
	 * @param portalId
	 * @return
	 */
	@Update("CREATE TABLE IF NOT EXISTS `harvest_statistics_${portalId}` ("
			+ "`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID', "
			+ "`start_time` INT(11) NULL COMMENT '采集开始时间', "
			+ "`end_time` INT(11) NULL COMMENT '采集结束时间', "
			+ "`list_num` INT NULL COMMENT 'list个数', "
			+ "`all_page` INT NOT NULL COMMENT '总页面数', "
			+ "`add_page` INT NULL COMMENT '本次添加的页面数', "
			+ "`have_page` INT NULL COMMENT '本次添加的页面数', "
			+ "`fail_page` INT NULL COMMENT '没有下载成功的页面数',"
			+ "`proc_name` INT NULL COMMENT '本次采集启动了多少个线程', "
			+ "PRIMARY KEY (`id`,`all_page`), "
			+ "INDEX `index_start_time` (`start_time` ASC)) "
			+ "ENGINE=MyISAM DEFAULT CHARSET=utf8")
	public Integer createStatisticsTable(@Param(value = "portalId") Integer portalId);

	/** 
	 * 插入一条统计数据
	 * 
	 * @param statistics
	 * @return
	 */
	@Insert("insert into `harvest_statistics_${portalId}` ("
			+ "id,start_time,end_time,list_num,all_page,add_page,have_page,fail_page,proc_name) "
			+ "values "
			+ "(#{s.id}, #{s.start_time}, #{s.end_time}, #{s.list_num}, #{s.all_page}, #{s.add_page}, #{s.have_page}, #{s.fail_page}, #{s.proc_name})")
	public Integer insertStatistics(@Param(value = "portalId") Integer portalId, @Param(value = "s") Statistics statistics);
	
	/**
	 * 删除portal对应的statistics表 
	 * do we really need to drop a statistics table?
	 * @param portalId
	 * @return
	 */
	@Update("DROP TABLE IF EXISTS `harvest_statistics_${portalId}`")
	public Integer dropStatisticsTable(@Param(value = "portalId") Integer portalId);
	
	@Select("SELECT * FROM `harvest_statistics_${portalId}` where id = #{id}")
	public Statistics findStatisticsById(@Param(value = "portalId") Integer portalId, @Param(value = "id") String id);

	/**
	 * 更新一条统计数据
	 * 
	 * @param content
	 * @return
	 */
	@Update("update `harvest_statistics_${portalId}` "
			+ "set start_time = #{s.start_time}, end_time = #{s.end_time}, list_num = #{s.list_num}, all_page = #{s.all_page}, add_page = #{s.add_page}, have_page = #{s.have_page}, fail_page = #{s.fail_page},proc_name = #{s.proc_name} "
			+ "where id = #{s.id}")
	public Integer updateStatistics(@Param(value = "portalId") Integer portalId, @Param(value = "s") Statistics statistics);
	
}
