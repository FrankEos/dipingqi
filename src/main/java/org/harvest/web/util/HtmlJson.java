
package org.harvest.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.harvest.web.bean.base.BaseResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HtmlJson {

	@SuppressWarnings("rawtypes")
	public void HTMLMessageUtil(HttpServletResponse response,BaseResponse baseResponse,List list){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			OutputStream out = response.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
					"utf-8"));
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			response.setCharacterEncoding("utf-8");// 指定为utf-8
			pw.println(gson.toJson(list));
			pw.flush();
			pw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
