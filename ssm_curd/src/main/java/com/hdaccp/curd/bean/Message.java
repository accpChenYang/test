package com.hdaccp.curd.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回json类型的数据
 * @author Administrator
 *
 */
public class Message {
	private int code;//状态码
	private String message;//提示信息
	private Map<String,Object> extend=new HashMap<>();//返回的json数据
	
	public static Message success(){
		Message message=new Message();
		message.setCode(100);
		message.setMessage("处理成功");
		return message;
	}
	
	public static Message error(){
		Message message=new Message();
		message.setCode(200);
		message.setMessage("处理失败");
		return message;
	}
	
	public Message add(String key,Object value){
		this.getExtend().put(key, value);
		return this;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getExtend() {
		return extend;
	}
	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
}
