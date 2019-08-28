package com.nahuo.quicksale.db;

public interface DBColumns {

	/**
	 * @description 省市区
	 * @created 2014-8-22 下午5:29:00
	 * @author ZZB
	 */
	interface Area {
		public static final String TABLE_NAME = "Sys_AreaPro";
		public static final String ID = "ID";
		public static final String NAME = "Name";
		public static final String LAYER = "Layer";
		public static final String SN = "SN";
		public static final String PARENT_ID = "ParentID";
		public static final String PARENT_NAME = "ParentName";
		public static final String FLAG = "Flag";
	}

	/**
	 * @description 银行
	 * @created 2014-8-22 下午5:29:16
	 * @author ZZB
	 */
	interface Bank {
		public static final String TABLE_NAME = "Sys_Bank";
		public static final String ID = "ID";
		public static final String NAME = "Name";
		public static final String PARENT_ID = "ParentId";
		public static final String PARENT_NAME = "ParentName";
		public static final String CITY_ID = "CityID";
		public static final String AREA_NAME = "AreaName";
		public static final String IS_UPDATE = "IsUpdate";
		public static final String SORT_NUMBER = "SortNumber";
		public static final String IS_ENABLED = "IsEnabled";

	}

	/**
	 * @description 聊天用户
	 */
	interface ChatUser {
		public static final String TABLE_NAME = "Chat_User";
		public static final String COLUMN_NAME_NICK = "nick";
		public static  final String COLUMN_NAME_ID="username";
		public static  final String COLUMN_NAME_IS_STRANGER="is_stranger";
	}
	
	/**
	 * @description 聊天用户
	 */
	interface ConversionUser {
		public static final String TABLE_NAME = "Conversion_User";
		public static final String COLUMN_NAME_NICK = "nick";
		public static  final String COLUMN_NAME_ID="username";
	}

}
