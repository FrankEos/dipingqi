package org.harvest.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.harvest.web.bean.UrlContent;

public interface IUrlContentDao {
	
	/**
	 * 统计字段
	 */
	@Select("SELECT count(*) FROM `harvest_content_${portalId}` WHERE url_type = 1")
	public Integer queryAllPageContent(@Param("portalId") Integer portalId);
	
	@Select("SELECT count(*) FROM `harvest_content_${portalId}` WHERE url_type = 1 AND oper_flag = 2")
	public Integer queryHavePageContent(@Param("portalId") Integer portalId);
	
	@Select("SELECT count(*) FROM `harvest_content_${portalId}` WHERE url_type = 1 AND oper_flag <> 2")
	public Integer queryFailPageContent(@Param("portalId") Integer portalId);
	
	@Select("SELECT count(*) FROM `harvest_content_${portalId}` WHERE url_type = 0")
	public Integer queryListPageContent(@Param("portalId") Integer portalId);
	

	/**
	 * 查询
	 * @param portalId
	 * @return
	 */
	@Select("SELECT * FROM `harvest_content_${portalId}` order by url_type limit #{limited},#{rows}")
	public List<UrlContent> queryContentList(@Param(value="portalId") Integer portalId,@Param("limited") int limited,@Param("rows") int rows);
	
	@Select("SELECT count(*) FROM `harvest_content_${portalId}`")
	public Integer queryContentCount(@Param("portalId") Integer portalId);
	
	/**
	 * 修改
	 * @param content
	 * @return
	 */
	@Update("update `harvest_content_${portalId}` set url_md5 = MD5(CONCAT(#{c.url}, #{c.tag})), url = #{c.url}, url_type = #{c.url_type}, cost_time = #{c.cost_time}, oper_flag = #{c.oper_flag}, content_md5 = #{c.content_md5}, update_time = #{c.update_time}, tag = #{c.tag}, `order` = #{c.order}, bak = #{c.bak} where url_md5 = #{c.url_md5}")
	public Integer updateContent(@Param(value="portalId") Integer portalId, @Param(value="c") UrlContent content);
	
	/**
	 * 新增
	 * @param content
	 * @return
	 */
	@Insert("insert into `harvest_content_${portalId}` (url_md5,url,url_type,cost_time,oper_flag,content_md5,update_time,tag,`order`,bak) values (MD5(CONCAT(#{c.url}, #{c.tag})), #{c.url}, #{c.url_type}, #{c.cost_time}, #{c.oper_flag}, #{c.content_md5}, #{c.update_time}, #{c.tag}, #{c.order}, #{c.bak})")
	public Integer addContent(@Param(value="portalId") Integer portalId, @Param(value="c") UrlContent content);
	
	/**
	 * 删除
	 * @param url_md5
	 * @return
	 */
	@Delete("DELETE FROM `harvest_content_${portalId}` where url_md5 = #{url_md5}")
	public Integer deleteContent(@Param(value="portalId") Integer portalId,@Param(value="url_md5") String url_md5);
	

	/**
	 * 新建表
	 * @param portalId
	 * @return
	 */
	@Update("CREATE TABLE `harvest_content_${portalId}` ( `url_md5` varchar(32) NOT NULL, `url` varchar(512), `url_type` smallint(6) DEFAULT '1', `cost_time` int(11), `oper_flag` smallint(6), `content_md5` varchar(32) DEFAULT '', `update_time` bigint(20), `tag` varchar(32) NOT NULL, `order` bigint(32), `bak` varchar(512), PRIMARY KEY (`url_md5`,`tag`))  ENGINE=InnoDB DEFAULT CHARSET=utf8")
	public Integer createContentTab(@Param(value="portalId") Integer portalId);
	

	
	/**
	 * 删除表
	 * @param portalId
	 * @return
	 */
	@Update("DROP TABLE IF EXISTS `harvest_content_${portalId}`")
	public Integer dropContentTab(@Param(value="portalId") Integer portalId);
	
	
	
	
	
	
	/***************************			crawler 部分使用			***************************/
	
	/**
	 * 设置所有的List Url 为未处理状态
	 * 
	 * @param portalId
	 * @return
	 */
	@Update("update `harvest_content_${portalId}` set oper_flag = 1 where url_type = 0")
	public Integer updateListUrlStatus(@Param(value="portalId") Integer portalId);
	
	
	/**
	 * 设置所有未成功提取的Content Url 为未处理状态
	 * @param portalId
	 * @return
	 */
	@Update("update `harvest_content_${portalId}` set oper_flag = 1 where oper_flag <> 2 and url_type = 1")
	public Integer updateContentUrlStatus(@Param(value="portalId") Integer portalId);
	
	
	/**
	 * 获取站点上次执行采集的时间
	 * @param portalId
	 * @return
	 */
	@Select("SELECT MIN(update_time) FROM `harvest_content_${portalId}` where url_type=0;")
	public Long queryListUrlUpdateTime(@Param(value="portalId") Integer portalId);
	
	
	/**
	 * 已经添加到内容表中的url数
	 * @param portalId
	 * @return
	 */
	@Select("SELECT count(*) FROM `harvest_content_${portalId}`")
	public Integer discoveredUriCount(@Param(value="portalId") Integer portalId);

	
	/**
	 * 已经添加到内容表中的已处理url数
	 * @param portalId
	 * @return
	 */
	@Select("SELECT count(*) FROM `harvest_content_${portalId}` where oper_flag !=1")
	public Integer discoveredProcUriCount(@Param(value="portalId") Integer portalId);
	
	/**
	 * 获取未处理的UrlContent
	 * @param portalId
	 * @param urlType
	 * @return
	 */
	@Select("SELECT * FROM `harvest_content_${portalId}` where oper_flag = 1 and url_type = #{urlType} order by `order` desc limit 0,500")
	public List<UrlContent> queryValidUrlContent(@Param(value="portalId") Integer portalId, @Param(value="urlType") Integer urlType);

	
	/**
	 * 判断是否存在UrlContent
	 * @param portalId
	 * @param urlType
	 * @return
	 */
	@Select("SELECT * FROM `harvest_content_${portalId}` where url = #{url} and tag = #{tag} and url_md5 = MD5(CONCAT(#{url}, #{tag}))")
	public UrlContent queryUrlContent(@Param(value="portalId") Integer portalId, @Param(value="url") String url, @Param(value="tag") String tag);
}
