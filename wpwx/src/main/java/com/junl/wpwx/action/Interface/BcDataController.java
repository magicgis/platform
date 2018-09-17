package com.junl.wpwx.action.Interface;


import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayClient;
import com.junl.frame.tools.CommonUtils;
import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.JsonMapper;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.form.AttenForm;
import com.junl.wpwx.model.*;
import com.junl.wpwx.pay.Alipay;
import com.junl.wpwx.pay.weixinPrePay;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.SysHolidayService;
import com.junl.wpwx.service.app.AppChildInfoService;
import com.junl.wpwx.service.jpush.JpushMessageService;
import com.junl.wpwx.service.vaccinate.*;
import com.junl.wpwx.utils.DateTools;
import com.junl.wpwx.utils.FuxinUtil;
import com.junl.wpwx.utils.ImageUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/bcdata")
public class BcDataController  extends BaseAction {

    @Autowired
    AsyncService async;
    @Resource
    private Ehcache cache;
    @Autowired
    private VacService vacService;
    @Autowired
    private VacChildTempService childTempService;
    @Autowired
    private VacChildInfoService childInfoService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private VacKnowledgeInfoService infoService;
    @Autowired
    private VacKnowledgeSicknessService sicknessService;
    @Autowired
    private VacKnowledgeListService listService;
    @Autowired
    private AppChildInfoService appChildService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DepartmentInfoService departmentInfoService;
    @Autowired
    private SysHolidayService sysService;
    @Autowired
    private WorkingHoursService workService;

    @Autowired
    private BsOrderService orderService;
    @Autowired
    private JpushMessageService jpushMessageService;
    @Autowired
    private ConfigProperty conf;



    // 登录开始

    /**
     * 发送短信验证码
     */
    @RequestMapping("/smscheckcode")
    public @ResponseBody JSONMessage sendchecksms( String phone){
        if(async.sendCheckSMS(phone)){
            return new JSONMessage( true, "短信发送成功");
        }else{
            return new JSONMessage(false, "短信发送失败");
        }
    }

    /**
     * 发送短信验证码 1：找回密码 2：注册
     */
    @RequestMapping("/retrievePwd")
    public @ResponseBody JSONMessage retrievePwd( String phone,String type){
        AppUser appUser=new AppUser();
        appUser.setPhone(phone);
        List<AppUser> appList = appChildService.findByList(appUser);
        if("1".equals(type)){
                if(appList.size()>0){
                if(async.sendCheckSMS(phone)){
                    return new JSONMessage( true, "短信发送成功");
                }else{
                    return new JSONMessage(false, "短信发送失败");
                }
            }else{
                return new JSONMessage(false, "你输入的手机号没有注册！");
            }
        }else if("2".equals(type)){
            if(appList.size()<1){
                if(async.sendCheckSMS(phone)){
                    return new JSONMessage( true, "短信发送成功");
                }else{
                    return new JSONMessage(false, "短信发送失败");
                }
            }else{
                return new JSONMessage(false, "注册用户名重复，请重新输入");
            }
        }
        return new JSONMessage(false, "类型错误");
    }

    /**
     注册接口   1：找回密码 2：注册
     */
    @RequestMapping("/registered")
    public @ResponseBody JSONMessage registered(String phone, String code ,String password,String registrationID,String type){
        if (StringUtils.isNoneBlank(phone) && StringUtils.isNotBlank(code)) {
            try {
                //获取缓存中的验证码
                String cc = (String) cache.getCache(phone, "messageCache");
                AppUser app=new AppUser();
                app.setPhone(phone);
                List<AppUser> appList = appChildService.findByList(app);
                if (code.equals(cc)) {
                    if("1".equals(type)){
                        if(appList.size()>0){
                            //找回密码修改数据库
                            AppUser appUser=new AppUser();
                            appUser.setPhone(phone);
                            appUser.setUserpwd(password);
                            appUser.setRegistrationid(registrationID);
                            appChildService.updateByApp(appUser);//修改app用户表
                            return new JSONMessage( true, "密码找回成功");
                        }else{
                            return new JSONMessage( false, "密码找回失败");
                        }
                    }else if("2".equals(type)){
                        if(appList.size()!=1){
                            //新建用户插入数据库
                            AppUser appUser=new AppUser();
                            appUser.setId( UUID.randomUUID().toString());
                            appUser.setPhone(phone);
                            appUser.setUserpwd(password);
                            appUser.setRegistrationid(registrationID);
                            appChildService.insertByApp(appUser);//插入app用户表
                        }
                        List<BsChildBaseInfo> list= appChildService.getByPhone(phone) ;
                        if(list.size()<1){
                            return new JSONMessage( true, "没有关注儿童");
                        }else {
                           // JsonMapper.toJsonString(list);
                            return new JSONMessage( true, list);
                        }
                    }
                }
            } catch (Exception e) {
                //捕获异常，不处理
            }
        }
        return new JSONMessage(false, "登录验证失败");
    }
        
    // headpath  phone

    //保存用户头像
    @RequestMapping("/imageSave") //imageSave
    public @ResponseBody JSONMessage imageSave(String phone,String headpath){
        String mc= ImageUtil.GenerateImage(headpath,conf.getImgPath(),"png");
        String url=conf.getImgurl()+mc+".png";
        AppUser appUser=new AppUser();
        appUser.setPhone(phone);
        appUser.setHeadurl(url);
        appChildService.updateByApp(appUser);//修改app用户表
        return new JSONMessage(true, "保存图片成功！");
    }
    //保存用户昵称
    @RequestMapping("/nicknameSave") //imageSave  Message
    public @ResponseBody JSONMessage nicknameSave(String phone,String nickname){
            AppUser appUser=new AppUser();
            appUser.setPhone(phone);
            appUser.setNickname(nickname);
            appChildService.updateByApp(appUser);//修改app用户表
            return new JSONMessage(true, "保存昵称成功！");
    }

    //查询用户头像
    @RequestMapping("/image")
    public @ResponseBody JSONMessage image(String phone){
        AppUser appUser=new AppUser();
        appUser.setPhone(phone);
        List<AppUser> appList = appChildService.findByList(appUser);
        if(appList.size()>0){
            Map<String,Object> map=new HashMap<String, Object>();
            if(null==appList.get(0).getHeadurl()){
                //return new JSONMessage(false, "没有头像");
                map.put("headurl","");
            }else {
                map.put("headurl",appList.get(0).getHeadurl());
            }
            if(null==appList.get(0).getNickname()){
                map.put("nickname",phone);
            }else {
                map.put("nickname",appList.get(0).getNickname());
            }
            return new JSONMessage(true, map);
        }else {
            return new JSONMessage(false, "没有此用户");
        }
    }

    /**
     登录接口 appChildService
     */
    @RequestMapping("/checksms")
    public @ResponseBody JSONMessage checksms(String phone, String password,String registrationID){
        if (StringUtils.isNoneBlank(phone) && StringUtils.isNotBlank(password)) {
            AppUser appUser=new AppUser();
            appUser.setPhone(phone);
            List<AppUser> usersList = appChildService.findByList(appUser);
            if (usersList.size() != 1) {
                return new JSONMessage(false, "没有此用户");
            } else {
                String pwd = usersList.get(0).getUserpwd();
                if (!"".equals(pwd) && pwd != null) {
                    if (!password.equals(pwd)) {
                        return new JSONMessage(false, "密码错误");
                    } else {
                        AppUser user=new AppUser();
                        user.setPhone(phone);
                        user.setRegistrationid(registrationID);
                        appChildService.updateByApp(user);
                        List<BsChildBaseInfo> list = appChildService.getByPhone(phone);
                        if (list.size() < 1) {
                            return new JSONMessage(true, "没有关注儿童");
                        } else {
                            return new JSONMessage(true, list);
                        }
                    }
                } else {
                    return new JSONMessage(false, "密码为空");
                }
            }
        }
        return new JSONMessage(false, "登录验证失败");
    }

    /*
    * 修改密码
    *
    * */
    @RequestMapping("/changePwd") //changePwd
    public @ResponseBody JSONMessage changePwd(String phone, String oldPassword, String newPassword){
        if (StringUtils.isNoneBlank(phone) && StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            AppUser appUser=new AppUser();
            appUser.setPhone(phone);
            List<AppUser> usersList = appChildService.findByList(appUser);
            if (usersList.size() != 1) {
                return new JSONMessage(false, "没有此用户");
            } else{
                String pwd = usersList.get(0).getUserpwd();
                if (!"".equals(pwd) && pwd != null) {
                    if (!oldPassword.equals(pwd)) {
                        return new JSONMessage(false, "原密码错误");
                    } else {
                        AppUser user=new AppUser();
                        user.setPhone(phone);
                        user.setUserpwd(newPassword);
                        appChildService.updateByApp(user);//
                    }
                }
                return new JSONMessage(true, "密码修改成功");
            }
        } else {
            return new JSONMessage(false, "入参错误");
        }
    }

    /**
     第三方登录接口 appChildService
     */
    @RequestMapping("/chexkUid")
    public @ResponseBody JSONMessage chexkUid(String uid,String type){
        AppUser appUser=new AppUser();
        appUser.setUid(uid);
        appUser.setType(type);
        List<AppUser> appList = appChildService.findByList(appUser);
        if(appList.size()<1){
            return new JSONMessage( false, "没有绑定手机号");
        }else {
            List<BsChildBaseInfo> list= appChildService.getByPhone(appList.get(0).getPhone()) ;
            Map<String,Object> map= new HashMap<>();
            map.put("phone",appList.get(0).getPhone());
            if(list.size()<1){
                map.put("msg","没有关注儿童");
            }else {
                map.put("msg","已关注宝宝");
            }
            return new JSONMessage( true, map);
        }

    }

    /**
     第三方绑定接口 appChildService
     */
    @RequestMapping("/bindUid")
    public @ResponseBody JSONMessage bindUid(String phone, String code ,String uid,String registrationID,String type){
        try {
                //获取缓存中的验证码
                String cc = (String) cache.getCache(phone, "messageCache");
                if ( code.equals(cc)) {
                    AppUser app=new AppUser();
                    app.setPhone(phone);
                    List<AppUser> usersList = appChildService.findByList(app);
                   /* List<AppUser> usersList = appChildService.findByPhone(phone);*/
                    AppUser appUser=new AppUser();
                    if(usersList.size()<1){
                        appUser.setId( UUID.randomUUID().toString());
                        appUser.setPhone(phone);
                        appUser.setUid(uid);
                        appUser.setRegistrationid(registrationID);
                        appUser.setType(type);
                        appChildService.insertByUid(appUser); //创建用户表 插入表
                    }else {
                        appUser.setId(usersList.get(0).getId());
                        appUser.setPhone(phone);
                        appUser.setUid(uid);
                        appUser.setRegistrationid(registrationID);
                        appUser.setType(type);
                        appChildService.updateByid(appUser);//修改app用户表的uid  registrationID
                    }
                    List<BsChildBaseInfo> list= appChildService.getByPhone(phone) ;
                    if(list.size()<1){
                        return new JSONMessage( true, "没有关注儿童");
                    }else {
                        return new JSONMessage( true, list);
                    }
                }
            }catch (Exception e) {
                //捕获异常，不处理
            }
        return new JSONMessage( false, "短信验证失败");
    }

    /**
     通过手机号获取已关注的儿童信息
     */
    @RequestMapping("/ChildBaseInfo")
    public @ResponseBody JSONMessage ChildBaseInfo( String phone,String code){
        List<BsChildBaseInfo> returnList =new ArrayList<BsChildBaseInfo>();
        List<BsChildBaseInfo> list=vacService.getBsChildBaseInfoByPhone(phone) ;
        if("1".equals(code)){
            returnList=list;
        }else {
            for(int i=0;i<list.size();i++){
                if(code.equals(list.get(i).getChildcode())){
                    returnList.add(list.get(i));
                }
            }
            for(int i=0;i<list.size();i++){
                if(!code.equals(list.get(i).getChildcode())){
                    returnList.add(list.get(i));
                }
            }
        }
        if(list.size()<1){
            return new JSONMessage(false, "未关注宝宝！");
        }else {
            return new JSONMessage(true, returnList);
        }
    }

    //登录结束

    @RequestMapping(value = "/baseinfo" ,method = RequestMethod.POST)
    public @ResponseBody JSONMessage detail( String code,String type){
        if("1".equals(type)){
            BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(code);
            if(null == baseinfo){
                return new JSONMessage(false, "儿童信息不存在");
            }
            baseinfo = vacService.updateAddr(baseinfo);
            baseinfo = vacService.updateDetail(baseinfo);
            return new JSONMessage(true, baseinfo);
        }else if("3".equals(type)){
            VacChildTemp baseinfo = childTempService.getByCode(code); //查询建卡表
            baseinfo = vacService.updateAddr(baseinfo);
            baseinfo = vacService.updateDetail(baseinfo);
            return new JSONMessage(true, baseinfo);
        }
        return new JSONMessage(false, "参数错误");
    }


    /**
     * 查询接种记录
     *  type=1 已种疫苗
     *  type=0 未种疫苗
     *  code 儿童编号
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/record/{type}")
    public @ResponseBody JSONMessage record( @PathVariable String type, @RequestParam(required = false) String code){
        if(!StringUtils.isNotBlank(code)){
            return new JSONMessage(false, "儿童编码为空");
        }
        List<List<?>> returnlist = new ArrayList<>();
        //未完成列表
        if(BsRecord.STATUS_WAIT.equals(type)){ //未接种记录
            try {
                returnlist = (List<List<?>>) vacService.getVaccList(code, BsVaccNum.TYPE_RECORD);
            } catch (Exception e) {

                return new JSONMessage(false, e.getMessage());
            }
            return new JSONMessage(true, returnlist);
        }else{
            List<BsRecord> listTemp = vacService.getRecordFinish(code);
            //数组转树形
            String op = "first";
            List<BsRecord> templist = new ArrayList<>();
            for(BsRecord l : listTemp){
                if(op.equals("first")){
                    op = l.getAllname();
                }
                if(!op.equals(l.getAllname())){
                    templist.get(0).setLeng(templist.size()+1);
                    returnlist.add(templist);
                    templist = new ArrayList<>();
                    op = l.getAllname();
                }
                templist.add(l);
            }
            if(templist.size() > 0){
                templist.get(0).setLeng(templist.size()+1);
                returnlist.add(templist);
            }
            //数组转树形 结束
            return new JSONMessage(true, returnlist);
        }
    }

    /**
     * 查询手机号码
     * birth 宝宝生日 年月日
     * name 家长/宝宝姓名
     */
    @RequestMapping(value="/choosephone")
    public @ResponseBody JSONMessage choosephone(@RequestParam(required = false) Date birth, @RequestParam(required = false) String name){
        if(null != birth || !StringUtils.isNotBlank(name)){
            List<String> data = vacService.choosephone(birth, name);
            if(data.size() == 0){
                return new JSONMessage(false, "未查询到相关手机号码");
            }else{
                return new JSONMessage(true, data);
            }
        }
        return new JSONMessage(false, "请输入宝宝信息");
    }

    /**
     * 关注宝宝
     * name 宝宝信息
     */
    @RequestMapping(value="/infoAtten", method=RequestMethod.POST)
    public @ResponseBody JSONMessage infoatten(AttenForm info){
        //表单数据验证 USERID
        if(info.getBirthday() == null || !StringUtils.isNotBlank(info.getName()) || !StringUtils.isNotBlank(info.getPhone())) {
            return new JSONMessage(false, "参数错误！");
        }else{
            try {
                //获取缓存中的验证码
                String cc = (String) cache.getCache(info.getPhone(), "messageCache");
                if (cc.equals(info.getCode())) {
                    BsChildBaseInfo bsInfo = vacService.chooseBaseInfo(info);
                    VacChildInfo i = VacChildInfo.parse(bsInfo);
                    i.setGuardianmobile(info.getPhone());
                    if(i != null){
                        //验证是否已被关注
                        VacChildInfo tempinfo = new VacChildInfo();
                        tempinfo.setGuardianmobile(info.getPhone());
                        tempinfo.setChildcode(i.getChildcode());
                        List<ChildAppinfo> infos =childInfoService.findAppUserList(tempinfo); //查询是否已被关注
                        if(infos.size() > 0){
                            return new JSONMessage(false, "宝宝已被关注");
                        }else {
                            childInfoService.insertAppUser(i); //插入APP关注表
                        }
                    }else{
                        return new JSONMessage(false, "宝宝建档信息尚未同步");
                    }
                }
            } catch (Exception e) {
                return new JSONMessage(false, "关注宝宝异常");
            }
        }
        return new JSONMessage(true, "宝宝关注成功");
    }

    /**
     * 根据pid获取区域信息 省 市 区 联动
     * pid  编号
     */
    @RequestMapping("/area/{pid}")
    public @ResponseBody JSONMessage findAreaByPid(@PathVariable String pid){
        if(StringUtils.isNotBlank(pid)){
            List<Area> list =vacService.findAreaByPid(pid);
            return new JSONMessage(true, list);
        }else{
            return new JSONMessage(false, "错误的Pid");
        }
    }


    //          宝宝接种结束

    //          自助建档开始 type
    /**
     *自助建档 页面数据
     */
    @RequestMapping(value="/form")
    public @ResponseBody JSONMessage nationlist (){
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("nationList",vacService.getNationList());//查询民族
        map.put("hostipalList",vacService.getHostipallist());//查询医院
        map.put("communityList",vacService.getCommunitylist());//查询社区
        map.put("paradoxicalreaction",vacService.getDictList("paradoxicalreaction")); //字典数据
        map.put("properties",vacService.getDictList("properties"));
        map.put("reside",vacService.getDictList("reside"));
       // map.put("nation",vacService.getDictList("nation"));
        map.put("deptList",vacService.findDeptList(new SysDept()));//查询接种单位
        return  new JSONMessage(true, map);
    }

    /**
     *自助建档保存数据
     * userId
     */
    @RequestMapping(value="/tempSave", method= RequestMethod.POST)
    public @ResponseBody JSONMessage tempSave(String code,String registrationID){
        VacChildTemp vacChildTemp=new VacChildTemp();
        code = StringEscapeUtils.unescapeHtml(code);
        try {
            vacChildTemp = (VacChildTemp) JsonMapper.fromJsonString(code, VacChildTemp.class);
            vacChildTemp.setUserId(registrationID);
            vacChildTemp.setFileorigin("3");
            vacChildTemp= childTempService.save1(vacChildTemp);
            vacChildTemp.setOfficeInfoName(vacService.getDeptlistByCode(vacChildTemp.getOfficeinfo()).getName());
           // vacChildTemp = vacService.updateAddr(vacChildTemp);  //更新住址信息
            vacChildTemp = vacService.updateDetail(vacChildTemp); //更新详细页面区域划分、户口类型、居住类型、出生医院信息
            String str=vacChildTemp.getChildcode();
            /*Notification notification=new Notification();
            notification.setChildcode("");
            notification.setNotifyname("自助建档通知");
            notification.setNotifytime(new Date());
            notification.setNotifycontent(str.substring(10,14)+","+vacChildTemp.getChildname());
            notification.setType("06");
            notificationService.insertNotification(notification);*/
            List<String> list=new ArrayList<>();
            list.add(registrationID);
            jpushMessageService.sendToRegistrationId(list,"自助建档成功","编码【"+str+"】有效期三天,请及时前往接种单位完成建档",vacChildTemp.getChildcode(),"06");
            return  new JSONMessage(true, vacChildTemp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *建卡列表
     */
    @RequestMapping(value="/tempDetail")
    public @ResponseBody JSONMessage tempDetail(String phone){
        if(null != phone && StringUtils.isNotBlank(phone)){
            List<VacChildTemp> list = childTempService.getByPhone(phone); //查询建卡表
            if(null == list||list.size()<1){
                return new JSONMessage(false, "该手机号没有建档信息");
            }else {
                List<VacChildTemp> listTemp=new ArrayList<VacChildTemp>();
                VacChildTemp vacChildTemp=new VacChildTemp();
                for(int i=0;i<list.size();i++){
                    vacChildTemp= vacService.updateAddr(list.get(i));
                    vacChildTemp = vacService.updateDetail(vacChildTemp);
                    listTemp.add(vacChildTemp);
                }
                return new JSONMessage(true, listTemp);
            }
        }else {
            return new JSONMessage(false, "参数错误");
        }
    }

    /**
     * 自助建档(犬伤) 页面数据
     *//*
    @RequestMapping(value = "/dogForm")
    public  @ResponseBody JSONMessage dogForm(VacRabiesTemp temp) {
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("animals",vacService.getDictList(VacRabiesTemp.DICTTYPE_ANIMAL)); //字典数据
        map.put("bites", vacService.getDictList(VacRabiesTemp.DICTTYPE_BITEPART));
        map.put("disposal_sites", vacService.getDictList(VacRabiesTemp.DICTTYPE_DEALADDRESS));
        map.put("bitetypes", vacService.getDictList(VacRabiesTemp.DICTTYPE_BITETYPE));
        return  new JSONMessage(true, map);
    }
    *//**
     *自助建档保存数据(犬伤）
     * userId
     *//*
    @RequestMapping(value = "/dogSave", method = RequestMethod.POST)
    public @ResponseBody JSONMessage dogSave(VacRabiesTemp temp) {
        String userId="";
        temp.setOpenid(userId);
        temp.setTempid(CommonUtils.UUIDGenerator());
        rabiesTempService.save(temp);
        return  new JSONMessage(true,temp);
    }
    *//**
     *建档信息(犬伤）
     *//*
    @RequestMapping("/dogDetail")
    public @ResponseBody JSONMessage dogDetail(VacRabiesTemp temp, Model model ,String id) {
        if(com.junl.frame.tools.string.StringUtils.isEmpty(temp.getId())){
            return new JSONMessage(false, "您的编号已过期");
        }
        VacRabiesTemp entity = rabiesTempService.get(id);
        temp.setAnimal(vacService.getDictLabel(temp.getAnimal(), VacRabiesTemp.DICTTYPE_ANIMAL, "其他"));
        temp.setBitepart(vacService.getDictLabel(temp.getBitepart(), VacRabiesTemp.DICTTYPE_BITEPART, "其他"));
        temp.setBitetype(vacService.getDictLabel(temp.getBitetype(), VacRabiesTemp.DICTTYPE_BITETYPE, "其他"));
        temp.setDealaddress(vacService.getDictLabel(temp.getDealaddress(), VacRabiesTemp.DICTTYPE_DEALADDRESS, "其他"));
        return new JSONMessage(true, temp);
    }*/

    //           自助建档结束

    //           妈咪课堂开始

    /**
     * 预防接种 1
     */
    @RequestMapping(value = {"/msgList"})
    public @ResponseBody JSONMessage list() {
        Article  article =new Article();
        article.setCategoryId("1");
        List<Article> findList = articleService.findByidList(article);
        if(findList.size()>0){
            List<PoDatamodel> listPodata =new ArrayList<PoDatamodel>();
            for(Article l : findList){
                PoDatamodel po=new PoDatamodel();
                po.setId(l.getId());
                po.setTitle(l.getTitle());
                po.setImage(l.getImage());
                listPodata.add(po);
            }
            return new JSONMessage(true, listPodata);
        }
        return new JSONMessage(false, "没有记录！");
    }


    @RequestMapping(value = "/vaccinationDetail")
    public  String vaccinationDetail( Model model ,String id) {
       Article article  =articleService.get(id);
       /* model.addAttribute("article", article.getArticleData());
        return "knowhow/vaccination-detail";*/
        // 如果当前传参有子节点，则选择取消传参选择
        model.addAttribute("article", article);
        return "cms/articleDetail";
    }

     /* * 行业资讯 4
      **/
     @RequestMapping(value = {"/hyList"})
     public @ResponseBody JSONMessage hyList() {
         Article  article =new Article();
         article.setCategoryId("2");
         List<Article> findList = articleService.findByidList(article);
         List<PoData> listPodata =new ArrayList<PoData>();
         for(Article l : findList){
             PoData po=new PoData();
             po.setId(l.getId());
             po.setTitle(l.getTitle());
             String[] str=new String[5];
             List<String> list=new ArrayList<>();
             if(l.getImage()!=null){
                 str=l.getImage().split(",");
                 if(str.length==1){
                     po.setType("1");
                 }else {
                     po.setType("2");
                 }
                 for(int i=0;i<str.length;i++){
                     list.add(str[i]);
                 }
             }else {
                 po.setType("0");
                 list=null;
             }
             po.setImage(list);
             listPodata.add(po);
         }
         return new JSONMessage(true, listPodata);
     }

        /* * 行业资讯 4
            * 办事指南 2
            * 育儿知识 3
            **/

    /**
     * 上面的图文详情
     */
    @RequestMapping(value = "/msgDetail")
    public  String msgDetail( Model model ,String id) {
        Article article  =articleService.get(id);
        model.addAttribute("article", article);
        return "cms/articleDetail";
        /*model.addAttribute("article", article);
        return "knowhow/industry-info-detail"; //vaccination-detail*/
    }

    /**
     * 关于我们页面
     */
    @RequestMapping(value = "/aboutUs")
    public  String aboutUs( ) {
        return "app/aboutus";
    }

    /**
     * 疫苗信息列表
     */
    @RequestMapping("/vaccinateList")
    public  @ResponseBody JSONMessage vaccinateList( ) throws SQLException{
        VacKnowledgeList knowlist = new VacKnowledgeList();
        knowlist.setOrderBy("a.ID ASC");
        List<VacKnowledgeList> list = listService.findList(knowlist);
        if(list.size()>0){
            List<PoDatamodel> listPodata =new ArrayList<PoDatamodel>();
            for(VacKnowledgeList l : list){
                PoDatamodel po=new PoDatamodel();
                po.setId(l.getId());
                po.setTitle(l.getTitle());
                po.setImage(l.getImg());
                listPodata.add(po);
            }
            return new JSONMessage(true, listPodata);
        }
        return new JSONMessage(false, "没有记录！");
    }

    /**
     * 疫苗详细信息
     */
    @RequestMapping("/vaccinateInfo")
    public  String vaccinateInfo(Model model, String id) {
        VacKnowledgeInfo info = infoService.get(id);
        model.addAttribute("article", info);
        return "knowhow/vaccine-detail";
    }

    /**
     * 疾病列表
     */
    @RequestMapping("/sicknessList")
    public @ResponseBody JSONMessage sicknessList( ) {
        VacKnowledgeSickness sickness = new VacKnowledgeSickness();
        sickness.setOrderBy("a.ID ASC");
        List<VacKnowledgeSickness> list = sicknessService.findList(sickness);
        if(list.size()>0){
            List<PoDatamodel> listPodata =new ArrayList<PoDatamodel>();
            for(VacKnowledgeSickness l : list){
                PoDatamodel po=new PoDatamodel();
                po.setId(l.getId());
                po.setTitle(l.getName());
                po.setImage(l.getImg());
                listPodata.add(po);
            }
            return new JSONMessage(true, listPodata);
        }
        return new JSONMessage(false, "没有记录！");
    }

    /**
     * 疾病详细信息
     */
    @RequestMapping("/sicknessInfo")
    public String sicknessInfo(Model model, String id) {
        VacKnowledgeSickness info = sicknessService.get(id);
        model.addAttribute("article", info);
        return "knowhow/disease-detail";
    }
    //           妈咪课堂结束


    /**
     * APP预约信息列表
     *
     */
    @RequestMapping(value = "/vacList")
    public @ResponseBody JSONMessage vaccList(String code, String phone,  Model model) {
        ChildAppinfo temp =new ChildAppinfo();
        temp.setPhone(phone);
        temp.setChildcode(code);
        //提示信息列表
        List<VacChildRemind> list = childInfoService.findChildAppList(temp);
        return new JSONMessage(true, list);
    }

    //点击签字
    @RequestMapping(value = "/sign")
    public  @ResponseBody JSONMessage sign(String id, String localcode, String vaccId, Model model) {
        //查询单条提示信息
        VacChildRemind temp=new VacChildRemind();
/*        temp.setVaccId(vaccId);
        temp.setLocalcode(localcode);*/
        temp.setId(id);
        VacChildRemind vr = childInfoService.findVacChildRemind(temp);
        String disContext = "";
        if(vr != null){
            //查询告知书
            CmsDisclosure bsp = vacService.getCmsDisclosureByVacid(vr.getVaccId().substring(0,2)); //vr.getVaccId().substring(0,2) vr.getCode()
            if(bsp != null && com.junl.frame.tools.string.StringUtils.isNotEmpty(bsp.getContext())){
                disContext = bsp.getContext();//.replaceAll("\r\n", "<br>&emsp;&emsp;");
                //disContext=bsp.getContext().replaceAll("<([^>]*)>", "").replaceAll("#([^#]*)#", "");
            }
            vr.setVaccId(vr.getVaccId());
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("VacChildRemind", vr);
            map.put("disContext", disContext);
            return new JSONMessage(true, map);
        }else {
            return new JSONMessage(false, "没有此条预约记录");
        }
    }

    //保存签字
    @RequestMapping(value = "/savesign")
    public @ResponseBody JSONMessage savesign( String id, String vaccId, String localcode, String signatureDataTo) {
        if(com.junl.frame.tools.string.StringUtils.isNotEmpty(id)){
            VacChildRemind temp = new VacChildRemind();
            temp.setId(id);
/*            temp.setNid(vaccId);
            temp.setLocalcode(localcode);
            temp.setSignatureData(signatureDataTo);*/
            //提示签字预约信息
            VacChildRemind vr = childInfoService.findVacChildRemind(temp);
            //List<VacChildRemind> vList = childInfoService.findVacRemindSignList(vr);
            if(com.junl.frame.tools.string.StringUtils.isNotEmpty(temp.getSignatureData())){
                String signatureData = signatureDataTo;
                try {
                    //转换签字内容
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] sign = decoder.decodeBuffer(signatureData);
                    //签字状态
                    if(null != sign && sign.length > 0){
                        vr.setSignature(sign);
                        vr.setStype(VacChildRemind.TEMP_CHILD_STYPE);
                        vr.setVid(vr.getId());
                        vr.setSign(VacChildRemind.TEMP_CHILD_SIGN);
                        vr.setVaccId(vr.getVaccId());
                        //新增签字
                        childInfoService.insertSignature(vr);
                        //更改记录签字状态
                        childInfoService.updateSign(vr);
                        return new JSONMessage(true, "签字成功");
                    }else{
                        return new JSONMessage(false, "签字内容为空2");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new JSONMessage(false, "签字存储异常");
                }
            }else{
                return new JSONMessage(false, "签字内容为空1");
            }
        }
        return new JSONMessage(false, "记录id为空");
    }


    //查看签字
    @RequestMapping(value = "/signimg")
    public  @ResponseBody JSONMessage signimg(String id, String localcode, String vaccId, Model model) {
        //查询单条提示信息
        VacChildRemind temp=new VacChildRemind();
/*        temp.setVaccId(vaccId);
        temp.setLocalcode(localcode);*/
        temp.setId(id);
        VacChildRemind vr = childInfoService.findVacChildRemind(temp);
        String disContext = "";
        if(vr != null){
            CmsDisclosure bsp =new CmsDisclosure();
            if(vr.getVaccId().length()==4){
                bsp= vacService.getCmsDisclosureByVacid(vr.getVaccId().substring(0,2));
            }else if(vr.getVaccId().length()==2){
                String viccid= vacService.getByModelId(vr.getVaccId());
                bsp= vacService.getCmsDisclosureByVacid(viccid.substring(0,2));
            }else {
                logger.error("预约记录错误！");
            }
            //查询告知书
            //CmsDisclosure bsp = vacService.getCmsDisclosureByVacid(temp.getVaccId().substring(0,2)); //vr.getVaccId().substring(0,2) vr.getCode()
            if(bsp != null && com.junl.frame.tools.string.StringUtils.isNotEmpty(bsp.getContext())){
                disContext = bsp.getContext();//.replaceAll("\r\n", "<br>&emsp;&emsp;");
                //disContext=bsp.getContext().replaceAll("<([^>]*)>", "").replaceAll("#([^#]*)#", "");
            }
            vr.setVaccId(vr.getVaccId());
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("VacChildRemind", vr);
            map.put("disContext", disContext);
            //VacChildRemind temp = childInfoService.findVacChildRemind(tm);
            ServletOutputStream os = null;
            try {
                if(null != vr && com.junl.frame.tools.string.StringUtils.isNotEmpty(vr.getId())){
                    vr.setVaccId(vr.getVaccId());
                    VacChildRemind vacRemind = childInfoService.findVacChildRemindById(vr);
                    if(vacRemind!=null){
                        byte[] stgn = vacRemind.getSignature();
                        map.put("stgn", stgn);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(null != os){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new JSONMessage(true, map);
        }else {
            return new JSONMessage(false, "没有此条预约记录");
        }
    }

    //服务通知列表
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

    //接种单位信息
    @RequestMapping(value = "/departmentInfo")
    public @ResponseBody JSONMessage departmentInfo( String childcode) {
        String localcode= vacService.getBsChildBaseInfoByCode(childcode).getOfficeinfo();
        List<DepartmentInfo> list= departmentInfoService.findByChildcode(localcode);
        return new JSONMessage(true, list.get(0));
    }

    //推广消息
    @RequestMapping(value = "/promotMessage")
    public @ResponseBody JSONMessage promotMessage( ) {
        List<PromotMessage>  list=  notificationService.findByMessage();
        return  new JSONMessage(true,list);

    }
    //推广消息详情
    @RequestMapping(value = "/detail")
    public  String  detail( Model model ,String id) {
        PromotMessage  message=  notificationService.findById(id);
        model.addAttribute("article", message);
        return "cms/detail";
    }

    //一般信息采集接口
    @RequestMapping(value = "/inoculationReply")
    public @ResponseBody JSONMessage inoculationReply( String childcode) {
        List<BsRecord> listTemp = vacService.getRecordFinish(childcode);
        for(int i=0;i<listTemp.size();i++){
            Map<String, String> map = new HashMap<>();
            map.put("childcode", listTemp.get(i).getChildcode());
            map.put("nid", listTemp.get(i).getNid());
            List<Response> listRes=  vacService.getByChildcode(map);
            if (listRes.size()>0){
                if(!"".equals(listRes.get(0).getReplycontent())){
                    listTemp.get(i).setReplyStatus("2");
                }else {
                    listTemp.get(i).setReplyStatus("1");
                }
            }else {
                listTemp.get(i).setReplyStatus("0");
            }
        }
        return new JSONMessage(true, listTemp);
    }

    //不良反应保存接口
    @RequestMapping(value = "/reactionInst")
    public @ResponseBody JSONMessage reactionInst( String nid,String childcode ,String id ,String submitMage ,String submitText ) {
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

    //不良反应显示接口
    @RequestMapping(value = "/reactionShow")
    public @ResponseBody JSONMessage reactionShow( String nid,String childcode ) {
        Map<String, String> map = new HashMap<>();
        map.put("childcode", childcode);
        map.put("nid", nid);
        List<Response> listRes=  vacService.getByChildcode(map);
        if(listRes.size()>0){
            return new JSONMessage(true, listRes);
        }else {
            return new JSONMessage(false, "没有记录！");
        }
    }


    /**
     * 预约 id  localcode  childcode
     */
    @RequestMapping(value = "/reservaTion")
    public @ResponseBody JSONMessage reservaTion(String id,  String localcode,  String childcode) throws Exception {//id
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        ParsePosition pos = new ParsePosition(0);
        VacChildRemind temp =new VacChildRemind();
        temp.setId(id);
        temp.setLocalcode(localcode);
        temp.setChildcode(childcode);
        List<VacChildRemind> vr = childInfoService.findRemindById(temp);//此儿童的预约信息
        Map<String,Object> map=new HashMap<>();
        List<String> selectDate =new ArrayList();
        Date  date = vr.get(0).getRemindDate();
        selectDate.add(sdf.format(date));
        for(int i=0;i<5;i++){
            SimpleModel sim=new SimpleModel();
            Date next=sysService.nextWorkDay(date,vr.get(0).getLocalcode());
            selectDate.add(sdf.format(next));
            date=next;
        }
        map.put("date",selectDate);
        map.put("localname",vr.get(0).getLocalname());
        map.put("selectDate", sdf.format(vr.get(0).getSelectDate()));
        return new JSONMessage(true,map);
    }

    /**
     * 预约列表
     */
    @RequestMapping(value = "/remindTable" )
    public @ResponseBody JSONMessage remindTable(String localcode,String selectDate,String id ) { //预约日期 //接种单位 //id
        String week= DateTools.dateToWeek(selectDate);
        WorkingHours work=new WorkingHours();
        work.setLocalcode(localcode);
        work.setWeek(week);
        List<WorkingHours> workList=workService.findByLocalcode(work);
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
       List<VacChildRemind>  vr = childInfoService.findRemindById(temp1);//此id的预约信息
        List<VacChildRemind>  vr1=  childInfoService.findRemindById(temp);//接种单位、日期
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
     */
    @RequestMapping(value = "/updateByTime" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage updateByTime( String selectTime ,String selectDate,String id ) {
        VacChildRemind temp =new VacChildRemind();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(selectDate, pos);
       temp.setSelectDate(currentTime_2);
       temp.setSelectTime(selectTime);
       temp.setId(id);
        int i= childInfoService.updateByTime(temp);
        if(i==1){
            return  new JSONMessage(true, "修改预约成功");
        }else {
            return  new JSONMessage(false, "预约的错误");
        }
    }
    //点击立即支付按钮需要的数据 //儿童编号   id
    @RequestMapping(value = "/beforePay" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage beforePay( String childcode ,String id ) {
        VacChildRemind temp =new VacChildRemind();
        temp.setId(id);
        //temp.setChildcode(childcode);
        VacChildRemind vr = childInfoService.findRemindById(temp).get(0); //此儿童的预约信息
        BsChildBaseInfo bsChild = vacService.getBsChildBaseInfoByCode(vr.getChildcode()); //通过儿童编号查询儿童信息
        Map<String,Object> map=new HashMap<>();
//        map.put("childRemind",vr);
//        map.put("childBase",bsChild);
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        map.put("codeNum",vr.getChildcode()); //  儿童编号
        map.put("name",bsChild.getChildname()); //  儿童姓名
        map.put("sex",bsChild.getGender()); //  性别
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

    @RequestMapping(value = "/payInfo" , method= RequestMethod.POST) //payType 1:支付宝 2:微信
    public @ResponseBody JSONMessage payInfo( String id ,String totalPrice ,String insurancePrice ,String insuranceId, String type ,String userId, HttpServletRequest request ) {
        totalPrice="0.01";
        VacChildRemind temp =new VacChildRemind();
        temp.setId(id);
        VacChildRemind vr = childInfoService.findRemindById(temp).get(0); //此儿童的预约信息
        BsChildBaseInfo bsChild = vacService.getBsChildBaseInfoByCode(vr.getChildcode()); //通过儿童编号查询儿童信息
        Order order=new Order();
        order.setVaccId(vr.getVaccId());
        order.setLocalcode(vr.getLocalcode());
        order.setChildCode(bsChild.getChildcode());
        List<Order> list= orderService.findOrderList(order);
        String orderNo=OrderUtil.getOrderId();
        if(list.size()==0){
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
        }else {
            Order or=new Order();
            or.setId(list.get(0).getId());
            or.setPayType(type);
            int i=orderService.updateOrderpayType(or);
            orderNo=list.get(0).getOrderNo();
        }
       if("0".equals(type)) {
           String str = Alipay.AlipayClient(vr.getRemindVacc(),orderNo,totalPrice,conf.getAliPayUrl());
           Map map = new HashMap();
           map.put("Alipay",str);
           return  new JSONMessage(true, map);
       }else if("1".equals(type)){
           JSONObject wenxin = weixinPrePay.createSignAgain(vr.getRemindVacc(),orderNo,totalPrice,conf.getWeixinPayUrl(),"APP",request);
           return  new JSONMessage(true, wenxin);
       }else {
           return  new JSONMessage(true, "支付类型错误！");
       }
    }

    //更新版本接口
    @RequestMapping(value = "/replaceVersion" , method= RequestMethod.POST) //
    public @ResponseBody JSONMessage replaceVersion(String version){
        Map<String,Object> map=new HashMap<>();
        map.put("version","1.0.0");
        map.put("apk_url","http://www.chinavacc.cn/apk/zhihuijiezhong.apk");
        return  new JSONMessage(true, map);
    }

    //支付 回调 //
    @RequestMapping(value = "/PayRequest")
    public  void aliPayRequest(  String payType,String orderNo) {//payType 1:支付宝 2:微信
        Order order=new Order();
        order.setOrderNo(orderNo);
        List<Order> list= orderService.findOrderList(order);
        if(list.size()<1){
            logger.info("订单号错误");
            logger.error("");
            logger.debug("");
            logger.warn("");
        }else {
            Order or=new Order();
            or.setId(list.get(0).getId());
            or.setStatus("2"); // TransactionNo
            or.setCallbackTime(new Date());
            orderService.updateOrderStuatus(or);
            VacChildRemind entity=new VacChildRemind();
            entity.setId(list.get(0).getVid());
            entity.setPayStatus("1");
            childInfoService.updatePayStatus(entity);
            //模板消息/激光推送 订单支付成
        }
    }

    @RequestMapping(value = "/headImgUpload")
    public  String  headImgUpload() {
        return "app/text";
    }
    /*
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> save(MultipartFile file)throws Exception {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("success", true);
        value.put("errorCode", 0);
        value.put("errorMsg", "");
        try {
            OSSClientUtil ossClient=new OSSClientUtil();
            String name = ossClient.uploadImg2Oss(file);
            String head = ossClient.getImgUrl(name);
            value.put("data", head);
        } catch (IOException e) {
            e.printStackTrace();
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "图片上传失败");
        }
        return value;
    }*/


    /**
     *自助建档保存数据
     * userId
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
            return new JSONMessage(false, "头文件参数错误");
        }

        code = StringEscapeUtils.unescapeHtml(code); //
        try {
            ChildTemp childTemp = (ChildTemp) JsonMapper.fromJsonString(code, ChildTemp.class);
            childTemp.setFileorigin("3");
            childTemp.setRegistrationID(registrationID);
            childTemp= childTempService.childSava(childTemp);
            childTemp = vacService.updateChildTemp(childTemp); //更新详细页面区域划分、户口类型、居住类型、出生医院信息
            List<String> list=new ArrayList<>();
            list.add(registrationID);
            jpushMessageService.sendToRegistrationId(list,"自助建档成功","编码【"+childTemp.getChildcode()+"】有效期三天,请及时前往接种单位完成建档",childTemp.getId(),"06");
            Map map =new HashMap();
            map.put("id",childTemp.getId());
            return  new JSONMessage(true, map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
