package com.xinlan.puerplestar.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 翻译提供类
 * @author Administrator
 *
 */
public class Translation 
{
	private String appKey="Zn0ATCCNsCPzzX6dlHk8bcEo";
	
	public Translation(String appKey)
	{
		this.appKey=appKey;
	}
	
	public Translation(){
	}
	
	/**
	 * 中文翻译成英文
	 * @param content
	 * @return
	 */
	public String chineseToEnglish(String content)
	{
		return doTranslation("zh","en",content);
	}
	
	/**
	 * 中文翻译日文
	 * @param content
	 * @return
	 */
	public String chineseToJapanese(String content)
	{
		return doTranslation("zh","jp",content);
	}
	
	/**
	 * 英文翻译
	 * @param content
	 * @return
	 */
	public String englishToChinese(String content)
	{
		return doTranslation("en","zh",content);
	}
	
	public String doTranslation(String fromLanguage,String toLanguage,String content)
	{
		HttpClient httpClient=new DefaultHttpClient();
		HttpPost post = new HttpPost( "http://openapi.baidu.com/public/2.0/bmt/translate"); 
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(); 
		parameters.add(new BasicNameValuePair("client_id", this.appKey));  
        parameters.add(new BasicNameValuePair("from", fromLanguage));  
        parameters.add(new BasicNameValuePair("to", toLanguage));
        parameters.add(new BasicNameValuePair("q", content));
        try
        {
			UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity( parameters,"UTF-8");
			post.setEntity(formEntiry);  
            HttpResponse response = httpClient.execute(post);  
            HttpEntity entity = response.getEntity();
            String responseString=readInputStream(entity.getContent());
            return getDistFromJSONObj(responseString);
		}
        catch (ClientProtocolException e)
        {
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}  
        return "";
	}
	
	private static String readInputStream(InputStream inputStream)
	{
		BufferedReader buffer=null;
		StringBuffer sb=new StringBuffer();
		String line=null;
		try
		{
			buffer=new BufferedReader(new InputStreamReader(inputStream));
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}//end while
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(inputStream!=null)
			{
				try
				{
					inputStream.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	private static String getDistFromJSONObj(String jsonValue)
	{
		StringBuffer sb=new StringBuffer();
		try
		{
			JSONObject obj = new JSONObject(jsonValue);
			//JSONObject obj=JSONObject.fromString(jsonValue);
			JSONArray arrays = obj.getJSONArray("trans_result");
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject item = arrays.getJSONObject(i);
				sb.append(item.get("dst")).append("\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}//end class
