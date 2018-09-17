/**
 * 
 * @author:wufang
 * @time: 2018年2月26日10:54:13
 */
var privinces = remote_ip_info["province"]+"省";
var citys =remote_ip_info["city"]+"市"; 
/*var areas = '';*/
var vm = {};

//微信地理位置定位

/*function getLocation(){
		$.ajax({
			url: ctx+"/child/getSign.do",
			type: 'get',
			dataType: 'json',
			success: function(data){
		
				wx.config({    
				    debug: false, 
				    appId: data.appid, 
				    timestamp: data.timestamp,     
				    nonceStr: data.nonceStr,   
				    signature: data.signature,  
				    jsApiList: ['checkJsApi',  
				                'chooseImage',  
				                'previewImage',  
				                 'uploadImage',  
				                 'downloadImage',  
				                  'getNetworkType',
				                  'openLocation',
				                  'getLocation'  
				               ] //   
				});
		 
				wx.getLocation({  
			      success: function (res) {  
			        //alert(JSON.stringify(res));  
			        $.ajax({
			        	url:"http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+res.latitude+","+res.longitude+"&output=json&ak=5ZCba4kItMwxnnt3aqqWrEuLzPBuQ6G1",
			        	type:"get",
			        	dataType: "jsonp",
			        	success: function(data){
			        		privinces = data.result.addressComponent.province;
			        		citys = data.result.addressComponent.city;
			        		areas = data.result.addressComponent.district;
			        		vm.eventController.areaSelect1();
			                
			        		var a = {
			        				privince: privince,
			        				city: city,
			        				area: area
			        		};
			        		 return a;
			        		
			        		 
			        	}
			        })
			        
			      }, 
			      error:function(res) {
			    	  vm.eventController.areaSelect1();
			      },
			      cancel: function (res) {  
			        alert('用户拒绝授权获取地理位置');  
			      }  
		    	}); 
				
			}
		});
	}*/
$(function() {

    //定义vm
    

    //定义全局变量
    var GLOBAL = {
        dataSubmit:{},   	//儿童建档数据
        dataRabiesSubmit:{},  		//犬伤建档数据
        dataHbsSubmit:{} 		//成人建档数据
    };

    //数据接口层
    var dao = {
    		
    }

    //事件绑定
    vm.addEvent = function() {
        $('body')
            .on('click', '.sex-radio', vm.eventController.radioSelect)   
            .on('click', '.baby-next', vm.eventController.babyNext) 
            .on('click', '.detail-next', vm.eventController.nextDetail)
            .on('click', '.backChildInfo', vm.eventController.backChildInfo)
            .on('click', '.backcustodianInfoBaby', vm.eventController.backcustodianInfoBaby)
            .on('click', '.detail-submit', vm.eventController.dataSubmit)
            .on('click', '.rabies-next', vm.eventController.rabiesNext)
            .on('click', '.backcustodianInfoRabies', vm.eventController.backcustodianInfoRabies)
            .on('click', '.backcustodianInfoHbs', vm.eventController.backcustodianInfoHbs)
            .on('click', '.rabies-submit', vm.eventController.rabiesSubmit)
            .on('click', '.hbs-next', vm.eventController.hbsNext)
            .on('click', '.hbs-submit', vm.eventController.hbsSubmit)
    };

    //事件处理
    vm.eventController = {

        //回菜单
    	radioSelect: function() {
    		$(".sex-radio").find("i").removeClass("selected");
			$(this).find("i").addClass("selected");
			$(".hide").attr("checked",false);
			$("#"+$(this).attr("data-id")).prop("checked","checked");
        },
        babyNext: function() {
        	
        	var babyName=$('input[name=babyName]').val();   //跳转到监护人信息
			var sex=$(".sex-group .selected").attr("data-sex");
			var sexLen=$(".sex-group .selected").length;
			var birthday=$("#birthday").val();
			var gravida=$("#gravida").val();
			var weight=$("#weight").val();
			var nation=$("#nation").val();
			var babyCardId=$("#babyCardId").val();
			var hospital=$("#hospital").val();
			var reaction=$("#reaction").val();
			var childIdCard=$("#childIdCard").val();
			if(babyName=="") {
				layer.msg('请输入宝宝姓名', {icon: 0});
				return false;
			 } else {
			  if(vm.eventController.isChinaName(babyName) == false) {
				  layer.msg("请输入正确姓名",{icon: 0});
				  return false;
			  }
			 }
			if(sexLen==0) {
				layer.msg("请选择宝宝性别",{icon: 0});
				return false;
			}
			if(birthday=='请选择...') {
				layer.msg("请选择宝宝生日",{icon: 0});
				return false;
			}
			if(gravida=='') {
				layer.msg("请输入宝宝胎次",{icon: 0});
				return false;
			}
			else {
				if(gravida<=0||gravida>9) {
					layer.msg("请输入正确胎次",{icon: 0});
					return false;
					
				}
			}
			if(weight=='') {
				layer.msg("请输入宝宝出生体重",{icon: 0});
				return false;
			}else {
				if(weight<2000||weight>8000) {
					layer.msg("请输入正确体重",{icon: 0});
					return false;
				}
			}
			if(childIdCard!='') {
				if(vm.eventController.isCardNo(childIdCard) == true) {
					/*layer.msg("请输入宝宝正确身份证号");
					return false;*/
					if(vm.eventController.exactCard(childIdCard)!=true){
						layer.msg("请输入宝宝正确身份证号",{icon: 0});
						return false;
					}
				 }
				else {
					layer.msg("请输入宝宝正确身份证号",{icon: 0});
					return false;
				}
				
			}
			GLOBAL.dataSubmit={
				"childname":babyName,
				"gender":sex,
				"birthday":birthday,
				"childorder":gravida,
				"birthweight":weight,
				"nation":nation,
				"birthcode":babyCardId,
				"birthhostipal":hospital,
				"paradoxicalreaction":reaction,
				"cardcode":childIdCard,
				"babyprovince":$("#babyprovince").val(),
				"babycity":$("#babycity").val(),
				"babycounty":$("#babycounty").val(),
				
			}
			/*mui('body').progressbar({
                progress: 66.6
            }).show();*/	
			$(".progressbar li:eq(1)").addClass("active");
			/*var step = $("#myStep").step();
			step.nextStep();*/
        	$(".babyInfo").addClass('hide');
			$(".custodianInfo").removeClass('hide');
			document.title="监护人信息";
        },
        nextDetail: function() {       //跳转到地址信息
        	
        	var motherName=$("#motherName").val();
        	var motherIdCard=$("#motherIdCard").val();
        	var motherPhoneNum=$("#motherPhoneNum").val();
        	
        	var fatherName=$("#fatherName").val();
        	var fatherIdCard=$("#fatherIdCard").val();
        	var fatherPhoneNum=$("#fatherPhoneNum").val();
        	
        	if(motherName=="") {
				layer.msg("请输入母亲姓名",{icon: 0});
				return false;
			 } else {
			  if(vm.eventController.isChinaName(motherName) == false) {
				  layer.msg("请输入母亲正确姓名",{icon: 0});
				  return false;
			  }
			 }
			if(motherIdCard=='') {
				layer.msg("请输入母亲身份证号",{icon: 0});
				return false;
			}else {
				if(vm.eventController.isCardNo(motherIdCard) == true) {
					/*layer.msg("请输入宝宝正确身份证号");
					return false;*/
					if(vm.eventController.exactCard(motherIdCard)!=true){
						layer.msg("请输入母亲正确身份证号",{icon: 0});
						return false;
					}
				 }
				else {
					layer.msg("请输入母亲正确身份证号",{icon: 0});
					return false;
				}
			}
			if(motherPhoneNum=='') {
				layer.msg("请输入母亲电话",{icon: 0});
				return false;
			}else {
				if(vm.eventController.isPhoneNo(motherPhoneNum) == false) {
					layer.msg("请输入母亲正确电话",{icon: 0});
					return false;
				  }
			}
			
			if(fatherName!='') {
				if(vm.eventController.isChinaName(fatherName) == false) {
					  layer.msg("请输入父亲正确姓名",{icon: 0});
					  return false;
				 }
			}
			if(fatherIdCard!='') {
				if(vm.eventController.isCardNo(fatherIdCard) == true) {
					/*layer.msg("请输入宝宝正确身份证号");
					return false;*/
					if(vm.eventController.exactCard(fatherIdCard)!=true){
						layer.msg("请输入父亲正确身份证号",{icon: 0});
						return false;
					}
				 }
				else {
					layer.msg("请输入父亲正确身份证号",{icon: 0});
					return false;
				}
			}
			if(fatherPhoneNum!='') {
				if(vm.eventController.isPhoneNo(fatherPhoneNum) == false) {
					layer.msg("请输入父亲正确电话",{icon: 0});
					return false;
				  }
			}
			/*mui('body').progressbar({
                progress: 100
            }).show();*/
			$(".progressbar li:eq(2)").addClass("active");
			GLOBAL.dataSubmit.guardianname=motherName;
			GLOBAL.dataSubmit.guardianidentificationnumber=motherIdCard;
			GLOBAL.dataSubmit.guardianmobile=motherPhoneNum;
			GLOBAL.dataSubmit.father=$("#fatherName").val();
			GLOBAL.dataSubmit.fathercard=$("#fatherIdCard").val();
			GLOBAL.dataSubmit.fatherphone=$("#fatherPhoneNum").val();
        	$(".custodianInfo").addClass('hide');
			$(".addressInfo").removeClass('hide');
			document.title="地址信息";
			vm.eventController.areaSelect2();
            vm.eventController.areaSelect3();
			
        },
        backChildInfo: function() {
        	/*mui('body').progressbar({
                progress: 33.3
            }).show();	*/				//返回宝宝信息
        	$(".babyInfo").removeClass('hide');
			$(".custodianInfo").addClass('hide');
			document.title="宝宝信息";
			$(".progressbar li:eq(1)").removeClass("active");
        },
        backcustodianInfoHbs:function() {
        	/*mui('body').progressbar({
                progress: 50
            }).show();	*/
        	$('.progressbar-hbs li:eq(1)').removeClass("active");
        	$(".selfInfo").addClass('hide');
			$(".babyInfo").removeClass('hide');
        },
        backcustodianInfoBaby: function() {
        	/*mui('body').progressbar({
                progress: 66.6
            }).show();		*/			//返回监护人信息
        	$(".custodianInfo").removeClass('hide');
			$(".addressInfo").addClass('hide');
			document.title="监护人信息";
			$(".progressbar li:eq(2)").removeClass("active");
        },
        
        backcustodianInfoRabies:function() {   //犬伤返回
        	/*mui('body').progressbar({
                progress: 50
            }).show();*/
        	$('.progressbar-rabies li:eq(1)').removeClass("active");
			$(".bodyInfo").removeClass('hide');
			$(".cutInfo").addClass('hide');
			document.title="个人信息";
        },
        dataSubmit: function() {       //建档提交
        	var htlb=$("#htlb").val();
        	var jzlb=$("#jzlb").val();
        	var province=$("#province").val();
        	var city=$("#city").val();
        	var county=$("#county").val();
        	var address=$("#address").val();
        	var add=$("#add").val();
        	var pr=$("#pr").val();
        	var ci=$("#ci").val();
        	var co=$("#co").val();
        	var remarks=$("#remarks").val();
        	if(address=="") {
        		layer.msg("请输入家庭详细地址",{icon: 0});
				return false;
        	}
        	GLOBAL.dataSubmit.properties=htlb;
        	GLOBAL.dataSubmit.reside=jzlb;
        	GLOBAL.dataSubmit.province=province;
        	GLOBAL.dataSubmit.city=city;
        	GLOBAL.dataSubmit.county=county;
        	GLOBAL.dataSubmit.address=address;
        	GLOBAL.dataSubmit.pr=pr;
        	GLOBAL.dataSubmit.ci=ci;
        	GLOBAL.dataSubmit.co=co;
        	GLOBAL.dataSubmit.add=add;
        	GLOBAL.dataSubmit.remarks=remarks;
        	$.ajax({
        		url:ctx+"child/temp/save.do",
                type: 'POST',
                dataType: 'json',
                data:GLOBAL.dataSubmit,
                success:function(data) {
                	layer.msg("儿童建档成功",{icon: 1});
                	window.location.href=ctx+"child/temp/detail.do?childcode="+data.data.id;
                }
        	})
        },
        isChinaName:function(name) {         //验证姓名
        	var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
        	return pattern.test(name);
        },
        isCardNo:function(card) {            //验证身份证号
        	var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
        	return pattern.test(card);
        },
        exactCard:function(idCard) {    //身份证号准确性验证
        	if(idCard.length==18){  
                var idCardWi=new Array( 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ); 
                var idCardY=new Array( 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ); 
                var idCardWiSum=0; 
                for(var i=0;i<17;i++){  
                    idCardWiSum+=idCard.substring(i,i+1)*idCardWi[i];  
                }  
      
                var idCardMod=idCardWiSum%11;
                var idCardLast=idCard.substring(17);
      
                if(idCardMod==2){  
                    if(idCardLast=="X"||idCardLast=="x"){  
                        return true;
                    }else{  
                        return false;
                    }  
                }else{  
                    //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码  
                    if(idCardLast==idCardY[idCardMod]){  
                    	return true;
                    }else{  
                    	return false;
                    }  
                }   
	        }
        	else{  
	        	return false; 
	        }  
        },
        isPhoneNo:function(phone) {          //验证手机号
        	var pattern = /^1[34578]\d{9}$/; 
        	return pattern.test(phone);
        },
        rabiesNext:function() {   //犬伤下一步
        	
        	var weight=$("#weight").val();
			var address=$("#address").val();
			var userName=$("#username").val();
			var sex=$(".sex-group .selected").attr("data-sex");
			var sexLen=$(".sex-group .selected").length;
			var phoneNum=$("#phoneNum").val();
			var age=$("#age").val();
			var province=$("#province").val();
			var city=$("#city").val();
			var county=$("#county").val();
			if(userName=="") {
				layer.msg("请输入姓名",{icon: 0});
				return false;
			 } else {
			  if(vm.eventController.isChinaName(userName) == false) {
				  layer.msg("请输入正确姓名",{icon: 0});
				  return false;
			  }
			 }
			if(sexLen==0) {
				layer.msg("请选择性别",{icon: 0});
				return false;
			}
			if(age=="") {
				layer.msg("请输入年龄",{icon: 0});
				return false;
			}
			
			if(phoneNum=='') {
				layer.msg("联系电话不能为空",{icon: 0});
				return false;
			}else {
				if(vm.eventController.isPhoneNo(phoneNum) == false) {
					layer.msg("请输入正确电话",{icon: 0});
					return false;
				  }
			}
			if(address=="") {
				layer.msg("请输入详细居住地址",{icon: 0});
				return false;
			}
			GLOBAL.dataRabiesSubmit={
					"username":userName,
					"sex":sex,
					"age":age,
					"linkphone":phoneNum,
					"weight":weight,
					"province":province,
					"city":city,
					"county":county,
					"address":address
				}
			/*mui('body').progressbar({
                progress:100
            }).show();*/
			$('.progressbar-rabies li:eq(1)').addClass("active");
			$(".bodyInfo").addClass('hide');
			$(".cutInfo").removeClass('hide');
			document.title="伤口信息";
        },
        rabiesSubmit:function() {   //犬伤提交
        	var biteTime=$("#time").val();
        	var animal=$("#animal").val();
        	var bitetype=$("#bitetype").val();
        	var bitepart=$("#bitepart").val();
        	var complTime=$("#complTime").val();
        	var dealaddress=$("#dealaddress").val();
        	if(biteTime=='请选择...') {
				layer.msg("请选择咬伤时间");
				return false;
			}
        	if(complTime=='请选择...') {
				layer.msg("请选择伤口处理时间");
				return false;
			}
        	GLOBAL.dataRabiesSubmit.bitedate=biteTime;
        	GLOBAL.dataRabiesSubmit.animal=animal;
        	GLOBAL.dataRabiesSubmit.bitetype=bitetype;
        	GLOBAL.dataRabiesSubmit.bitepart=bitepart;
        	GLOBAL.dataRabiesSubmit.dealdate=complTime;
        	GLOBAL.dataRabiesSubmit.dealaddress=dealaddress;
        	$.ajax({
        		url:ctx+"rabies/save.do",
                type: 'POST',
                dataType: 'json',
                data:GLOBAL.dataRabiesSubmit,
                success:function(data) {
                	layer.msg("犬伤建档成功",{icon: 1});
                    window.location.href=ctx+data.msg;
                }
        	})
        },
        hbsNext:function() {		//成人建档下一步
        	var userName=$("#username").val();
        	var sex=$(".sex-group .selected").attr("data-sex");
			var sexLen=$(".sex-group .selected").length;
			var idcardNo=$("#idcardNo").val();
			var birthday=$("#birthday").val();
			var age=$("#age").val();
			var linkPhone=$("#linkPhone").val();
			if(userName=="") {
				layer.msg("请输入宝宝姓名",{icon: 0});
				return false;
			 } else {
			  if(vm.eventController.isChinaName(userName) == false) {
				  layer.msg("请输入正确宝宝姓名",{icon: 0});
				  return false;
			  }
			 }
			if(sexLen==0) {
				layer.msg("请选择性别",{icon: 0});
				return false;
			}
			if(idcardNo!='') {
				if(vm.eventController.isCardNo(idcardNo) == true) {
					/*layer.msg("请输入宝宝正确身份证号");
					return false;*/
					if(vm.eventController.exactCard(idcardNo)!=true){
						layer.msg("请输入正确身份证号",{icon: 0});
						return false;
					}
				 }
				else {
					layer.msg("请输入正确身份证号",{icon: 0});
					return false;
				}
			}
			if(age=="") {
				layer.msg("请填写年龄");
				return false;
			}else {
				  if(age<0||age>120) {
					  layer.msg("请输入正确的年龄",{icon: 0});
					  $("#age").val("");
					  return false;
				  }
			}
			if(linkPhone=='') {
				layer.msg("联系电话不能为空",{icon: 0});
				return false;
			}else {
				if(vm.eventController.isPhoneNo(linkPhone) == false) {
					layer.msg("请输入正确联系电话",{icon: 0});
					return false;
				  }
			}
			GLOBAL.dataHbsSubmit={
					"username":userName,
					"sex":sex,
					"idcardNo":idcardNo,
					"birthday":birthday,
					"age":age,
					"linkPhone":linkPhone,
				}
        	/*mui('body').progressbar({
	            progress: 100
	        }).show();*/
			$('.progressbar-hbs li:eq(1)').addClass("active");
        	$(".babyInfo").addClass('hide');
			$(".selfInfo").removeClass('hide');
        },
        hbsSubmit:function() {      //成人提交
        	var province=$("#province").val();
			var city=$("#city").val();
			var county=$("#county").val();
			var address=$("#address").val();
			var history=$("#history").val();
			if(address=="") {
				layer.msg("请输入详细居住地址",{icon: 0});
				return false;
			}
			GLOBAL.dataHbsSubmit.province=province;
			GLOBAL.dataHbsSubmit.city=city;
			GLOBAL.dataHbsSubmit.county=county;
			GLOBAL.dataHbsSubmit.address=address;
			GLOBAL.dataHbsSubmit.history=history;
			$.ajax({
        		url:ctx+"rabies/hepbSave.do",
                type: 'POST',
                dataType: 'json',
                data:GLOBAL.dataHbsSubmit,
                success:function(data) {
                	layer.msg("成人建档成功",{icon: 1});
                    window.location.href=ctx+data.msg;
                }
        	})
        },
        	areaSelect1: function() {
        	
        	//请求省级数据
			$.ajax({
				url:ctx+"child/area/0.do",
				success:function(data){          
					if(data){
	                    var html = '';
						for(var i = 0; i < data.length; i ++){
	                         html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						$(".one1").each(function(){
							$(this).html(html);
							var options = $(this).find("option[value='340000']");
					    	/*options.attr("selected", true);
					    	$(this).val("340000");*/
							//getLocation();
							$(this).find("option").each(function(i,item){
								if($(item).text() == privinces){
									$(item).attr("selected", true)
								}
							});
					    	$(this).change();
						});
					}
				}
			});
			
			$(".one1").change(function(){
				
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						
						$(".two1").each(function(){
							$(this).html(html);
							/*var options = $(this).find("option[value='340600']");
					    	options.attr("selected", true);*/
							//getLocation();
							$(this).find("option").each(function(i,item){
								if($(item).text() == citys){
									$(item).attr("selected", true)
								}
							});
					    	$(this).change();
						});
							
						}
					}
				});
			})
			
			$(".two1").change(function(){
				$.ajax({
					url:ctx+"api/hostipallist.do",
					data: {code:$(this).val()},
					success:function(data){
						var data=data.data;
						if(data){
	                        var html = "";
	                        
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].CODE + "'>" + data[i].NAME + "</option>"
							}
							$("#hospital").html('');
							$("#hospital").each(function(){
								$(this).html(html);
								var options = $(this).find("option[value='340603']");
						    	options.attr("selected", true);
						    	$("#hospital").attr("disabled",false);
							});
						}
						else {
							$("#hospital").html('<option value="">暂无医院</option>');
							$("#hospital").attr("disabled",true);
						}
					}
				});
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
							}
							$(".three1").each(function(){
								$(this).html(html);
							});
						}
					}
				});
			});
			
			/*$(".three1").change(function(){
				$.ajax({
					url:ctx+"api/hostipallist.do",
					data: {code:$(this).val()},
					success:function(data){
						var data=data.data;
						if(data){
	                        var html = "";
	                        
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].CODE + "'>" + data[i].NAME + "</option>"
							}
							$("#hospital").html('');
							$("#hospital").each(function(){
								$(this).html(html);
								var options = $(this).find("option[value='340603']");
						    	options.attr("selected", true);
						    	$("#hospital").attr("disabled",false);
								
							});
							
						}
						else {
							$("#hospital").html('<option value="">暂无医院</option>');
							$("#hospital").attr("disabled",true);
						}
					}
				});
			})*/
        },
        areaSelect2: function() {
        	
        	//请求省级数据
			$.ajax({
				url:ctx+"child/area/0.do",
				success:function(data){          
					if(data){
	                    var html = '';
						for(var i = 0; i < data.length; i ++){
	                         html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						$(".one2").each(function(){
							$(this).html(html);
							var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babyprovince+"']");
					    	options.attr("selected", true);
					    	$(this).change();
						});
					}
				}
			});
			
			$(".one2").change(function(){
				
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						
						$(".two2").each(function(){
							$(this).html(html);
							var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babycity+"']");
					    	options.attr("selected", true);
					    	$(this).change();
						});
							
						}
					}
				});
			})
			
			$(".two2").change(function(){
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
							}
							$(".three2").each(function(){
								$(this).html(html);
								var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babycounty+"']");
						    	options.attr("selected", true);
							});
						}
					}
				});
			});
			
        },

        areaSelect3: function() {
        	
        	//请求省级数据
			$.ajax({
				url:ctx+"child/area/0.do",
				success:function(data){          
					if(data){
	                    var html = '';
						for(var i = 0; i < data.length; i ++){
	                         html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						$(".one3").each(function(){
							$(this).html(html);
							var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babyprovince+"']");
					    	options.attr("selected", true);
					    	$(this).change();
						});
					}
				}
			});
			
			$(".one3").change(function(){
				
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						
						$(".two3").each(function(){
							$(this).html(html);
							var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babycity+"']");
					    	options.attr("selected", true);
					    	$(this).change();
						});
							
						}
					}
				});
			})
			
			$(".two3").change(function(){
				$.ajax({
					url:ctx+"child/area/" + $(this).val() + ".do",
					success:function(data){
						if(data){
	                        var html = "";
							for(var i = 0; i < data.length; i ++){
	                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
							}
							$(".three3").each(function(){
								$(this).html(html);
								var options = $(this).find("option[value='"+GLOBAL.dataSubmit.babycounty+"']");
						    	options.attr("selected", true);
							});
						}
					}
				});
			});
			
        }

    };

    //初始化函数
    vm.init = function() {
        //加载首页
        vm.addEvent();
        vm.eventController.areaSelect1()
        
    };

    //执行函数
    /*getLocation();*/
    vm.init();
    
   
})
