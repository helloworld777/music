package com.music.model;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.music.bean.Mp3Info;

import android.text.TextUtils;

public class XmlParseModel {
	SAXReader saxReader=new SAXReader();
	Document document;
	public Mp3Info parseMp3FromString(String string){
		Mp3Info mp3Info=null;
		if(TextUtils.isEmpty(string)){
			return null;
		}
		try {
			mp3Info=new Mp3Info();
			document=saxReader.read(new ByteArrayInputStream(string.getBytes()));
			Element element=document.getRootElement();
			listNodes(element,mp3Info);
			String encode=mp3Info.encode;
			String decode=mp3Info.decode;
			if(!TextUtils.isEmpty(decode)&&!TextUtils.isEmpty(encode)){
				String url=encode.substring(0, encode.lastIndexOf("/")+1)+decode;
//				mp3Info.setUrl(url);
				mp3Info.setDownUrl(url);
			}else{
				return null;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		return mp3Info;
		
	}
	@SuppressWarnings("unchecked")
	public  void listNodes(Element node,Mp3Info mp3Info) {  
        System.out.println("node name:" + node.getName());  
        // ��ȡ��ǰ�ڵ���������Խڵ�  
		
		List<Attribute> list = node.attributes();  
        // �������Խڵ�  
        for (Attribute attr : list) {  
            System.out.println(attr.getText() + "-----" + attr.getName()  
                    + "---" + attr.getValue());  
        }  
        if(node.getName().equals("type")){
//        	mp3.setTitle(auto_tv_key.getText().toString()+"."+node.getText());
        }
        if(node.getName().equals("size")){
        	mp3Info.setSize(Long.parseLong(node.getText()));
        }
        if(node.getName().equals("decode")){
        	mp3Info.decode=node.getText();;
        }
        if(node.getName().equals("encode")){
        	mp3Info.encode=node.getText();
        }
        if (!(node.getTextTrim().equals(""))) {  
            System.out.println("node.getText��" + node.getText());  
        }  
    	
        // ��ǰ�ڵ������ӽڵ������  
        Iterator<Element> it = node.elementIterator();  
        // ����  
        while (it.hasNext()) {  
            // ��ȡĳ���ӽڵ����  
            Element e = it.next();  
            // ���ӽڵ���б���  
            listNodes(e,mp3Info);  
        }  
    }  
}
