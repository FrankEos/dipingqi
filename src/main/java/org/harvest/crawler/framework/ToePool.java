package org.harvest.crawler.framework;

import org.apache.log4j.Logger;

public class ToePool extends ThreadGroup {
	
	private static Logger logger = Logger.getLogger(ToePool.class);

	public static int DEFAULT_TOE_PRIORITY = Thread.NORM_PRIORITY - 1;

	protected CrawlController controller;

	protected int nextSerialNumber = 1;

	public ToePool(CrawlController c, String name) {
		super(name);
		this.controller = c;
		setDaemon(true);
	}

	public void cleanup() {
		this.controller = null;
	}

	/**
	 * @return 返回活动线程数
	 */
	public int getActiveToeCount() {
		Thread[] toes = getToes();
		int count = 0;
		for (int i = 0; i < toes.length; i++) {
			if ((toes[i] instanceof ToeThread) && ((ToeThread) toes[i]).isActive()) {
				count++;
			}
		}
		logger.debug(this.getName() + "--ActiveToeCount：" + count);
		return count;
	}

	private Thread[] getToes() {
		
		logger.debug("=============activeCount===>>>==="+activeCount());
		Thread[] toes = new Thread[activeCount() + 10];
		// 由于ToePool继承自java.lang.ThreadGroup类
		// 因此当调用enumerate(Thread[] toes)方法时，
		// 实际上是将所有该ThreadGroup中开辟的线程放入
		// toes这个数组中，以备后面的管理
		this.enumerate(toes);
		return toes;
	}

	/**
	 * @return 返回所有线程数
	 */
	public int getToeCount() {
		Thread[] toes = getToes();
		int count = 0;
		for (int i = 0; i < toes.length; i++) {
			if ((toes[i] instanceof ToeThread)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Change the number of ToeThreads.
	 * 
	 * @param newsize
	 *            The new number of ToeThreads. 线程池本身在创建的时候，并没有任何活动的线程实例，只有当它的setSize方法被调用时， 才有可能创建新线程；如果当setSize方法被调用多次而传入不同的参数时， 线程池会根据参数里所设定的值的大小，来决定池中所管理线程数量的增减。
	 */
	public void setSize(int newsize) {
		int difference = newsize - getToeCount();
		if (difference == 0)
			return;
		if (difference > 0) {
			// must create threads
			for (int i = 1; i <= difference; i++) {
				startNewThread();
			}
		} else {
			// must retire extra threads
			int retainedToes = newsize;
			// 将线程池中的线程管理起来放入数组中
			Thread[] toes = this.getToes();
			// 循环去除多余的线程
			for (int i = 0; i < toes.length; i++) {
				if (!(toes[i] instanceof ToeThread)) {
					continue;
				}
				retainedToes--;
				if (retainedToes >= 0) {
					continue; // this toe is spared
				}
				// otherwise:
				ToeThread tt = (ToeThread) toes[i];
				tt.retire();
			}
		}
	}

	private void startNewThread() {
		
		
		ToeThread newThread = new ToeThread(this, nextSerialNumber++);
		newThread.setPriority(DEFAULT_TOE_PRIORITY);
		controller.getCrawlerInf().addCurThread();// 状态变更-增加一个活动线程
		newThread.start();
	}

	/**
	 * @return Instance of CrawlController.
	 */
	public CrawlController getController() {
		return controller;
	}
}
