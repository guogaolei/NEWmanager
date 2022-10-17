package com.ghl.manage.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ghl.manage.utils.RSAEncryptUtil;

@Component
public class DataFilter implements Filter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RSAEncryptUtil RSAEncryptUtil;
	@Value("${ras.activate}")
	boolean RasFlag;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String method = httpServletRequest.getMethod();
		String url = httpServletRequest.getRequestURI();
		ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
		// 数据解析成功标志,如果不上送数据就不解析，
		boolean isSuccess = true;
		String result = "";
		try {
			RequestWrapper requestWrapper = null;
			if ("GET".equals(method)) {
				requestWrapper = new RequestWrapper((HttpServletRequest) request, RSAEncryptUtil, RasFlag, url, method);
				chain.doFilter(requestWrapper, responseWrapper);
			}
			if ("POST".equals(method)) {
				requestWrapper = new RequestWrapper((HttpServletRequest) request, RSAEncryptUtil, RasFlag, url, method);
				chain.doFilter(requestWrapper, responseWrapper);
			}
		} catch (Exception e) {
			isSuccess = false;
			logger.info("************异常*******：" + e);
		}
		if (!isSuccess) {
			result= "{\"code\":\"100\",\"msg\":\"交易失败\"}";
		} else {
			result = new String(responseWrapper.getResponseData(), "UTF-8");
		}
		logger.info("DateFilter:>url:" + url + ",------- 返回数据: " + result);
		response.setContentLength(-1);
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();

	}

	@Override
	public void destroy() {

	}
}
