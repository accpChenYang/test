package com.hdaccp.curd.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * ����json���͵�����
 * @author Administrator
 *
 */
public class Message {
	private int code;//״̬��
	private String message;//��ʾ��Ϣ
	private Map<String,Object> extend=new HashMap<>();//���ص�json����
	
	public static Message success(){
		Message message=new Message();
		message.setCode(100);
		message.setMessage("����ɹ�");
		return message;
	}
	
	public static Message error(){
		Message message=new Message();
		message.setCode(200);
		message.setMessage("����ʧ��");
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
