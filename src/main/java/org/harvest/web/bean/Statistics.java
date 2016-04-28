package org.harvest.web.bean;

public class Statistics {

	private long id; // INT(11) NOT NULL AUTO_INCREMENT, "
	private long start_time; // INT(11) NULL, "
	private long end_time;// INT(11) NULL, "
	private long list_num; // INT NULL, "
	private long all_page; // INT NOT NULL, "
	private long add_page; // INT NULL, "
	private long have_page; // INT NULL, "
	private long fail_page; // INT NULL, 
	private long proc_name; // INT NULL, "

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public long getList_num() {
		return list_num;
	}

	public void setList_num(long list_num) {
		this.list_num = list_num;
	}

	public long getAll_page() {
		return all_page;
	}

	public void setAll_page(long all_page) {
		this.all_page = all_page;
	}

	public long getAdd_page() {
		return add_page;
	}

	public void setAdd_page(long add_page) {
		this.add_page = add_page;
	}

	public long getHave_page() {
		return have_page;
	}

	public void setHave_page(long have_page) {
		this.have_page = have_page;
	}

	public long getProc_name() {
		return proc_name;
	}

	public void setProc_name(long proc_name) {
		this.proc_name = proc_name;
	}

	public long getFail_page() {
		return fail_page;
	}

	public void setFail_page(long fail_page) {
		this.fail_page = fail_page;
	}

}
