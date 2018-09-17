package com.junl.wpwx.action.Interface;


import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.junl.frame.tools.CommonUtils;
import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.JsonMapper;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.form.AttenForm;
import com.junl.wpwx.model.*;
import com.junl.wpwx.pay.Alipay;
import com.junl.wpwx.pay.PayCommonUtil;
import com.junl.wpwx.pay.weixinPrePay;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.SysHolidayService;
import com.junl.wpwx.service.app.AppChildInfoService;
import com.junl.wpwx.service.jpush.JpushMessageService;
import com.junl.wpwx.service.vaccinate.*;
import com.junl.wpwx.utils.DateTools;
import com.junl.wpwx.utils.FuxinUtil;
import com.junl.wpwx.utils.ImageUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/api")
public class AppDataController  extends BaseAction {

    @Autowired
    AsyncService async;
    @Resource
    private Ehcache cache;
    @Autowired
    private AppChildInfoService appChildService;

    @Autowired
    private VacService vacService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private VacChildTempService vacchildTempService;
    
    @Autowired
    private VacKnowledgeListService vacknowledgelistService;//疫苗知识
    
    @Autowired
    private VacKnowledgeSicknessService vacknowledgeSicknessService;//疾病知识
    
    @Autowired
    private VacChildInfoService vacchildInfoService;
    
    @Autowired
    private SysHolidayService sysholidayService;
    
    @Autowired
    private WorkingHoursService workService;
    
    @Autowired
    private BsOrderService orderService;//订单表
    
    @Autowired
    private ConfigProperty conf;
    
    @Autowired
    private JpushMessageService jpushMessageService;//推送
    
    @Autowired
    private DepartmentInfoService departmentInfoService;//接种单位详情
    
    @Autowired
    private NotificationService notificationService;//服务通知
    
   /**登录接口 --参数
    * edit by wangnan
    * 2018-2-7
    * 参数：
    * phone，注册手机
    * password，密码
    * request中有registrationID
	APP,通过post方式
    */
   @RequestMapping(value="/checkloginAPP", method = RequestMethod.POST)
   public @ResponseBody JSONMessage checkloginAPP(HttpServletRequest request, String mobile, String password){

	   String registrationID = null;//极光推送标识
	   String type_ = null;//区分IOS/ANDROID
	   
	   //获取headers文件
	   Enumeration<String> headerNames=request.getHeaderNames();
	   for(Enumeration<String> e=headerNames;e.hasMoreElements();){
		      String thisName=e.nextElement().toString();
		      String thisValue=request.getHeader(thisName);
		        System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
		      if("registrationid".equals(thisName)){
		    	  registrationID = thisValue;
		      }
		      if("apptype".equals(thisName)){
		    	  type_ = thisValue;
		      }
	   }
	  
	   System.out.println(registrationID+"--"+type_+"--");
	   if (!StringUtils.isNoneBlank(registrationID,type_)) {
		   logger.info("headers参数传输为空");
//           return new JSONMessage(false, "头文件参数错误");
	   }
	   
	//APP
	   //极光推送可能为空
//		   if (StringUtils.isNoneBlank(mobile,password,registrationID)) {  
	   if (StringUtils.isNoneBlank(mobile,password)) {
		   //通过手机号查询用户是否已经注册，通过获得用户密码
         
	   String pwd = appChildService.getPwdByPhone(mobile);
          //数据库中密码不存在，未在APP中注册
          if ((pwd == null) || ("".equals(pwd.trim()))) {
       	   
        	  logger.info("用户未注册");
              return new JSONMessage(false, "用户未注册");
              
          } else {
        	  
       		   //密码存在，判断密码是否正确
   		   if (!password.equals(pwd)) {
   			   
           	   logger.info("密码错误");
                  return new JSONMessage(false, "密码错误");
              } else {
           	   //密码正确，返回关注儿童信息
           	   //类需要修改
                  AppUser user=new AppUser();
                  user.setPhone(mobile);
                  user.setRegistrationid(registrationID);
                  //类需要修改，更新极光推送标识
                  appChildService.updateByregisId_1(user);
                  
                  //返回是否关注宝宝
                  Integer count = appChildService.getByPhone_1(mobile);
                  HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
                  userInfo.put("isAttention", count+"");

                  return new JSONMessage(true, "登录成功",userInfo);
                   
              }//else
          }//else
	   }else{
		   //用户名或密码为空
		   logger.info("请输入用户名或密码");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
	   }

  }
   
   
   /**登录接口：APP第三方 mark=wx微信，mark=qq QQ，mark=wb微博,通过post方式
    * edit by wangnan 2018-2-7
    * 参数： mark，标志区分登录方式
    * uid：unionid
    * mark=qq QQ，mark=wb微博,通过post方式
    */
    @RequestMapping(value="/checkloginAPPSS", method = RequestMethod.POST)
    public @ResponseBody JSONMessage checkloginAPPSS(HttpServletRequest request,String uid,String type){
  	  
    	String registrationID = null;//极光推送标识
    	String type_ = null;//区分IOS/ANDROID
  	   
  	   //获取headers文件
  	   	Enumeration<String> headerNames=request.getHeaderNames();
  	   	for(Enumeration<String> e=headerNames;e.hasMoreElements();){
  		      String thisName=e.nextElement().toString();
  		      String thisValue=request.getHeader(thisName);
  		        System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
  		      if("registrationid".equals(thisName)){
  		    	  registrationID = thisValue;
  		      }
  		      if("apptype".equals(thisName)){
  		    	type_ = thisValue;
  		      }
  	   	}
  	   	
//  	   	System.out.println(registrationID+"--"+type_+"--");
  	   	if (!StringUtils.isNoneBlank(registrationID,type_)) {
  	   		logger.info("headers参数传输为空");
//             return new JSONMessage(false, "头文件参数错误");
  	   }
  	   
  	   //registrationID，mark，uid
  	//极光推送可能为空
//  	   if (StringUtils.isNoneBlank(registrationID,type,uid)) {
  	  if (StringUtils.isNoneBlank(type,uid)) {
  		   String phone_value=null;
  		   //根据mark 区分登录方式,获得手机号码
  		   if("wx".equals(type)){
  			   phone_value = appChildService.getPhoneByUID_WX(uid);
  		   }
  		   else if("qq".equals(type)){
  			   phone_value = appChildService.getPhoneByUID_QQ(uid);
    		   
  		   }
  		   else if("wb".equals(type)){
  			   phone_value = appChildService.getPhoneByUID_WB(uid);
  		   }else{
  			   
  			 return new JSONMessage(false, "参数错误");
  		   }
  		   
           // 对比是否一致，为空则未注册，不一致则已绑定其他第三方
            //为空null
  		   if ((phone_value == null) || ("".equals(phone_value.trim()))) {
         	   
  			   logger.info("用户未绑定手机号");
  			   HashMap<String, String> userInfo = new HashMap<String, String>();
  			   userInfo.put("ISBIND", "0");
  			   return new JSONMessage(true, "用户未绑定手机号",userInfo);//前台绑定页面
                
  		   }
  		   
  		   if (phone_value.length()!=0) {
         	   //手机绑定其他微信/qq/微博   			   
             	 
     		   //账号密码一致，查询是否关注宝宝	        		   
     		   	AppUser user=new AppUser();
                user.setPhone(phone_value);
                user.setRegistrationid(registrationID);
                //类需要修改，更新极光推送标识
                appChildService.updateByregisId_1(user);
                
              //返回是否关注宝宝
                Integer count = appChildService.getByPhone_1(phone_value);
                HashMap<String, String> userInfo = appChildService.getIDByPhone(phone_value);
                userInfo.put("isAttention", count+"");
                userInfo.put("ISBIND", "1");
                
                return new JSONMessage(true, "登录成功",userInfo);
                
            }//if
            
  	   }else{//为空
  		   //用户名或密码为空
  		   logger.info("参数错误");
  		   return new JSONMessage(false, "参数传输错误，参数不能为空");
  	   }
  	   return new JSONMessage(false, "参数错误");
    }
     
    //

	/**
	微信登录接口 --参数：openid
	
	*/
	@RequestMapping("/checkloginWX")
	public @ResponseBody JSONMessage checkloginWX(String openID){

	//微信
	   if (StringUtils.isNoneBlank(openID)) {
		   
		   //通过手机号查询用户是否已经注册，通过获得用户密码
	       String phone_value = appChildService.getPhoneByOpenID(openID);
	       
	       if ((phone_value == null) || ("".equals(phone_value.trim()))) {
	    	   
	    	   logger.info("用户未绑定手机号");
	           return new JSONMessage(false, "用户未绑定手机号");//跳转到绑定手机号界面
	           
	       } else {
	    	  
			   //手机号存在，判断是否一致
	    	   //手机号和微信已绑定
	           //返回是否关注宝宝
	           Integer count = appChildService.getByPhone_1(phone_value);
	           if (count < 1) {
	        	   
	               return new JSONMessage(true, "没有关注宝宝");
	           } else {
	        	   
	               return new JSONMessage(true, "已经关注宝宝");
	            }
	       }//else
	
	   }else{
		   //用户名或密码为空
		   logger.info("参数错误");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
	   }	           
	   
	}
	
	//退出登录
	
	/**
	退出登录，需要更改
	
	*/
	@RequestMapping("/loginOut")
	public @ResponseBody JSONMessage loginOut(String ID){
		 if (StringUtils.isNoneBlank(ID)) {
			 
	    	   logger.info("用户退出");
	           return new JSONMessage(true, "用户退出成功");// 单一登录需要修改
		 }
		 return new JSONMessage(false, "参数错误");// 
	}


    /**
     * 发送短信验证码
     * 参数：手机号
     */
    @RequestMapping(value="/sendchecksms", method = RequestMethod.POST)
    public @ResponseBody JSONMessage sendchecksms( String mobile){
        if(async.sendCheckSMS(mobile)){
            return new JSONMessage( true, "短信发送成功");
        }else{
            return new JSONMessage(false, "短信发送失败");
        }
    }

    /**
    	APP注册接口   1：注册 2：找回密码 
    	edit by wangnan 2018-2-7
    	通过post方式
    */
   @RequestMapping(value="/register", method = RequestMethod.POST)
   public @ResponseBody JSONMessage register(HttpServletRequest request,String mobile,String password, String code ,String type){
      
	   	String registrationID = null;//极光推送标识
   		String type_ = null;//区分IOS/ANDROID
 	   
 	   //获取headers文件
 	   	Enumeration<String> headerNames=request.getHeaderNames();
 	   	for(Enumeration<String> e=headerNames;e.hasMoreElements();){
 		      String thisName=e.nextElement().toString();
 		      String thisValue=request.getHeader(thisName);
 		      System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
 		      if("registrationid".equals(thisName)){
 		    	  registrationID = thisValue;
 		      }
 		      if("apptype".equals(thisName)){
 		    	type_ = thisValue;
 		      }
 	   	}
 	   	
// 	   	System.out.println(registrationID+"--"+type_+"--");
 	   	if (!StringUtils.isNoneBlank(registrationID,type_)) {
 	   		logger.info("headers参数传输为空");
//            return new JSONMessage(false, "头文件参数错误");
 	   }
 	   	
	   //判断参数是否为空
//	   if (StringUtils.isNoneBlank(mobile,password,code,registrationID)) {
 	   if (StringUtils.isNoneBlank(mobile,password,code)) {   
		   try {
               //获取缓存中的验证码
               String code_value = (String) cache.getCache(mobile, "messageCache");//获取验证码
              //测试接口，验证码固定
//        	   String code_value = "1234";
              
               if (code.equals(code_value)) {//验证码正确
                   if("1".equals(type)){
                	   //查询用户是否存在
                	   Integer appList = appChildService.getIsExistByPhone(mobile);//用户是否存在                       
                	   //不存在，插入用户信息
                	   if(appList<1){
                		   //此处未改完2018-2-5
                           //新建用户插入数据库
                           AppUser appUser=new AppUser();
                           String ID = UUID.randomUUID().toString();
                           appUser.setId(ID);
                           appUser.setPhone(mobile);
                           appUser.setUserpwd(password);
                           appUser.setRegistrationid(registrationID);
//                           appChildService.save(appUser);//应该用此方式，不写ID
                           appChildService.insertUserByApp(appUser);//插入app用户表
                           
                           //注册成功，查询是否关注宝宝
                           //返回是否关注宝宝
                           Integer count = appChildService.getByPhone_1(mobile);
//                           HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
                           HashMap<String, String> userInfo = new HashMap<String, String>();
                           
                           userInfo.put("isAttention", count+"");
                           userInfo.put("PHONE", mobile);
                           userInfo.put("ID", ID);
                        //未关注宝宝,已关注宝宝一起
                           
                           return new JSONMessage(true, "注册成功",userInfo);
                            
                       }else{
                    	   //存在，是否注册过？注册过提示已经注册
                    	   return new JSONMessage( false, "此手机号已经注册");
                       }
                       
                   }else if("2".equals(type)){
                	   
                	   //查询用户是否存在
                	   Integer appList = appChildService.getIsExistByPhone(mobile);//用户是否存在                       
                	   
                	   //用户存在，进入忘记密码流程
                	   if(appList>0){

                           //找回密码修改数据库
                           AppUser appUser=new AppUser();
                           appUser.setPhone(mobile);
                           appUser.setUserpwd(password);
                           appUser.setRegistrationid(registrationID);
                           appChildService.updateUserByPhone(appUser);//修改用户表
                           
                         //返回是否关注宝宝
                           Integer count = appChildService.getByPhone_1(mobile);
                           HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
                           userInfo.put("isAttention", count+"");
                         
                           return new JSONMessage( true, "密码找回成功",userInfo);//之后是否直接登录
                           
                       }else{
                    	   
                           return new JSONMessage( false, "此手机号未注册");
                       }
                	   
                   }else{
                	   return new JSONMessage( false, "参数错误");//注册、找回密码
                   }
                   
               }else{
            	   return new JSONMessage( false, "验证码输入错误");
               }
              
           } catch (Exception e) {
               //捕获异常，不处理
        	   logger.error(e+"");
           }
       }else{
		   //用户名或密码为空
		   logger.info("参数不能为空");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
	   }
       return new JSONMessage(false, "登录验证失败");
   }
  
	   /**
		* APP注册接口   1：注册 2：找回密码 
		* edit by wangnan 2018-2-7
		* 参数：headers文件，，
	    * mobile，手机
	    * password，密码
	    * code，验证码
		* 通过post方式
	*/
	@RequestMapping(value="/registerAPP", method = RequestMethod.POST)
	public @ResponseBody JSONMessage registerAPP(HttpServletRequest request,String mobile,String password, String code){
	 
		String registrationID = null;//极光推送标识
    	String type_ = null;//区分IOS/ANDROID
  	   
  	   //获取headers文件
  	   	Enumeration<String> headerNames=request.getHeaderNames();
  	   	for(Enumeration<String> e=headerNames;e.hasMoreElements();){
  		      String thisName=e.nextElement().toString();
  		      String thisValue=request.getHeader(thisName);
  		        System.out.println("registerAPP:header的key:"+thisName+"--------------header的value:"+thisValue);
  		      if("registrationid".equals(thisName)){
  		    	  registrationID = thisValue;
  		      }
  		      if("apptype".equals(thisName)){
  		    	type_ = thisValue;
  		      }
  	   	}
  	   	
//  	   	System.out.println(registrationID+"--"+type_+"--");
  	   	if (!StringUtils.isNoneBlank(registrationID,type_)) {
  	   		logger.info("headers参数传输为空");
//             return new JSONMessage(false, "头文件参数错误");
  	   }
  	   	
//		if (StringUtils.isNoneBlank(mobile,password,code,registrationID)) {
  	  if (StringUtils.isNoneBlank(mobile,password,code)) {
	      try {
	          //获取缓存中的验证码
	          String code_value = (String) cache.getCache(mobile, "messageCache");//获取验证码
	         //测试接口，验证码固定
	//   	   String code_value = "1234";
	         
	          if (code.equals(code_value)) {//验证码正确
	           	   //查询用户是否存在
	           	   Integer appList = appChildService.getIsExistByPhone(mobile);//用户是否存在                       
	           	   //不存在，插入用户信息
	           	   if(appList<1){
	           		   //此处未改完2018-2-5
	                      //新建用户插入数据库
	                      AppUser appUser=new AppUser();
	                      String ID = UUID.randomUUID().toString();
	                      appUser.setId(ID);
	                      appUser.setPhone(mobile);
	                      appUser.setUserpwd(password);
	                      appUser.setRegistrationid(registrationID);
	//                      appChildService.save(appUser);//应该用此方式，不写ID
	                      appChildService.insertUserByApp(appUser);//插入app用户表
	                      
	                      //注册成功，查询是否关注宝宝
	                      //返回是否关注宝宝
	                      Integer count = appChildService.getByPhone_1(mobile);
	//                      HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
	                      HashMap<String, String> userInfo = new HashMap<String, String>();
	                      
	                      userInfo.put("isAttention", count+"");
	                      userInfo.put("PHONE", mobile);
	                      userInfo.put("ID", ID);
	                   //未关注宝宝,已关注宝宝一起
	                      
	                      return new JSONMessage(true, "登录成功",userInfo);
	                       
	                  }else{
	               	   //存在，是否注册过？注册过提示已经注册
	               	   	return new JSONMessage( false, "此手机号已经注册");
	                  }	                  
	              
	          }else{
	        	  return new JSONMessage( false, "验证码输入错误");
	          }
	         
	      } catch (Exception e) {
	          //捕获异常，不处理
	   	   	logger.error(e+"");
	      }
	  }else{
		   //用户名或密码为空
		   logger.info("参数不能为空");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
	  	}
	  	return new JSONMessage(false, "登录验证失败");
	}


	/**
	* 第三方绑定接口 appChildService
    * edit by wn 2018-2-7
    * 参数：headers文件，
    * type(区分微信、qq、微博)，
    * mobile，手机
    * password，密码
    * code，验证码
    * uid：unionid
    */
   @RequestMapping(value="/bindUid", method = RequestMethod.POST)
   public @ResponseBody JSONMessage bindUid(HttpServletRequest request,String type,String mobile,String password, String code ,String uid){
      
	   try {
    	  
		   String registrationID = null;//极光推送标识
    	   String type_ = null;//区分IOS/ANDROID
    	   
    	   //获取headers文件
    	   Enumeration<String> headerNames=request.getHeaderNames();
    	   for(Enumeration<String> e=headerNames;e.hasMoreElements();){
    		      String thisName=e.nextElement().toString();
    		      String thisValue=request.getHeader(thisName);
    		        System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
    		      if("registrationid".equals(thisName)){
    		    	  registrationID = thisValue;
    		      }
    		      if("apptype".equals(thisName)){
    		    	  type_ = thisValue;
    		      }
    	   }
    	  
//    	   System.out.println(registrationID+"--"+type_+"--");
    	   if (!StringUtils.isNoneBlank(registrationID,type_)) {
    		   logger.info("headers参数传输为空");
//               return new JSONMessage(false, "头文件参数错误");
    	   }   
    	   
//    	   if (StringUtils.isNoneBlank(mobile,password,code,uid,registrationID)) {
    	   if (StringUtils.isNoneBlank(mobile,password,code,uid)) {
    	   //获取缓存中的验证码
    		   String code_value = (String) cache.getCache(mobile, "messageCache");
//    		   String code_value = "1234";
    		   //验证码正确
    		   if ( code.equals(code_value)) {
        	   
	        	   Integer appList = appChildService.getIsExistByPhone(mobile);//用户是否存在                       
	           	   //不存在，插入用户信息
	           	   if(appList<1){
           		    
	           		   String ID=null;
	           		   if("wx".equals(type)){
	           			//新建用户插入数据库
	                       AppUser appUser=new AppUser();
	                       ID = UUID.randomUUID().toString();
	                       appUser.setId(ID);
	                       appUser.setPhone(mobile);
	                       appUser.setUserpwd(password);//密码
	                       appUser.setRegistrationid(registrationID);
	                       appUser.setUid(uid);//唯一标识，type区分微信、qq、微博
	                       appChildService.insertUserByAppWX(appUser);//插入用户表
		       		   }
		       		   else if("qq".equals(type)){
		       			   
		       			//新建用户插入数据库
		                      AppUser appUser=new AppUser();
		                      ID = UUID.randomUUID().toString();
		                      appUser.setId(ID);
		                      appUser.setPhone(mobile);
		                      appUser.setUserpwd(password);//密码
		                      appUser.setRegistrationid(registrationID);
		                      appUser.setUid(uid);//唯一标识，type区分微信、qq、微博
		                      appChildService.insertUserByAppQQ(appUser);//插入用户表
		       		   }
		       		   else if("wb".equals(type)){
		       			//新建用户插入数据库
		                      AppUser appUser=new AppUser();
		                      ID = UUID.randomUUID().toString();
		                      appUser.setId(ID);
		                      appUser.setPhone(mobile);
		                      appUser.setUserpwd(password);//密码
		                      appUser.setRegistrationid(registrationID);
		                      appUser.setUid(uid);//唯一标识，type区分微信、qq、微博
		                      appChildService.insertUserByAppWB(appUser);//插入用户表
		       		   }else{
		       			   
		       			   return new JSONMessage(false, "参数错误");
		       		   }
                     
                      //注册成功，查询是否关注宝宝
                      //返回是否关注宝宝
                      Integer count = appChildService.getByPhone_1(mobile);
//                      HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
                      HashMap<String, String> userInfo = new HashMap<String, String>();
                      
                      userInfo.put("isAttention", count+"");
                      userInfo.put("PHONE", mobile);
                      userInfo.put("ID", ID);
                   //未关注宝宝,已关注宝宝一起
                      
                      return new JSONMessage(true, "注册成功",userInfo);
                       
                  }else{
               	   //存在，是否注册过？注册过提示已经注册
               	   	return new JSONMessage( false, "此手机号已经注册，请在APP个人中心绑定第三方登录");
                  }	  
           	   
    		   }else{
    			   return new JSONMessage( false, "验证码输入错误");
    		   	}
    		}else{
    			 logger.info("参数不能为空");
    			 return new JSONMessage(false, "参数传输错误，参数不能为空");
  		   }
	   }catch (Exception e) {
               //捕获异常，不处理
		   logger.info(""+e);
      }
      return new JSONMessage( false, "短信验证失败");
   }

   /**
	* APP找回密码（忘记密码） 
	* edit by wangnan 2018-2-7
	* 参数：
	* mobile：手机
	* password：密码
	* code：验证码
	* 通过post方式
	*/
	@RequestMapping(value="/retrievePwd", method = RequestMethod.POST)
	public @ResponseBody JSONMessage retrievePwd(HttpServletRequest request,String mobile,String password, String code ){
		
		String registrationID = null;//极光推送标识
    	String type_ = null;//区分IOS/ANDROID
  	   
  	   //获取headers文件
  	   	Enumeration<String> headerNames=request.getHeaderNames();
  	   	for(Enumeration<String> e=headerNames;e.hasMoreElements();){
  		      String thisName=e.nextElement().toString();
  		      String thisValue=request.getHeader(thisName);
  		      System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
  		      if("registrationid".equals(thisName)){
  		    	  registrationID = thisValue;
  		      }
  		      if("apptype".equals(thisName)){
  		    	type_ = thisValue;
  		      }
  	   	}
  	   	
//  	   	System.out.println(registrationID+"--"+type_+"--");
  	   	if (!StringUtils.isNoneBlank(registrationID,type_)) {
  	   		logger.info("headers参数传输为空");
//             return new JSONMessage(false, "头文件参数错误");
  	   	}
  	   	
  	 
//		if (StringUtils.isNoneBlank(mobile,password,code,registrationID)) {
  	   	if (StringUtils.isNoneBlank(mobile,password,code)) {	
  	   		try {
	          //获取缓存中的验证码
	          String code_value = (String) cache.getCache(mobile, "messageCache");//获取验证码
	         //测试接口，验证码固定
				//String code_value = "1234";         
				if (code.equals(code_value)) {//验证码正确                      	   
	           	   //查询用户是否存在
					Integer appList = appChildService.getIsExistByPhone(mobile);//用户是否存在                       
	           	   
	           	   //用户存在，进入忘记密码流程
	           	   	if(appList>0){
	
	                      //找回密码修改数据库
	           	   		AppUser appUser=new AppUser();
	           	   		appUser.setPhone(mobile);
	           	   		appUser.setUserpwd(password);
	                    appUser.setRegistrationid(registrationID);
	                    appChildService.updateUserByPhone(appUser);//修改用户表
	                      
	                    //返回是否关注宝宝
	                    Integer count = appChildService.getByPhone_1(mobile);
	                    HashMap<String, String> userInfo = appChildService.getIDByPhone(mobile);
	                    userInfo.put("isAttention", count+"");
	                    
	                    return new JSONMessage( true, "密码找回成功",userInfo);//之后是否直接登录
                      
	           	   	}else{
	               	   
	                    return new JSONMessage( false, "此手机号未注册");
	           	   		}
	      
				}else{
	       	   		return new JSONMessage( false, "验证码输入错误");
					}
	         
	      } catch (Exception e) {
	          //捕获异常，不处理
	   	   		logger.error(e+"");
	      }
		}else{
		   //参数为空
		   logger.info("参数不能为空");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
		}
		return new JSONMessage(false, "登录验证失败");
	}

	
	 /**通过用户ID获取已关注的儿童信息
	  * 参数
	  * userId：用户id
	  * edit by wangnan 2018-2-9
    */
   @RequestMapping(value="/getChild", method = RequestMethod.POST)
   public @ResponseBody JSONMessage getChild( String userId,String code){
      
	   if (!StringUtils.isNoneBlank(userId,code)){
		   logger.info("参数为空");
		   return new JSONMessage(false, "参数错误");
	  }
	   //获得列表
	  List<HashMap<String, String>> list = appChildService.getChildByID(userId);
	 //无宝宝信息
	  /*if(list.size()==0){
		  
		  return new JSONMessage(false, "没有宝宝信息");
	  }*/

	  //返回列表
	  List<HashMap<String, String>> returnList =new  ArrayList<HashMap<String,String>>();
	  if("1".equals(code)){
          returnList=list;
      }else {
   
    	  //先加传过来的儿童
          for(HashMap<String, String> map :list){
        	  String childcode = map.get("CHILDCODE");//儿童编号
              if(code.equals(childcode)){
                  returnList.add(map);
              }
          }
        //再加其他儿童
          for(HashMap<String, String> map :list){
        	  String childcode = map.get("CHILDCODE");//儿童编号
              if(!code.equals(childcode)){
                  returnList.add(map);
              }
          }
      }
	  
	  return new JSONMessage(true, "调用接口成功",returnList);	  

   }
   
   /**
    * 查询手机号码
    * birth 宝宝生日 年月日
    * name 宝宝姓名
    * edit by wangnan 2018-2-7
    */
   @RequestMapping(value="/choosephone", method = RequestMethod.POST)
   public @ResponseBody JSONMessage choosephone(String birth, String name){
        if(StringUtils.isNoneBlank(birth,name)){
    	   HashMap<String, String> phonelist = appChildService.choosephone_1(birth, name);
           if(phonelist==null){
               return new JSONMessage(false, "未查询到相关手机号码");
           }else{
        	  /* String phone_value = phonelist.get("FATHERPHONE");
        	   if(phone_value==null)
        		   phonelist.put("FATHERPHONE", "");*/
               return new JSONMessage(true,"接口调用成功", phonelist);
           }
       }else{
    	   return new JSONMessage(false, "参数错误");
       }
       
   }
   
   
   //绑定宝宝档案
   /**
	绑定宝宝档案接口 --参数：birthday，name，phone，code,ID
	宝宝生日，宝宝姓名，选择手机，验证码,用户ID(注册用户ID)
	edit by wangnan 2018-2-8
	*/ 
   @RequestMapping(value="/AttenChild", method=RequestMethod.POST)
   public @ResponseBody JSONMessage AttenChild(String birth, String name,String mobile,String code,String userId){
       //表单数据验证 USERID
       if(!StringUtils.isNoneBlank(birth, name,mobile,code,userId)) {
    	   logger.error("参数不能为空");
           return new JSONMessage(false, "参数错误");
       }else{
           try {
               //获取缓存中的验证码
        	   String code_value = (String) cache.getCache(mobile, "messageCache");
//               String code_value ="1234";
              //验证码比对
               if (code_value.equals(code)) {
                   
                   //验证码正确
            	   String phone_value = appChildService.getPhoneByID(userId);//获取用户手机号
    			   if(!StringUtils.isNotBlank(phone_value)){
    				   return new JSONMessage(false, "用户不存在");
    			   }
            	   //判断用户是否已经关注宝宝
            	   Integer count = appChildService.getIsAtten(birth, name,userId);
            	   
            	   if(count>0){
            		 //已关注，提示宝宝已被关注
            		   return new JSONMessage(false, "宝宝已被关注");
            	   }else{
            		 //未关注，插入关联关系，并提示关注宝宝成功
            		 //根据宝宝姓名，姓名，手机号到BS_CHILD_BASEINFO查询宝宝信息
            		   HashMap<String, String> childinfo = appChildService.getChildBaseInfo(birth, name,mobile);
            		 //需要用户的手机号或者ID，用于存储
            		   if(childinfo==null){
            			   return new JSONMessage(false, "宝宝档案已被删除");
            		   }

            		   String CHILDCODE = childinfo.get("CHILDCODE"); 
        			   String CHILDNAME = name; 
        			   String CARDCODE = childinfo.get("CARDCODE")==null?"":childinfo.get("CARDCODE"); 
        			   String BIRTHCODE = childinfo.get("BIRTHCODE")==null?"":childinfo.get("BIRTHCODE"); 
        			   String BIRTHDAY = birth; 
        			   String GUARDIANNAME = childinfo.get("GUARDIANNAME")==null?"":childinfo.get("GUARDIANNAME"); 
        			   String ID = UUID.randomUUID().toString();//随机生成
        			   Date createtime = new Date();
        			   
//                       appChildService.save(appUser);//应该用此方式，不写ID
                       appChildService.insertChildInfo(phone_value,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);//插入app用户表
        			   
            		   return new JSONMessage(true, "宝宝关注成功");
            	   }
            	   
            	   
               }else{
            	   return new JSONMessage( false, "验证码输入错误");
               }
           } catch (Exception e) {
               return new JSONMessage(false, "关注宝宝异常");
           }
       }
       
   }

   
   // 妈咪课堂开始

	/**
	* 妈咪课堂-预防接种 cms_article 表category_ID=1
	* edit by wangnan 2018-2-8
	*/
	@RequestMapping(value = {"/vaccInocList"})
	public @ResponseBody JSONMessage vaccInocList() {
		
		List<HashMap<String, String>> List = articleService.findByCategoryID("1");
		if(List.size()>0){
			  /*List<PoDatamodel> listPodata =new ArrayList<PoDatamodel>();
	            for(HashMap<String, String> map : List){
	                PoDatamodel po=new PoDatamodel();
	                String id = map.get("ID");
	                String title = map.get("TITLE");
	                String image = map.get("IMAGE")==null?"":map.get("IMAGE");
	                po.setId(id);
	                po.setTitle(title);
	                po.setImage(image);
	                listPodata.add(po);
	            }*/
			
			 return new JSONMessage(true, "接口调用成功",List);
		}
		return new JSONMessage(false, "没有记录！");
	}

	/**
	* //妈咪课堂-预防接种详情 edit by wn 2018-2-9
	* edit by wangnan 2018-2-8
	* cms_article_data
	*/
	@RequestMapping(value = {"/vaccInocDetail"})
	public @ResponseBody JSONMessage vaccInocDetail(String ID) {
		if(!StringUtils.isNotBlank(ID)){
			return new JSONMessage(false, "参数不能为空！");
		}
		String List = articleService.getInocDetailByID(ID);
		return new JSONMessage(true, "接口调用成功！",List);
	}	
	
	/**
	* 妈咪课堂-行业资讯 cms_article 表category_ID=2，image可能有多个 
	* edit by wangnan 2018-2-8
	*/
	@RequestMapping(value = "/vaccInfoList")
	public @ResponseBody JSONMessage vaccInfoList() {
		
		List<HashMap<String, String>> List = articleService.findByCategoryID("2");
		List<HashMap<String, Object>> ll = new ArrayList<HashMap<String,Object>>();
		if(List.size()>0){
			 
			  for (HashMap<String, String> map : List) {  
				  String id = map.get("ID");
				  String title = map.get("TITLE");
//				  String imagelist = map.get("IMAGE")==null?"":map.get("IMAGE");//分割image
				  String imagelist = map.get("IMAGE");//分割image
				  HashMap<String, Object> m = new HashMap<String, Object>();
				  if(imagelist!=null){
					  String[] ilist = imagelist.split(",");
					  m.put("IMAGE",ilist);
					  m.put("COUNT",ilist.length);
				  }else{
					  m.put("IMAGE",null);
					  m.put("COUNT",0);
				  }				  				  
				 
				  m.put("ID",id);
				  m.put("TITLE",title);
				  
				  ll.add(m);
			  }
			
			 return new JSONMessage(true,"接口调用成功", ll);
		}
		return new JSONMessage(false, "没有记录！");
	}

	/**
	* 妈咪课堂-疫苗知识 vac_knowledge_list 表
	* edit by wangnan 2018-2-9
	*/
	@RequestMapping(value = "/vaccKnowList")
	public @ResponseBody JSONMessage vaccKnowList() {
		
		List<HashMap<String, String>> List = vacknowledgelistService.findKnowList();
		if(List.size()>0){			
			 return new JSONMessage(true,"接口调用成功", List);
		}
		return new JSONMessage(false, "没有记录！");
	}

	/**
	* 妈咪课堂-疫苗知识 详情vac_knowledge_info 表
	* edit by wangnan 2018-2-24
	*/
	@RequestMapping(value = {"/vaccKnowDetail"})
	public @ResponseBody JSONMessage vaccKnowDetail(String ID) {
		if(!StringUtils.isNotBlank(ID)){
			return new JSONMessage(false, "参数不能为空！");
		}
		//ID必须为数字
		Pattern pattern = Pattern.compile("[0-9]*");
	    Matcher isNum = pattern.matcher(ID);
	    if( !isNum.matches() ){
	    	return new JSONMessage(false, "参数错误！");
	    }
	    
	    //查询疫苗知识详情
		String List = vacknowledgelistService.getKnowDetail(ID);
		return new JSONMessage(true, "接口调用成功！",List);
		
	}	
	
	/**
	* 妈咪课堂-疾病知识 vac_knowledge_sickness 表
	* edit by wangnan 2018-2-9
	*/
	@RequestMapping(value = "/sicknessList")
	public @ResponseBody JSONMessage sicknessList() {
		
		List<HashMap<String, String>> List = vacknowledgeSicknessService.findSickList();
		if(List.size()>0){			
			 return new JSONMessage(true,"接口调用成功" ,List);
		}
		return new JSONMessage(false, "没有记录！");
	}
	
	/**
	* 妈咪课堂-疾病知识详情 vac_knowledge_sickness 表
	* edit by wangnan 2018-2-24
	* 
	*/
	@RequestMapping(value = {"/sicknessDetail"})
	public @ResponseBody JSONMessage sicknessDetail(String ID) {
		if(!StringUtils.isNotBlank(ID)){
			return new JSONMessage(false, "参数不能为空！");
		}
		
		//ID必须为数字
		Pattern pattern = Pattern.compile("[0-9]*");
	    Matcher isNum = pattern.matcher(ID);
	    if( !isNum.matches() ){
	    	return new JSONMessage(false, "参数错误！");
	    }
			    
		//查询疾病知识详情    
		String List = vacknowledgeSicknessService.getSickDetail(ID);
		return new JSONMessage(true, "接口调用成功！",List);
		
	}	
	
	
	 /**
     *建卡列表，建档记录tempRecord
     *edit by wn
     *2018-2-8
     */
    @RequestMapping(value="/tempRecord", method=RequestMethod.POST)
    public @ResponseBody JSONMessage tempRecord(String userId){
        if(StringUtils.isNotBlank(userId)){
            List<HashMap<String,String>> list = vacchildTempService.getRecordByUserID(userId); //查询建卡表VAC_CHILD_TEMP
            
            if(list.size()==0){
                return new JSONMessage(true, "没有建档信息");
            }
            return new JSONMessage(true, "调用接口成功",list);
        }else {
            return new JSONMessage(false, "参数错误");
        }
    }
    
    /**
     *建卡记录详情
     *edit by wangnan
     *2018-2-8
     */
    @RequestMapping(value="/tempRecordDetail", method=RequestMethod.POST)
    public @ResponseBody JSONMessage tempRecordDetail(String childId){
        if(StringUtils.isNotBlank(childId)){
        	 return new JSONMessage(false, "参数错误");
        }
        
        HashMap<String,String> list = vacchildTempService.getRecordByID(childId); //查询建卡表VAC_CHILD_TEMP
        
        if(list==null){
            return new JSONMessage(true, "档案信息不存在");
        }
        
        return new JSONMessage(true, "调用接口成功",list);
     
    }
    
    /**
     *建卡记录详情+儿童档案详情，type区分
     *type=1儿童档案详情
     *type=3建卡记录详情
     *edit by wangnan
     *2018-2-24
     */
    @RequestMapping(value="/tempDetail", method=RequestMethod.POST)
    public @ResponseBody JSONMessage tempDetail(String childId,String type){
        if(!StringUtils.isNoneBlank(childId,type)){
        	return new JSONMessage(false, "参数不能为空");
        }
        
        HashMap<String,String> list = new HashMap<String,String>();
        
        if("1".equals(type)){
        	
        	//儿童档案详情
        	list = appChildService.getChildInfoByID(childId); //查询建档表VAC_CHILD_TEMP，childid对应首页表中的
        	
        }else if("3".equals(type)){
        	//建卡信息
        	list = vacchildTempService.getRecordByID(childId); //查询建卡表VAC_CHILD_TEMP
            
        }else{
        	return new JSONMessage(false, "参数错误");
        }
        
        if(list==null){
            return new JSONMessage(true, "档案信息不存在");
        }
        
        return new JSONMessage(true, "调用接口成功",list);
     
    }
    
    /**
     *建卡记录详情+儿童档案详情，type区分
     *type=1儿童档案详情
     *type=3建卡记录详情
     *childId表变childcode
     *edit by wangnan
     *2018-2-24
     */
    @RequestMapping(value="/tempDetails", method=RequestMethod.POST)
    public @ResponseBody JSONMessage tempDetails(String childcode,String type){
        if(!StringUtils.isNoneBlank(childcode,type)){
        	return new JSONMessage(false, "参数不能为空");
        }
        
        HashMap<String,String> list = new HashMap<String,String>();
        
        if("1".equals(type)){
        	
        	//儿童档案详情，传儿童编号
        	list = appChildService.getChildInfoByCode(childcode);  //查询建档表VAC_CHILD_TEMP，childid对应首页表中的
        	
        }else if("3".equals(type)){
        	//建卡信息，传childId不变
//        	list = vacchildTempService.getRecordByCode(childcode); //查询建卡表VAC_CHILD_TEMP
        	list = vacchildTempService.getRecordByID(childcode); //查询建卡表VAC_CHILD_TEMP
        }else{
        	return new JSONMessage(false, "参数错误");
        }
        
        if(list==null){
            return new JSONMessage(true, "档案信息不存在");
        }
        
        return new JSONMessage(true, "调用接口成功",list);
     
    }
    
    
    /**
     *预约记录
     *edit by wn
     *2018-2-23
     */
    @RequestMapping(value="/remindRecord", method=RequestMethod.POST)
    public @ResponseBody JSONMessage remindRecord(String childId){
        if(StringUtils.isNotBlank(childId)){
        	//查找儿童姓名+生日
        	HashMap<String, Object> info = appChildService.getChildDataByID(childId);
        	if(info==null){
                return new JSONMessage(false, "儿童信息错误");
            }

        	
//        	List<List<HashMap<String,String>>> returnlist = new ArrayList<List<HashMap<String,String>>>();
        	//查找预约记录
        	List<HashMap<String,String>> list = vacchildTempService.getRemidRecordByID(childId); //查询建卡表VAC_CHILD_TEMP

        	info.put("LIST", list);

            return new JSONMessage(true, "调用接口成功",info);
        }else {
            return new JSONMessage(false, "参数不能为空");
        }
    }
    
    /**
     *预约记录
     *edit by wn
     *2018-3-2
     */
    @RequestMapping(value="/remindRecords", method=RequestMethod.POST)
    public @ResponseBody JSONMessage remindRecords(String childcode){
        if(StringUtils.isNotBlank(childcode)){
        	//查找儿童姓名+生日，由于系统问题导致可能不止一个儿童
        	List<HashMap<String, Object>> infolist = appChildService.getChildDataByCode(childcode);
        	if(infolist.size()==0){
                return new JSONMessage(false, "儿童信息不存在");
            }
        	if(infolist.size()>1){
                return new JSONMessage(false, "儿童编号重复");
            }
        	HashMap<String, Object> info = infolist.get(0);
        	//查找预约记录
        	List<HashMap<String,String>> list = vacchildTempService.getRemidRecordByCode(childcode); //查询建卡表VAC_CHILD_TEMP
        	info.put("LIST", list);

            return new JSONMessage(true, "调用接口成功",info);
            
        }else {
            return new JSONMessage(false, "参数不能为空");
        }
    }
    
    
    /**
     *医院列表
     *edit by wangnan
     *2018-2-23
     */
    @RequestMapping(value="/hostipallist")
    public @ResponseBody JSONMessage hostipallist (String code){
    	try {
    		if(StringUtils.isNotBlank(code)){
    			String code1 = code.substring(0, 4);
		    	List<HashMap<String,String>> list=  vacchildTempService.getHostipallist(code1);
				
		        if(list.size()==0){
		        	return  new JSONMessage(true, "无医院信息");
		        }
		        return  new JSONMessage(true, "调用接口成功",list);
		        
		     }else{
		    	 return  new JSONMessage(false, "参数不能为空");
		     }
    		
    	} catch (Exception e) {
			return new JSONMessage(false, "调用接口失败",e);
		}
    }
    
    //点击签字
    /*
     * edit by wangnan
     * 2018-2-23
     * 返回告知书内容
     */
    @RequestMapping(value = "/getdisclosure")
    public  @ResponseBody JSONMessage getdisclosure(String id,String vaccid) {
    	if(!StringUtils.isNoneBlank(id,vaccid)){
    		 logger.error("参数不能为空");
             return new JSONMessage(false, "参数错误");
         }else{
    	
	    	//查询单条提示信息
	    	Integer count = vacchildTempService.findChildRemind(id);//查询记录是否存在，应该从预约表中查询
	        String disContext = "";
	        if(count>0){
	            //查询告知书
	            CmsDisclosure bsp = vacService.getCmsDisclosureByVacid(vaccid.substring(0,2)); //vr.getVaccId().substring(0,2) vr.getCode()
	            if(bsp != null && StringUtils.isNotEmpty(bsp.getContext())){
	                disContext = bsp.getContext();
	                //.replaceAll("\r\n", "<br>&emsp;&emsp;");
	                //disContext=bsp.getContext().replaceAll("<([^>]*)>", "").replaceAll("#([^#]*)#", "");
	            }
	
	            Map<String,Object> map=new HashMap<String, Object>();
	            map.put("ID", id);
	            map.put("disContext", disContext);
	            return new JSONMessage(true, map);
	        }else {
	            return new JSONMessage(false, "没有此条预约记录");
	        }
        }
    }
    

    //保存签字/签名
    /*
     * edit by wangnan
     * 2018-2-24
     */
    @RequestMapping(value = "/savesign")
    public @ResponseBody JSONMessage savesign( String id, String vaccid,String signatureDataTo) {
    	if(!StringUtils.isNoneBlank(id,vaccid,signatureDataTo)){
    		logger.error("参数不能为空");
            return new JSONMessage(false, "参数不能为空");
        }else{
//            Integer count = vacchildTempService.findChildRemind(id);//查询记录是否存在，应该从预约表中查询
            VacChildRemind temp = new VacChildRemind();
            temp.setId(id);
            //提示签字预约信息
            VacChildRemind vr = vacchildInfoService.findVacChildRemind(temp);
            if(vr!=null){
//            	System.out.println(vr.getSign());
            	if("1".equals(vr.getSign())){
            		 return new JSONMessage(false, "此预约记录已经签字，请勿重复签字");
            	}
	        	try {
                    //转换签字内容
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] sign = decoder.decodeBuffer(signatureDataTo);
                    //签字状态
//                    VacChildRemind vr = new VacChildRemind();
                    if(null != sign && sign.length > 0){
                        vr.setSignature(sign);
                        vr.setStype(VacChildRemind.TEMP_CHILD_STYPE);
                        vr.setVid(id);
                        vr.setSign(VacChildRemind.TEMP_CHILD_SIGN);
                        vr.setVaccId(vaccid);
                        //新增签字
                        vacchildInfoService.insertSignature(vr);
                        //更改记录签字状态
                        vacchildInfoService.updateSign(vr);
                        return new JSONMessage(true, "签字成功");
                    }else{
                        return new JSONMessage(false, "签字内容为空");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new JSONMessage(false, "签字存储异常");
                }
            }else{
	        	 return new JSONMessage(false, "没有此条预约记录，签字提交失败");
	        }          
           
        }
      
    }

    
    //查看签字/签名/告知书
    /*
     * edit by wangnan
     * 2018-2-24
     */
    @RequestMapping(value = "/viewsign")
    public  @ResponseBody JSONMessage viewsign(String id, String vaccid) {
        //查询单条提示信息
    	if(!StringUtils.isNoneBlank(id,vaccid)){
    		logger.error("参数不能为空");
            return new JSONMessage(false, "参数不能为空");
        } 
    	
        VacChildRemind temp=new VacChildRemind();
        temp.setId(id);
        VacChildRemind vr = vacchildInfoService.findVacChildRemind(temp);//查看记录是否存在
        String disContext = "";
        if(vr != null){
        	//判断是否签名
        	if("0".equals(vr.getSign())){
       		 	return new JSONMessage(false, "此预约记录未签字");//未签名
        	}
            CmsDisclosure bsp =new CmsDisclosure();
            //取得告知书
            if(vaccid.length()==4){//小类
                bsp= vacService.getCmsDisclosureByVacid(vaccid.substring(0,2));
            }else if(vaccid.length()==2){//大类
                String viccid1= vacService.getByModelId(vaccid);
                bsp= vacService.getCmsDisclosureByVacid(viccid1.substring(0,2));//取得告知书内容
            }else {
                logger.error("预约记录错误！");
                return new JSONMessage(false, "预约记录错误！");//未签名
            }
            //查询告知书
            //CmsDisclosure bsp = vacService.getCmsDisclosureByVacid(temp.getVaccId().substring(0,2)); //vr.getVaccId().substring(0,2) vr.getCode()
            if(bsp != null && StringUtils.isNotEmpty(bsp.getContext())){
                disContext = bsp.getContext();
            }
//            vr.setVaccId(vaccid);
            Map<String,Object> map=new HashMap<String, Object>();
//            map.put("VacChildRemind", vr);
            map.put("ID", id);
            map.put("disContext", disContext);
            //VacChildRemind temp = childInfoService.findVacChildRemind(tm);
     
            try {
                if(null != vr && StringUtils.isNotEmpty(id)){
//                    vr.setVaccId(vaccid);
                    VacChildRemind vacRemind = vacchildInfoService.findVacChildRemindById(vr);
                    if(vacRemind!=null){
                        byte[] stgn = vacRemind.getSignature();
                        map.put("stgn", stgn);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return new JSONMessage(true, "调用接口成功",map);
        }else {//null值
            return new JSONMessage(false, "没有此条预约记录，查看签名失败");
        }
     
    }
    
    /**
     *预约具体日期时间接种单位
     *edit by wn
     *2018-2-24
     *修改预约
     * 
     */
    @RequestMapping(value="/remindTime")
//    public @ResponseBody JSONMessage remindTime(@RequestBody Map<String,String> args) throws ParseException{
//        String recordid = args.get("recordid");
    public @ResponseBody JSONMessage remindTime(String id){
        if(!StringUtils.isNotBlank(id)){
        	return new JSONMessage(false, "参数不能为空");
        }
    	//通过记录id查询儿童预约信息，vac_child_baseinfo表中的id
        HashMap<String, String> map = vacchildInfoService.findRemindInfoById(id);//此儿童的预约信息
        if(map==null){
        	return new JSONMessage(false, "没有此条预约记录");
        }
        String LOCALCODE = map.get("LOCALCODE");//接种单位编码
        String LOCALNAME = map.get("LOCALNAME");//接种单位名称
//        String SELECT_DATE = map.get("SELECT_DATE");//预约日期
        String SELECT_DATE = map.get("REMIND_DATE");//预约日期,医生预约
//        String SELECT_TIME = map.get("SELECT_TIME");//预约时间段
        if(!StringUtils.isNotBlank(SELECT_DATE)){
        	return new JSONMessage(false, "预约记录错误");
        }
        List<String> selectDate =new ArrayList();
        selectDate.add(SELECT_DATE);
        
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date date;
        HashMap<String,Object> mapp=new HashMap<>();
		try {
			date = sdf.parse(SELECT_DATE);
			//String 转date
	        
	        //计算可以预约的日期（最晚打针日期要计入），暂定5天内
	        //过了预约日期，是否允许修改预约
	        for(int i=0;i<5;i++){
	            SimpleModel sim=new SimpleModel();
	            Date next=sysholidayService.nextWorkDay(date,LOCALCODE);
	            selectDate.add(sdf.format(next));
	            date=next;
	        }
//	        String sdate =sdf.format(date);//date转String
	        
	               
	        mapp.put("LOCALCODE",LOCALCODE);//接种单位编号
	        mapp.put("LOCALNAME",LOCALNAME);//接种单位名称
	        mapp.put("SELECT_DATE", SELECT_DATE);//已选日期
	        mapp.put("V_DATE",selectDate);//可选日期
	       
	       
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new JSONMessage(false, "预约记录错误");
		}
		 
		return new JSONMessage(true, "调用接口成功",mapp);
    }
    
    /**
     * 查询预约可选时间段及剩余名额
     * edit by cdl
     * 2018-2-26
     * 参数：localcode接种单位编码
     * selectDate选择的日期
     * id预约记录id
     */
    @RequestMapping(value = "/remindTable" )
    public @ResponseBody JSONMessage remindTable(String localcode,String selectDate,String id ) { //预约日期 //接种单位 //id
        String week= DateTools.dateToWeek(selectDate);
        WorkingHours work=new WorkingHours();
        work.setLocalcode(localcode);
        work.setWeek(week);
        List<WorkingHours> workList=workService.findByLocalcode(work);
        //预约时间为周末，接种单位编码不存在时间段表中，查询记录为空
        if(workList.size()==0){
        	return  new JSONMessage(false, "预约记录错误");
        }
        List<SimpleModel> listSimp=new ArrayList<>();
        VacChildRemind  temp =new VacChildRemind();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(selectDate, pos);
        temp.setSelectDate(currentTime_2);
        temp.setLocalcode(localcode);
        VacChildRemind temp1 =new VacChildRemind();
        temp1.setId(id);
        temp1.setSelectDate(currentTime_2);
       List<VacChildRemind>  vr = vacchildInfoService.findRemindById(temp1);//此id的预约信息
        List<VacChildRemind>  vr1=  vacchildInfoService.findRemindById(temp);//接种单位、日期
        Map<Object, List<VacChildRemind>>  remindMap= FuxinUtil.getTreeDateByParam(VacChildRemind.class,vr1,"selectTime");
        //String selectTime=vr.getSelectTime();
        for(WorkingHours sh : workList){
            SimpleModel simpleModel=new SimpleModel();
            List<VacChildRemind> li = remindMap.get(sh.getTimeSlice());
            simpleModel.setStr1(sh.getTimeSlice());
            simpleModel.setInt1(sh.getMaximum());
            int num=0;
            if(li != null){
                 num=li.size();
            }
            int int2=sh.getMaximum()-num;
            if(int2<0){
                int2=0;
            }
            simpleModel.setInt2(int2);
            simpleModel.setInt1(sh.getMaximum());
            listSimp.add(simpleModel);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("listSimp",listSimp);
        if(vr.size()==0){
            map.put("selectTime","");
        }else {
            map.put("selectTime",vr.get(0).getSelectTime());
        }
        if(listSimp.size()>0){
            return  new JSONMessage(true, map);
        }else {
            return  new JSONMessage(false, "查询错误");
        }
    }

    /**
     * 确定预约按钮
     * * edit by cdl
     * 2018-2-26
     * 参数： 
     * selectDate选择的日期
     * selectTime选择的时间
     * id预约记录id
     */
    @RequestMapping(value = "/updateRemindTime" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage updateRemindTime( String selectTime ,String selectDate,String id ) {
        VacChildRemind temp =new VacChildRemind();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(selectDate, pos);
       temp.setSelectDate(currentTime_2);
       temp.setSelectTime(selectTime);
       temp.setId(id);
        int i= vacchildInfoService.updateByTime(temp);
        if(i==1){
            return  new JSONMessage(true, "修改预约成功");
        }else {
            return  new JSONMessage(false, "预约的错误");
        }
    }
    
    
    //点击立即支付按钮需要的数据 //儿童编号   id
    /**
     * 获取订单信息-查看订单/立即支付
     * * edit by cdl
     * 2018-2-26
     * 参数： 
     * id预约记录id
     */
    @RequestMapping(value = "/beforePay" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage beforePay( String id ) {
        VacChildRemind temp =new VacChildRemind();
        temp.setId(id);
        //temp.setChildcode(childcode);
        VacChildRemind vr = vacchildInfoService.findRemindById(temp).get(0); //此儿童的预约信息
        BsChildBaseInfo bsChild = vacService.getBsChildBaseInfoByCode(vr.getChildcode()); //通过儿童编号查询儿童信息
        Map<String,Object> map=new HashMap<>();
//        map.put("childRemind",vr);
//        map.put("childBase",bsChild);
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        map.put("codeNum",vr.getChildcode()); //  儿童编号
        map.put("name",bsChild.getChildname()); //  儿童姓名
        map.put("sex",  (bsChild.getGender().equals("1"))?'男':'女'); //  性别
        map.put("birthdy", sdf.format(bsChild.getBirthday())); //  生日
        map.put("guardian",bsChild.getGuardianname()); //  监护人 姓名
        map.put("vaccName",vr.getRemindVacc()); //  疫苗名称
        map.put("vaccPrice",vr.getPayPrice()); //  疫苗价格
        map.put("insuranceName",vr.getInsuranceName()); //  保险名称
        map.put("insurancePrice",vr.getInsurancePrice()); //  保险价格
        map.put("insuranceId","1"); //  保险ID
        map.put("id",id);
        return  new JSONMessage(true, map);

    }

    /**
     * 支付疫苗
     * * edit by cdl
     * 2018-2-26
     * 参数： 
     * id预约记录id
     * totalPrice支付总价
     * insurancePrice保险价格
     * insuranceId保险id
     * type：1是支付宝 2是微信
     * userId用户id
     */
    @RequestMapping(value = "/payInfo" , method= RequestMethod.POST) //payType 1:支付宝 2:微信
    public @ResponseBody JSONMessage payInfo( String id ,String totalPrice ,String insurancePrice ,String insuranceId, String type ,String userId, HttpServletRequest request ) {
        totalPrice="0.01";
        VacChildRemind temp =new VacChildRemind();
        temp.setId(id);
        VacChildRemind vr = vacchildInfoService.findRemindById(temp).get(0); //此儿童的预约信息
        BsChildBaseInfo bsChild = vacService.getBsChildBaseInfoByCode(vr.getChildcode()); //通过儿童编号查询儿童信息
        Order order=new Order();
        order.setVaccId(vr.getVaccId());
        order.setLocalcode(vr.getLocalcode());
        order.setChildCode(bsChild.getChildcode());
        String orderNo=OrderUtil.getOrderId();
		order.setOrderNo(orderNo);
		order.setSource("1");
		order.setPayType(type);
		order.setPayPrice(totalPrice);
		order.setStatus("1");
		order.setOrderTime(new Date());
		order.setUserId(userId);
		order.setNid(vr.getNid());
		order.setVaccName(vr.getRemindVacc());
		order.setVaccPrice(vr.getPayPrice());
		order.setVid(id);
		int i=orderService.insertVacOrder(order);
       if("0".equals(type)) {
           String str = Alipay.AlipayClient(vr.getRemindVacc(),orderNo,totalPrice,conf.getAliPayUrl());
           Map map = new HashMap();
           map.put("Alipay",str);
           return  new JSONMessage(true, map);
       }else if("1".equals(type)){
		   // tradeType  JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付
           JSONObject wenxin = weixinPrePay.createSignAgain(vr.getRemindVacc(),orderNo,totalPrice,conf.getWeixinPayUrl(),"APP",request);
           return  new JSONMessage(true, wenxin);
       }else {
           return  new JSONMessage(true, "支付类型错误！");
       }
    }


	/**
     *自助建档保存数据
     * * edit by cdl
     * 2018-2-26
     * 参数： 
     * code：用户填写信息json串
     */
    @RequestMapping(value="/tempChildSave", method= RequestMethod.POST)
    public @ResponseBody JSONMessage tempChildSave(String code,HttpServletRequest request){

       String registrationID = null;//极光推送标识
        String type_ = null;//区分IOS/ANDROID

        //获取headers文件
        Enumeration<String> headerNames=request.getHeaderNames();
        for(Enumeration<String> e=headerNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=request.getHeader(thisName);
            System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
            if("registrationid".equals(thisName)){
                registrationID = thisValue;
            }
            if("apptype".equals(thisName)){
                type_ = thisValue;
            }
        }
        if (!StringUtils.isNoneBlank(registrationID,type_)) {
            logger.info("headers参数传输为空");
//            return new JSONMessage(false, "头文件参数错误");
        }

        code = StringEscapeUtils.unescapeHtml(code); //
        try {
            ChildTemp childTemp = (ChildTemp) JsonMapper.fromJsonString(code, ChildTemp.class);
            childTemp.setFileorigin("3");
            childTemp.setRegistrationID(registrationID);
            childTemp= vacchildTempService.childSava(childTemp);//建档编号+childcode+省市县等
            childTemp = vacService.updateChildTemp(childTemp); //更新详细页面区域划分、户口类型、居住类型、出生医院信息
            List<String> list=new ArrayList<>();
            list.add(registrationID);
            jpushMessageService.sendToRegistrationId(list,"自助建档成功","编码【"+childTemp.getChildcode()+"】有效期三天,请及时前往接种单位完成建档",childTemp.getId(),"06");
            Map map =new HashMap();
            /*map.put("childcode",childTemp.getChildcode());*/
            map.put("id",childTemp.getId());
            return  new JSONMessage(true, map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    /**
     *我的个人信息
     * * edit by wangnan
     * 2018-2-26
     * 参数： 
     * userId：用户id
     */
    @RequestMapping("/myInfo")
    public @ResponseBody JSONMessage myInfo(String userId){
    	if(!StringUtils.isNoneBlank(userId)){
    		logger.error("参数不能为空");
            return new JSONMessage(false, "参数不能为空");
        } 
        HashMap<String,String> info = appChildService.getmyInfo(userId);//获取用户信息
        if(info==null){
        	return new JSONMessage(false, "没有此用户");
        }
        
        return new JSONMessage(true, "接口调用成功",info);
        //判断头像和昵称为空的情况
        /*String path = "http://www.chinavacc.cn/avatar/2018_0207.png"; 
        
        String PHONE = info.get("PHONE");
        String IMAGEPATH =  info.get("IMAGEPATH")==null?path: info.get("IMAGEPATH");
        String NICKNAME =  info.get("NICKNAME")==null?PHONE: info.get("NICKNAME");

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("PHONE", PHONE);
        map.put("IMAGEPATH", IMAGEPATH);
        map.put("NICKNAME", NICKNAME);
        return new JSONMessage(true, "接口调用成功",map);*/
    }
   

    /**
     *保存用户头像
     * * edit by wangnan
     * 2018-2-26
     * 参数： 
     * userId：用户id
     * headpath：图像Base64格式
     */
    @RequestMapping(value="/imageSave", method= RequestMethod.POST) //imageSave
    public @ResponseBody JSONMessage imageSave(String userId,String headpath){
    	if(!StringUtils.isNoneBlank(userId,headpath)){
    		logger.error("参数不能为空");
            return new JSONMessage(false, "参数不能为空");
        } 
    	String mc= ImageUtil.GenerateImage(headpath,conf.getImgPath(),"png");//生成图片名称
        String url=conf.getImgurl()+mc+".png";//生成图片地址+名称
        
        AppUser appUser=new AppUser();
        appUser.setId(userId);
        appUser.setHeadurl(url);
        appChildService.updateMyInfoByID(appUser);//修改app用户表
//        appChildService.updateMyInfoByID(userId,headpath);//修改app用户表
        return new JSONMessage(true, "保存图片成功！");
    }
    
    //
    /**
     *保存用户昵称
     * * edit by wangnan
     * 2018-2-26
     * 参数： 
     * userId：用户id
     * nickname：昵称
     */
    @RequestMapping("/nicknameSave") //imageSave  Message
    public @ResponseBody JSONMessage nicknameSave(String userId,String nickName){
    	
    	if(!StringUtils.isNoneBlank(userId,nickName)){
    		logger.error("参数不能为空");
            return new JSONMessage(false, "参数不能为空");
        } 
    	
        AppUser appUser=new AppUser();
        appUser.setId(userId);
        appUser.setNickname(nickName);
        appChildService.updateMyInfoByID(appUser);//修改app用户表
        return new JSONMessage(true, "保存昵称成功！");
    }

    /*
     * 修改密码
     * * edit by wangnan
     * 2018-2-27
     * 参数： 
     * userId：用户id
     * oldPassword：旧密码
     * newPassword：新密码
     * 
     */
     @RequestMapping("/changePwd") //changePwd
     public @ResponseBody JSONMessage changePwd(String userId, String oldPwd, String newPwd){
         if (!StringUtils.isNoneBlank(userId,oldPwd, newPwd) ) {
        	 logger.error("参数不能为空");
             return new JSONMessage(false, "参数不能为空"); 
         }

         //查询id对应的密码
         String pwd  = appChildService.findPasswordByID(userId);
//         System.out.println("haha"+pwd+"123");
         if (!StringUtils.isNoneBlank(pwd)) {
             return new JSONMessage(false, "没有此用户");
         } 
  
         if (!oldPwd.equals(pwd)) {
             return new JSONMessage(false, "原密码错误");
         } else {
             AppUser user=new AppUser();
             user.setId(userId);
             user.setUserpwd(newPwd);
             appChildService.updateMyInfoByID(user);//修改app用户表
         }

         return new JSONMessage(true, "密码修改成功");
	  
     }

    
     /*
      * 接种单位信息
      * * edit by wangnan
      * 2018-2-27
      * 参数： 
      * childId：儿童记录id
      * 
      */
     @RequestMapping(value = "/departmentInfo")
     public @ResponseBody JSONMessage departmentInfo( String childId) {
         
         HashMap<String,String> map= departmentInfoService.findByChildId(childId);
         if(map==null){
        	 return new JSONMessage(false, "无接种单位");
         }
         return new JSONMessage(true, map);
     }
     

      /**
      * 查询接种记录
      * * edit by wangnan
      * 2018-2-27
      * 参数： 
      *  type=1 已种疫苗
      *  type=0 未种疫苗
      *  code 儿童编号
      * 
      */
     @RequestMapping(value ="/vaccRecord")
     public @ResponseBody JSONMessage vaccRecord( String type,String childcode){
         if(!StringUtils.isNoneBlank(type,childcode)){
             return new JSONMessage(false, "参数不能为空");
         }
         List<List<HashMap<String,String>>> returnlist1 = new ArrayList<List<HashMap<String,String>>>();
        
         List<List<?>> returnlist = new ArrayList<>();
         //已接种记录
         if(BsRecord.STATUS_FINISH.equals(type)){
        	 /*List<HashMap<String,Object>> returnlist = new ArrayList<HashMap<String,Object>>();
        	 //查询记录
        	 List<HashMap<String,String>> list = vacService.getvaccRecordFinish(childcode);
        	 if(list.size()==0){
        		 return new JSONMessage(false, "无接种记录");
        	 }
        	 return new JSONMessage(true, "接口调用成功",returnlist);*/
//        	 List<BsRecord> listTemp = vacService.getRecordFinish(childcode);
        	  
             //数组转树形
             String op = "first";
             //查询已接种记录
             List<HashMap<String,String>> list = vacService.getvaccRecordFinish(childcode);//得到已接种记录
             
             //转换格式
             List<HashMap<String,String>> templist1 = new ArrayList<HashMap<String,String>>();
             for(HashMap<String,String> map :list){
        		 String VACCNAME = map.get("VACCNAME");
        		 //第一个记录
        		 if(op.equals("first")){
                     op = VACCNAME;
                 }
        		 //后面的记录判断是否是一种疫苗
        		 if(!op.equals(VACCNAME)){
//                     templist.get(0).setLeng(templist.size()+1);
                     returnlist1.add(templist1);
                     templist1 = new ArrayList<HashMap<String,String>>();

                     op = VACCNAME;
                 } 
        		 
        		 templist1.add(map);
        		        		 
        	 }
              //最后一条记录          
             if(templist1.size() > 0){ 
                 returnlist1.add(templist1);
             }
             //数组转树形 结束
             return new JSONMessage(true, returnlist1);
         }
         
         
         //未完成列表
         if(BsRecord.STATUS_WAIT.equals(type)){ //未接种记录
             try {
                 
            	 returnlist = (List<List<?>>) vacService.getVaccList(childcode, BsVaccNum.TYPE_RECORD);
            
             } catch (Exception e) {

                 return new JSONMessage(false, e.getMessage());
             }
             
         } 
         return new JSONMessage(true, returnlist);   
          
     }
     
     //不良反应首页展示
     /**
      * 不良反应首页展示
      * * edit by wangnan
      * 2018-2-28
      * 参数： 
      *  childcode 儿童编号
      * 
      */
     @RequestMapping(value = "/AFEIshow")
     public @ResponseBody JSONMessage AFEIshow( String childcode) {
    	 if(!StringUtils.isNoneBlank(childcode)){
             return new JSONMessage(false, "参数不能为空");
         }
    	 //取得已接种疫苗
    	 List<HashMap<String,String>> list = vacService.getvaccRecordFinish(childcode);//得到已接种记录
    	 
    	 List<HashMap<String,String>> returnlist =  new ArrayList<HashMap<String,String>>();
    	 
    	 for(HashMap<String,String> map :list){
//    		 String NID =map.get("NID");
    		 HashMap<String,String> ss = new HashMap<String,String>();
    		 ss.put("childcode", childcode);
             ss.put("nid", map.get("NID"));           
    		 List<Response> listRes=  vacService.getByChildcode(ss);
             if (listRes.size()>0){
                 if(!"".equals(listRes.get(0).getReplycontent())){
                     map.put("STATUS", "2");
                 }else {
                	 map.put("STATUS", "1");
                 }
                 /*map.put("IMAGE",listRes.get(0).getSubmitmage());
                 map.put("TEXT", listRes.get(0).getSubmittext());*/
                 
             }else {
            	 map.put("STATUS", "0");
            	 /*map.put("IMAGE","");
                 map.put("TEXT", "");*/
             }
             
    		 /*String submit = vacService.getAFEI(childcode,NID);
    		 String status = "1";//默认回复
    		 if(!StringUtils.isNotBlank(submit)){
                  //为空，未回复
    			 status = "0";
   			
             }
    		 map.put("STATUS", status);*/
    		 returnlist.add(map);
         }
    	 
         return new JSONMessage(true, "接口调用成功",returnlist);
     }

   //不良反应保存接口
     /**
      * 不良反应保存接口
      * * edit by wangnan
      * 2018-2-28
      * 参数： 
      *  childcode 儿童编号
      *  nid 计划id
      *  id 疫苗记录id
      * submitMage	上传的图片
      * submitText	提交内容
      */
     @RequestMapping(value = "/AFEIsave")
     public @ResponseBody JSONMessage AFEIsave( String nid,String childcode ,String id ,String submitMage ,String submitText ) {
    	 if(!StringUtils.isNoneBlank(childcode,nid,id)){
             return new JSONMessage(false, "参数不能为空");
         }
    	 String mc= ImageUtil.GenerateImage(submitMage,conf.getImgPath(),"png"); //http://192.168.1.100:81/img/hyzx1.jpg
         String url=conf.getImgurl()+mc+".png";
         Response response=new Response();
         response.setChildcode(childcode);
         response.setNid(nid);
         response.setSubmittime(new Date());
         response.setSubmitmage(url);
         response.setSubmittext(submitText);
         response.setVaccinId(id);
         int i=  vacService.insertResponse(response);
         if(i==1){
             return new JSONMessage(true, "保存成功！");
         }else {
             return new JSONMessage(true, "保存失败！");
         }
     } 
     
   
     /**
      * 不良反应显示-详情接口
      * * edit by wangnan
      * 2018-2-28
      * 参数： 
      *  childcode 儿童编号
      *  nid 计划id
      */
     @RequestMapping(value = "/AFEIdetail")
     public @ResponseBody JSONMessage AFEIdetail( String nid,String childcode ) {
    	 if(!StringUtils.isNoneBlank(childcode,nid)){
             return new JSONMessage(false, "参数不能为空");
         }
        /* Map<String, String> map = new HashMap<>();
         map.put("childcode", childcode);
         map.put("nid", nid);
         List<Response> listRes=  vacService.getByChildcode(map);*/
         //查询回复列表
         List<HashMap<String,String>> list=  vacService.getResponseDetail(childcode,nid);
         if(list.size()>0){
             return new JSONMessage(true,"接口调用成功", list);
         }else {
             return new JSONMessage(false, "没有记录！");
         }
     }
     
     //服务通知列表
     /**
      * 消息中心-服务消息通知列表
      * * edit by cdl
      * 2018-3-1
      * 参数： 
      *  childcode 儿童编号
      */
     @RequestMapping(value = "/notificaList")
     public @ResponseBody JSONMessage notificaList( String childcode) {
         List<Notification>  list=  notificationService.findByChildcode(childcode);
         List<Notification> notiList=new ArrayList<Notification>();
         Notification notification=new Notification();
         for (int j=0;j<list.size();j++) {
             Map<String,String> map= new HashMap<>();
             notification=list.get(j);
             String str=  list.get(j).getNotifycontent();
             String[]  strs=str.split(",");
             for(int i=0;i<strs.length;i++){
                 map.put("keyword"+i,strs[i]);
             }
             notification.setContent(map);
             notiList.add(j,notification);
         }
         return new JSONMessage(true, notiList);
     }

   //推广消息
     /**
      * 消息中心-系统消息
      * * edit by wangnan
      * 2018-3-1
      */
     @RequestMapping(value = "/promotMessage")
     public @ResponseBody JSONMessage promotMessage( ) {
        
    	 //获取系统消息
    	List<HashMap<String, String>>  List=  notificationService.findMessage();

 		List<HashMap<String, Object>> ll = new ArrayList<HashMap<String,Object>>();
 		if(List.size()>0){
 		 
 			  for (HashMap<String, String> map : List) {  
 				  String id = map.get("ID");
 				  String title = map.get("TITLE");
 				  String link = map.get("LINK");
 				  String description = map.get("DESCRIPTION");
// 				  String imagelist = map.get("IMAGE")==null?"":map.get("IMAGE");//分割image
 				  String imagelist = map.get("IMAGE");//分割image
 				  HashMap<String, Object> m = new HashMap<String, Object>();
 				  if(imagelist!=null){
 					  String[] ilist = imagelist.split(",");
 					  m.put("IMAGE",ilist);
 					  m.put("COUNT",ilist.length);
 				  }else{
 					  m.put("IMAGE",null);
 					  m.put("COUNT",0);
 				  }				  				  
 				 
 				  m.put("ID",id);
 				  m.put("TITLE",title);
 				  m.put("LINK",link);
				  m.put("DESCRIPTION",description);
 				  
 				  ll.add(m);
 			  }
 			
 			 return new JSONMessage(true,"接口调用成功", ll);
 		}
 		return new JSONMessage(false, "没有记录！");
 		
     }
      
     
   //推广消息
     /**
      * 消息中心-系统消息-详情
      * * edit by wangnan
      * 2018-3-1
      */
     @RequestMapping(value = "/promotDetails")
     public @ResponseBody JSONMessage promotDetails(String id) {
        
    	 //获取系统消息详情
    	if(!StringUtils.isNotBlank(id)){
			return new JSONMessage(false, "参数不能为空！");
		}
		String content = notificationService.getMessDetail(id);
		return new JSONMessage(true, "接口调用成功！",content);
 		
     }
     
     
     //更新版本接口
     /**
      * 更新版本接口
      * * edit by wangnan
      * 2018-3-1
      */
     @RequestMapping(value = "/updateVersion") //
     public @ResponseBody JSONMessage updateVersion(String version){
         Map<String,String> map =  vacService.getAPPVersion();//获取最新的版本信息
         if(map==null){
        	 return  new JSONMessage(false, "系统版本错误，请联系管理员");
         }
         String appversion = map.get("VERSION");//获取最新版本

         if(appversion.equals(version)){
        	 return  new JSONMessage(true, "系统为最新版本");
         }
         
         return  new JSONMessage(true,"接口调用成功", map);
     }
     
     
     
     /**
  	 * 建档成功自动关注宝宝
  	 * 可去掉改成触发器方式
  	 * @author wangnan
  	 * @date 2018年3月27日 
  	 * @description 
  	 *		TODO
  	 * @return
  	 *
  	 */
  	@RequestMapping(value="/AttenBaby", method = RequestMethod.POST)
  	public @ResponseBody JSONMessage AttenBaby(@RequestBody(required = false) Map<String, String> baby){
// 	public @ResponseBody JSONMessage AttenBaby(String childCode,String userId,String fileorigin, @RequestBody(required = false) Map<String, String> baby){
  		if(null != baby && baby.size() > 0){
//  		if(1==1){//测试使用
  			try {
  				
  				String childCode = baby.get("childCode").toString();
 				String userId = baby.get("userId").toString();
 				String fileorigin = baby.get("fileorigin").toString();
 				//参数不能为空
 				if(!StringUtils.isNoneBlank(childCode,fileorigin)){
 		             return new JSONMessage(false, "参数不能为空");
 		        }
 				logger.info("建档成功自动关注宝宝接口入参儿童编号:" + childCode+"用户ID"+userId);
 				
 				//根据宝宝编号查询宝宝信息
 				BsChildBaseInfo bsInfo = vacService.getBsChildBaseInfoByCode(childCode);
 				VacChildInfo childinfo = VacChildInfo.parse(bsInfo);
 				if(childinfo==null){
 					//根据宝宝编号查询不到信息
 					return new JSONMessage(false, "宝宝建档信息尚未同步");
 				}
 				
 				//微信
 				if("2".equals(fileorigin)){
 					//验证是否已被关注
 					VacChildInfo tempinfo = new VacChildInfo();
 					tempinfo.setUserid(userId);
 					tempinfo.setChildcode(childinfo.getChildcode());
 					List<VacChildInfo> infos =vacchildInfoService.findList(tempinfo);
 					if(infos.size() > 0){
 						return new JSONMessage(false, "宝宝已被关注");
 					}else{
 						childinfo.setUserid(userId);
 						vacchildInfoService.save(childinfo);
 						return new JSONMessage(true, "宝宝关注成功！");
 					}
 				} 										
 				
 				//APP数据
 				if("3".equals(fileorigin)) {
 					//验证是否已被关注
 					String Guardianmobile = bsInfo.getGuardianmobile();//父亲手机号
 					//验证母亲手机号
 					if(!StringUtils.isNoneBlank(Guardianmobile)){
 						//母亲手机号不为空
 						return new JSONMessage(false, "建档记录有误，母亲号码未填");
 					}
 					
 					//查询是否已被关注
 					Integer mcount = appChildService.getIsAttenByPhone(Guardianmobile, childCode);
 	            	   
 					if(mcount>0){
             		 //已关注，提示宝宝已被关注
 						logger.error("宝宝已被关注-->"+bsInfo.getGuardianmobile());
 					}else{
             		
 						SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );						
 						String CHILDCODE = bsInfo.getChildcode(); 
 						String CHILDNAME = bsInfo.getChildname(); 
 						String CARDCODE = bsInfo.getCardcode(); 
 						String BIRTHCODE = bsInfo.getBirthcode(); 
 						String BIRTHDAY = sdf.format(bsInfo.getBirthday()); 
 						String GUARDIANNAME = bsInfo.getGuardianname(); 
 						String ID = UUID.randomUUID().toString();//随机生成
 						Date createtime = new Date();
 	        			//插入关联表
 	                    appChildService.insertChildInfo(Guardianmobile,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);//插入app用户表
 	                    
 						logger.error("关联成功-->"+Guardianmobile);
 						//return new JSONMessage(true, "关联成功");
 						}
 					
 					String Fatherphone = bsInfo.getFatherphone();
 					
 					//验证父亲手机号
 					if(StringUtils.isNoneBlank(Fatherphone)){
 						//父亲手机号不为空
 						//爸爸手机号和妈妈手机号不一致
 						if(!Fatherphone.equals(Guardianmobile))
 						{
							
							Integer fcount = appChildService.getIsAttenByPhone(Fatherphone, childCode);
			            	   
							if(fcount>0){
		            		 //已关注，提示宝宝已被关注
								logger.error("宝宝已被关注-->"+bsInfo.getGuardianmobile());
							}else{
								SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );						
								String CHILDCODE = bsInfo.getChildcode(); 
								String CHILDNAME = bsInfo.getChildname(); 
								String CARDCODE = bsInfo.getCardcode(); 
								String BIRTHCODE = bsInfo.getBirthcode(); 
								String BIRTHDAY = sdf.format(bsInfo.getBirthday()); 
								String GUARDIANNAME = bsInfo.getGuardianname(); 
								String ID = UUID.randomUUID().toString();//随机生成
								Date createtime = new Date();
			        			
			        			//插入关联表
			                    appChildService.insertChildInfo(Fatherphone,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);//插入app用户表
			                   
								logger.error("关联成功-->"+Fatherphone);
								//return new JSONMessage(true, "关联成功");
							}
 						}else{
 							logger.error("父母手机号一致-->"+childCode);
 						}
 					} 
 					
 					return new JSONMessage(true, "执行完成！");
 					
 				}
  				
  			} catch (Exception e) {
  				logger.error("关注宝宝接口错误",e);
  			}
  		}
  		//没有查询的结果
  		return new JSONMessage(false, "关注宝宝失败");
  	}
  	
  	/**
     * 查询接种记录+回复时间--用于不良反应使用
     * * edit by wangnan
     * 2018-2-28
     * 参数： 
     *  code 儿童编号
     * 
     */
   /* @RequestMapping(value ="/reactionRecord")
    public @ResponseBody JSONMessage reactionRecord( String childcode){
        if(!StringUtils.isNoneBlank(childcode)){
            return new JSONMessage(false, "参数不能为空");
        }
        
        //已接种记录
       	//查询记录
       	List<HashMap<String,String>> list = vacService.getreacRecord(childcode);
       	if(list.size()==0){
       		return new JSONMessage(false, "无接种记录");
       	}
       	return new JSONMessage(true, "接口调用成功",list);
       
       
    } 
     */
   /**
	登录接口 --统一，参数：type,phone，password，registrationID，mark，uid，openid
	type:区分APP、APP第三方、微信
	*/
	@RequestMapping("/checklogin")
	public @ResponseBody JSONMessage checklogin(String type,String phone, String password,String registrationID,String mark,String uid,String openID){
	 
	  //根据类型判断，type=1为APP，type=2为APP第三方，type=3 为微信
	  if (!StringUtils.isNotBlank(type)){
		   logger.info("登录类型参数为空");
		   return new JSONMessage(false, "参数错误");
	  }
	  if("1".equals(type)){
		//APP
//		   if (StringUtils.isNoneBlank(phone,password,registrationID)) {
		  if (StringUtils.isNoneBlank(phone,password)) {   
			   //通过手机号查询用户是否已经注册，通过获得用户密码
	          String pwd = appChildService.getPwdByPhone(phone);
	          //数据库中密码不存在，未在APP中注册
	          if ((pwd == null) || ("".equals(pwd.trim()))) {
	       	   
	       	   logger.info("用户未注册");
	              return new JSONMessage(false, "用户未注册");
	              
	          } else {
	       	  
	       		   //密码存在，判断密码是否正确
	   		   if (!password.equals(pwd)) {
	   			   
	           	   logger.info("密码错误");
	                  return new JSONMessage(false, "密码错误");
	              } else {
	           	   //密码正确，返回关注儿童信息
	           	   //类需要修改
	                  AppUser user=new AppUser();
	                  user.setPhone(phone);
	                  user.setRegistrationid(registrationID);
	                  //类需要修改，更新极光推送标识
	                  appChildService.updateByregisId_1(user);
	                  
	                  //返回是否关注宝宝
	                  Integer count = appChildService.getByPhone_1(phone);
	                  if (count < 1) {
	               	   
	                      return new JSONMessage(true, "没有关注宝宝");
	                  } else {
	               	   
	                      return new JSONMessage(true, "已经关注宝宝");
	                   }
	              }//else
	          }//else
		   }else{
			   //用户名或密码为空
			   logger.info("请输入用户名或密码");
			   return new JSONMessage(false, "参数传输错误，参数不能为空");
		   }
	  }
	  //APP第三方  ,不需要手机号
	  else  if("2".equals(type)){
		 
		   //phone，registrationID，mark，uid
		   //phone不传送
		   if (StringUtils.isNoneBlank(registrationID,mark,uid)) {
			 
			   //根据uid，获得mark,phone的值
			   HashMap<String, String> map = appChildService.getInfoByUID(uid);
			   //为空
			   if(map==null){
				   logger.info("用户未关联第三方登录");
	              return new JSONMessage(false, "用户未关联第三方登录");//跳转到绑定手机号界面
			   }
			   String phone_value = map.get("PHONE"); 
			   String mark_value = map.get("MARK"); 
		      // String uid_value = map.get("UNIONID");
		       
			   
	         // 对比是否一致，为空则未注册，不一致则已绑定其他第三方
	          //为空null
	          if ((phone_value == null) || ("".equals(phone_value.trim()))||(mark_value == null) || ("".equals(mark_value.trim()))) {
	       	   
	       	   logger.info("用户未关联第三方登录");
	              return new JSONMessage(false, "用户未关联第三方登录");
	              
	          }
	          
	          if (phone_value.length()!=0&&mark_value.length()!=0) {
	       	   //手机绑定其他微信/qq/微博
	       	   if (mark.equals(mark_value)) {       			   
	           	 
	       		   //账号密码一致，查询是否关注宝宝	        		   
	       		   AppUser user=new AppUser();
	                  user.setPhone(phone_value);
	                  user.setRegistrationid(registrationID);
	                  //类需要修改，更新极光推送标识
	                  appChildService.updateByregisId_1(user);
	                  
	                  //返回是否关注宝宝
	                  Integer count = appChildService.getByPhone_1(phone_value);
	                  if (count < 1) {
	               	   
	                      return new JSONMessage(true, "没有关注宝宝");
	                  } else {
	               	   
	                      return new JSONMessage(true, "已经关注宝宝");
	                   }
	       	   }else{
	       		   
	       		   return new JSONMessage(false, "用户手机号已关联其他第三方登录");//UID唯一，没可能
	       	   }
	              
	          }
	          
		   }else{
			   //用户名或密码为空
			   logger.info("参数错误");
			   return new JSONMessage(false, "参数传输错误，参数不能为空");
		   }
	  }
	  else if("3".equals(type)){
		//微信
		   if (StringUtils.isNoneBlank(openID)) {
			   
			   //通过手机号查询用户是否已经注册，通过获得用户密码
	          String phone_value = appChildService.getPhoneByOpenID(openID);
	          
	          if ((phone_value == null) || ("".equals(phone_value.trim()))) {
	       	   
	       	   logger.info("用户未绑定手机号");
	              return new JSONMessage(false, "用户未绑定手机号");//跳转到绑定手机号界面
	              
	          } else {
	       	  
	   		   //手机号存在，判断是否一致
	       	   //手机号和微信已绑定
	              //返回是否关注宝宝
	              Integer count = appChildService.getByPhone_1(phone_value);
	              if (count < 1) {
	           	   
	                  return new JSONMessage(true, "没有关注宝宝");
	              } else {
	           	   
	                  return new JSONMessage(true, "已经关注宝宝");
	               }
	          }//else
	  
		   }else{
			   //用户名或密码为空
			   logger.info("参数错误");
			   return new JSONMessage(false, "参数传输错误，参数不能为空");
		   }	           
		   
	  }
	  //type错误
	  logger.info("登录类型错误："+type);
	  return new JSONMessage(false, "不知类型的登录");
	
	
	}
	
	/**
	登录接口 --统一，参数：registrationID，mark，uid
	APP第三方
	*/
	@RequestMapping("/checkloginAPPS")
	public @ResponseBody JSONMessage checkloginAPPS(String uid,String type,String registrationID){
	 
	  //registrationID，mark，uid
	  if (StringUtils.isNoneBlank(registrationID,type,uid)) {
		 
		   //根据uid，获得mark,phone的值
		   HashMap<String, String> map = appChildService.getInfoByUID(uid);
		   //为空
		   if(map==null){
			   logger.info("用户未关联第三方登录");
			   return new JSONMessage(false, "用户未关联第三方登录");//跳转到绑定手机号界面
		   }
		   String phone_value = map.get("PHONE"); 
		   String mark_value = map.get("MARK"); 
	     // String uid_value = map.get("UNIONID");
	      
		   
	    // 对比是否一致，为空则未注册，不一致则已绑定其他第三方
	     //为空null
		   if ((phone_value == null) || ("".equals(phone_value.trim()))||(mark_value == null) || ("".equals(mark_value.trim()))) {
	  	   
			   logger.info("用户未关联第三方登录");
			   return new JSONMessage(false, "用户未关联第三方登录");
	         
		   }
		   
		   if (phone_value.length()!=0&&mark_value.length()!=0) {
	  	   //手机绑定其他微信/qq/微博
	   	  if (type.equals(mark_value)) {       			   
	      	 
	  		   //账号密码一致，查询是否关注宝宝	        		   
	  		   	  AppUser user=new AppUser();
	             user.setPhone(phone_value);
	             user.setRegistrationid(registrationID);
	             //类需要修改，更新极光推送标识
	             appChildService.updateByregisId_1(user);
	             
	             //返回是否关注宝宝
	             Integer count = appChildService.getByPhone_1(phone_value);
	             if (count < 1) {
	          	   
	                 return new JSONMessage(true, "没有关注宝宝");
	             } else {
	          	   
	                 return new JSONMessage(true, "已经关注宝宝");
	              }
	  	   }else{
	  		   
	  		   return new JSONMessage(false, "用户手机号已关联其他第三方登录");//UID唯一，没可能
	  	   	}
	         
	     }//if
	     
	  }else{//为空
		   //用户名或密码为空
		   logger.info("参数错误");
		   return new JSONMessage(false, "参数传输错误，参数不能为空");
	  	}
	  return new JSONMessage(false, "参数错误");
	}
	


}
