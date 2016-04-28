package org.harvest.crawler.util;

public class String2IntegerUtils {
	public static Integer String2Integer(String str){
		int i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (Exception e) {

		}
		return i;
	}
}
