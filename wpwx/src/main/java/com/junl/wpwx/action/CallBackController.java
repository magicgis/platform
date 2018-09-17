package com.junl.wpwx.action;

import cn.beecloud.BCCache;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.DesUtil;
import com.junl.wpwx.common.Global;
import com.junl.wpwx.service.vaccinate.VacOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 回调使用的Controller
 * @author fuxin
 * @version 2017-03-02
 */
@Controller
@RequestMapping(value = "/callBack")
public class CallBackController extends BaseAction {

	@Autowired
	ConfigProperty conf;
	@Autowired
	VacOrderService vacOrderService;
	
	//beeCloud支付回调================================================================================================= 
	
	@RequestMapping("/beePay")
	public @ResponseBody String beePay(HttpServletRequest request, HttpServletResponse response, Model model){
		    StringBuffer json = new StringBuffer();
		    String line = null;
		    try {
		        request.setCharacterEncoding("utf-8");
		        BufferedReader reader = request.getReader();
		        while ((line = reader.readLine()) != null) {
		            json.append(line);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    JSONObject jsonObj = JSONObject.fromObject(json.toString());
		    
		    String signature = jsonObj.getString("signature");
		    String transactionId=jsonObj.getString("transaction_id");
		    String transactionType=jsonObj.getString("transaction_type");
		    String channelType=jsonObj.getString("channel_type");
		    String transactionFee=jsonObj.getString("transaction_fee");
		   //生成订单时 添加的自定义参数
		    StringBuffer toSign = new StringBuffer();
		    toSign.append(BCCache.getAppID()).append(transactionId)
		            .append(transactionType).append(channelType)
		            .append(transactionFee);
		   boolean status = verifySign(toSign.toString(),conf.getBeeMaster(),signature);
		    if (status) { //验证成功
		    	// 此处需要验证购买的产品与订单金额是否匹配:
		    	// 验证购买的产品与订单金额是否匹配的目的在于防止黑客反编译了iOS或者Android app的代码，
		    	// 将本来比如100元的订单金额改成了1分钱，开发者应该识别这种情况，避免误以为用户已经足额支付。
		    	// Webhook传入的消息里面应该以某种形式包含此次购买的商品信息，比如title或者optional里面的某个参数说明此次购买的产品是一部iPhone手机，
		    	// 开发者需要在客户服务端去查询自己内部的数据库看看iPhone的金额是否与该Webhook的订单金额一致，仅有一致的情况下，才继续走正常的业务逻辑。
		    	// 如果发现不一致的情况，排除程序bug外，需要去查明原因，防止不法分子对你的app进行二次打包，对你的客户的利益构成潜在威胁。
		    	// 如果发现这样的情况，请及时与我们联系，我们会与客户一起与这些不法分子做斗争。而且即使有这样极端的情况发生，
		    	// 只要按照前述要求做了购买的产品与订单金额的匹配性验证，在你的后端服务器不被入侵的前提下，你就不会有任何经济损失。
		    	
		    	// 处理业务逻辑
		    	//更新订单信息
		    	logger.info("接收到bee Cloud回调请求" + jsonObj);
		        vacOrderService.finishOrder(jsonObj);
		        
		        return "success"; //请不要修改或删除
		    } else { //验证失败
		       return "fail";
		    }
	}
	
	boolean verifySign(String text,String masterKey,String signature) {
        boolean isVerified = verify(text, signature, masterKey, "UTF-8");
        if (!isVerified) {
            return false;
        }
        return true;
    }


     boolean verify(String text, String sign, String key, String inputCharset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
        return mysign.equals(sign);
    }

    byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    //beeCloud支付回调  end======================================================================================================== 
    
    
    //江南银行支付回调============================================================================================================== 
	/**
	 * 支付回调controller
	 * @author Jack
	 * @date 2017年10月10日 下午2:12:21
	 * TODO
	 * @description
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 *
	 */
	@RequestMapping(value = "/jnBank" , method=RequestMethod.POST)
	public @ResponseBody String jnBank(HttpServletRequest request,HttpServletResponse response, Model model) throws IOException{
		logger.info("[江南银行支付：接收请求开始处理  START]");
		StringBuffer json = new StringBuffer();
	    String line = null;
	    try {
	        request.setCharacterEncoding("utf-8");
	        BufferedReader reader = request.getReader();
	        while ((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		logger.info("江南银行支付回调信息" + json.toString());
		String jsonStr = json.toString();
		if(StringUtils.isEmpty(jsonStr)){
			logger.error("[江南银行支付：回调处理失败 resp is null]");
			return "[resp is null]";
		}
		if(jsonStr.indexOf("resp=") < 0){
			logger.error("[江南银行支付：回调处理失败 resp is empty]");
			return "[resp is empty]";
		}
		String optional = DesUtil.decode(jsonStr.replace("resp=", ""),Global.getConfig("JnBank_decCode"));
		logger.info("江南银行支付回调信息解码后信息" + optional);
		//对收到json String处理
		vacOrderService.finishJnBank(optional.replace("attach=", ""));
		logger.info("[江南银行支付：回调处理结束  success]");
		return "success";
	}
	//江南银行支付回调 end========================================================================================================== 
}
