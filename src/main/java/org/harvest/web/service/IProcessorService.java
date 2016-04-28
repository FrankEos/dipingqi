package org.harvest.web.service;

import java.util.List;

import org.harvest.web.bean.Processor;



public interface IProcessorService {


	/**
	 * 根据处理器名称查询
	 * @param proc_name
	 * @return
	 */
	public List<Processor> queryProcessorList(String proc_name, int page, int rows);
	
	public Integer queryProcessorCount(String proc_name);
	
	
	/**
	 * 修改
	 * @param processor
	 * @return
	 */
	public int updateProcessor(Processor processor);
	
	/**
	 * 新增
	 * @param processor
	 * @return
	 */
	public int addProcessor(Processor processor);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int deleteProcessor(List<String>  proc_classes);
	

	public List<Processor> queryProcessorOptions();
	
}
