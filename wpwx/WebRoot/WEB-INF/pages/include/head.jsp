<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static/"/>
<c:set var="ctx" value="${pageContext.request.contextPath}/"/>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link rel="stylesheet" href="${ctxStatic}css/common.css">
<link rel="stylesheet" href="${ctxStatic}layer-v3.0.3/layer/skin/default/layer.css">
<script src="${ctxStatic}js/jquery.js" type="text/javascript"></script>
<script src="${ctxStatic}layer-v3.0.3/layer/layer.js" type="text/javascript"></script>
<!-- <script src="${ctxStatic}layer-v3.0.3/layer/mobile/layer.js" type="text/javascript"></script> -->
<!-- cookies -->
<script src="${ctxStatic}js/cookies.js"></script>

<script>
    var ctxStatic ="${ctxStatic}";
    var ctx ="${ctx}";
</script>
<!-- select2 -->
<script src="${ctxStatic}select2-4.0.3/dist/js/select2.full.js"></script>
<link rel="stylesheet" href="${ctxStatic}select2-4.0.3/dist/css/select2.css">

<script src="${ctxStatic}js/jquery.validate.js"></script>
<script src="${ctxStatic}js/jquery.validate-method.js"></script>

<c:if test="${not empty msg}">
	<script type="text/javascript">
		layer.msg("${msg}",{icon:1, time: 2000});
	</script>
</c:if>

<c:if test="${not empty error}">
	<script type="text/javascript">
		layer.msg("${error}",{icon:0, time: 2000});
	</script>
</c:if>
<script>
    /*根据出生日期算出年龄*/
    function jsGetAge(strBirthday){
        var returnAge;
        var strBirthdayArr=strBirthday.split("-");
        var birthYear = strBirthdayArr[0];
        var birthMonth = strBirthdayArr[1];
        var birthDay = strBirthdayArr[2];

        d = new Date();
        var nowYear = d.getFullYear();
        var nowMonth = d.getMonth() + 1;
        var nowDay = d.getDate();

        if(nowYear == birthYear){
            returnAge = 0;//同年 则为0岁
        }
        else{
            var ageDiff = nowYear - birthYear ; //年之差
            if(ageDiff > 0){
                if(nowMonth == birthMonth) {
                    var dayDiff = nowDay - birthDay;//日之差
                    if(dayDiff < 0)
                    {
                        returnAge = ageDiff - 1;
                    }
                    else
                    {
                        returnAge = ageDiff ;
                    }
                }
                else
                {
                    var monthDiff = nowMonth - birthMonth;//月之差
                    if(monthDiff < 0)
                    {
                        returnAge = ageDiff - 1;
                    }
                    else
                    {
                        returnAge = ageDiff ;
                    }
                }
            }
            else
            {
                returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
            }
        }

        return returnAge;//返回周岁年龄

    }
</script>
