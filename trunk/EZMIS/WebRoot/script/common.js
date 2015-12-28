var ua = navigator.userAgent
var isIE = navigator.appName == "Microsoft Internet Explorer" ? true : false
var ieVer = 8.0;
if (isIE) {
	ieVer = parseFloat(ua.substring(ua.indexOf("MSIE ") + 5, ua.indexOf(";", ua
			.indexOf("MSIE "))))
}

//实现prototype.js快速定位元素的功能
if(!$){
	var $ = function (id) {
		var ret = id;
		if("string" == typeof id){
			ret = document.getElementById(id);
			if(!ret){
				ret = document.getElementsByTagName(id);
				if(ret.length>0){
					ret = ret[0];
				}
			}
		}
		return  ret;
	};
}


/**
 * 布尔值验证
 * @param v 字符串为'true|yes|1'均判断为true
 */
function $bool(v){
	var ret = false;
	if(typeof v == 'string'){
		ret = (v=='true' || v=='1' || v=='yes')
	}
	if(typeof v == 'boolean'){
		ret = v;
	}
	return ret;
}



/**
 * @author:sds
 * @description:用户提供一些扩展的公共方法
 */
if (typeof Ext != 'undefined') {
	/**
	 * 跳转到制定的url
	 * 
	 * @url 制定的url
	 */
	Ext.forward = function(url) {
		window.document.location = url;
	}

	if (Ext.form) {
		if (Ext.form.Field) {
			Ext.form.Field.prototype.msgTarget = 'side';
		}

		/**
		 * 为组合框添加一个取得显示的值的方法
		 */
		Ext.form.ComboBox.prototype.getDisplayValue = function() {
			return this.getEl().dom.value;
		}
	}
	

	if (typeof Ext.tree != 'undefined') {
		if (Ext.tree.TreeNode) {
			/**
			 * 为TreeNode添加一个方法 用于判断当前节点是否为树的根节点
			 */
			Ext.tree.TreeNode.prototype.isRootNode = function() {
				if (this.parentNode == null) {
					return true;
				} else {
					return false;
				}
			}

			/**
			 * 取得当前节点所在的位置的索引
			 * 
			 */
			Ext.tree.TreeNode.prototype.getIndex = function() {
				var i = 0;
				var previous = this.previousSibling;
				while (previous) {
					i++;
					previous = previous.previousSibling;
				}
				return i;
			}
		}
	}

	if (typeof Ext.form != 'undefined') {

		Ext.override(Ext.form.CheckboxGroup, {
			getNames : function() {
				var n = [];

				this.items.each(function(item) {
					if (item.getValue()) {
						n.push(item.getName());
					}
				});

				return n;
			},

			getValues : function() {
				var v = [];
				if (this.items && this.items.length > 0) {
					this.items.each(function(item) {
						if (item.getValue()) {
							v.push(item.getRawValue());
						}
					});
				}

				return v;
			},

			setValues : function(v) {
				var r = new RegExp('(' + v.join('|') + ')');
				if (this.items) {
					this.items.each(function(item) {
						item.setValue(r.test(item.getRawValue()));
					});
				}
			}
		});

		Ext.override(Ext.form.RadioGroup, {
			getName : function() {
				return this.items.first().getName();
			},

			getValue : function() {
				var v;

				this.items.each(function(item) {
					v = item.getRawValue();
					return !item.getValue();
				});

				return v;
			},

			setValue : function(v) {
				this.items.each(function(item) {
					item.setValue(item.getRawValue() == v);
				});
			}
		});

		/**
		 * 取得当前选中的radio box 的值
		 */
		Ext.form.RadioGroup.prototype.getGroupValue = function() {
			var result = "";

			Ext.each(this.items.items, function(radioItem) {
				if (radioItem.checked == true) {
					result = radioItem.inputValue;
				}
			})

			return result;
		}

		/**
		 * 指定哪个radio box被选中
		 */
		Ext.form.RadioGroup.prototype.setGroupValue = function(value) {

			Ext.each(this.items.items, function(radioItem) {
				if (radioItem.inputValue == value) {
					radioItem.setValue(true);
				} else {
					radioItem.setValue(false);
				}
			})
		}

	}
	/**
	 * 显示Mask
	 */
	function showExtMask(msg) {
		var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : msg
		});
		myMask.show();
		return myMask;
	}

	/**
	 * 根据指定的URL打开一个基于EXT的窗口
	 * 
	 * @param {}
	 *            url
	 * @param {}
	 *            width
	 * @param {}
	 *            height
	 * @param {}
	 *            config
	 *            比如：showExtWindow(url,500,400,{x:100,y:100,title:"调整纳税人配额",listeners:{"close":cbFn}});
	 */
	function showExtWindow(url, width, height, config) {
		var div = $("windowDiv");
		if (div == null) {
			Element.insert(document.body, "<div id='windowDiv'></div>")
		}
		var cfg = {
			renderTo : 'windowDiv',
			width : width,
			height : height,
			x : 50,
			y : 100,
			closable : true,
			shadow : false,
			draggable : true,
			resizable : false,
			modal : true,
			shim : true,
			listeners : {
				'close' : function() {
					Element.remove("windowDiv");
				}
			},
			html : '<iframe style="width:100%;height:100%" src="' + url
					+ '" scrolling="yes" frameBorder="no"></iframe>'
		}
		Ext.apply(cfg, config);
		var win = new Ext.Window(cfg);
		win.show();
		return win;

	}
	/**
	 * 为quicktip默认增加更长时间的延时
	 */
	var QuickTipsInit = Ext.QuickTips.init;
	Ext.QuickTips.init = function (config){
		QuickTipsInit(config);
		Ext.apply(Ext.QuickTips.getQuickTip(), {
		    dismissDelay: 100000
		});
	}
	
	

}else{

	/**
     * Checks whether or not the specified object exists in the array.
     * @param {Object} o The object to check for
     * @return {Number} The index of o in the array (or -1 if it is not found)
     */
	Array.prototype.indexOf = function(o){
	       for (var i = 0, len = this.length; i < len; i++){
	 	      if(this[i] == o) return i;
	       }
	 	   return -1;
	    };
	    
	    /**
	     * Removes the specified object from the array.  If the object is not found nothing happens.
	     * @param {Object} o The object to remove
	     * @return {Array} this array
	     */
	Array.prototype.remove = function(o){
	   var index = this.indexOf(o);
	   if(index != -1){
	       this.splice(index, 1);
	   }
	   return this;
	}
}


/**
 * 在指定的form中创建隐藏域对象 form 表单对象 name 隐藏域名称 value 隐藏域的值
 */
function createHiddenField(form, name, value) {
	var hf = document.createElement('input');
	hf.type = "hidden";
	hf.name = name;
	hf.value = value;
	form.appendChild(hf);
}

/**
 * 统一主工具栏事件触发函数
 */
function mainToolBarButtonClick(oButton) {
	var clickFuncName = oButton.id + "_Click";
	var clickFunc = null;
	try {
		clickFunc = eval(clickFuncName);
	} catch (e) {
	}
	if (clickFunc != null && typeof clickFunc == 'function') {
		clickFunc();
	} else {
		alert('该功能还未实现,方法【' + clickFuncName + '()】' + '不存在')
	}
}

/**
 * 初始化操作工具栏
 */
operationsToolbarInitialize = function(toolbar) {
	// ops来自<jteap:operations>标签，如果定义了该标签，标签内部将会定义该变量，用于存放所有具有权限的操作的json对象
	if (ops && ops.length > 0) {
		for (var i = 0;i < ops.length; i++) {
			var op = ops[i];
			// debugger;
			if (op.sn == "->" || op.sn == "-" || op.sn == "separator"
					|| op.sn == " ") {
				toolbar.add(op.sn);
				continue;
			}
			var btConfig = {
				id : op.sn,
				cls : 'x-btn-text-icon',
				icon : (op.icon && op.icon != '') ? op.icon : "icon/icon_1.gif"
			};
			if (op.tip && op.tip != '')
				btConfig.tooltip = op.tip;
			btConfig.text = ((op.showText) ? op.resName : "");
			var opBt = toolbar.addButton(btConfig);
			/*
			 * var handler; try{ handler=eval(op.sn+"_Click"); }catch(e){
			 * handler=function(){alert('该功能尚未实现，请联系系统管理员')}; }
			 */
			opBt.on("click", mainToolBarButtonClick);
			// 是否需要初始化工具栏按钮的状态，在各自模块的js文件中定义initToolbarButtonStatus(Ext.Toolbar.Button)函数
			if (initToolbarButtonStatus) {
				initToolbarButtonStatus(opBt);
			}
		}
	}
}

/**
 * 点击导出按钮 path是Ajax请求的路径 cm是所要导出的GridPanel的ColumnModel,用来动态的获取header
 * flag标志GridPanel中是否有checkbox
 */
// exportExcel = function(path,cm,flag) {
/**
 * 点击导出按钮 obj是要导出表的GridPanel对象（有一约定：obj的exportLink属性）
 * flag标志GridPanel中是否有checkbox
 */
exportExcel = function(obj, flag) {
	var path = obj.store.proxy.url;
	var paths = path.split("?")
	var temp1 = paths[0]
	var temp3 = paths[1]
	temp1 = temp1.split("!")[0]
	path = temp1 + "!exportExcel.do?" + temp3
	var cm = obj.getColumnModel();
	var sum = cm.getColumnCount();
	var j = 1;
	if (!flag) {
		j = 0
	}
	var paraHeader = "";
	var paraDataIndex = "";
	var paraWidth = ""
	for (var i = j;i < sum; i++) {
		if(!cm.isHidden(i)){
			paraHeader += cm.getColumnHeader(i) + ","
			paraDataIndex += cm.getDataIndex(i) + ","
			paraWidth += cm.getColumnWidth(i) + ","
		}
	}
	paraHeader = paraHeader.substr(0, paraHeader.length - 1)
	paraDataIndex = paraDataIndex.substr(0, paraDataIndex.length - 1)
	paraWidth = paraWidth.substr(0, paraWidth.length - 1)
	path = path + "&paraHeader=" + encodeURIComponent(paraHeader)
			+ "&paraDataIndex=" + encodeURIComponent(paraDataIndex)
			+ "&paraWidth=" + encodeURIComponent(paraWidth);
	// alert(path);
	window.open(path)
}

/**
 * 显示等待窗口
 * 
 * @date 2003-12-29
 * 
 * 
 * var oPubPopup = window.createPopup(); var oPubPopupBody =
 * oPubPopup.document.body;
 */
function ShowWait(displaystr) {
	/*
	 * var oWin = parent if(oWin == null ) oWin = window oPubPopup =
	 * oWin.createPopup(); oPubPopupBody = oPubPopup.document.body;
	 * 
	 */
	if (displaystr == "end") {
		oPubPopup.hide();
	} else {

		if (event != null) {
			if (event.srcElement != null) {
				// alert(event.srcElement.tagName)
				if (event.srcElement.tagName.toUpperCase() == "SELECT")
					return;
			}
		}
		// alert(event.srcElement.outerHTML)
		var strHTML = "";// "<html><head></head><body leftmargin=0
		// topmargin=0>";
		strHTML += "<TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0><TR><td width=0%></td>";
		strHTML += "<TD bgcolor=#ff9900><TABLE WIDTH=100% height=60 BORDER=0 CELLSPACING=2 CELLPADDING=0>";
		strHTML += "<TR><td bgcolor=#eeeeee align=center>" + displaystr
				+ "</td></tr></table></td>";
		strHTML += "<td width=0%></td></tr></table>";

		oPubPopupBody.innerHTML = strHTML;
		var iwidth = 300;
		var iheight = 60;
		var ileft = (screen.availWidth - iwidth) / 2;
		var itop = (screen.availHeight - iheight) / 2;
		oPubPopup.show(ileft, itop, iwidth, iheight);
	}
}

/*
 * ====================================================================
 * 函数名称:showModule[创建日期2005-6-24][最后修改日期2005-6-24] 功能 :显示一个web模式窗口,主要是传参数用 参数:
 * sUrl 一个需要请求的链接地址 sCenter 窗口是否居中 yes:居中 no:不居中 iWidth 窗口的宽度 iHeight 窗口的高度
 * sArguments 向模式窗口传递参数的数组,每个参数都有一个值,相当是一个键值对串行化字符串
 * 格式为:param1|value1;param2|value2;....;paramN|valueN iLeft
 * 窗口左边距,如果指定了窗口居中了,sCenter="yes",那么iLeft无效 iTop
 * 窗口上边距,如果指定了窗口居中了,sCenter="yes",那么iTop无效 返回值 字付串
 * ======================================================================
 */
function showModule(sUrl, sCenter, iWidth, iHeight, sArguments, iLeft, iTop,
		sFeature) {
	var myobject = new Object();

	// 拆分参数值对,存入myobject对象中,该对象将会被传入Module窗口里，供其调用,
	// 在Module窗口中也可以改变参数的值

	if (sArguments != null && sArguments != "") {
		var argList = sArguments.split(";");
		for (i = 0;i < argList.length; i++) {
			var paramName = argList[i].substring(0, argList[i].indexOf("|"));
			var paramValue = argList[i].substring(argList[i].indexOf("|") + 1);
			eval("myobject." + paramName + "='" + paramValue + "'");
		}
	}
	myobject.opener = window;
	myobject.returnVal = "";
	var ver = navigator.appVersion;

	if (ver.indexOf("MSIE 7") > -1 || ver.indexOf("MSIE 8") > -1 || ver.indexOf("MSIE 9") > -1) {
		iHeight = iHeight + 25;

	}
	if (typeof sFeature == "undefined") {
		// 组合窗口特征字符串
		var sFeature = "dialogWidth:" + iWidth + "px;dialogHeight:" + iHeight
				+ "px;center:" + sCenter + ";status:0;help:0;scroll:no";
		if (iLeft != null && iTop != null)
			sFeature += "dialogLeft:" + iLeft + "px;dialogTop:" + iTop + "px";
	}
	return window.showModalDialog(sUrl, myobject, sFeature);
}

/*
 * ====================================================================
 * 函数名称:showModule2[创建日期2009-7-21] 功能 :显示一个web模式窗口,主要是传参数用 参数: sUrl 一个需要请求的链接地址
 * sCenter 窗口是否居中 yes:居中 no:不居中 iWidth 窗口的宽度 iHeight 窗口的高度 oArgObj 一个object对象{}
 * iLeft 窗口左边距,如果指定了窗口居中了,sCenter="yes",那么iLeft无效 iTop
 * 窗口上边距,如果指定了窗口居中了,sCenter="yes",那么iTop无效 返回值 字付串
 * ======================================================================
 */

function showModule2(sUrl, sCenter, iWidth, iHeight, oArgObj, iLeft, iTop,
		sFeature,bUseUrlParam) {
	var myobject = new Object();

	
	var ver = navigator.appVersion;
	
	//如不注释此脚本,弹出模式窗口在IE7下显示不全
//	if (ver.indexOf("MSIE 7") > -1) {
//		iHeight = iHeight - 25;
//
//	}
	if (typeof sFeature == "undefined" || sFeature == null || sFeature == '') {
		// 组合窗口特征字符串
		var sFeature = "dialogWidth:" + iWidth + "px;dialogHeight:" + iHeight
				+ "px;center:" + sCenter + ";status:0;help:0;scroll:no";
		if (iLeft != null && iTop != null)
			sFeature += "dialogLeft:" + iLeft + "px;dialogTop:" + iTop + "px";
	}
	
	if(typeof bUseUrlParam=='undefined'){
		bUseUrlParam = false;
	}
	if(bUseUrlParam == true){
		for(var p in oArgObj){
			var name = p;
			var value = encodeURIComponent(eval("oArgObj."+p));
			if(typeof value !='object'){
				sUrl = _appendUrlParam(sUrl,name,value);
			}
		}
		oArgObj = {};
	}
	if(oArgObj == null) oArgObj = {};
	oArgObj.opener = window;
	oArgObj.returnVal = "";
	return window.showModalDialog(sUrl, oArgObj, sFeature);
}
/**
 * 重新调整窗口大小，并将调整后的窗口放在正中间
 */
function $resize(width,height){
	 var screen_Width = window.screen.availWidth;
	 var screen_Height = window.screen.availHeight;
	 window.dialogWidth = width+"px";
     window.dialogHeight = height+"px"; 
	 window.dialogLeft = (screen_Width - width)/2;
	 window.dialogTop = (screen_Height - height)/2;
}

var $mw = showModule2;

/**
 * 取得模式窗口参数
 */
function $mw_param(param){
	var value = null;
	eval("value = window.dialogArguments."+param+";");
	return value;
}

/**
 * 显示可用于刷新的模式窗口
 * @param {} sUrl		
 * @param {} sTitle   模式窗口标题
 * @param {} sCenter	是否显示在屏幕中间
 * @param {} iWidth	宽度
 * @param {} iHeight	高度
 * @param {} oArgObj	参数对象{}
 * @param {} iLeft		iLeft 窗口左边距,如果指定了窗口居中了,sCenter="yes",那么iLeft无效
 * @param {} iTop		iTop 窗口上边距,如果指定了窗口居中了,sCenter="yes",那么iTop无效
 * @param {} sFeature	
 * @param {} bUseUrlParam	将oArgObj参数作为url参数的方式最加到url后面进行数据传递
 * @return {}
 */
function showIFModule(sUrl,sTitle, sCenter, iWidth, iHeight, oArgObj, iLeft, iTop,
	sFeature,bUseUrlParam,scrolling){
	if(scrolling == null || scrolling == ''){
		scrolling = 'no';
	}else if(scrolling == "auto"){
		scrolling = 'yes';
	}
	var url = contextPath + "/jteap/ModuleWindowForm.jsp?scrolling="+scrolling;
	if(oArgObj==null) oArgObj={};
	oArgObj.title = sTitle;
	oArgObj.url = sUrl;
	return showModule2(url,sCenter,iWidth,iHeight,oArgObj,iLeft,iTop,sFeature,bUseUrlParam);
}
var $mw_if = showIFModule;

function _appendUrlParam(url,paramName,paramValue){
	if(url.indexOf("?") < 0){
		url=url + "?";
	}else{
		url=url + "&";
	}
	url += (paramName + "=" + paramValue );
	return url;
}
/**
 * //字符串替换
 * 
 */
function repStr(mainStr, findStr, replaceStr) {
	if (typeof mainStr == "undefined" || mainStr == null) {
		return "";
	}

	var convertedString = mainStr.split(findStr);
	convertedString = convertedString.join(replaceStr);
	return convertedString;
}

/**
 * 将指定字符串中的指定字符替换成同其对应的Ascii码 eg. '#'->'$35$' 其中35为#的Ascii码
 * 
 * @param sString
 *            源字符串
 * @param sChar
 *            特殊字符
 * @return
 */
function encodeSpecialChar(str1, chr) {
	var code = chr.charCodeAt();
	return repStr(str1, chr, '$' + code + '$');
}

function decodeSpecialChar(str1, chr) {
	var code = chr.charCodeAt();
	return repStr(str1, '$' + code + '$', chr);
}

/**
 * 将所有特殊字符串编码
 */
function encodeChar(str) {

	str = encodeSpecialChar(str, "'");
	str = encodeSpecialChar(str, ";");
	str = encodeSpecialChar(str, "&");
	str = encodeSpecialChar(str, "\"");
	return str;
}

function decodeChar(str) {
	str = decodeSpecialChar(str, "'");
	str = decodeSpecialChar(str, ";");
	str = decodeSpecialChar(str, "&");
	str = decodeSpecialChar(str, "\"");
	return str;
}

/**
 * 将制定的特殊符号编码
 * 
 * @str 需要编码的字符串
 * @splitStr 逗号分隔特殊字符 "',%,&,*,/,\"
 */
function encodeChars(str, splitStr) {
	var splitArray = splitStr.split(",");
	for (var i = 0;i < splitArray.length; i++) {
		var specialChar = splitArray[i];
		str = encodeSpecialChar(str, specialChar);
	}
	return str;
}
/**
 * 将制定的特殊符号编码
 * 
 * @str 逗号分隔特殊字符 "',%,&,*,/,\"
 */
function decodeChars(str, splitStr) {
	var splitArray = splitStr.split(",");
	for (var i = 0;i < splitArray.length; i++) {
		var specialChar = splitArray[i];
		str = decodeSpecialChar(str, specialChar);
	}
	return str;
}
/**
 * 建立XMLDOM对象
 * 
 * @param sPath
 *            服务器端文件的url
 * @return XML对象
 * @date 2005-02-17
 */
function getDom(sXml) {
	var oXml;
	try {
		oXml = new ActiveXObject("Msxml2.DOMDocument");
	} catch (e) {

	}
	if (typeof oXml == "undefined")
		oXml = new ActiveXObject("Microsoft.XMLDOM");
	oXml.async = false;
	oXml.loadXML(sXml);
	return oXml;
}

/**
 * 取得指定XML NODE的属性值
 */
function getXmlAttribute(oXMLNode, sAttributeName) {
	var attr = oXMLNode.attributes.getNamedItem(sAttributeName);
	var result = "";
	if (attr != null)
		result = attr.value;
	return result;
}
/**
 * 设置xml节点属性
 */
function setXmlAttribute(oXMLNode, sAttrName, sValue) {
	var r = oXMLNode.attributes.getNamedItem(sAttrName);
	if(r==null){
		r = oXMLNode.ownerDocument.createAttribute(sAttrName);
		oXMLNode.setAttributeNode(r);
	}
	r.value = sValue;
}




/**
 * //替代非法XML字符
 */
function encodeXml(sXml) {
	sXml = encodeSpecialChar(sXml, "&");
	sXml = encodeSpecialChar(sXml, ">");
	sXml = encodeSpecialChar(sXml, "<");
	return sXml;
}
/**
 * //转回原串
 */
function decodeXml(sXml) {
	sXml = decodeSpecialChar(sXml, "<");
	sXml = decodeSpecialChar(sXml, ">");
	sXml = decodeSpecialChar(sXml, "&");
	return sXml;
}
/**
 * //替代非法XML字符
 */
function repXml(sRun) {
	sRun = repStr(sRun, "&", "&amp;");
	sRun = repStr(sRun, ">", "&gt;");
	sRun = repStr(sRun, "<", "&lt;");
	return sRun;
}
/**
 * //转回原串
 */
function UnRepXml(sSql) {
	sSql = repStr(sSql, "&lt;", "<");
	sSql = repStr(sSql, "&gt;", ">");
	sSql = repStr(sSql, "&amp;", "&");
	return sSql;
}
/**
 * //多次替代字符串
 * 
 */
function repStr(mainStr, findStr, replaceStr) {
	if (typeof mainStr == "undefined" || mainStr == null) {
		return "";
	}

	var convertedString = mainStr.split(findStr);
	convertedString = convertedString.join(replaceStr);
	return convertedString;
}

String.prototype.trim = function() {
	// 用正则表达式将前后空格
	// 用空字符串替代。
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
/**
 * 判断该字符串是否是数字
 */
String.prototype.isNumber = function() {
	return !isNaN(this);
}
/**
 * 同步Ajax调用 url:请求地址 param:参数对象 callFN:请求之后调用的函数 依赖于prototype.js中的Ajax对象
 */
AjaxRequest_Sync = function(url, param, callFN) {
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : param,
		asynchronous : false,// 同步调用
			onComplete : callFN,
			onFailure : function(e) {
				alert("Ajax请求失败：" + e.responseText);
			}
		});

}
var $ajax_syn = AjaxRequest_Sync;

/**
 * 格式化文件大小 xx.xxB xx.xxGB xx.xxMB
 */
function formatFileSize(lSize) {
	if (lSize < 0) {
		return "0B";
	} else if (lSize >= 1024 * 1024 * 1024) {
		return (Math.round((lSize / 1024 / 1024 / 1024) * 100)) / 100 + "GB";
	} else if (lSize >= 1024 * 1024) {
		return (Math.round((lSize / 1024 / 1024) * 100)) / 100 + "MB";
	} else if (lSize >= 1024) {
		return (Math.round((lSize / 1024) * 100)) / 100 + "KB";
	} else {
		return lSize + "B";
	}
}

/**
 * 加载XML文件并返回一个XMLDom对象，该方法兼容IE FIREFOX CHROME
 * 
 * @param xmlFile
 *            可以返回xml内容的url地址
 */
function loadXmlFile(xmlFile) {
	xmlDoc = null;
	if (window.ActiveXObject) {
		xmlDoc = new ActiveXObject('Msxml2.DOMDocument');
		xmlDoc.async = false;
		xmlDoc.load(xmlFile);
	} else if (document.implementation
			&& document.implementation.createDocument) {
		var xmlhttp = new window.XMLHttpRequest();
		xmlhttp.open("GET", xmlFile, false);
		xmlhttp.send(null);
		var xmlDoc = xmlhttp.responseXML.documentElement;
	} else {
		xmlDoc = null;
	}
	return xmlDoc;
}

/**
 * 根据分类唯一名称取得字典对象数组
 * 配合<jteap:dict catalog="2222,bbbbb">使用
 */
function $dictList(catalog) {
	var result = null;
	if (jteap_dict) {
		if (jteap_dict[catalog]) {
			result = jteap_dict[catalog];
		}
	}
	return result;
}
/**
 * 向服务器请求指定的数据字典，并返回对应的字典列表
 * [{key:'',value:''},{key:'',value:''}]
 * @catalog 数据字典标识
 * @cbFn 回调函数
 */
function $dictListAjax(catalog){
	var url = contextPath + "/jteap/system/dict/DictAction!listDictByUniqueCatalogAction.do";
	var param = {catalog:catalog};
	var result = null;
	AjaxRequest_Sync(url,param,function(ajax){
		var obj = new String(ajax.responseText).evalJSON();
		result = obj.list;
	})
	return result;
}
/**
 * 根据字典类型以及字典key查询该字典的值
 */
function $dictValue(catalog, key) {
	var result = null;
	if (jteap_dict) {
		var dicts = jteap_dict[catalog];
		if (dicts) {
			for (var i = 0;i < dicts.length; i++) {
				var dict = dicts[i];
				if (dict.key == key) {
					result = dict.value;
					break;
				}
			}
		}
	}
	return result;
}
/**
 * 根据字典类型以及字典value查询该字典的值
 */
function $dictKey(catalog, value) {
	var result = null;
	if (jteap_dict) {
		var dicts = jteap_dict[catalog];
		if (dicts) {
			for (var i = 0;i < dicts.length; i++) {
				var dict = dicts[i];
				if (dict.value == value) {
					result = dict.key;
					break;
				}
			}
		}
	}
	return result;
}
/**
 * 当前时间的长整型，一般用在ajax请求后，用于解决ajax缓存问题
 */
function $time(){
	return new Date().getTime();
}

/**
 * js map

function Map() {
	this.elements = new Array();
	this.size = function() {
		return this.elements.length;
	}
	this.isEmpty = function() {
		return (this.elements.length < 1);
	}
	this.clear = function() {
		this.elements = new Array();
	}
	this.put = function(_key, _value) {
		if (_key) {
			this.remove(_key);
		}
		this.elements.push( {
			key : _key,
			value : _value
		});
	}
	this.remove = function(_key) {
		var bln = false;
		try {
			for (i = 0;i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	}
	this.get = function(_key) {
		try {
			for (i = 0;i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return null;
		}
	}
	this.element = function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	}
	this.containsKey = function(_key) {
		var bln = false;
		try {
			for (i = 0;i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	}
	this.containsValue = function(_value) {
		var bln = false;
		try {
			for (i = 0;i < this.elements.length; i++) {
				if (this.elements[i].value == _value) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	}
	this.values = function() {
		var arr = new Array();
		for (i = 0;i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	}
	this.keys = function() {
		var arr = new Array();
		for (i = 0;i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	}
}
 */

/**
 * 服务器端new Date().getTime()取得的长整型数据是无时区信息的UTC时间
 * 但是客户端javascript中new Date(time)是以当前浏览所在时区作为基准生成的UTC+X时间
 * 导致两边时间表示不一致。
 * 根据指定的time取得相对于当前浏览器的UTC时间
 * time:服务器端产生的utc时间的time
 * return:客户端解析后生成的服务器端的UTC时间对象
 */
function $utctime(time){
	var dt = new Date(time);
	var localOffset = dt.getTimezoneOffset() * 60000;
	var utc = time + localOffset;
	var nd = new Date(utc); 
	return nd;
}

/**
 * 将html去标签，转换为文本
 */
function textToHtml(text){
    text=text.replace(/\r\n/g, "<br>");  
    return text;
}

/**
 * IFRAME 高度 宽度自适应
 */
function $ifAutoFix(id) {
	var iframeid = document.getElementById(id); // iframe id
	if (document.getElementById) {
		if (iframeid && !window.opera) {
			if (iframeid.contentDocument
					&& iframeid.contentDocument.body.offsetHeight) {
				iframeid.height = iframeid.contentDocument.body.offsetHeight;
				iframeid.width = iframeid.contentDocument.body.offsetWidth;
			} else if (iframeid.Document && iframeid.Document.body.scrollHeight) {
				iframeid.height = iframeid.Document.body.scrollHeight;
				iframeid.width = iframeid.Document.body.scrollWidth;
			}
		}
	}
}
/**
 * 取得指定IFRAME的内部DOCUMENT对象并返回
 */
function $ifDocument(id){
	var doc = null;
  if (document.all){//IE
       doc = document.frames[id].document;
    }else{//Firefox    
       doc = document.getElementById(id).contentDocument;
    }
    return doc;
}

/**
 * 获取指定IFRAME对象的内容
 * iframe string|object 字符串为iframe的id或name object表示是iframe对象本身
 * return iframe的内容
 */
function $ifContent(iframe){
	if(typeof iframe =='string'){
		iframe = $("iframe");
	}
	if (document.getElementById) {
		if (iframe && !window.opera) {
			if (iframe.contentDocument) {
				return iframe.contentDocument.body.innerHTML;
			} else if (iframe.Document) {
				return iframe.Document.body.innerHTML;
			}
		}
	}
}


/**
 * 产生一个新的编号
 */
var globalIdSeed = 0;
function $id(el,prefix){
	prefix = prefix || "jteap-gen";
    el = $(el);
    var id = prefix + (++globalIdSeed);
    return el ? (el.id ? el.id : (el.id = id)) : id;
}


function $request(){
   if(typeof(XMLHttpRequest)!='undefined')  
        return new XMLHttpRequest();  
    var axO=['Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.4.0',  
        'Msxml2.XMLHTTP.3.0', 'Msxml2.XMLHTTP', 'Microsoft.XMLHTTP'], i;  
    for(i=0;i<axO.length;i++)  
        try{  
            return new ActiveXObject(axO[i]);  
        }catch(e){}  
    return null;  
}


/**
 * 
 */
function $bool(v){
	var ret = false;
	if(typeof v == 'string'){
		ret = (v=='true' || v=='1' || v=='yes')
	}
	if(typeof v == 'boolean'){
		ret = v;
	}
	return ret;
}



/**
 * 异步加载js文件
 */
function $import(uri, callback , doc) {
	if(typeof doc == 'undefined')
		doc = document;
		
	if(!doc.parentWindow.importUrlList) doc.parentWindow.importUrlList = [];
	if(doc.parentWindow.importUrlList.indexOf(uri)>=0){
		return;
	}else{
		doc.parentWindow.importUrlList.push(uri);
	}
	
	var f = arguments.callee.caller;
	if (typeof f == "function") {
		var scriptEle = doc.getElementsByTagName("head")[0].appendChild(doc.createElement("script"));
		scriptEle.type = "text/javascript";
		scriptEle.src = uri;
		
		if (callback) {
			if (isIE) {
				scriptEle.onreadystatechange = function(){
					if (/loaded/.test(this.readyState)) {
						callback();
					}
				}
			} else {
				scriptEle.onload = callback;
			}
		}
	}
}


/**
 * 同步加载js文件,支持批量js文件同步加载
 * doc 可以为空 默认为document 
 */
function $import_syn(url,doc){
	if(doc == null) doc = document;
	if(!doc.parentWindow.importUrlList) doc.parentWindow.importUrlList = [];
	if(doc.parentWindow.importUrlList.indexOf(url)>=0){
		return;
	}else{
		doc.parentWindow.importUrlList.push(url);
	}
	var oXml = null;
	//执行url中指定的js中的script
	var _evalScript = function (url){
		if(oXml == null)
			oXml = $request();
		oXml.open('GET', url, false);  
	    oXml.send('');
	    //html中必须存在至少一个script标记，否则window.eval方法无效
		if(doc.getElementsByTagName("script").length<=0){
			doc.getElementsByTagName("head")[0].appendChild(doc.createElement("script"));
		}
		
	    //execScript在全局环境中执行脚本，而eval则只会在当前环境中执行脚本，所以可能会由于变量作用域问题出现错误
	    doc.parentWindow.execScript(oXml.responseText); 
	}
	
	if(doc == null)
		doc = document;
	//是否是单一的地址还是地址数组，做不同的处理
	if(typeof url == 'string'){
		_evalScript(url);
	}else{
		if(typeof url == 'object' && typeof url.sort == 'function' && typeof url.length == 'number'){
			for(var i=0;i<url.length;i++){
				_evalScript(url[i]);
			}
		}
	}
}

/**
 * 导入样式表
 */
function $importCss(url,doc){
	if(doc == null)
		doc = document;
		
	if(!doc.parentWindow.importUrlList) doc.parentWindow.importUrlList = [];
	if(doc.parentWindow.importUrlList.indexOf(url)>=0){
		return;
	}else{
		doc.parentWindow.importUrlList.push(url);
	}
	
	var fileref = doc.createElement("link") ;
	fileref.setAttribute("rel", "stylesheet"); 
	fileref.setAttribute("type", "text/css"); 
	fileref.setAttribute("href", url);
	if (typeof fileref!="undefined")
		doc.getElementsByTagName("head")[0].appendChild(fileref);

}

/**
 * 格式化文本->HTML
 * 单引号->&#34;
 * 双引号->&#39;
 * <符号->&lt;
 * >符号->&gt;
 * &符号->&amp;
 * "->&quot;
 * 
 */
function $escapeHtml(str){
	if(str == null) str = "";
	str = str.replace(/\&/g,"&amp;"); //输入框中显示双引号问题
	str = str.replace(/\'/g,"&#39;"); //输入框中显示双引号问题
	str = str.replace(/\"/g,"&#34;"); //输入框中显示双引号问题
	str = str.replace(/\</g,"&lt;"); //输入框中显示双引号问题
	str = str.replace(/\>/g,"&gt;"); //输入框中显示双引号问题
	str = str.replace(/\r\n/g,"<br/>"); //显示回车换行
	return str;	
}
/**
 * 反转义特殊符号
 */
function $unesacpeHtml(str){
	str = str.replace(/\&amp;/g,"&");
	str = str.replace(/\&#39;/g,"'");
	str = str.replace(/&#34;/g,"\"");
	str = str.replace(/&lt;/g,"<");
	str = str.replace(/&gt;/g,">");
	str = str.replace(/<br\/>/g,"\r\n");
	return str;
}

/**
 * 获取select输入控件的当前显示值
 */
function getComboRawValue(combo){
	var ops = combo.options;
	for(var i=0;i<ops.length;i++){
		var op = ops[i];
		if(op.selected == true){
			return op.text;
		}
	}
	return "";
}

/*
======================================================
TableUtil:谭畅:2006-4-14										
与Table相关的工具方法
======================================================
*/
function TableUtil(){

}

/**将items加入到指定的element对象中,在生成每一个cell时会调用指定的回调函数*/

TableUtil.addRows=function(oTable,items,callback){
	for(var i=0;i<items.length;i++){
		if(items[i]!=null && items[i]!=undefined && items[i]!=""){
			var oRow=oTable.insertRow();
			for(var j=0;j<callback.length;j++){
				var func=callback[j];
				if(typeof func=="function"){				
					var oCell=oRow.insertCell();
					var sCellValue=func(items[i],oCell);
					oCell.innerHTML=sCellValue;					
				}
			}		
		}
	}
}

/*删除表格的所有行*/

TableUtil.removeAllRows=function(oTable){
	while(oTable.rows.length>0){
		oTable.deleteRow();
	}
}
//确保导入了prototype.js
if(typeof Class == 'object'){

/**
 *伪模式窗口
 */
var FakeWindow = Class.create({
	
	/**
	 *构造函数
	 *@id 窗口id 可以为空,为空时，指定Date().getTime()作为id，如果指定相同id，则相同ID的窗口只会打开一次，如果为null，每次打开新的窗口
	 *@url 窗口地址
	 *@width 宽度
	 *@height 高度
	 *@param 参数对象
	 *@callback 窗口关闭后的回调函数
	 */
	initialize:function(config){
		if(config==null) config = {};
		this._id = config.id || new Date().getTime();
		this._url = config.url || "";
		this._width = config.width || 800;
		this._height = config.height || 600;
		this._title = config.title || "弹出窗口";
		this._param = config.baseParam || {};
		this._xscroll = config.xscroll || "auto";
		this._yscroll = config.yscroll || "auto";
		this._callback = config.callback || function(){};
		this._type = config.type || FakeWindow.TYPE_NORMAL;

		this._resizable = config.resizable?"yes":"no";
		this._status = config.status?"yes":"no";
		this._toolbar = config.toolbar?"yes":"no";
		this._menubar = config.menubar?"yes":"no";
		this._location = config.location?"yes":"no";
		this._scrollbars = config.scrollbars?"yes":"no";
		this._useIF = config.userIF || false;
	},
	/**
	 * 打开窗口
	 */
	show:function(){
		var fw = this;
		var win = FakeWindow.windowMap.get(this._id);
		if(win != null && typeof win == 'object' && typeof win.document == 'object' ){
			win.focus();
		}else{
			this.doOpen();
		}
	},
	/**
	 * 打开窗口
	 */
	doOpen:function(){
		var fw = this;
	
		var openUrl = this._url;
		var openParam = this._param;
		if(this._useIF){
			openUrl = contextPath + "/jteap/ModuleWindowForm.jsp?scrolling=no";
			openParam.title = this._title;
			openParam.url = this._url;
			openParam.opener = window;
		}
		//根据不同的类型打开不同的窗口
		if(this._type == FakeWindow.TYPE_NORMAL){
			var features = "height="+this._height+",width="+this._width+"," +
					"resizable="+this._resizable+",status="+this._status+",toolbar="+this._toolbar+"," +
					"menubar="+this._menubar+",location="+this._location+",scrollbars="+this._scrollbars+"";
			var win = window.open(this._url,"_blank",features);
			
			//注入窗口关闭事件，窗口关闭后，需要将该窗口从windowMap中移除
			win.attachEvent("onunload",function(){
				var retValue = win.returnValue;
				if(typeof fw._callback == 'function'){
					fw._callback(retValue);
				}
				FakeWindow.windowMap.unset(fw._id);
			});
			FakeWindow.windowMap.set(this._id,win);
		}else if(this._type == FakeWindow.TYPE_MODEL){
			var features = "dialogWidth:" + this._width + "px;dialogHeight:" + this._height
				+ "px;center:yes;status:"+this._status+";help:0;scroll:"+this._scrollbars+"";
			var ret = window.showModalDialog(openUrl,openParam,features);
			this._callback(ret);
		}else if(this._type == FakeWindow.TYPE_MODELESS){
			var features = "dialogWidth:" + this._width + "px;dialogHeight:" + this._height
				+ "px;center:yes;status:"+this._status+";help:0;scroll:"+this._scrollbars+"";
			var win = window.showModelessDialog(openUrl,openParam,features);
			win.param = this._param;
			//注入窗口关闭事件，窗口关闭后，需要将该窗口从windowMap中移除
			win.attachEvent("onunload",function(){
				var retValue = win.returnValue;
				if(typeof fw._callback == 'function'){
					fw._callback(retValue);
				}
				FakeWindow.windowMap.unset(fw._id);
			});
			FakeWindow.windowMap.set(this._id,win);
		}
	}
});
FakeWindow.TYPE_NORMAL = "T1";  	//普通窗口,采用open方式打开
FakeWindow.TYPE_MODEL = "T2";		//模式窗口,采用showModalDialog方式打开
FakeWindow.TYPE_MODELESS = "T3";  	//非模式窗口采用showModelessDialog方式打开
FakeWindow.windowMap = new Hash();
var $FW = FakeWindow;
}



