package com.fivetrue.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javafx.util.Pair;


public abstract class BaseApiHandler {
	
	private static final String TAG = "BaseApiHandler";
	
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
	
	public static String requestApi(String api, String method, boolean userCaches, Pair<String, String>[] headers, Pair<String, String>...parameters){
		return requestApi(api, method, userCaches, headers, getPostDataString(parameters));
	}
	
	public static String requestApi(String api, String method, boolean userCaches, Pair<String, String>[] headers, String data){
		String response = "";
		try {
			boolean hasoutbody = method.equalsIgnoreCase("POST");
            final URL url = new URL(api);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            
            if(headers != null){
            	for(Pair<String, String> header : headers){
            		conn.addRequestProperty(header.getKey(), header.getValue());
            	}
            }

            conn.setUseCaches(userCaches);
            conn.setDoInput(true);
            conn.setDoOutput(hasoutbody);
            conn.connect();
            
            if(hasoutbody){
            	if(data != null && data.length() > 0){
            		OutputStream os = conn.getOutputStream();
            		BufferedWriter writer = new BufferedWriter(
            				new OutputStreamWriter(os, "UTF-8"));
            		writer.write(data);
            		writer.flush();
            		writer.close();
            		os.close();
            	}
            }
            
            int responseCode =conn.getResponseCode();
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
//            }else {
//                response="";
//            }
                
//                getContext().log(TAG + " : requestAPi ("
//                		+ "responseCode = " + responseCode + " / "
//                		+ "api = " + api + " / "
//                		+ "method = " + method + " / "
//                		+ "parameter = " + parameters + " / "
//                				+ "response = " + response + " / "
//                				+ ")" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String getPostDataString(Pair<String, String>[] pairs){
		String data = "";
		if(pairs != null && pairs.length > 0){
			for(Pair<String, String> p : pairs){
				data += p.getKey() + "=" + p.getValue() + "&"; 
			}
		}
		return data;
		
	}
}
