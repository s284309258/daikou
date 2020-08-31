package service.bean;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgBean {

	private String MSG_TYPE = "";
	private String BATCH_NO = "";
	private String USER_NAME = "";
	private String TRANS_STATE = "";
	private String MSG_SIGN = "";
	
	private List<MsgBody> BODYS = new ArrayList<MsgBody>();

	public String getBATCH_NO() {
		return BATCH_NO;
	}

	public void setBATCH_NO(String batch_no) {
		BATCH_NO = batch_no;
	}

	public List<MsgBody> getBODYS() {
		return BODYS;
	}

	public void setBODYS(List<MsgBody> body) {
		this.BODYS = body;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String user_name) {
		USER_NAME = user_name;
	}
	
	public static void main(String[] args) {
		MsgBean bean = new MsgBean();
		MsgBody body = new MsgBody();
		MsgBody body2 = new MsgBody();
		bean.getBODYS().add(body);
		bean.getBODYS().add(body2);
		
		String xml = bean.toXml();
		
		System.out.println(xml);
		
		bean.toBean(xml);
		
		xml = bean.toXml();
		
		System.out.println(xml);
		
    	List<MsgBody> bodys = bean.getBODYS();
    	for(int i = 0;i < bodys.size();i++) {

    		MsgBody b = bodys.get(i);
			b.setPAY_STATE("P001");
			b.setAMOUNT("10.23");
    	}
		xml = bean.toXml();
		
		System.out.println(xml);
		
		bean.toBean(xml);
		xml = bean.toXml();
		System.out.println(xml);
	}
    
	@SuppressWarnings("unchecked")
	public String toXml() {
		
		StringBuffer buf = new StringBuffer();
		Class cl = this.getClass();
		String rootName = cl.getSimpleName().toUpperCase();
		buf.append("<" + rootName + ">");
		Field[] fields = cl.getDeclaredFields();
		Method[] methods = cl.getDeclaredMethods();
		for(Field fd: fields){
			try {
				String fieldName = fd.getName();
				String fieldGetName = parGetName(fd.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cl.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(this, new Object[] {});
				if(null != fieldVal){
					if(fieldGetMet.getReturnType().getName().startsWith("java.util")){
						buf.append(toMultXml((List)fieldVal));
					} else {
						buf.append("<"+fieldName+">" + fieldVal + "</"+fieldName+">");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		buf.append("</" + rootName + ">");
		return buf.toString();
	}
	
	public String toOneXml(Object obj){
		StringBuffer buf = new StringBuffer();
		buf.append("<TRANS_DETAIL>");
		Class cl = obj.getClass();
		Field[] fields = cl.getDeclaredFields();
		Method[] methods = cl.getDeclaredMethods();
		for(Field fd: fields){
			try {
				String fieldName = fd.getName();
				String fieldGetName = parGetName(fd.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cl.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				fieldVal = fieldVal==null?"":fieldVal;
				/*if("SETT_DATE".equals(fieldName.toUpperCase())) //??? ???  CYL
					continue;*/
				buf.append("<"+fieldName+">" + fieldVal + "</"+fieldName+">");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buf.append("</TRANS_DETAIL>");
		return buf.toString();
	}
	
	public String toMultXml(List list){
		StringBuffer buf = new StringBuffer();
		buf.append("<TRANS_DETAILS>");
		for(Object obj: list){
			buf.append(toOneXml(obj));
		}
		buf.append("</TRANS_DETAILS>");
		return buf.toString();
	}
	
	public void toBean(String xml){
		Map<String, Object> map = this.toMap(xml);
		this.fitToObject(map);
	}
	
	@SuppressWarnings("unchecked")
	private void fitToObject(Map<String, Object> map) {
		try{
			Class<?> cls = this.getClass();
			Method[] methods = cls.getDeclaredMethods();  
	        Field[] fields = cls.getDeclaredFields();
	        
	        String listName = "";
	        Class<?> listType = null;
	        for (Field field : fields) {
	        	try {
	        		String fieldSetName = parSetName(field.getName());
	        		if (!checkSetMet(methods, fieldSetName)) {  
	                    continue;  
	                }
	        		Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
					Object value = map.get(field.getName());
					String fieldType = field.getType().toString();
					if (fieldType.contains("String")) {  
	                    fieldSetMet.invoke(this, value);  
	                } else if(fieldType.contains("List")){
	                	listName = field.getName();
	                	listType = field.getType();
	                }
	        	} catch (Exception e) {  
	                continue;  
	            } 
	        }
	        List<Map<String, Object>> detailMaps = (List<Map<String, Object>>)map.get("TRANS_DETAILS");
	        if(null != detailMaps && detailMaps.size() > 0){
		        Class<?> dcl = MsgBody.class;
		        List details = new ArrayList();
		    	for(Map<String, Object> detailMap: detailMaps){
		    		Object detail = dcl.newInstance();
		    		Method[] dmethods = dcl.getDeclaredMethods();  
		            Field[] dfields = dcl.getDeclaredFields();
		            for (Field field : dfields) {
				    	try {
			        		String fieldSetName = parSetName(field.getName());
			        		if (!checkSetMet(dmethods, fieldSetName)) {  
			                    continue;  
			                }
			        		Method fieldSetMet = dcl.getMethod(fieldSetName, field.getType());
							Object value = detailMap.get(field.getName());
							String fieldType = field.getType().toString();
							if (fieldType.contains("String")) {  
			                    fieldSetMet.invoke(detail, value);  
			                }
			        	} catch (Exception e) {  
			                continue;  
			            } 
		            }
		            details.add(detail);
		    	}
		    	String fieldSetName = parSetName(listName);
        		if (checkSetMet(methods, fieldSetName)) {  
        			Method fieldSetMet = cls.getMethod(fieldSetName, listType);
        			fieldSetMet.invoke(this, details);
                }
	        }
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ??xml????????????map
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> toMap(String xml){
		Map<String, Object> maps = new HashMap<String, Object>();
		try {
			// ???????????????
			StringReader read = new StringReader(xml);
			// ????????????SAX ??????????? InputSource ???????????ζ?? XML ????
			InputSource source = new InputSource(read);
			// ??????????SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// ???????????????Document
			Document doc = sb.build(source);
			// ???????
			Element root = doc.getRootElement();
			List<Element> children = root.getChildren();
			for(Element child: children){
				if("TRANS_DETAILS".equals(child.getName())){
					List<Element> details = child.getChildren();
					List<Map<String, Object>> detailinfos = new ArrayList<Map<String, Object>>();
					for(Element detail: details){
						Map<String, Object> detailMap = new HashMap<String, Object>();
						List<Element> infos = detail.getChildren();
						for(Element info: infos){
							detailMap.put(info.getName(), info.getTextTrim());
						}
						detailinfos.add(detailMap);
					}
					maps.put("TRANS_DETAILS", detailinfos);
				} else 
					maps.put(child.getName(), child.getTextTrim());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	/**
	 * ????????? get????
	 * 
	 * @param fieldName
	 * @return String
	 */
	private String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
	
	/** 
     * ??????????? set???? 
     * @param fieldName 
     * @return String 
     */  
    private String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        return "set" + fieldName.substring(0, 1).toUpperCase()  
                + fieldName.substring(1);  
    } 

	/**
	 * ?ж????????????? get????
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
    private boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}
	/** 
     * ?ж????????????? set???? 
     * @param methods 
     * @param fieldSetMet 
     * @return boolean 
     */  
    private boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }

	public String getMSG_SIGN() {
		return MSG_SIGN;
	}

	public void setMSG_SIGN(String msg_sign) {
		MSG_SIGN = msg_sign;
	}

	public String getTRANS_STATE() {
		return TRANS_STATE;
	}

	public void setTRANS_STATE(String trans_state) {
		TRANS_STATE = trans_state;
	}

	public String getMSG_TYPE() {
		return MSG_TYPE;
	}

	public void setMSG_TYPE(String msg_type) {
		MSG_TYPE = msg_type;
	} 
}
