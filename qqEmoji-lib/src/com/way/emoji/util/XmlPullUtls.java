package com.way.emoji.util;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jame on 2017/6/8.
 */

public class XmlPullUtls {
	private static XmlPullUtls instance;

	public static XmlPullUtls getInstance() {
		if (null == instance)
			instance = new XmlPullUtls();
		return instance;
	}
	public Map<String, String> parse(InputStream is) throws Exception {
		Map<String, String> mEmojiMap = null;
		// 由android.util.Xml创建一个XmlPullParser实例
		XmlPullParser xpp = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		xpp.setInput(is,"UTF-8");
		// 产生第一个事件
		int eventType = xpp.getEventType();
		String key="";
		String value="";
		while (eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
				// 判断当前事件是否为文档开始事件
				case XmlPullParser.START_DOCUMENT:
					mEmojiMap=new LinkedHashMap<String, String>();
					break;
				// 判断当前事件是否为标签元素开始事件
				case XmlPullParser.START_TAG:

					if (xpp.getName().equals("dict")) { // 判断开始标签元素是否是book
						//beauty = new Beauty();
					} else if (xpp.getName().equals("key")) {
						eventType = xpp.next();//让解析器指向name属性的值
						// 得到name标签的属性值，并设置beauty的name
						if (xpp.getText().length()>0){
							key="["+xpp.getText().substring(1,xpp.getText().length())+"]";
						}

					} else if (xpp.getName().equals("string")) { // 判断开始标签元素是否是book
						eventType = xpp.next();//让解析器指向age属性的值
						// 得到age标签的属性值，并设置beauty的age
						value=xpp.getText();
					}

					break;

				// 判断当前事件是否为标签元素结束事件
				case XmlPullParser.END_TAG:
					if (xpp.getName().equals("dict")) { // 判断结束标签元素是否是book
					/*	mList.add(beauty); // 将book添加到books集合
						beauty = null;*/
						if (!TextUtils.isEmpty(key))
							mEmojiMap.put(key, value);
					}
					break;
			}
			// 进入下一个元素并触发相应事件
			eventType = xpp.next();
		}
		return mEmojiMap;

	}

}
