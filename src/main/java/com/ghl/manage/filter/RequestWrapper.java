package com.ghl.manage.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.ghl.manage.utils.RSAEncryptUtil;

public class RequestWrapper extends HttpServletRequestWrapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    RSAEncryptUtil RSAEncryptUtil;
    boolean RasFlag=false;
    private final byte[] body;
    String url;
    String method;
    public RequestWrapper(HttpServletRequest request, RSAEncryptUtil RSAEncryptUtil, boolean rasFlag, String url, String method) throws Exception {
        super(request);
        this.RSAEncryptUtil=RSAEncryptUtil;
        this.RasFlag=rasFlag;
        this.url=url;
        this.method=method;
        body=getBody(request).getBytes(Charset.forName("UTF-8"));

    }

    public ServletInputStream getInputStream(){
        final ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(body);
        return new ServletInputStream() {


            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
               return byteArrayInputStream.read();
            }
        };

    }

    private String getBody(ServletRequest request) throws Exception {
    	if("GET".equals(method)) {
    		return GETMethod(request);
    	}else {
    		return POSTMethod(request);
    	}
       
     
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
    private String GETMethod(ServletRequest request) {
    	HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        String queryString = httpServletRequest.getQueryString();
        logger.info("DateFilter:>url:"+url+",------- requestParam: "+queryString);
        if(StringUtils.isEmpty(queryString)) {
        	return "";
        }
    	return queryString;
    }
    private String POSTMethod(ServletRequest request) throws Exception {
    	 StringBuffer sb=new StringBuffer();
         InputStream inputStream=null;
         BufferedReader bufferedReader=null;
          try {
              inputStream = request.getInputStream();
              bufferedReader= new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
              String line=null;
              while((line=bufferedReader.readLine())!=null){
                  sb.append(line);
              }
          }catch(Exception e){
                 logger.info("RequestWrapper.getBody异常："+e);
          }finally {
              if(null != inputStream){
                  try{
                  inputStream.close();}
                  catch (Exception e){
                      logger.info("inputStream.close异常："+e);
                  }
              }
              if(null != bufferedReader){
                  try{
                      bufferedReader.close();}
                  catch (Exception e){
                      logger.info("bufferedReader.close异常："+e);
                  }
              }
          }
          //ras加密的数据
         String decrypt=sb.toString();
         if(RasFlag){
              //ras开始解密
              decrypt = RSAEncryptUtil.decrypt(decrypt);
          }
         logger.info("DateFilter:>url:"+url+",------- requestParam: "+decrypt);
         //没有开启解密直接返回
         return decrypt;
    }

}
