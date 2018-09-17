package com.junl.wpwx.service.app;


import com.junl.wpwx.mapper.AppChildInfoMapper;
import com.junl.wpwx.model.AppUser;
import com.junl.wpwx.model.BsChildBaseInfo;
import com.junl.wpwx.model.ChildAppinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * APP 用户管理Service
 * @author lonny
 * @version 2017-12-22
 */
@Service
@Transactional(readOnly = true)
public class AppChildInfoService {

    @Autowired()
    AppChildInfoMapper mapper;


    public ChildAppinfo get(String id) {
        return mapper.get(id);
    }

    public void save(ChildAppinfo info){
        mapper.insert(info);
    }

    public void updateByid(AppUser info){
        mapper.updateByid(info);
    }

    public List<ChildAppinfo> findList(ChildAppinfo info) {
        return mapper.findList(info);
    }
    /**
     * 根据手机号码获取用户信息
     * @description
     *		TODO
     * @param phone
     * @return
     *
     */
    public List<AppUser> findByPhone(String phone) {
        return mapper.findByPhone(phone);
    }


    public List<AppUser> findByList(AppUser appUser) {
        return mapper.findByList(appUser);
	}
   
    public List<AppUser> findByUid(String uid) {
        return mapper.findByUid(uid);

    }

/*    public List<AppUser> findByUid(String uid) {
        return mapper.findByUid(uid);
    }*/

    public List<BsChildBaseInfo> getByPhone(String phone){
        return mapper.getByPhone(phone);
    }

    //根据手机号修改极光推送ID
    public int updateByregisId(AppUser info){
       return  mapper.updateByregisId(info);
    }
   
    
    public int insertByApp(AppUser info){
       return mapper.insertByApp(info);
    }
    
    //新增用户信息 edit by wangnan 2018-2-6
    public int insertUserByApp(AppUser info){
        return mapper.insertUserByApp(info);
     }
    
    public int insertByUid(AppUser info){
        return mapper.insertByUid(info);
    }

    public int updateByApp(AppUser info){
        return mapper.updateByApp(info);
    }

    public int updateByHeadpath(AppUser info){
        return mapper.updateByHeadpath(info);
    }

    //根据手机号修改极光推送ID
/*    public int updateByregisId(AppUser info){
        return  mapper.updateByregisId(info);
    }*/

    //根据手机号修改密码
    public int updateByPwd(AppUser info){
        return  mapper.updateByPwd(info);
    }

    public int updateByNickname(AppUser info){
        return  mapper.updateByNickname(info);
    }


    // edit by wangnan 2018-2-1 通过手机号获得用户密码 
    public String getPwdByPhone(String phone) {
        return mapper.getPwdByPhone(phone);
    }
    
    //根据手机号修改极光推送ID edit by wangnan 2018-2-1
    public int updateByregisId_1(AppUser info){
       return  mapper.updateByregisId_1(info);
    }
   
    //edit by wangnan 2018-2-1  是否关注宝宝  
    public Integer getByPhone_1(String phone){
        return mapper.getByPhone_1(phone);
    }   
    
  //edit by wangnan 2018-2-5  根据手机号获取用户ID 
    public HashMap<String, String> getIDByPhone(String phone){
        return mapper.getIDByPhone(phone);
    } 
    
    
   // //edit by wangnan 2018-2-1  通过手机号获得MARK,UID
    public  HashMap<String, String> getInfoByPhone(String phone) {
        return mapper.getInfoByPhone(phone);
    } 
    
    public  HashMap<String, String> getInfoByUID(String uid) {
        return mapper.getInfoByUID(uid);
    } 
    
    
    //通过手机号获取openID
    public String getOpenIDByPhone(String phone) {
        return mapper.getOpenIDByPhone(phone);
    }
    
    //通过openID获取手机号
    public String getPhoneByOpenID(String openID) {
        return mapper.getPhoneByOpenID(openID);
    }

    //第三方微信登录uid，获取手机号
    public  String getPhoneByUID_WX(String uid) {
        return mapper.getPhoneByUID_WX(uid);
    }
    
    //第三方QQ登录uid，获取手机号
    public  String getPhoneByUID_QQ(String uid) {
        return mapper.getPhoneByUID_QQ(uid);
    }
    
    //第三方为微博登录uid，获取手机号
    public  String getPhoneByUID_WB(String uid) {
        return mapper.getPhoneByUID_WB(uid);
    }
    
    //edit by wangnan 2018-2-5  用户是否存在  
    public Integer getIsExistByPhone(String phone){
        return mapper.getIsExistByPhone(phone);
    }   
    
    //APP密码找回  edit by wangnan 2018-2-6
    public int updateUserByPhone(AppUser info){
       return mapper.updateUserByPhone(info);
    }
    
    //通过ID获取关注儿童列表  edit by wangnan 2018-2-7   
    public List<HashMap<String, String>> getChildByID(String id){
        return mapper.getChildByID(id);
     }
    
  //查询手机号码 关注宝宝用 edit by wangnan 2018-2-7
  	public HashMap<String, String> choosephone_1(String birth, String name){
        return mapper.choosephone_1(birth, name);
    }
  	
  //新增第三方微信绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppWX(AppUser info){
        return mapper.insertUserByAppWX(info);
     }
    
  //新增第三方qq绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppQQ(AppUser info){
        return mapper.insertUserByAppQQ(info);
     }
    
  //新增第三方微博绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppWB(AppUser info){
        return mapper.insertUserByAppWB(info);
     }
    
  //查询用户是否已经关注该宝宝  edit by wangnan 2018-2-7
    public Integer getIsAtten(String birth, String name,String userId){
        return mapper.getIsAtten(birth, name,userId);
    } 
    
   //查询宝宝信息  edit by wangnan 2018-2-7
    public HashMap<String, String> getChildBaseInfo(String birth, String name,String mobile){
        return mapper.getChildBaseInfo(birth, name,mobile);
    } 
    
    
    //获取手机号
    public  String getPhoneByID(String userId) {
        return mapper.getPhoneByID(userId);
    }
    
  //插入宝宝关联信息  edit by wangnan 2018-2-8
    public int insertChildInfo(String mobile,String ID,Date createtime,String CHILDCODE,String CHILDNAME,String CARDCODE,String BIRTHCODE,String BIRTHDAY,String GUARDIANNAME){
        return mapper.insertChildInfo(mobile,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);
    } 

	   
  //通过ID获取儿童档案信息详情  edit by wangnan 2018-2-24   
    public HashMap<String, String> getChildInfoByID(String id){
        return mapper.getChildInfoByID(id);
     } 
    
  //通过childcode获取儿童档案信息详情  edit by wangnan 2018-3-2   
    public HashMap<String, String> getChildInfoByCode(String childcode){
        return mapper.getChildInfoByCode(childcode);
     } 
      
  //通过我的界面-用户信息  edit by wangnan 2018-2-26   
    public HashMap<String, String> getmyInfo(String userId){
        return mapper.getmyInfo(userId);
     } 
    
  //我的界面-保存用户信息  edit by wangnan 2018-2-26 
    public int updateMyInfoByID(AppUser info){
       return  mapper.updateMyInfoByID(info);
    }
    
    //我的界面-修改密码  edit by wangnan 2018-2-26 
    public String findPasswordByID(String userId) {
        return mapper.findPasswordByID(userId);
	}
    
  //查询用户是否已经关注该宝宝 -手机号+儿童编号  edit by wangnan 2018-2-27
    public Integer getIsAttenByPhone(String phone, String childcode){
        return mapper.getIsAttenByPhone(phone,childcode);
    } 
     
    //根据儿童编号获取儿童ID  edit by wangnan 2018-3-2 
    public String getIDByChildcode(String childcode) {
        return mapper.getIDByChildcode(childcode);
	}
    
    //根据儿童id获取儿童姓名+生日 edit by wangnan 2018-3-5 
    public HashMap<String, Object> getChildDataByID(String id){
        return mapper.getChildDataByID(id);
     } 
    
    //根据儿童code获取儿童姓名+生日 edit by wangnan 2018-3-5 
    public List<HashMap<String, Object>> getChildDataByCode(String id){
        return mapper.getChildDataByCode(id);
     } 
    
}
