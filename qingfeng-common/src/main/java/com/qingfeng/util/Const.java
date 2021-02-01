package com.qingfeng.util;

/**
 * @Title: Const
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2020-9-27 11:28
 */
public class Const {

	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(toLogin)|(outLogin)|(code)|(getCode)|(api)|(lost_login)|(getToken)|(phoneRecord)|(getScore)|(getScore)|(securityReminder)|(authorize)|(getuserinfo)|(error)|(port)|(orchidport)|(backProperty)|(backParking)|(http)).*";    //不对匹配该值的访问路径拦截（正则）


	public static final String S_KEY = ".*((select)|(from)).*";	//查询必须匹配
	public static final String E_KEY = ".*((insert)|(delete)|(update)|(drop)|(alter)).*";

	public static final String  WX_SAFE_PATH = "";

}


