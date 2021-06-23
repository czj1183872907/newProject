package com.chery.exeed.crm.lead.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

/**
 * Http Socket Factory
 *
 * @author Admin
 *
 */
public class HttpSSLProtocolSocketFactoryUtil implements ProtocolSocketFactory {

	private SSLContext sslcontext = null;



	private SSLContext createSSLContext()
	{
		SSLContext sslcontext = null;
		try
		{
			sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (KeyManagementException e)
		{
			e.printStackTrace();
		}
		return sslcontext;
	}

	private SSLContext getSSLContext()
	{
		if (this.sslcontext == null)
		{
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	public Socket createSocket(String host, int port) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException
	{
		if (params == null)
		{
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0)
		{
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else
		{
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	/**
	 * 自定义私有类
	 *
	 * @author Admin
	 *
	 */
	private static class TrustAnyTrustManager implements X509TrustManager
	{
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[] {};
		}

	}

	/**
	 * HTTP GET 给接口
	 * @param hget
	 * @return
	 */
	public static String sendHttpsGetRequest(GetMethod hget, String token){
		HttpClient client = new HttpClient();
		Protocol myhttps = new Protocol("https", new HttpSSLProtocolSocketFactoryUtil(), 443);
		Protocol.registerProtocol("https", myhttps);
//		client.setParams(params);
		String result = "";
		hget.setRequestHeader("cookie",token);
		try
		{
			client.executeMethod(hget);
			result = hget.getResponseBodyAsString();
		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			hget.releaseConnection();
		}
		return result;
	}

	/**
	 * HTTP Post Json 给接口
	 *
	 * @param hpost
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String sendHttpsPostJsonRequest(PostMethod hpost, String data)
	{
		HttpClient client = new HttpClient();
		Protocol myhttps = new Protocol("https", new HttpSSLProtocolSocketFactoryUtil(), 443);
		Protocol.registerProtocol("https", myhttps);
		String result = "";
		String token="";
		try
		{
			// 设置请求头部类型
			hpost.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
			hpost.setRequestBody(data);
			client.executeMethod(hpost);
			int code = hpost.getStatusCode();
			Header[] headers = hpost.getResponseHeaders();
			int i = 0;

			while (i < headers.length) {
				if(headers[i].getName().endsWith("Set-Cookie")){
					token=headers[i].getValue().substring(0,headers[i].getValue().indexOf("/")+1);
					break;
				}
				i++;
			}
			if (code == HttpStatus.SC_OK)
			{
				BufferedInputStream bis = new BufferedInputStream(hpost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1)
				{
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				result = new String(strByte, 0, strByte.length, "utf-8");
			}
		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			hpost.releaseConnection();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static String sendHttpsDmsPostJsonRequest(PostMethod hpost, String token)
	{
		HttpClient client = new HttpClient();
		Protocol myhttps = new Protocol("https", new HttpSSLProtocolSocketFactoryUtil(), 443);
		Protocol.registerProtocol("https", myhttps);
		String result = "";
		try
		{
			// 设置请求头部类型
			hpost.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
			hpost.setRequestHeader("Cookie",token);
//			hpost.setRequestBody(data);
			client.executeMethod(hpost);
			int code = hpost.getStatusCode();
			int i = 0;

			if (code == HttpStatus.SC_OK)
			{
				BufferedInputStream bis = new BufferedInputStream(hpost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1)
				{
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				result = new String(strByte, 0, strByte.length, "utf-8");
			}
		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			hpost.releaseConnection();
		}
		return result;
	}

	/**
	 * HTTP POST XML 给接口
	 *
	 * @param hpost
	 * @param data
	 * @return
	 */
	public static String sendHttpsPostXMLRequest(PostMethod hpost, String data)
	{

		HttpClient client = new HttpClient();
		Protocol myhttps = new Protocol("https", new HttpSSLProtocolSocketFactoryUtil(), 443);
		Protocol.registerProtocol("https", myhttps);
		String result = "";

		try
		{
			// 设置请求头部类型
			hpost.setRequestHeader("Content-Type", "text/xml");
			hpost.setRequestHeader("charset", "utf-8");

			RequestEntity entity = new StringRequestEntity(data, "text/xml", "UTF-8");
			hpost.setRequestEntity(entity);
			client.executeMethod(hpost);
			int code = hpost.getStatusCode();
			if (code == HttpStatus.SC_OK)
			{
				BufferedInputStream bis = new BufferedInputStream(hpost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1)
				{
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				result = new String(strByte, 0, strByte.length, "utf-8");
			}
		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			hpost.releaseConnection();
		}
		return result;
	}
	public static String doRequestPost(String url, String data){
		CloseableHttpResponse response = null;
		//创建CloseableHttpClient
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//实现HttpPost
		HttpPost httpPost = new HttpPost(url);
		//设置httpPost的请求头中的MIME类型为json
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("cookie",data);
//		if(!StringUtils.isEmpty(data)){
//			StringEntity requestEntity = new StringEntity(data, "utf-8");
//			//设置请求体
//			httpPost.setEntity(requestEntity);
//		}
		try {
			//执行请求返回结果
			response = httpClient.execute(httpPost, new BasicHttpContext());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "utf-8");
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (response != null) {
				try {
					//最后关闭response
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * HTTP Put Json 给接口
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HashMap sendHttpsPUtJsonRequest(String strUrl, String scrToken,String param)
	{
		HashMap map =new HashMap();
//		HttpClient client = new HttpClient();
//		Protocol myhttps = new Protocol("https", new HttpSSLProtocolSocketFactoryUtil(), 443);
//		Protocol.registerProtocol("https", myhttps);
//		String result = "";
//		String token="";
//		hPut.setRequestHeader("cookie",scrToken);
//		try
//		{
//			// 设置请求头部类型
//			hPut.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
////			hPut.setRequestBody(data);
//			RequestEntity entity = new StringRequestEntity(data, "application/json", "UTF-8");
//			hPut.setRequestEntity(entity);
//			client.executeMethod(hPut);
//			int code = hPut.getStatusCode();
//			token=String.valueOf(code);
//			org.apache.commons.httpclient.Header[] headers = hPut.getResponseHeaders();
//			int i = 0;
//
//			while (i < headers.length) {
//				if(headers[i].getName().endsWith("Set-Cookie")){
//					token=headers[i].getValue().substring(0,headers[i].getValue().indexOf("/")+1);
//					break;
//				}
//				i++;
//			}
//			if (code == HttpStatus.SC_OK)
//			{
//				BufferedInputStream bis = new BufferedInputStream(hPut.getResponseBodyAsStream());
//				byte[] bytes = new byte[1024];
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				int count = 0;
//				while ((count = bis.read(bytes)) != -1)
//				{
//					bos.write(bytes, 0, count);
//				}
//				byte[] strByte = bos.toByteArray();
//				result = new String(strByte, 0, strByte.length, "utf-8");
//			}
//		} catch (HttpException e)
//		{
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		} finally
//		{
//			hPut.releaseConnection();
//		}
//		return token;
		HttpClient httpclient = new HttpClient();
		String strReturn="";
		PutMethod httpput=new PutMethod(strUrl);
		httpput.setRequestHeader("cookie",scrToken);
		try {
			if(param!=null)
			{
				RequestEntity entity = new StringRequestEntity(param, "application/json", "UTF-8");
				httpput.setRequestEntity(entity);
			}
			httpclient.executeMethod(httpput);
			int code = httpput.getStatusCode();
			map.put("code",code);
			byte[] bytes = httpput.getResponseBody();
			strReturn=  new String(bytes) ;
			HashMap result = JSONObject.parseObject(strReturn, HashMap.class);
			map.put("data",result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}



	/**
	 * HTTP GET 给接口
	 * @param hget
	 * @param param
	 * @return
	 */
	public static String sendAppHttpGetRequest(GetMethod hget, String param){
		HttpClient client = new HttpClient();
		Protocol myhttps = new Protocol("http", new HttpSSLProtocolSocketFactoryUtil(), 443);
		Protocol.registerProtocol("http", myhttps);
		hget.setRequestHeader("Accept", "application/json;charset=UTF-8");
		String result = "";
		try
		{
			client.executeMethod(hget);
			result = hget.getResponseBodyAsString();

		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			hget.releaseConnection();
		}
		return result;
	}

}
