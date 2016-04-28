package org.harvest.crawler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class CrawlServlet extends HttpServlet {
	
	private Logger log = Logger.getLogger(CrawlServlet.class);

	private static final long serialVersionUID = 123899232L;

	public static int threadSleep = 1;

	public static int ipLockSleep = 15;

	public void init() throws ServletException {
		super.init();
		log.debug("这是入口。。。。。。。");
		new Thread() {
			@Override
			public void run() {
				// 设置参数
				int vthreadSleep = Integer.valueOf(getInitParameter("threadSleep"));
				int vipLockSleep = Integer.valueOf(getInitParameter("ipLockSleep"));
				if (vthreadSleep > 1)
					threadSleep = vthreadSleep;
				if (vipLockSleep > 10)
					ipLockSleep = vipLockSleep;
				
				log.debug("后台爬取servlet程序运行..threadSleep: " + threadSleep + ", ipLockSleep: " + ipLockSleep);
				CrawlTaskCenter cc = new CrawlTaskCenter();
				cc.start();
			}
		}.start();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
