<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">  
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<title>预约详情列表</title>
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css" rel="stylesheet" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js" ></script>

</head>
<style>
    *{font-family: "微软雅黑"!important;} body{width: 100%; font-size: 14px!important; background: #fafafa; margin: 0; padding: 15px; } .whitebg{background: #FFFFFF; border-radius: 6px; box-shadow: 0 4px 6px #eaeaea; padding: 15px 0; font-size: 1.1em; color: #000; margin-bottom: 15px; } .ctn-box{ padding: 0 15px;} .order-list tr td , .order-list tr th{border-top: 0!important; text-align: center; } .table{ margin-bottom: 0;} .s700{ font-weight: 700;} .w88{ width: 88px;} .w100{ width: 100px;} .mr20{ margin-right: 20px;} .pd0{ padding: 8px 0!important;} .pdtop15{ padding-top: 15px;} .pdbot15{ padding-bottom: 15px;} .progress{ margin-bottom: 0;} .progress-bar{ background-color: #64d5d7; float: right;} .select-group{width: 100%; height: 51px; position: relative; } .select-group label{ line-height: 36px;} .select-style{height: 36px; line-height: 36px; border-radius: 6px; width: 70%; min-width: 118px; position: absolute; right: 0; padding-left: 8px; background: transparent; } .btn-style{font-size: 1.2em; background-color: #64d5d7; color: #fff; border: 0; border-radius: 18px; padding: 4px 20px; }.table > tbody + tbody{border-top: 0;}
</style>

<body>
<div class="bodybg">
    <div class="whitebg">
        <div class="ctn-box">
            <table class="table order-list">
                <tr>
                    <th colspan="3" style="font-size: 1.2em;" ><span id="date_yyr"> </span>预约情况</th>
                </tr>
                <tr>
                    <th class="w88">预约时段</th>
                    <th class="w100">预约人数</th>
                    <th>剩余名额</th>
                </tr>
                <tbody id="remind">

                </tbody>
            </table>
        </div>
        <div style="width: 100%; border-bottom: 1px solid #f1f1f1;"></div>
        <div class="ctn-box text-right pdtop15 s700" id="total" ></div>
    </div>
    <div class="whitebg">
        <div class="ctn-box">
            <div class="pdbot15 s700">您当前已预约&nbsp;&nbsp;<span id="date_rq"></span>&nbsp;&nbsp;<span id="date_sj"></span> </div>
            <div class="select-group pdbot15">
                <label>预约日期：</label>
                <select id="selectDate" class="select-style">
                    <c:forEach var="data" items="${selectDate}" varStatus="loop">
                        <option value="${data.str2}"> ${data.str2}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="select-group pdbot15">
                <label>预约时段：</label>
                <select id="selectTime" class="select-style">
                    <%--<c:forEach var="data" items="${selectTime}" varStatus="loop">
                        <option value="${data.str2}"> ${data.str1}</option>
                    </c:forEach>--%>
                </select>
            </div>
        </div>
        <div style="width: 100%; border-bottom: 1px solid #f1f1f1;"></div>
        <div class="ctn-box text-right pdtop15">
            <input type="submit" class="btn-style mr20"  id= "btnSave" value="确认预约"/>
            <input type="button" class="btn-style" onclick="self.location=document.referrer;" value="返回"/>
        </div>
    </div>
</div>
</body>
</html>

<script type="text/javascript">
    $(function () {
        var  vr=${vr};

        var str=vr[0].selectTime;
        if(""==str){str="a";}
        $("#selectTime").val(str);
        var date=vr[0].remindVacc;
        $("#selectDate").val( date);
        $("#date_yyr").text(date);
        $("#date_rq").text(date);
        $("#date_sj").text(str);
        var selectDate =  $("#selectDate").val();
        var localcode= vr[0].localcode;
        _searchData(selectDate,localcode);
        $("#btnSave").click(function () {
            var id=vr[0].id;
            var selec =  $("#selectDate").val();
            var selectTime  = $("#selectTime").val();
            var refoundBean = {
                "selectTime":selectTime,
                "selectDate":selec,
                "id":id
            };
            if(selectTime=="a"){
                alert("请选择时间段");
                return false;
            }
            $.ajax({
                type:"POST",
                url:"${ctx}vac/updateByTime.do",
                async: false,
                data: JSON.stringify(refoundBean),
                contentType: "application/json; charset=utf-8",
                success:function(data){
                    if(data.success){
                        alert("预约成功");
                        var sel =  $("#selectDate").val();
                        $("#date_rq").text(selec);
                        //$("#date_sj").text( chgerData (selectTime));
                        _searchData(sel,localcode);
                    }else {
                        alert("预约失败");
                    }
                }, //调用出错执行的函数
                error: function(){
                    //请求出错处理
                }
            });
        });

        $("#selectDate").change(function () {
            var sel =  $("#selectDate").val();
            _searchData(sel,localcode);
        })
    })

    function _searchData (selectDate,localcode) {
        var bean = {
            "selectDate":selectDate,
            "localcode":localcode
        };
        $.ajax({
            type:"POST",
            url:"${ctx}vac/remindTable.do",
            async: false,
            data: JSON.stringify(bean),
            contentType: "application/json; charset=utf-8",
            success:function(data){
                /*console.log(data);
                console.log(data.listSimp);*/
                var list=data.data.listSimp;
                $("#remind").html("");
                if (list.length > 0) {
                    var html = "";
                    var cunt=0;
                    var str = '';
                    $.each(list, function (i,t) {
                        str += '<option value="'+t.str1+'">'+t.str1+'</option>';
                        var yu=parseInt(t.int1)-parseInt(t.int2);
                        html = "<tr>";
                        html += "<td class='pd0'> "+ t.str1 + "</td>"; //
                        html += "<td>" + yu + "</td>"; //
                        var su= parseInt(t.int2)/parseInt(t.int1)*100;
                        html += "<td class='pd0'> <div class='progress'><div class='progress-bar' role='progressbar' aria-valuenow='"
                            +su+"' aria-valuemin='0'aria-valuemax='100' style='width:" + su +"%;'>"+ t.int2 +"</div></div></td>";
                        html += "</tr>";
                        cunt=parseInt(cunt)+parseInt(yu);
                        $("#remind").append(html);
                    });
                    $('#selectTime').html(str);
                    $("#total").html("");
                    $("#total").append("预约总人数:"+cunt);
                    $("#date_yyr").text(selectDate);
                    //alert(cunt); //总人数
                }
            }, //调用出错执行的函数
            error: function(){
                //请求出错处理
            }
        });
    };

/*    function chgerData (date){
        if(date=="1"){
            return "8:00-9:00"
        }else if(date=="2"){
            return "9:00-10:00"
        }else if(date=="3"){
            return "10:00-11:00"
        }else if(date=="4"){
            return "13:00-15:00"
        }else if(date=="5"){
            return "15:00-17:00"
        }else if(date=="a"){
            return "未选时段"
        }

    };*/

</script>  
