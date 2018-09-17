<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">  
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>疫苗接种列表</title>
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css" rel="stylesheet" />  
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css" rel="stylesheet" />  
	<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js" ></script>  
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap.min.js"></script>  
	<style type="text/css">
		*{font-family: "微软雅黑"!important;}
       	html, body { height: 100%; width: 100%; } 
		p{text-indent: 2em;margin:5px 0;}
	 	button{
	 		font-size: 16px!important;
	 		padding: 6px 30px!important;
	 		color: #fff;
   			background: #64d5d7;
	 	}
	 	button:hover , button:active , button:focus , button:visited{
	 		color: #fff!important;
	 		opacity: 0.6;
	 	}
	 	canvas{ border-radius: 8px;}
        p{text-indent: 2em;margin:5px 0;font-size: 1em;line-height: 24px;}
        .title{text-align: center; padding: 15px 0; font-size: 1.2em; font-weight: 700;}
        .unit{text-align: right;font-size: 0.9em;padding-top: 15px;}
        img{height: 200px;width: 288px}
    </style>
</head>
<body onload="redea()">
		<!-- 主页面标题 -->  
        <header class="container-fluid">
            <%--<h2 class="text-center" id="title">告知书</h2>  --%>
            <div>${disContext}</div>
        </header>  
        <div class="mui-content">  
        	<div class="container-fluid">
                <div style="margin-top:20px;">  
                    <h3 class="text-left">签名：</h3>  
                    <div id="myImg" class="text-center"><img  src="${ctx}vac/signimg.do?id=${VacChildRemind.id}&vaccId=${VacChildRemind.vaccId}&localcode=${VacChildRemind.localcode}"/></div>
               </div>
           </div> 
        </div>
</body>
<script type="text/javascript">
    /* mui.init();
     mui.plusReady(function(){

     });  */
     function redea(){
         $('.unit').text('淮北市疾病预防控制中心');
         new WritingPad();
     }
</script>
</html>
<script type="text/javascript">
/**
 * 功能：签名canvas面板初始化,为WritingPad.js手写面板js服务。
 */

(function (window, document, jQuery) {
    'use strict';

  // 获取绘制屏幕的规则间隔
  window.requestAnimFrame = (function (callback) {
    return window.requestAnimationFrame ||
      window.webkitRequestAnimationFrame ||
      window.mozRequestAnimationFrame ||
      window.oRequestAnimationFrame ||
      window.msRequestAnimaitonFrame ||
      function (callback) {
        window.setTimeout(callback, 1000/60);
      };
  })();

  /*
  * 插件构造函数
  */

  var pluginName = 'jqSignature',
      defaults = {
        lineColor: '#222222',
        lineWidth: 1,
        border: '1px dashed #CCFF99',
        background: '#FFFFFF',
        width: ($(document).innerWidth())*0.8,
        height: 200,
        autoFit: false
      },
      canvasFixture = '<canvas></canvas>';

  function Signature(element, options) {
    // DOM元素/对象
    this.element = element;
    this.jQueryelement = jQuery(this.element);
    this.canvas = false;
    this.jQuerycanvas = false;
    this.ctx = false;
    // Drawing state
    this.drawing = false;
    this.currentPos = {
      x: 0,
      y: 0
    };
    this.lastPos = this.currentPos;
    // 确定插件的设置
    this._data = this.jQueryelement.data();
    this.settings = jQuery.extend({}, defaults, options, this._data);
    // 初始化插件
    this.init();
  }

  Signature.prototype = {
    // 初始化签名画布
    init: function() {
      // 设置画布
      this.jQuerycanvas = jQuery(canvasFixture).appendTo(this.jQueryelement);
      this.jQuerycanvas.attr({
        width: this.settings.width,
        height: this.settings.height
      });
      this.jQuerycanvas.css({
        boxSizing: 'border-box',
        width: this.settings.width + 'px',
        height: this.settings.height + 'px',
        border: this.settings.border,
        background: this.settings.background,
        cursor: 'crosshair'
      });
      //将画布安装到父宽
      if (this.settings.autoFit === true) {
        this._resizeCanvas();
      }
      this.canvas = this.jQuerycanvas[0];
      this._resetCanvas();
      //设置鼠标事件
      this.jQuerycanvas.on('mousedown touchstart', jQuery.proxy(function(e) {
        this.drawing = true;
        this.lastPos = this.currentPos = this._getPosition(e);
      }, this));
      this.jQuerycanvas.on('mousemove touchmove', jQuery.proxy(function(e) {
        this.currentPos = this._getPosition(e);
      }, this));
      this.jQuerycanvas.on('mouseup touchend', jQuery.proxy(function(e) {
        this.drawing = false;
        // 触发更改事件
        var changedEvent = jQuery.Event('jq.signature.changed');
        this.jQueryelement.trigger(changedEvent);
      }, this));
      // 触摸画布时防止文件滚动
      jQuery(document).on('touchstart touchmove touchend', jQuery.proxy(function(e) {
        if (e.target === this.canvas) {
          e.preventDefault();
        }
      }, this));
      // 开始画
      var that = this;
      (function drawLoop() {
        window.requestAnimFrame(drawLoop);
        that._renderCanvas();
      })();
    },
    //重置的画布
    clearCanvas: function() {
      this.canvas.width = this.canvas.width;
      this._resetCanvas();
    },
    // 把画布的内容为Base64编码数据的URL
    getDataURL: function() {
      return this.canvas.toDataURL();
    },

    reLoadData: function () {
        this.jQuerycanvas.remove();
        this._data = this.jQueryelement.data();
        this.init();
    },
    // 获取鼠标/触摸的位置
    _getPosition: function(event) {
      var xPos, yPos, rect;
      rect = this.canvas.getBoundingClientRect();
      event = event.originalEvent;
      // 触摸事件
      if (event.type.indexOf('touch') !== -1) { // event.constructor === TouchEvent
        xPos = event.touches[0].clientX - rect.left;
        yPos = event.touches[0].clientY - rect.top;
      }
      // 鼠标事件
      else {
        xPos = event.clientX - rect.left;
        yPos = event.clientY - rect.top;
      }
      return {
        x: xPos,
        y: yPos
      };
    },
    // 将签名渲染到画布
    _renderCanvas: function() {
      if (this.drawing) {
        this.ctx.moveTo(this.lastPos.x, this.lastPos.y);
        this.ctx.lineTo(this.currentPos.x, this.currentPos.y);
        this.ctx.stroke();
        this.lastPos = this.currentPos;
      }
    },
    //重置画布上下文
    _resetCanvas: function() {
      this.ctx = this.canvas.getContext("2d");
      this.ctx.strokeStyle = this.settings.lineColor;
      this.ctx.lineWidth = this.settings.lineWidth;
    },
    // 调整画布元素的大小
    _resizeCanvas: function() {
      var width = this.jQueryelement.outerWidth();
      this.jQuerycanvas.attr('width', width);
      this.jQuerycanvas.css('width', width + 'px');
    }
  };

  /*
  * 插件和初始化
  */

  jQuery.fn[pluginName] = function ( options ) {
    var args = arguments;
    if (options === undefined || typeof options === 'object') {
      return this.each(function () {
        if (!jQuery.data(this, 'plugin_' + pluginName)) {
          jQuery.data(this, 'plugin_' + pluginName, new Signature( this, options ));
        }
      });
    }
    else if (typeof options === 'string' && options[0] !== '_' && options !== 'init') {
      var returns;
      this.each(function () {
        var instance = jQuery.data(this, 'plugin_' + pluginName);
        if (instance instanceof Signature && typeof instance[options] === 'function') {
            var myArr=Array.prototype.slice.call( args, 1 );
            returns = instance[options].apply(instance, myArr);
        }
        if (options === 'destroy') {
          jQuery.data(this, 'plugin_' + pluginName, null);
        }
      });
      return returns !== undefined ? returns : this;
    }
  };

})(window, document, jQuery);
</script>

<script type="text/javascript">
/**
 * 功能：使用该jQuery插件来制作在线签名或涂鸦板，用户绘制的东西可以用图片的形式保存下来。
 */

var WritingPad = function () {

    var current = null;

    jQuery(function () {

        initTable();

        initSignature();

        if (jQuery(".modal")) {
            jQuery(".modal").modal("toggle");
        } else {
            mui.alert("没用手写面板");
        }

        jQuery(document).on("click", "#mySave", null, function () {
            var myImg=jQuery("#myImg").empty();
            var dataUrl = jQuery('.js-signature').jqSignature('getDataURL');
            /* var img=jQuery('<img>').attr("src",dataUrl);
            jQuery(myImg).append(img);   */
            //保存信息
  			$.ajax({
				url:'${ctx}vac/savesign.do',
				data:{'id':"${VacChildRemind.id}",'vaccId':"${VacChildRemind.vaccId}",'localcode':"${VacChildRemind.localcode}",'signatureData':dataUrl},
				type:"post",
				success:function(data){
					if(data.success){
             			layer.msg(data.msg,{icon: 1,time: 800});
             			//跳转原页面
             			setTimeout(function reflush(){
             				window.location.href="${ctx}vac/vaccList.do";
						},1500);
   					}else{
   						layer.msg(data.msg,{icon: 5,time: 800});
   					}
				},
				error:function(){
					layer.msg("请求失败，请稍后再试",{icon:0, time: 2000});
				}
			});
        });

        jQuery(document).on("click", "#myEmpty", null, function () {
            jQuery('.js-signature').jqSignature('clearCanvas');
        });

        jQuery(document).on("click", "#myBackColor", null, function () {

            jQuery('#colorpanel').css('left', '95px').css('top', '45px').css("display", "block").fadeIn();
            jQuery("#btnSave").data("sender", "#myBackColor");
        });

        jQuery(document).on("click", "#myColor", null, function () {
            jQuery('#colorpanel').css('left', '205px').css('top', '45px').css("display", "block").fadeIn();
            jQuery("#btnSave").data("sender", "#myColor");
        });

        jQuery(document).on("mouseover", "#myTable", null, function () {

            if ((event.srcElement.tagName == "TD") && (current != event.srcElement)) {
                if (current != null) { current.style.backgroundColor = current._background }
                event.srcElement._background = event.srcElement.style.backgroundColor;
                current = event.srcElement;
            }

        });

        jQuery(document).on("mouseout", "#myTable", null, function () {

            if (current != null) current.style.backgroundColor = current._background

        });

        jQuery(document).on("click", "#myTable", null, function () {

            if (event.srcElement.tagName == "TD") {
                var color = event.srcElement._background;
                if (color) {
                    jQuery("input[name=DisColor]").css("background-color", color);
                    var strArr = color.substring(4, color.length - 1).split(',');
                    var num = showRGB(strArr);
                    jQuery("input[name=HexColor]").val(num);
                }
            }

        });

        jQuery(document).on("click", "#btnSave", null, function () {

            jQuery('#colorpanel').css("display", "none");
            var typeData = jQuery("#btnSave").data("sender");
            var HexColor = jQuery("input[name=HexColor]").val();
            var data = jQuery(".js-signature").data();
            if (typeData == "#myColor") {
                data["plugin_jqSignature"]["settings"]["lineColor"] = HexColor;
                jQuery('.js-signature').jqSignature('reLoadData');
            }
            if (typeData == "#myBackColor") {

                data["plugin_jqSignature"]["settings"]["background"] = HexColor;
                jQuery('.js-signature').jqSignature('reLoadData');
            }
        });

        jQuery("#mymodal").on('hide.bs.modal', function () {
            jQuery("#colorpanel").remove();
            jQuery("#mymodal").remove();
            jQuery("#myTable").remove();
        });

    });

    function initTable() {
        var colorTable = "";
        var ColorHex = new Array('00', '33', '66', '99', 'CC', 'FF');
        var SpColorHex = new Array('FF0000', '00FF00', '0000FF', 'FFFF00', '00FFFF', 'FF00FF');
        for (var i = 0; i < 2; i++)
        {
            for (var j = 0; j < 6; j++)
            {
                colorTable = colorTable + '<tr height=12>';
                colorTable = colorTable + '<td width=11 style="background-color:#000000"></td>';

                if (i == 0)
                {
                    colorTable = colorTable + '<td width=11 style="background-color:#' + ColorHex[j] + ColorHex[j] + ColorHex[j] + '"></td>';
                }
                else
                {
                    colorTable = colorTable + '<td width=11 style="background-color:#' + SpColorHex[j] + '"></td>';
                }

                //colorTable = colorTable + '<td width=11 style="background-color:#000000"></td>';

                for (var k = 0; k < 3; k++)
                {
                    for (l = 0; l < 6; l++)
                    {
                        colorTable = colorTable + '<td width=11 style="background-color:#' + ColorHex[k + i * 3] + ColorHex[l] + ColorHex[j] + '"></td>';
                    }
                }
                colorTable = colorTable + '</tr>';


            }
        }
        colorTable =
        '<table border="1" id="myTable" cellspacing="0" cellpadding="0" style="border-collapse: collapse;cursor:pointer;" bordercolor="000000">'
        + colorTable + '</table>' +
        '<table width=225 border="0" cellspacing="0" cellpadding="0" style="border:1px #000000 solid;border-collapse: collapse;background-color:#000000">' +
        '<tr style="height:30px">' +
        '<td colspan=21 bgcolor=#cccccc>' +

        '<table cellpadding="0" cellspacing="1" border="0" style="border-collapse: collapse">' +
        '<tr>' +
        '<td width="3"><input type="text" name="DisColor" size="6" disabled style="border:solid 1px #000000;background-color:#ffff00"></td>' +
        '<td width="3"><input type="hidden" name="HexColor" size="7" style="border:inset 1px;font-family:Arial;" value="#000000" ></td>' +
         '<td width="3"><button type="button" class="btn btn-primary btn-sm" id="btnSave">确认</button></td>' +
        '</tr>' +
        '</table>' +

        '</td>' +
        '</tr>' +
        '</table>';
        jQuery("#colorpanel").append(colorTable);
    }

    function initSignature() {

        if (window.requestAnimFrame) {
            var signature = jQuery("#mySignature");
            signature.jqSignature({ border: '1px solid #fafafa', background: '#ebebeb', lineColor: '#969696', lineWidth: 2, autoFit: false });
        } else {

            mui.alert("请加载jq-signature.js");
            return;
        }
    }

    function showRGB(arr) {
        hexcode = "#";
        for (x = 0; x < 3; x++) {
            var n = arr[x];
            if (n == "") n = "0";
            if (parseInt(n) != n)
                return mui.alert("RGB颜色值不是数字！");
            if (n > 255)
                return mui.alert("RGB颜色数字必须在0-255之间！");
            var c = "0123456789ABCDEF", b = "", a = n % 16;
            b = c.substr(a, 1); a = (n - a) / 16;
            hexcode += c.substr(a, 1) + b
        }
        return hexcode;
    }

    function init() {


    }

    return {
        init: function () {
            init();
        }
    };
}
</script>