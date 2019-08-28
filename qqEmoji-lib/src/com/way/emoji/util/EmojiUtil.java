package com.way.emoji.util;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author way
 *
 */
public class EmojiUtil {
	public static final String STATIC_FACE_PREFIX = "f_static_";
	public static final String DYNAMIC_FACE_PREFIX = "f";
    public Context context;
	private EmojiUtil(Context context) {
        this.context=context;
		initEmojiMap();
	}

	private static String[] emojis = new String[]{
			EaseSmileUtils.ee_1,
			EaseSmileUtils.ee_2,
			EaseSmileUtils.ee_3,
			EaseSmileUtils.ee_4,
			EaseSmileUtils.ee_5,
			EaseSmileUtils.ee_6,
			EaseSmileUtils.ee_7,
			EaseSmileUtils.ee_8,
			EaseSmileUtils.ee_9,
			EaseSmileUtils.ee_10,
			EaseSmileUtils.ee_11,
			EaseSmileUtils.ee_12,
			EaseSmileUtils.ee_13,
			EaseSmileUtils.ee_14,
			EaseSmileUtils.ee_15,
			EaseSmileUtils.ee_16,
			EaseSmileUtils.ee_17,
			EaseSmileUtils.ee_18,
			EaseSmileUtils.ee_19,
			EaseSmileUtils.ee_20,
			EaseSmileUtils.ee_21,
			EaseSmileUtils.ee_22,
			EaseSmileUtils.ee_23,
			EaseSmileUtils.ee_24,
			EaseSmileUtils.ee_25,
			EaseSmileUtils.ee_26,
			EaseSmileUtils.ee_27,
			EaseSmileUtils.ee_28,
			EaseSmileUtils.ee_29,
			EaseSmileUtils.ee_30,
			EaseSmileUtils.ee_31,
			EaseSmileUtils.ee_32,
			EaseSmileUtils.ee_33,
			EaseSmileUtils.ee_34,
			EaseSmileUtils.ee_35,

	};
	private static String[] countName=new String[]{
			"142",
			"143",
			"144",
			"145",
			"146",
			"147",
			"148",
			"149",
			"150",
			"151",
			"152",
			"153",
			"154",
			"155",
			"156",
			"157",
			"158",
			"159",
			"160",
			"161",
			"162",
			"163",
			"164",
			"165",
			"166",
			"167",
			"168",
			"169",
			"170",
			"171",
			"172",
			"173",
			"174",
			"175",
			"176",
	};
//	private static int[] icons = new int[]{
//			R.drawable.ee_1,
//			R.drawable.ee_2,
//			R.drawable.ee_3,
//			R.drawable.ee_4,
//			R.drawable.ee_5,
//			R.drawable.ee_6,
//			R.drawable.ee_7,
//			R.drawable.ee_8,
//			R.drawable.ee_9,
//			R.drawable.ee_10,
//			R.drawable.ee_11,
//			R.drawable.ee_12,
//			R.drawable.ee_13,
//			R.drawable.ee_14,
//			R.drawable.ee_15,
//			R.drawable.ee_16,
//			R.drawable.ee_17,
//			R.drawable.ee_18,
//			R.drawable.ee_19,
//			R.drawable.ee_20,
//			R.drawable.ee_21,
//			R.drawable.ee_22,
//			R.drawable.ee_23,
//			R.drawable.ee_24,
//			R.drawable.ee_25,
//			R.drawable.ee_26,
//			R.drawable.ee_27,
//			R.drawable.ee_28,
//			R.drawable.ee_29,
//			R.drawable.ee_30,
//			R.drawable.ee_31,
//			R.drawable.ee_32,
//			R.drawable.ee_33,
//			R.drawable.ee_34,
//			R.drawable.ee_35,
//	};

	private static EmojiUtil instance;

	public static EmojiUtil getInstance(Context context) {
		if (null == instance)
			instance = new EmojiUtil(context);
		return instance;
	}

	private Map<String, String> mEmojiMap;

	private void initEmojiMap() {
		mEmojiMap = new LinkedHashMap<String, String>();
        try {
            //mEmojiMap= XmlPullUtls.getInstance().parse(context.getAssets().open("emoji.xml"));
			for (int i=0;i<emojis.length;i++) {
				mEmojiMap.put(emojis[i],countName[i]);

			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Map<String, String> getFaceMap() {
		return mEmojiMap;
	}

	public String getFaceId(String faceStr) {
		if (mEmojiMap.containsKey(faceStr)) {
			return mEmojiMap.get(faceStr);
		}
		return "";
	}

}
