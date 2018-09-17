package com.junl.wpwx.mapper;

import com.junl.wpwx.model.AppUser;
import com.junl.wpwx.model.BsChildBaseInfo;
import com.junl.wpwx.model.ChildAppinfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppChildInfoMapper extends CrudMapper<ChildAppinfo> {


    /**
     * 根据手机号码获取用户信息
     * @author fuxin
     * @date 2017年3月25日 下午4:28:25
     * @description
     *		TODO
     * @param phone
     * @return
     *
     */
  //  List<AppUser> findByPhone(String phone);

    List<AppUser> findByList(AppUser appUser);

    List<AppUser> findByPhone(String phone);
    
    List<AppUser> findByUid(String uid);
    
    //修改 用户标识
     int updateByid(AppUser entity);


     List<BsChildBaseInfo> getByPhone(String phone);

     int insertByApp(AppUser entity);

    int insertByUid(AppUser entity);

    //修改 用户标识
    int updateByregisId(AppUser entity);

    int updateByApp(AppUser entity);

   //int updateByregisId(AppUser entity);

    int updateByHeadpath(AppUser entity);

    int updateByPwd(AppUser entity);

    int updateByNickname(AppUser entity);


  //edit by wangnan 2018-2-1 通过手机号获得用户密码 
    public  String getPwdByPhone(String phone);
    //edit by wangnan 2018-2-1
    
    public int updateByregisId_1(AppUser entity);
    
    //edit by wangnan 2018-2-1 用户是否关注宝宝    
    public Integer getByPhone_1(String phone);
    
    //edit by wangnan 2018-2-1 查询手机号对应的uid、mark
    public HashMap<String, String> getInfoByPhone(String phone);
    
    //通过手机号获取用户信息 edit by wangnan 2018-2-1
    public  HashMap<String, String> getInfoByUID(String uid);
    
    //通过手机号获取UID edit by wangnan 2018-2-1
    public String getOpenIDByPhone(String phone);
    
    //通过openID获取手机号 edit by wangnan 2018-2-5
    public String getPhoneByOpenID(String openID);
    
    //第三方微信登录uid，获取手机号 edit by wangnan 2018-2-5
    public  String getPhoneByUID_WX(String uid);
    
    //第三方QQ登录uid，获取手机号 edit by wangnan 2018-2-5
    public  String getPhoneByUID_QQ(String uid) ;
    
    //第三方为微博登录uid，获取手机号 edit by wangnan 2018-2-5
    public  String getPhoneByUID_WB(String uid);
    
    //edit by wangnan 2018-2-5  根据手机号获取用户ID 
    public HashMap<String, String> getIDByPhone(String phone);
    
    //edit by wangnan 2018-2-5  用户是否存在  
    public Integer getIsExistByPhone(String phone);
    
    //新增用户信息 edit by wangnan 2018-2-6
    public int insertUserByApp(AppUser info);
    
    //APP密码找回  更新密码 edit by wangnan 2018-2-6
    public int updateUserByPhone(AppUser info);
   
  //通过ID获取关注儿童列表  edit by wangnan 2018-2-7   
    public List<HashMap<String, String>> getChildByID(String id);

	//查询手机号码 关注宝宝用  edit by wangnan 2018-2-7
	public HashMap<String, String> choosephone_1(@Param(value = "birth")String birth, @Param(value = "name")String name);

	//新增第三方微信绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppWX(AppUser info);
    
  //新增第三方qq绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppQQ(AppUser info);
    
  //新增第三方微博绑定用户信息 edit by wangnan 2018-2-7
    public int insertUserByAppWB(AppUser info);
    
    //查询用户是否已经关注该宝宝  edit by wangnan 2018-2-7
    public Integer getIsAtten(@Param(value = "birth")String birth, @Param(value = "name")String name,@Param(value = "userId")String userId);

  //查询宝宝信息  edit by wangnan 2018-2-7
    public HashMap<String, String> getChildBaseInfo(@Param(value = "birth")String birth, @Param(value = "name")String name,@Param(value = "mobile")String mobile);

    //获取手机号
    public  String getPhoneByID(@Param(value = "userId")String userId);
    
  //插入宝宝关联信息  edit by wangnan 2018-2-8
    public int insertChildInfo(@Param(value = "mobile")String mobile,@Param(value = "ID")String ID,@Param(value = "createtime")Date createtime,@Param(value = "CHILDCODE")String CHILDCODE,@Param(value = "CHILDNAME")String CHILDNAME,@Param(value = "CARDCODE")String CARDCODE,@Param(value = "BIRTHCODE")String BIRTHCODE,@Param(value = "BIRTHDAY")String BIRTHDAY,@Param(value = "GUARDIANNAME")String GUARDIANNAME);
    
  //通过ID获取儿童档案信息详情  edit by wangnan 2018-2-24   
    public HashMap<String, String> getChildInfoByID(@Param(value = "id")String id);

  //通过我的界面-用户信息  edit by wangnan 2018-2-26   
    public HashMap<String, String> getmyInfo(@Param(value = "userId")String userId);
    
  //我的界面-保存用户信息  edit by wangnan 2018-2-26 
    public int updateMyInfoByID(AppUser info);
    
  //我的界面-获取密码  edit by wangnan 2018-2-26 
    public String findPasswordByID(@Param(value = "userId")String userId);
    
  //查询用户是否已经关注该宝宝 -手机号+儿童编号  edit by wangnan 2018-2-27
    public Integer getIsAttenByPhone(@Param(value = "phone")String phone, @Param(value = "childcode")String childcode);
    
  //根据儿童编号获取儿童ID  edit by wangnan 2018-3-2 
    public String getIDByChildcode(@Param(value = "childcode")String childcode);
    
    //通过childcode获取儿童档案信息详情  edit by wangnan 2018-3-2   
    public HashMap<String, String> getChildInfoByCode(@Param(value = "childcode")String childcode);

    //根据儿童id获取儿童姓名+生日 edit by wangnan 2018-3-5 
    public HashMap<String, Object> getChildDataByID(@Param(value = "id")String id);

  //根据儿童code获取儿童姓名+生日 edit by wangnan 2018-3-5 
    public List<HashMap<String, Object>> getChildDataByCode(@Param(value = "childcode")String childcode); 
    
}
