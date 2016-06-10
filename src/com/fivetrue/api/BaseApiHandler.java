package com.fivetrue.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class BaseApiHandler {
	
	private HttpServletRequest mRequest = null;
	private HttpServletResponse mResponse = null;
	private ServletContext mContext = null;
	
	private SimpleDateFormat mSdf = null;
	
	
	public BaseApiHandler(ServletContext context, HttpServletRequest request, HttpServletResponse response){
		mContext = context;
		mRequest = request;
		mResponse = response;
		mSdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		try {
			mRequest.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		checkRequestValidation();
	}
	
	protected boolean checkRequestValidation(){
		return true;
	}
	
	protected String getParameter(String key){
		String param = null;
		if(mRequest != null){
			param = mRequest.getParameter(key);
		}
		return param;
	}
	
	
	protected void writeContent(String content){
		if(content != null && mResponse != null){
			try {
				mResponse.getWriter().println(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	protected void writeObject(Object obj){
//		if(obj != null && mResponse != null){
//			try {
////				String content = new Gson
//				mResponse.getWriter().println(obj.toString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	protected SimpleDateFormat getSimpleDataFormat(){
		return mSdf;
	}
	
	public HttpServletRequest getRequest(){
		return mRequest;
	}
	
	public HttpServletResponse getResponse(){
		return mResponse;
	}
	
	public ServletContext getContext(){
		return mContext;
	}
}
