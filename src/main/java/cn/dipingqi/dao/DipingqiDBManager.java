
package cn.dipingqi.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class DipingqiDBManager {

	public static final Logger logger = Logger.getLogger(DipingqiDBManager.class);

	public static final int KNOWLEDGE = 1;

	public static final int NEWS = 2;

	private static DipingqiDBManager mInstance;

	private SqlSessionFactory sqlSessionFactory;

	private static final String NS = "cn.dipingqi.dao.DipingqiDBManager.";

	private DipingqiDBManager() {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("dipingqi_db_config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reader = null;
		}
	}

	public static DipingqiDBManager getInstance() {
		if (mInstance == null) {
			mInstance = new DipingqiDBManager();
		}
		return mInstance;
	}

	public int insert(HashMap<String, String> map, int type) {

		map.put("post_keywords", "地坪漆的技术指标 地坪漆施工工艺 地坪漆的性能");
		insert("insert_posts", map);
		int id = getInsertId(map);

		map.put("object_id", String.valueOf(id));
		map.put("term_id", String.valueOf(type));

		insert("insert_relationships", map);

		return id;
	}

	public void updateContent(HashMap<String, String> param) {
		update("update_content", param);
	}

	/**
	 * 通过md5(content)去数据库取md5，如果取到，说明改应用已经存在于数据库中
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isMd5Exist(Map<String, String> map) {
		List list = selectList("selectMd5", map);
		return list.size() > 0 ? true : false;
	}

	@SuppressWarnings("rawtypes")
	public boolean isIdExist(Map<String, String> map) {
		List list = selectList("selectId", map);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 通过content_md5获取插入记录的对应id
	 * 
	 * @param map
	 * @return
	 */
	public int getInsertId(Map<String, String> map) {
		return selectId("selectIdbyMd5", map);
	}

	public Integer deleteById(HashMap<String, String> param) {
		return delete("deleteById", param);
	}

	// Factory Methods Begin
	public Integer update(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.update(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("update error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	public Integer delete(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.delete(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("delete error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	public Integer insert(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.insert(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("insert error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		List<Map<String, Object>> list = null;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectList(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return list;
	}

	public Integer selectId(String sqlKey) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}

	public Integer selectId(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}

	public String selectOne(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}
	// Factory Methods Begin

}
