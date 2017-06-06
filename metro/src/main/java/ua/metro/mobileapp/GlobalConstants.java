package ua.metro.mobileapp;

public class GlobalConstants {
	
	public final static int retCodeLeft = 1;
	public final static int retCodeRight = 2;
	public final static int retCodeClose = 0;
	
	public final static int REFRESH_INET_TIME = 1000;
	
	public final static int REQUEST_CODE_ZBAR = 9955;
	public final static int REQUEST_CODE_SCAN = 9956;
	public final static int REQUEST_CODE_TEMP = 9957;
	public final static int REQUEST_CODE_CONFIRM = 9958;
	public final static int REQUEST_CODE_INET = 9959;
	
	//public final static String API_PATH = "https://stage.metro.lestro.com/api/v1/";
	public final static String API_PATH = "https://prod.metro.lestro.com/api/v1/";

	public final static int HTTP_GET = 0;
	public final static int HTTP_POST = 1;
	public final static int HTTP_DEL = 2;
	
	public final static int CARD_HAS_PROFILE = 0;
	public final static int REGISTER_CARD = 1;
	public final static int RESET_PASSWORD = 2;
	public final static int FEEDBACK = 3;
	public final static int AUTH = 4;
	public final static int CATALOG = 5;
	public final static int CATALOG_ID = 6;
	public final static int CART_GET = 7;
	public final static int CART_ADD = 8;
	public final static int CART_DEL = 9;
	public final static int PRODUCT = 10;
	public final static int GET_REG_META = 11;
	public final static int GET_GEO_AREA = 12;
	public final static int GET_GEO_STREETTYPE = 13;
	public final static int POST_REGISTRATION_FIZ = 14;
	public final static int POST_REGISTRATION_YUR = 15;
	public final static int GET_REGION = 16;
	public final static int GET_USER_REGION = 17;
	public final static int POST_USER_REGION = 18;
	public final static int GET_LOCALE = 19;
	public final static int POST_LOCALE = 20;
	public final static int GET_SHOPS = 21;
	public final static int GET_NEAREST_SHOPS = 22;
	public final static int POST_PERMANENT_CARD = 23;
	public final static int GET_PROFILE_INFO = 24;
	public final static int POST_PROFILE_INFO = 25;
	public final static int GET_SHOP = 26;
	public final static int POST_SEARCH = 27;
	
	public final static int NO_INTERNET_CONNECTION = 999;
	public final static int ERROR_401 = 401;
	public final static int ERROR_500 = 500;
	public final static int ERROR_502 = 502;
	public final static int ERROR_503 = 503;
	public final static int UNDEFINED_ERROR = 997;
	
	
    public static final int[] IMAGE_RESOURCES = { R.drawable.pr_2_00000,
        R.drawable.pr_2_00001, R.drawable.pr_2_00002, R.drawable.pr_2_00003,
        R.drawable.pr_2_00004, R.drawable.pr_2_00005, R.drawable.pr_2_00006,
        R.drawable.pr_2_00007, R.drawable.pr_2_00008, R.drawable.pr_2_00009,
        R.drawable.pr_2_00010, R.drawable.pr_2_00011, R.drawable.pr_2_00012,
        R.drawable.pr_2_00013, R.drawable.pr_2_00014, R.drawable.pr_2_00015,
        R.drawable.pr_2_00016, R.drawable.pr_2_00017, R.drawable.pr_2_00018,
        R.drawable.pr_2_00019, R.drawable.pr_2_00020, R.drawable.pr_2_00021,
        R.drawable.pr_2_00022, R.drawable.pr_2_00023, R.drawable.pr_2_00024,
        R.drawable.pr_2_00025, R.drawable.pr_2_00026, R.drawable.pr_2_00027,
        R.drawable.pr_2_00028, R.drawable.pr_2_00029, R.drawable.pr_2_00030,
        R.drawable.pr_2_00031, R.drawable.pr_2_00032 };
}
