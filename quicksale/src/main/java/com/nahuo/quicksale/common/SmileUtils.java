package com.nahuo.quicksale.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

//专门用来处理笑脸
public class SmileUtils {

	public final static String qqface_array = "微笑,撇嘴,色,发呆,得意,流泪,害羞,闭嘴,睡,大哭,尴尬,发怒,调皮,呲牙,惊讶,难过,酷,冷汗,抓狂,吐,偷笑,愉快,白眼,傲慢,饥饿,困,惊恐,流汗,憨笑,"
			+ "悠闲,奋斗,咒骂,疑问,嘘,晕,疯了,衰,骷髅,敲打,再见,擦汗,抠鼻,鼓掌,糗大了,坏笑,左哼哼,右哼哼,哈欠,鄙视,委屈,快哭了,阴险,亲亲,吓,可怜,菜刀,西瓜,啤酒,篮球,乒乓,咖啡,饭,猪头,玫瑰,凋谢,嘴唇,"
			+ "爱心,心碎,蛋糕,闪电,炸弹,刀,足球,瓢虫,便便,月亮,太阳,礼物,拥抱,强,弱,握手,胜利,抱拳,勾引,拳头,差劲,爱你,NO,OK,爱情,飞吻,跳跳,发抖,怄火,转圈,磕头,回头,跳绳,挥手,激动,街舞,献吻,"
			+ "左太极,右太极";
	/*
	 * public static final String ee_1 = "[):]"; public static final String ee_2
	 * = "[:D]"; public static final String ee_3 = "[;)]"; public static final
	 * String ee_4 = "[:-o]"; public static final String ee_5 = "[:p]"; public
	 * static final String ee_6 = "[(H)]"; public static final String ee_7 =
	 * "[:@]"; public static final String ee_8 = "[:s]"; public static final
	 * String ee_9 = "[:$]"; public static final String ee_10 = "[:(]"; public
	 * static final String ee_11 = "[:'(]"; public static final String ee_12 =
	 * "[:|]"; public static final String ee_13 = "[(a)]"; public static final
	 * String ee_14 = "[8o|]"; public static final String ee_15 = "[8-|]";
	 * public static final String ee_16 = "[+o(]"; public static final String
	 * ee_17 = "[<o)]"; public static final String ee_18 = "[|-)]"; public
	 * static final String ee_19 = "[*-)]"; public static final String ee_20 =
	 * "[:-#]"; public static final String ee_21 = "[:-*]"; public static final
	 * String ee_22 = "[^o)]"; public static final String ee_23 = "[8-)]";
	 * public static final String ee_24 = "[(|)]"; public static final String
	 * ee_25 = "[(u)]"; public static final String ee_26 = "[(S)]"; public
	 * static final String ee_27 = "[(*)]"; public static final String ee_28 =
	 * "[(#)]"; public static final String ee_29 = "[(R)]"; public static final
	 * String ee_30 = "[({)]"; public static final String ee_31 = "[(})]";
	 * public static final String ee_32 = "[(k)]"; public static final String
	 * ee_33 = "[(F)]"; public static final String ee_34 = "[(W)]"; public
	 * static final String ee_35 = "[(D)]";
	 */

	private static final Factory spannableFactory = Spannable.Factory
			.getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();
	private static final Map<String,String> smail=new HashMap<String, String>();

	public static void setinitparm(Context context) {

		if (emoticons.isEmpty()) {
			List<String> faceList = Arrays.asList(qqface_array.split(","));
			int i = 0;
			for (String text : faceList) {
				String Content = "[" + text + "]";
				String filename = "qq_" + i;
				int rid = context.getResources().getIdentifier(filename,
						"drawable", context.getPackageName());
				addPattern(emoticons, Content, rid);
				smail.put(filename, Content);
				i++;
			}
		}
	}
	
	public static String getSmailStr(String ico,Context context)
	{
		if(smail.isEmpty())
		{
			setinitparm(context);
		}
		return smail.get(ico);
	}
	
	
  
	


	private static void addPattern(Map<Pattern, Integer> map, String smile,
			int resource) {

		map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * 
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
		boolean hasChanges = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(spannable);
			while (matcher.find()) {
				boolean set = true;
				for (ImageSpan span : spannable.getSpans(matcher.start(),
						matcher.end(), ImageSpan.class))
					if (spannable.getSpanStart(span) >= matcher.start()
							&& spannable.getSpanEnd(span) <= matcher.end())
						spannable.removeSpan(span);
					else {
						set = false;
						break;
					}
				if (set) {
					hasChanges = true;
					spannable.setSpan(new ImageSpan(context, entry.getValue()),
							matcher.start(), matcher.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
		Spannable spannable = spannableFactory.newSpannable(text);
		addSmiles(context, spannable);
		return spannable;
	}

	public static boolean containsKey(String key) {
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(key);
			if (matcher.find()) {
				b = true;
				break;
			}
		}

		return b;
	}

}
