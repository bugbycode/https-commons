package com.bugbycode.https_commons;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Test;

import com.bugbycode.https.HttpsClient;

import sun.misc.BASE64Encoder;

public class AppTest {
	
	private HttpsClient httpClient;
	
	private final String BASE_URL = "https://192.168.0.103/agent-oauth2";
	
	private final String CLIENT_ID = "fort";
	
	private final String SECRET = "j1d1sec.c0m";
	
	private final String SCOPE = "web";
	
	@Before
	public void before() {
		httpClient = new HttpsClient("c:/localhost.keystore", "changeit");
	}
	
	@Test
	public void testGetToken() {
		String url = BASE_URL + "/oauth/token";
		String result = httpClient.getToken(url, "client_credentials", CLIENT_ID, SECRET, SCOPE);
		System.out.println(result);
	}
	
	@Test
	public void checkToken() {
		String url = BASE_URL + "/oauth/check_token";
		String result = httpClient.checkToken(url, CLIENT_ID, SECRET, "8e86b419-a616-401a-8e5e-ad0d33e8d76e");
		System.out.println(result);
	}
	
	@Test
	public void checkResource() {
		String url = "https://192.168.0.103/cloud_proxy/api/getChannel";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clientId", "fort");
		String result = httpClient.getResource(url, "8e86b419-a616-401a-8e5e-ad0d33e8d76e", map);
		System.out.println(result);
	}
	
	@Test
	public void testHttpsReuqest() {
		try {
			HttpsURLConnection conn = httpClient.getHttpsURLConnection("https://192.168.0.103/agent-oauth2/oauth/check_token");
			conn.setAllowUserInteraction(true);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			
			String token = "token=030b8443-5531-491e-8813-bd046c897b4f";
			
			BASE64Encoder encoder = new BASE64Encoder();
			encoder.encode("fort:J1d1sec.c0m".getBytes());
			
			conn.setRequestProperty("Authorization", "Basic " + encoder.encode("fort:J1d1sec.c0m".getBytes()));
			
			OutputStream out = conn.getOutputStream();
			out.write(token.getBytes());
			out.flush();
			
			int code = conn.getResponseCode();
			if(code == 200) {
				 // 取得该连接的输入流，以读取响应内容
		        InputStreamReader insr = new InputStreamReader(conn.getInputStream());
		        // 读取服务器的响应内容并显示
		        int respInt = insr.read();
		        while (respInt != -1) {
		            System.out.print((char) respInt);
		            respInt = insr.read();
		        }
			}else {
				InputStreamReader insr = new InputStreamReader(conn.getErrorStream());
		        // 读取服务器的响应内容并显示
		        int respInt = insr.read();
		        while (respInt != -1) {
		            System.out.print((char) respInt);
		            respInt = insr.read();
		        }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
