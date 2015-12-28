
/**
 * 全局的editorsMap容器,所有创建的组件将会放入该容器
 * <id,editor>
 */
var editorsMap = new Hash();
function getEditor(name){
	return editorsMap.get(name);
}

/**
 * 控件抽象类
 */
var AbstractEdirot = Class.create({
	/**
	 * name:控件名字
	 * param:控件参数字符串
	 * doc:指定渲染目标document
	 * status:状态,编辑状态|打印状态
	 * formSn:表示表单标识
	 * docid:当前记录编号
	 */
	initialize:function(name,param,status,doc,cp,formSn,docid){
		//value、style、readonly,evt
		this.id = $id("",this.wai()+"_");
		this.name = name;
		
		this.cp = (cp!=null && cp!='')?cp:name;//数据项中文caption，如果为空，则等于name
		this.formSn = formSn;
		this.evtEnum = this.eventEnum();
		this.st = status;				//状态 01|编辑 02|打印
		this.param = this._parseParam(param);
		
		this.evts = this.param.evts;	//控件配置中的事件集合
		this.vl = this.param.vl;		//验证表达式 正则表达式
		this.vltip = this.param.vltip;	//验证提示
		this.nn = this.param.nn;	//NOT NULL 
		this.unq = this.param.unq;
		this.doc = doc || document;
		this.docid = docid;
		this._value = this.param._value || '';
		this.value = this.param.value || '';
		
		this.avalible = true;		//控件是否有效，在渲染的时候有可能由于某些异常导致该控件无效，并通过该标识告诉用户控件无效
		this.init();
		this.evtMap = new Hash();//事件集合
		editorsMap.set(this.name,this);
	},
	
	init:function(){
	
	},
	/**
	 * 附加已经定义了的事件
	 */
	attachEvent:function(){
		var edr = this.wai();
		if(this.st == '02' && edr!='BTN') return;//打印状态不添加事件
		if(this.evts){ 
			for(var i = 0;i<this.evtEnum.length;i++){
				var evt = this.evtEnum[i];
				eval("var evtD = this.evts."+evt);
				if(evtD!=null && evtD.trim()!=''){
					try{
						//替换事件定义中的通配符,支持#{fd}
						evtD = new Template(evtD).evaluate({fd:this.name});
						
						//alert(evtD);
						this.doc.parentWindow.execScript(evtD);
						eval("var fn = this.doc.parentWindow.evt_"+this.name+"_"+evt);
						this.evtMap.set(evt,fn);
						if(this.dom!=null){
							this.dom.attachEvent(evt,fn);
						}
					}catch(ex){
						alert('控件'+this.name+'添加事件'+evt+'失败,\r\n'+evtD);
					}
				}
			}
		}
	},
	/**
	 * 触发指定的事件
	 */
	fireEvent:function(evt){
		var fn = this.evtMap.get(evt);
		if(typeof fn == 'function'){
			var args = [];
			for(var i=1;i<arguments.length;i++){
				args[i-1]=arguments[i];
			}
			return fn(this,args);
		}
	},
	/**
	 * 控件渲染成功之后调用该方法，该方法为抽象发法，在具体的控件中实现
	 */
	onRender:function(){
		
	},
	setFocus:function(){
		this.dom.focus();
	},
	/**
	 * 表单提交之前回调用该方法
	 */
	onBeforeSave:function(){
	
	},
	/**
	 * 事件数组，列出当前控件支持的事件名称集合，具体控件支持什么事件，控件自身提供
	 */
	eventEnum:function(){
		return [];
	},
	/**
	 * 解析参数对象
	 * 参数对象主要用于控件属性、事件等控件的描述信息
	 */
	_parseParam:function(param){
		if(param && typeof param == 'string'){
			param = new String(param);
			if(param.isJSON()){
				param = param.evalJSON();
			}else{
				param = {};
			}
		}
		return param;
	},
	/**
	 * what am i
	 */
	wai:function(){
		return "IDONOTKNOW";
	},
	/**
	 * 渲染控件html片段
	 */
	toHTML:function(){
		return "";
	},
	/**
	 * 渲染控件
	 */
	renderTo:function(el){
		var html = this.toHTML();
		if(el && el.innerHTML!=null){
			el.innerHTML = html;
			this.dom = el.firstChild;
			//附加事件
			this.attachEvent();
			this.onRender();
		}
	},
	/**
	 * 重新渲染,刷新
	 */
	reRender:function(){
		var el = this.dom.parentNode;
		if(el){
			el.innerHTML = "";
		}
		this.renderTo(el);
	},
	/**
	 * 获取控件标识
	 */
	getId:function(){
		return this.id;
	},
	/**
	 * 为控件设置值
	 */
	setValue:function(value){
		this.value = value;
		this.reRender();
	},
	getValue:function(){
		if(this.st != '02')
			this.value = this.dom.value;
		return this.value;
	},
	getRawValue:function() {
	
	},
	/**
	 * 验证控件的值，通过vl构建正则表达式验证控件的值
	 * 参数 ： 
	 * 		cp ： 字段名称
	 * 		is_Not_Valid_Null ： 是否不进行为空验证（默认不传参，值为undefined）
	 * 验证成功返回true 否则返回false
	 */
	validate:function(cp, is_Not_Valid_Null){
		var value = this.getValue();

		//是否为空验证
		if(this.nn == "true" && (value== null || value == '') && !is_Not_Valid_Null){
			if (cp == null || cp == '') {
				alert(this.name+"不能为空");
			} else {
				alert("字段【" + cp + "】为必填项，不能为空\r\n");
			}
			this.setFocus();
			return false;
		}
		
		//正则表达式验证
		if(this.vl!=null && this.vl!='' && value!=null && value!=''){
			try{
				eval("var regx = "+this.vl);
				if (!regx.test(value)) {
					alert(this.cp+":"+this.vltip);
					this.setFocus();
					return false;
				}
			}catch(e){}
		}
		var unique = true;
		//唯一性验证
		if($bool(this.unq) == true){
			var url = contextPath + "/jteap/form/eform/EFormAction!uniqueDiValidateAction.do";
			var param = {formSn:this.formSn,fd:this.name,docid:this.docid,fdValue:value};
			AjaxRequest_Sync(url,param,function(ajax){
				var ret = ajax.responseText.evalJSON();
				if(ret.success == false || ret.unique == false){
					unique = false;
				}
			});
		}
		if(unique ==  false){
			alert("字段【" + cp + "】的值在数据库中已经存在，请修改字段的值，确保数据的唯一性");
			this.setFocus();
			return false;
		}
		
		var ret = this.fireEvent("onValidate",this);
		if(ret == null){
			ret = true;
		}
		return ret;
	}
});


/**
 * 文本框控件
 */
var TXT = Class.create(AbstractEdirot,{
	init:function(){
		this.style = this.param.style;
		this.value = this.param.value;
		this.readonly = this.param.readonly;
		this.disabled = this.param.disabled;
		this.isPassword = this.param.isPassword;

		if(typeof this.value == 'number'){
			this.value = this.value+"";
		}
	},
	toHTML:function(){
		var html = "";
		if(this.st=='02'){
			var displayValue = ((this.isPassword == true)?"******":((this.value && this.value.length>0)?$escapeHtml(this.value):""));
			html = "<span style='padding:5;"+this.style+"'>"+displayValue+"</span>";
		}else{
			var type = ((this.isPassword == true)?"password":"textfield");
			html = "<input type='"+type+"' name='"+this.name+"' "+((this.disabled == 'true')?"disabled='true' ":" ") +
				((this.value && this.value.length>0)?"value='"+$escapeHtml(this.value)+"' ":"") +
				((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
				($bool(this.readonly)?"readonly":"") +
				" id='"+this.id+
				"'/>";
		}
		return html;
	},
	wai:function(){
		return "TXT";
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur"];
	}
});

/**
 * 文本框控件
 */
var TXTA = Class.create(AbstractEdirot,{
	init:function(){
		this.style = this.param.style;
		this.value = this.param.value;
	},
	toHTML:function(){
		var html = "";
		if(this.st=='02'){
			html = "" +
					"<div style='overflow-y:auto;line-height:1.6;"+this.style+"'>" +
						((this.value && this.value.length>0)?$escapeHtml(this.value):"") +
					"</div>";
		}else{   
			html = "<textarea name='"+this.name+"' " +((this.disabled == 'true')?"disabled='true' ":" ")+
				((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
				"id='"+this.id+
				"'/>"+
				((this.value && this.value.length>0)?this.value:"") +
				"</textarea>";
		}
		return html;
	},
	wai:function(){
		return "TXTA";
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur"];
	}
});

/**
 * 下拉框的数据来源方式可以为以下三种
 * list:
 * dict:
 * sql:
 * 加入三种属性均有设置，则优先级为list>dict>sql
 */
var COMB = Class.create(AbstractEdirot,{
	init:function(){
		this.list = this.param.list;//常量枚举数据列表 ID|NAME,ID|NAME....
		this.style = this.param.style;	//样式
		this.sql = this.param.sql;		//数据库查询 
		this.sqlKeyField = this.param.sqlKeyField || "KEY";
		this.sqlValueField = this.param.sqlValueField || "VALUE";
		this.dict = this.param.dict;	//数据字典
		this.value = this.param.value;	//默认值
		this.disabled = this.param.disabled;
		this.allowNull = this.param.allowNull;
		if(typeof this.allowNull == 'undefined'){
			this.allowNull = true;
		}
	},
	wai:function(){
		return "COMB";
	},
	toHTML:function(){
		var html = "<select id='"+this.id+"' " + ((this.disabled == 'true')?"disabled='true":"")+
				"' name='"+this.name+"' " +
				((this.style && this.style.length>0)?" style='"+this.style+"' ":"") + 
				">" +
				(this.allowNull?"<option value=''></option>":"") +
				"</select>";
		return html;
	},
	/**
	 * 渲染完成之后，初始化下拉框数据
	 */
	onRender:function(){
		if(this.list){
			this.initComboInList(this.list);
		}else if(this.dict){
			this.initComboInDict(this.dict);
		}else if(this.sql){
			this.initComboInSql(this.sql);
		}
		if(typeof this.value != 'undefined'){
			this.dom.value = this.value;
		}
		
		if(this.st == '02'){
			var rawValue = getComboRawValue(this.dom);
			var html = "<span style='padding:5';"+this.style+">"+((rawValue && rawValue.length>0)?$escapeHtml(rawValue):"")+"</span>";
			this.dom.parentNode.innerHTML = html;
		}
	},
	
	//设置下拉框是否可用
	//添加人：程志超
	//添加时间：2010-11-22 15:29:43
	setDisabled : function(flag){
		this.dom.disabled = flag;
	},
	getRawValue : function() {
		return this.dom.options[this.dom.selectedIndex].text
	},
	/**
	 * 根据list参数指定的常量值设置下拉框的备选数据
	 * ID|NAME,ID|NAME....
	 */
	initComboInList:function(list){
		var items = list.split(",");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item!=null && item!=''){
				var tmp = item.split("|");
				var key = tmp[0];
				var value = tmp[1];
				if(typeof value  == 'undefined'){
					value = key;
				}
				var oOption = this.doc.createElement("option");
				oOption.appendChild(this.doc.createTextNode(key));
				oOption.setAttribute("value", value);
				this.dom.appendChild(oOption);
			}
		}
	},
	/**
	 * 根据数据字典初始化下拉框
	 * 数据字典查询结果[{key:'',value:''},{}....]
	 */
	initComboInDict:function(dict){
		var dicts = $dictListAjax(dict);
		for(var i=0;i<dicts.length;i++){
			var dict = dicts[i];
			var oOption = this.doc.createElement("option");
			oOption.appendChild(this.doc.createTextNode(dict.key));
			oOption.setAttribute("value", dict.value);
			this.dom.appendChild(oOption);
		}
	},
	/**
	 * 根据SQL语句查询得到查询结果添加到下拉框中
	 */
	initComboInSql:function(sql){
		var url = contextPath + "/jteap/system/jdbc/JdbcAction!doQueryBySqlAction.do";
		var comb = this;
		AjaxRequest_Sync(url,{sql:sql},function(ajax){
			var responseObj = ajax.responseText.evalJSON();
			if(responseObj.success == true){
				var list = responseObj.list;
				for(var i=0;i<list.length;i++){
					var item = list[i];
					eval("var key = item."+comb.sqlKeyField);
					eval("var value = item."+comb.sqlValueField);
					var oOption = comb.doc.createElement("option");
					oOption.appendChild(comb.doc.createTextNode(key));
					oOption.setAttribute("value", value);
					comb.dom.appendChild(oOption);
				}
			}
		})
		//设置默认值value，并显示key(曹飞)
		if(typeof(this.value) != 'undefined' && this.value != null){
			for(var i = 0; i < comb.dom.options.length; i++){
				if(comb.dom.options[i].value == this.value){
					comb.dom.options[i].selected = true;
				}
			}
		}
	},
	eventEnum:function(){
		return ["onchange"];
	}
});

/**
 * 下拉文本框
 * 下拉框的数据来源方式可以为以下三种
 * list:
 * dict:
 * sql:
 * 加入三种属性均有设置，则优先级为list>dict>sql
 * 添加人：吕超
 */
var COMBTEXT = Class.create(AbstractEdirot,{
	init:function(){
		try{
			$import_syn(contextPath+"/script/adapter/ext/ext-base.js",this.doc);
			$importCss(contextPath + "/resources/css/ext-all.css",this.doc);
			$import_syn(contextPath+"/script/ext-all-debug.js",this.doc);
						
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
			
			//this.url = contextPath + this.param.url || '';
			this.width = this.param.width || 200;
			this.width = parseInt(this.width);
			//this.p_listWidth = this.param.listWidth || 200;
			//this.p_listWidth = parseInt(this.p_listWidth);
			//this.p_listHeight = this.param.listHeight || 200; 
			//this.p_listHeight = parseInt(this.p_listHeight);
			//this.emptyText = this.param.emptyText || '';
			//this.mode = this.param.mode||'local';
			//this.valueField = this.param.valueField||'text';
			//this.displayField = this.param.displayField||'value';
			//this.root = this.param.root;
			this.dict = this.param.dict;	//数据字典
			
		}catch(ex){
	        this.avalible = false;
		}
	},
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			html = "<div>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/>" +
						"<input name='hide_"+this.name+"'  type='text' value=''/></div>" +
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onSelect:function(node){
		this.fireEvent("onselect", this, node);
	},
	onRender:function(){
		if(this.list){
			this.initComboInList(this.list);
		}else if(this.dict){
			this.initComboInDict(this.dict);
		}else if(this.sql){
			this.initComboInSql(this.sql);
		}
		var editor = this;
		if(!this.avalible) return;
//		$import_syn(contextPath+"/script/ext-extend/form/ComboTree.js",this.doc);
//		$import_syn(contextPath+"/jteap/system/person/script/GroupNodeUI.js",this.doc);
//		$import_syn(contextPath+"/jteap/system/role/script/RoleNodeUI.js",this.doc);
		
		if(this.st == '02'){
			var rawValue = this.param.value;
			this.dom.parentNode.innerHTML = "<span " +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					">" +
					rawValue +
					"</span>";
		}else{
			this.combText = new Ext.form.ComboBox({
				id: 'ct_' + this.id,
				xtype:'combo', 
				store:this.store,  
              	valueField:'value',  
                displayField:'text',  
                mode:'local',  
                //height:700,
//              hiddenName:'userName', 
//         		editable:true,selectOnFocus:true,
                forceSelection:false,  
                triggerAction:'all',
                //fieldLabel:false,
                //hideLabel:true,
                //fieldLabel:'s',
                //anchor:'99%',
                listeners: {
						collapse: function(t,node){
							var txtValue = editor.dom.childNodes[0].childNodes[0];
							var txtHide = editor.dom.childNodes[0].childNodes[1];
							if(txtValue != null){
								txtValue.value = editor.combText.getRawValue();
								if(editor.combText.hideMode != "display"){
									txtHide.value = editor.combText.hideMode;
								}
							}
						},
						select:function(t,node){
							editor.onSelect(node);
						},
						blur:function(){
							var txtValue = editor.dom.childNodes[0].childNodes[0];
							var txtHide = editor.dom.childNodes[0].childNodes[1];
							if(txtValue != null){
								txtValue.value = editor.combText.getRawValue();
								if(editor.combText.hideMode != "display"){
									txtHide.value = editor.combText.hideMode;
								}
								this.combText.setValue(editor.combText.getRawValue());
							}
						}
					}
			})
		   	this.combText.render(this.id+"_div");
		   	$(this.name).value=this.value;
		   	this.combText.setValue(this.value);
			//alert(this.combTree.getValue()+"-----------");
		}
		
	},
	reRender:function(){
		var el = this.dom.parentNode;
		if(el){
			el.innerHTML = "";
		}
		this.renderTo(el);
	},
	getId:function(){
		return this.id;
	},
	wai:function(){
		return "combText";
	},	
	setValue:function(value){
		this.combText.setValue(value);
	},
	getValue:function(){
		return this.combText.getRawValue();
	},
	initComboInDict:function(dict){	
		var dicts = $dictListAjax(dict);
		var datas = new Array();
		for(var i=0;i<dicts.length;i++){
			var dict = dicts[i];
			var data = [dict.key,dict.value];
			datas[i] = data;
		}
		this.store = new Ext.data.SimpleStore({
	        fields: ['text','value'],
	        data : datas // from states.js
    	})
	},
	validate:function(cp, is_Not_Valid_Null){
		var value = this.combText.getValue();
		if(this.nn == "true" && (value== null || value == '') && !is_Not_Valid_Null){
			if (cp == null || cp == '') {
				alert(this.name+"不能为空");
			} else {
				alert("字段【" + cp + "】为必填项，不能为空\r\n");
			}
			this.combText.focus();
			return false;
		}
		return true;
	},
	eventEnum:function(){
		return ["onselect"];
	}
});

/**
 * 按钮控件
 */
var BTN = Class.create(AbstractEdirot,{
	init:function(){
		this.value = this.param.value;
		this.style = this.param.style;
		this.showInPrint = this.param.showInPrint || false;	//是否在打印的时候显示该按钮
	},
	wai:function(){
		return "BTN";
	},
	toHTML:function(){
		var html = "";
		if(this.st!='02' || $bool(this.showInPrint) == true){
			html = "<button id='"+this.id+"' name='"+this.name+"'" +
						(this.style?" style='"+this.style+"'":"")+
					">"+this.value+"</button>";
		}
		return html;
	},
	onRender:function(){
	
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur"];
	}
});

/**
 * 日历控件
 */
var CAL = Class.create(AbstractEdirot,{
	init:function(){
		var doc = this.doc;
		this.style = this.param.style;	//样式
		this.value = this.param.value || "";	//样式
		this.minDate = this.param.minDate || "1900-01-01 00:00:00";
		this.maxDate = this.param.maxDate || "2099-12-31 23:59:59";
		this.readonly = (this.param.readonly || this.param.readonly == 'true')?true:false;
		this.disabled = this.param.disabled;
		this.fm = this.param.fm || 'yyyy-MM-dd';
		
		if(typeof doc.parentWindow.WdatePicker == 'undefined'){
			var url = contextPath + "/component/My97DatePicker/WdatePicker.js";
			//该JS组件必须采用异步JS的加载方式才成功，具体原因目前不清
			$import(url,function(){
				var pickerLoaded = false;
				
				try{
				//My97控件在嵌入页面中的时候，主页面初次加载时，必须初始化一次，但是刷新IFRAME的时候不需要初始化该组件，否则该日历会显示异常
					//alert("看状态:"+doc.parentWindow.parent.parent.pickerLoaded);
				if(doc.parentWindow.parent.parent.parent && typeof doc.parentWindow.parent.parent.parent.pickerLoaded!='undefined'){
					pickerLoaded = true;
				}}catch(e){alert(e);}
				
				if(!pickerLoaded){
					try{
						doc.parentWindow.WdatePicker();
					}catch(e){
					
					}
					doc.parentWindow.parent.parent.parent.pickerLoaded = true;
					//alert("设置成功："+doc.parentWindow.parent.parent.pickerLoaded);
				}
			},doc);
		}
		
		//可以通过{time:2234234}对象进行初始化日期
		if(typeof this.value == "object" && this.value.time){
			var dt = new Date(this.value.time);
			//var dt = new Date($utctime(this.value.time));
			this.value = formatDate(dt,this.fm);
		}
	},
	wai:function(){
		return "CAL";
	},
	toHTML:function(){
		var html = "";
		if(this.st=='02'){
			var html = "<span style='padding:5;"+this.style+"'>"+((this.value && this.value.length>0)?$escapeHtml(this.value):"")+"</span>";
		}else{
			
			
			var html = "<input class='Wdate' type='text' name='"+this.name+"' " +((this.disabled == 'true')?"disabled='true' ":" ")+
					((this.value && this.value.length>0)?"value='"+this.value+"' ":"value=''") +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					((this.readonly && this.readonly==true)?"readonly ":"") +
					" id='"+this.id+"'" +
					" onfocus=\""+(this.readonly?"return;":"")+"WdatePicker({" +
					"	dateFmt:'"+this.fm+"'," +
					"	skin:'default'," +
					"	minDate:'"+this.minDate+"'," +
					"	maxDate:'"+this.maxDate+"'," +
					"	doubleCalendar:false," +
					"	autoPickDate:false,"+
					"	isShowWeek:false," +
					"	isShowClear:false," +
					"	position:''," +
					"	firstDayOfWeek:1" +
					"})\"/>";
		}
		return html;
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur"];
	}
});

/**
 * 上传控件
 */
var UPL = Class.create(AbstractEdirot,{
	init:function(){
		this.value = this.param.value;	//值，主要是附件ID
		this.style = this.param.style;	//样式
		this.attach =null;
		if(this.value!=null){
			this._initAttach();
		}
	},
	toHTML:function(){
		var html = ""; 
		if(this.attach!=null){
			var downloadUrl = contextPath + "/EFormFjAction!downloadFj.do?id="+this.attach.id;
			// 编辑状态
			if(this.st == '01') {
				html = "<span id='"+this.id+"' style='padding:5px'><a href='#' onclick=window.open('" + downloadUrl + "');>"+this.attach.name+"</a><span style='width:5px'></span><a href='javascript:void(0)' attachId='"+this.value+"'>重新上传</a></span>";
			// 打印状态
			}else if(this.st == '02') {
				html = "<span id='"+this.id+"' style='padding:5px'><a href='#' onclick=window.open('" + downloadUrl + "');>"+this.attach.name+"</a></span>";;
			}
		}else{
			if(this.st == '02'){
				html = "";
			}else{
				html = "<input id='"+this.id+"' type='file' name='"+this.name+"' " +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					"id='"+this.id+
					"'/>";
			}
		}
		return html;
	},
	onRender:function(){
		if(this.st == '02' || this.st == '03') return;
		var upl = this;
		//如果已经存在有上传过的附件，则添加重新上传功能按钮
		if(this.attach!=null){
			var btn = this.dom.childNodes[2];
			/**
				 * 重新上传附件
				 * 1.ajax删除已经上传的附件
				 * 2.重新渲染组件
				 */
			btn.attachEvent("onclick",function(event){
				if(!confirm("确定需要删除该附件，重新上传附件吗？")){
					return;
				}
				var url = contextPath + "/jteap/form/eform/EFormFjAction!removeAction.do";
				AjaxRequest_Sync(url,{ids:upl.value},function(ajax){
					var obj = ajax.responseText.evalJSON();
					if(obj.success==true){
						alert("附件删除成功");
						upl.attach=null;
						upl.reRender();
					}
				})
				
			});
		}
		
	},
	wai:function(){
		return "UPL";
	},
	/**
	 * 初始化附件对象
	 * 请求服务器生成附件对象
	 * {id:'',name:'',type:'',dt:'',creator:'',path:''}
	 */
	_initAttach:function(){
		var url = contextPath + "/jteap/form/eform/EFormFjAction!getEFormFjByIdAction.do?id="+this.value;
		var upl = this;
		AjaxRequest_Sync(url,null,function(ajax){
			var responseObject = new String(ajax.responseText).evalJSON();
			if(responseObject.success == true){
				upl.attach = responseObject.data;
			}else{
				upl.attach = null;
			}
		});
	}
});



/**
 * HTML富文本框控件FCK
 */
var HTML = Class.create(AbstractEdirot,{
	init:function(){
		this.value = this.param.value || "";
		this.style = this.param.style || "";
		this.width = this.param.width || "100%";
		this.height = this.param.height || "200";
	},
	toHTML:function(){
		if(this.st=='02'){
			return "<div style='overflow-y:auto;height:"+this.height+"'" + 
					">" +
					((this.value && this.value.length>0)?(this.value):"") +
					"</div>";
		}
		var html = "<textarea name='"+this.name+"' " +
				((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
				"id='"+this.id+
				"'/>"+$escapeHtml(this.value)+"</textarea>";
		return html;
	},
	wai:function(){
		return "HTML";
	},
	onRender:function(){
		if(this.st == '02') return;
		var url = contextPath + "/component/FCKeditor/fckeditor.js";
		var html = this;
		var doc = this.doc;
		
		$import(url,function(){
			html.renderHTML();
		},doc);
		
	},
	renderHTML:function(){
		var doc = this.doc;
		fckEditor = new doc.parentWindow.FCKeditor(this.id);// 初始化FCK
		fckEditor.Height = this.height;// 设置FCK编辑器的高度
		fckEditor.Width = this.width;// 设置FCK编辑器的宽度
		fckEditor.ToolbarSet = "Jteap";
		fckEditor.BasePath = contextPath + "/component/FCKeditor/";// 设定FCK的源文件路径，这里要注意相对和绝对路径
		fckEditor.ReplaceTextarea();// 用FCK编辑器替换Ext中的textarea
	}
});



/**
 * 从表控件
 * 从表控件支持
 * TXT、COMB、RADIO、CHECKBOX
 */
var SUB = Class.create(AbstractEdirot,{
	delArr: [],               //从表删除数据
	init:function(){
		try{
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
	
			this.width = this.param.width || '100%';
			try{
				this.p_columns = eval(this.param.subTableCM || '[]');
			}catch(ex){
				alert('列模型定义错误');
				throw ex;
			}
			
			this.p_subTableName = this.param.subTableName;//子表表明,可带schema一起
			this.p_fkFieldName = this.param.subTableFK;//外键字段名
			this.p_pkFieldName = this.param.subTablePK;	//主键字段名
			this.p_clicksToEdit = this.param.clicksToEdit || 2;
			this.p_width = this.param.width || 400;
			this.p_width = parseInt(this.p_width);
			this.p_height = this.param.height  || 200;
			this.p_height = parseInt(this.p_height);
			
			this.p_where = this.param.subTableWhere || "";	//扩展过滤条件
			this.p_order = this.param.subTableOrder || "";
			this.p_bShowTB = this.param.showToolBar;
			if(this.p_bShowTB==null)
				this.p_bShowTB = true;
			this.docid = this.param.docid;
		}catch(ex){
	        this.avalible = false;
		}
	},
	
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			html = "<div>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/></div>" +
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onRender:function(){
		if(!this.avalible) return;
		//渲染EXT Grid
		var sub = this;
		this.csm= new Ext.grid.CheckboxSelectionModel();
		$import_syn(contextPath+"/jteap/form/eform/script/eformEditorSubGrid.js",this.doc);
		var config = {
				width:sub.p_width,
		   		height:sub.p_height
			};
		if(this.st == '02'){
			sub.grid = new SubGrid({
		   		id:'sg_'+sub.id,
		   		width:sub.p_width,
		   		height:sub.p_height,
		   		listeners:{
		   		}
	   		});
		}else{
			sub.grid = new SubEditGrid({
		   		id:'sg_'+sub.id,
		   		width:sub.p_width,
		   		height:sub.p_height,
		   		clicksToEdit :sub.p_clicksToEdit,
		   		bShowToolBar:(sub.p_bShowTB == true || sub.p_bShowTB=='true'),
		   		sm:sub.csm,
		   		listeners:{
		   			afteredit :function(e){
		   				sub.fireEvent("afteredit",e);
		   			},
		   			beforeedit  :function(e){
		   				sub.fireEvent("beforeedit",e);
		   			}
		   		}
	   		});
		}
		
	   	sub.grid.editor = sub;
	   	sub.grid.render(sub.id+"_div");
	   	var cm = null;
	   	if(this.st=='02'){
	   		cm = sub._buildReadOnlyCm();
	   	}else{
	   		cm = sub._buildEditableCm();
	   	}
 		var ds = sub._buildDs();
 		sub.grid.updateData(cm,ds);
	},
	wai:function(){
		return "SUB";
	},
	/**
	 * 创建新的记录
	 */
	createNew:function(order){
		var rec = this.grid.createNewRecord();
		var bOK = this.fireEvent("onadd",rec);
		if(typeof bOK == 'undefined' || bOK == true){
			this.grid.createNew(rec,order);
		}
	},
	
	/**
	 * 取值,获取该组件的值，该值将会根据bJustDirty判断是否包含所有数据还是只包含修改过的数据
	 */
	getValue:function(bJustDirty){
		if(typeof bJustDirty == 'undefined')
			bJustDirty = false;
		var data = [];
		
		var records = this.grid.store.getRange();
		for (var i = 0;i < records.length; i++) {
			//盘点明细时 dirty为false 无法保存 暂时采用此方法过滤 （吕超 2011.10.13）
			if(this.cp=="盘点明细"){
				data.push(records[i].data);
			}else if(records[i].dirty==true){
				data.push(records[i].data);
			}
		}
		var json = "";
		if(data.length>0){
			json = Ext.encode(data);
			
		}
		return json;
	},
	onBeforeSave:function(){
		var ret = this.fireEvent("onsave",this.grid.getStore());
		var json = this.getValue(true);
		if(getEditor("SFYRZ") && getEditor("SFYRZ").getValue()){
			getEditor("XJSJSZ").setValue(json);
		}
		this.dom.firstChild.firstChild.value = json;
	},
	/**
	 * 单独保存子表
	 */
	doSave:function(){
		if(this.docid == null || this.docid == ''){
			alert('主表没保存，请先保存主表');
			return;
		}
		var ret = this.fireEvent("onsave",this.grid.getStore());
		var json = this.getValue();
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecSaveSubAction.do";
		var sub = this;
		AjaxRequest_Sync(url,{json:json,docid:this.docid,pkFieldName:this.p_pkFieldName,fkFieldName:this.p_fkFieldName,subTableName:this.p_subTableName},function(ajax){
			var obj = ajax.responseText.evalJSON();
			if(obj.success == true){
				//alert('保存成功');
				sub.grid.store.reload();
			}else{
				alert('保存失败');
			}
		});
	},
	getSelections:function(){
		var items = this.grid.selModel.getSelections();
		return items;
	},
	/**
	 * 刷新
	 */
	doRefresh:function(){
		this.grid.store.reload();
	},
	/**
	 * 删除操作
	 */
	doDelete:function(){
		var ret = "";
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecDeleteSubAction.do";
		var ids = "";
		var items = this.grid.selModel.getSelections();
		this.fireEvent("ondelete");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item.json){
				if(getEditor("SFYRZ")){
					if(getEditor("SFYRZ").getValue()){
					this.delArr = [];
					this.delArr.push("{wzmc:'"+item.data.WZMC+"'");
					this.delArr.push("xhgg:'"+item.data.XHGG+"'");
					this.delArr.push("sqsl:'"+item.data.SQSL+"'");
					this.delArr.push("jldw:'"+item.data.JLDW+"'");
					this.delArr.push("remark:'"+item.data.REMARK+"'}");
						if(items.length == 1){
							ret = this.delArr;
						}else{
							if(i == 0){
								ret = this.delArr;
							}else{
								ret = ret + (","+this.delArr);
							}
						}
					}				
				}
				var id = eval("item.json."+this.p_pkFieldName);
				ids+=(id+",");
			}else{
				this.grid.store.remove(item);
			}
		}
		if(getEditor("SFYRZ") && getEditor("SFYRZ").getValue()){
			getEditor("SCSJSZ").setValue("["+ret+"]");
		}
		if(ids!='' && confirm("确定取消删除选中的记录数据吗？")){
			var sub = this;
			AjaxRequest_Sync(url,{ids:ids,subTableName:this.p_subTableName,pkFieldName:this.p_pkFieldName},function(ajax){
				var obj = ajax.responseText.evalJSON();
				if(obj.success == true){
					alert('删除成功');
					sub.grid.store.reload();
				}else{
					alert('删除失败');
				}
			})
		}
	},
	//构建列模型对象
	_buildEditableCm:function(){
		var tmpArray=new Array();
		tmpArray.push(this.csm);
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			if(!cols[i].hidden || cols[i].hidden==false || cols[i].hidden=='false'){
				var tmpCm={"id":cols[i].id,"header":cols[i].header,"width":cols[i].width,"sortable":cols[i].sortable || false,"dataIndex":cols[i].dataIndex};
				var editor = cols[i].editor;
				try{
					if(editor!=null)
					      tmpCm.editor = new Ext.ComponentMgr.create(editor);
				}catch(ex){
					alert(ex.message);
				}
				var render = cols[i].renderer;
				if(render!=null)
					tmpCm.renderer = render;
				
				if(cols[i].align)
				    tmpCm.align = cols[i].align;
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	},//构建列模型对象
	_buildReadOnlyCm:function(){
		var tmpArray=new Array();
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			if(!cols[i].hidden || cols[i].hidden==false || cols[i].hidden=='false'){
				var tmpCm={"id":cols[i].id,"header":cols[i].header,"width":cols[i].width,"sortable":false,"dataIndex":cols[i].dataIndex};
				var render = cols[i].renderer;
				if(render!=null)
					tmpCm.renderer = render;
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	},
	//构建数据源对象
	_buildDs: function(){
		var subgrid = this;
		var colArray=new Array();
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			var fd = cols[i].dataIndex;
			colArray.push(fd);
		}
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecGetSubDataAction.do";
		var ds = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: url
	        }),
	        baseParams :{
	        	subTableName:this.p_subTableName,
	        	fkFieldName:this.p_fkFieldName,
	        	docid:this.docid,
	        	where:this.p_where,
	        	subTableOrder:this.p_order
	        },
	        listeners:{
	        loadexception:function(a){
	        	alert('数据加载失败');
	        } 
	        },
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount'
	        }, colArray),
	        remoteSort: false,
	        listeners : {
	        	load : function() {
	        		subgrid.fireEvent("onload",this);
	        	}
	        }
	    });
		return ds;
	},
	eventEnum:function(){
		return ["onadd","ondelete","afteredit","beforeedit","onsave","onValidate", "onload"];
	}
});
		

/**
 * 文本框控件
 */
var LABL = Class.create(AbstractEdirot,{
	init:function(){
		this.style = this.param.style;
		this.value = this.param.value;
	},
	getValue:function(){
		return this.value;
	},
	toHTML:function(){
		if(typeof this.value == 'undefined'){
			this.value = "";
		}
		var html = "<span " +
				((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
				">"+this.value+"</span>";
		return html;
	},
	wai:function(){
		return "LABL";
	},
	eventEnum:function(){
		return [];
	}
});

/**
 * 上传控件
 */
var IMG = Class.create(AbstractEdirot,{
	init:function(){
		this.value = this.param.value;	//值，主要是附件ID
		this.style = this.param.style;	//样式
		this.width = this.param.width;	//宽度
		this.height = this.param.height;//高度 
	},
	getValue:function(){
		if(this.st != '02'){
			return this.dom.value || this.value;
		}else
			return this.value;
	},
	toHTML:function(){
		var html = "";
		if(this.value == null || this.value == ''){
			if(this.st == '02') return "";
			html = "<input type='file' name='"+this.name+"'" +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					" id='"+this.id+"'"+
					"></input>";
		}else{
			var url = this.value;
			var attachId = "";
			var idx =this.value.indexOf("id:"); 
			if(idx>=0){
				attachId = this.value.substr(3);
				url =contextPath + "/EFormFjAction!downloadImageFj.do?id="+ attachId;
			}
			html = "<span id='"+this.id+"'><img src='"+ url +"' " +
					(this.height == null?"":"height='"+this.height+"' ") +
					(this.width == null?"":"width='"+this.width+"' ") +
					" valign='absmiddle'></img>" +
					(this.st == '02'?"":"<br/><button attachId='"+attachId+"'>重新上传</button>") +
					"</span>"
		}
		return html;
	},
	onRender:function(){
		if(this.st == '02') return;
		//如果已经存在有上传过的附件，则添加重新上传功能按钮
		if(this.value!=null && this.value!=''){
			var btn = this.dom.childNodes[3];
			/**
				 * 重新上传附件
				 * 1.ajax删除已经上传的附件
				 * 2.重新渲染组件
				 */
			var img = this;
			btn.attachEvent("onclick",function(event){
				if(!confirm("确定需要删除该图片，重新上传图片吗？")){
					return;
				}
				var url = contextPath + "/jteap/form/eform/EFormFjAction!removeAction.do";
				var attachId = event.srcElement.attachId;
				if(attachId == null || attachId == ''){
					this.value = null;
					this.reRender();
				}else{
					AjaxRequest_Sync(url,{ids:attachId},function(ajax){
						var obj = ajax.responseText.evalJSON();
						if(obj.success==true){
							alert("附件删除成功");
							img.value = null;
							img.reRender();
						}
					})
				}
				
				
			});
		}else{
			
		}
	},
	wai:function(){
		return "IMG";
	}
});



/**
 * 签名框控件
 */
var SIGN = Class.create(AbstractEdirot,{
	init:function(){
		this.style = this.param.style;
		this.value = this.param.value;
		this.disabled = this.param.disabled;
	},
	toHTML:function(){
		var html = "";
		if(this.st=='02'){
			html = "<span " +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					">" +
					((this.value && this.value.length>0)?$escapeHtml(this.value):"") +
					"</span>";
		}else{
			html = "<input type='text' name='"+this.name+"' " + ((this.disabled == 'true')?"disabled='true' ":" ") +
				((this.value && this.value.length>0)?"value='"+$escapeHtml(this.value)+"' ":"") +
				((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
				" id='"+this.id+ "'" +
				" readonly/>";
		}
		return html;
	},
	wai:function(){
		return "SIGN";
	},
	onRender:function(){
		var sign = this;
		this.dom.attachEvent("onclick",function(){
			sign.doSign();
		});
	},
	eventEnum:function(){
		return ["onsign"];
	},
	doSign:function(){
		if (this.st != '02') {
			var url = contextPath + "/jteap/form/eform/eformRecSign.jsp"
			var dt = showModule(url, "yes",300, 140, "");
			if(dt!=null){
				this.setValue(dt.userName);
				this.fireEvent("onsign",dt);
			}
		}
	}
	
});


/**
 * 复选框控件
 */
var CHK = Class.create(AbstractEdirot,{
	init:function(){
		this.list = this.param.list;//常量枚举数据列表 ID|NAME,ID|NAME...
		this.style = this.param.style;	//样式
		this.txtStyle = this.param.txtStyle || 'font-size:9pt;';	//文本样式
		this.sql = this.param.sql;		//数据库查询 
		this.sqlKeyField = this.param.sqlKeyField || "KEY";
		this.sqlValueField = this.param.sqlValueField || "VALUE";
		this.dict = this.param.dict;	//数据字典
		this.value = this.param.value;	//默认值
		this.allowNull = this.param.allowNull;
		if(typeof this.allowNull == 'undefined'){
			this.allowNull = true;
		}
	},
	wai:function(){
		return "CHK";
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur"];
	},
	toHTML:function(){
		var html = "<span id='"+this.id+"'>" +
				"</span>";
		return html;
	},
	/**
	 * 渲染完成之后，初始化下拉框数据
	 */
	onRender:function(){
		if(this.list){
			this.initChkInList(this.list);
		}else if(this.dict){
			this.initChkInDict(this.dict);
		}else if(this.sql){
			this.initChkInSql(this.sql);
		}
		if(typeof this.value != 'undefined'){
			this.dom.value = this.value;
		}
		//打印状态
		if(this.st == '02'){
			var rawValue = this.getChkText();
			this.dom.parentNode.innerHTML = "<span style='padding:5px'>" +
					rawValue +
					"</span>";
		}
	},
	getValue:function(){
		var chk = this.dom;
		var str = "";
		for(var i=0;i<chk.all.length/2;i++){
			var obj = chk.all[i*2]
			if(this.value!=null)
				if((","+this.value+",").indexOf(","+obj.value+",")!=-1){
					str += ","+chk.all[i*2].value;
				}
		}
		return str.substring(1);
	},
	getChkText:function(){
		var chk = this.dom;
		var str = "";
		for(var i=0;i<chk.all.length/2;i++){
			var obj = chk.all[i*2]
			if(this.value!=null)
				if((","+this.value+",").indexOf(","+obj.value+",")!=-1){
					str += ","+chk.all[i*2+1].innerText;
				}
		}
		return str.substring(1);
	},
	/**
	 * 根据list参数指定的常量值设置下拉框的备选数据
	 * ID|NAME,ID|NAME....
	 */
	initChkInList:function(list){
		var items = list.split(",");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item!=null && item!=''){
				var tmp = item.split("|");
				var key = tmp[0];
				var value = tmp[1];
				if(typeof value  == 'undefined'){
					value = key;
				}
				var input = this.doc.createElement("input");
				input.setAttribute("type", "checkbox");
				input.setAttribute("name", this.name);
				input.setAttribute("value", value);
				input.setAttribute("style", this.style);
//				alert((","+this.value+",").indexOf(","+value+","));
				if((","+this.value+",").indexOf(","+value+",")!=-1)
					input.setAttribute("defaultChecked", "true");
				
				var font = this.doc.createElement("<span>");
				font.setAttribute("style",this.txtStyle);
				font.innerHTML = key;
				
				this.dom.appendChild(input);
				this.dom.appendChild(font);
			}
		}
	},
	/**
	 * 根据数据字典初始化下拉框
	 * 数据字典查询结果[{key:'',value:''},{}....]
	 */
	initChkInDict:function(dict){
		var dicts = $dictListAjax(dict);
		for(var i=0;i<dicts.length;i++){
			var dict = dicts[i];
			var input = this.doc.createElement("input");
			input.setAttribute("type", "checkbox");
			input.setAttribute("name", this.name);
			input.setAttribute("value", dict.value);
			input.setAttribute("style", this.style);
			if((","+this.value+",").indexOf(","+dict.value+",")!=-1)
					input.setAttribute("defaultChecked", "true");
			
			var font = this.doc.createElement("<span>");
			font.setAttribute("style",this.txtStyle);
			font.innerHTML = dict.key;
				
			this.dom.appendChild(input);
			this.dom.appendChild(font);
		}
	},
	/**
	 * 根据SQL语句查询得到查询结果添加到下拉框中
	 */
	initChkInSql:function(sql){
		var url = contextPath + "/jteap/system/jdbc/JdbcAction!doQueryBySqlAction.do";
		var comb = this;
		AjaxRequest_Sync(url,{sql:sql},function(ajax){
			var responseObj = ajax.responseText.evalJSON();
			if(responseObj.success == true){
				var list = responseObj.list;
				for(var i=0;i<list.length;i++){
					var item = list[i];
					
					eval("var key = item."+comb.sqlKeyField);
					eval("var value = item."+comb.sqlValueField);
					var input = comb.doc.createElement("input");
					input.setAttribute("type", "checkbox");
					input.setAttribute("name", comb.name);
					input.setAttribute("value", value);
					input.setAttribute("style", comb.style);
					if((","+comb.value+",").indexOf(","+value+",")!=-1)
						input.setAttribute("defaultChecked", "true");
					var font = comb.doc.createElement("<span>");
					font.setAttribute("style",comb.txtStyle);
					font.innerHTML = key;
					comb.dom.appendChild(input);
					comb.dom.appendChild(font);
				}
			}
		})
	}
});
/**
 * 单选框控件
 */
var RAD = Class.create(AbstractEdirot,{
	init:function(){
		this.list = this.param.list;//常量枚举数据列表 ID|NAME,ID|NAME...
		this.style = this.param.style;	//样式
		this.txtStyle = this.param.txtStyle || 'font-size:9pt;';	//样式
		this.sql = this.param.sql;		//数据库查询 
		this.sqlKeyField = this.param.sqlKeyField || "KEY";
		this.sqlValueField = this.param.sqlValueField || "VALUE";
		this.dict = this.param.dict;	//数据字典
		this.value = this.param.value;	//默认值
		this.allowNull = this.param.allowNull;
		if(typeof this.allowNull == 'undefined'){
			this.allowNull = true;
		}
	},
	wai:function(){
		return "RAD";
	},
	toHTML:function(){
		var html = "<span id='"+this.id+"'>" +
				"</span>";
		return html;
	},
	/**
	 * 渲染完成之后，初始化下拉框数据
	 */
	onRender:function(){
		if(this.list){
			this.initRadInList(this.list);
		}else if(this.dict){
			this.initRadInDict(this.dict);
		}else if(this.sql){
			this.initRadInSql(this.sql);
		}
		if(typeof this.value != 'undefined'){
			this.dom.value = this.value;
		}
		
		if(this.st == '02'){
			var rawValue = this.getRadValue(this.dom);
			this.dom.parentNode.innerHTML = "<span style='padding:5px'>" +
					rawValue +
					"</span>";
		}
	},
	getRadValue:function(chk){
		for(var i=0;i<chk.all.length/2;i++){
			var obj = chk.all[i*2];
			if(this.value!=null)
				if(obj.value == this.value){				
					return chk.all[i*2+1].innerText;
				}
		}
		return "";
	},
	/**
	 * 根据list参数指定的常量值设置下拉框的备选数据
	 * ID|NAME,ID|NAME....
	 */
	initRadInList:function(list){
		var items = list.split(",");
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item!=null && item!=''){
				var tmp = item.split("|");
				var key = tmp[0];
				var value = tmp[1];
				if(typeof value  == 'undefined'){
					value = key;
				}
				
				/*
				var input = this.doc.createElement("input");
				input.setAttribute("type", "radio");
				input.setAttribute("name", this.name);				
				input.setAttribute("value", value);
				input.setAttribute("style", this.style);
				if(value==this.value)
					input.setAttribute("defaultChecked","true");
				*/
				var input = this.doc.createElement('<input '+(value==this.value?'checked':'')+' type="radio" name="'+this.name+'" value="'+value+'"/>');
				var font = this.doc.createElement("<span>");
				font.setAttribute("style",this.txtStyle);
				font.innerHTML = key;
				
				this.dom.appendChild(input);
				this.dom.appendChild(font);
			}
		}
	},
	/**
	 * 根据数据字典初始化下拉框
	 * 数据字典查询结果[{key:'',value:''},{}....]
	 */
	initRadInDict:function(dict){
		var dicts = $dictListAjax(dict);
		for(var i=0;i<dicts.length;i++){
			var dict = dicts[i];
			
			/*
			 * 这种方式在IE7下会无法点击单选框
			var input = this.doc.createElement("input");
			input.setAttribute("type", "radio");
			input.setAttribute("name", this.name);
			input.setAttribute("value", dict.value);
			input.setAttribute("style", this.style);
			if(dict.value==this.value)
				input.setAttribute("defaultChecked","true");
			*/
			var input = this.doc.createElement('<input '+(dict.value==this.value?'checked':'')+' type="radio" name="'+this.name+'" value="'+dict.value+'"/>');
			var font = this.doc.createElement("<span>");
			font.setAttribute("style",this.txtStyle);
			font.innerHTML =  dict.key;
			
			this.dom.appendChild(input);
			this.dom.appendChild(font);			
		}
	},
	/**
	 * 根据SQL语句查询得到查询结果添加到下拉框中
	 */
	initRadInSql:function(sql){
		var url = contextPath + "/jteap/system/jdbc/JdbcAction!doQueryBySqlAction.do";
		var comb = this;
		AjaxRequest_Sync(url,{sql:sql},function(ajax){
			var responseObj = ajax.responseText.evalJSON();
			if(responseObj.success == true){
				var list = responseObj.list;
				for(var i=0;i<list.length;i++){
					var item = list[i];
					eval("var key = item."+comb.sqlKeyField);
					eval("var value = item."+comb.sqlValueField);
					/*
					var input = comb.doc.createElement("input");
					input.setAttribute("type", "radio");
					input.setAttribute("name", comb.name);
					input.setAttribute("value", value);
					input.setAttribute("style", comb.style);
					if(value==comb.value)
						input.setAttribute("defaultChecked","true");
					*/
					var input = this.doc.createElement('<input '+(value==this.value?'checked':'')+' type="radio" name="'+comb.name+'" value="'+value+'"/>');
					var font = this.doc.createElement("<span>");
					font.setAttribute("style",this.txtStyle);
					font.innerHTML = key;
					comb.dom.appendChild(input);
					comb.dom.appendChild(font);					
				}
			}
		})
	}
});
/**
 * 文本框控件
 */
  var TXTE = Class.create(AbstractEdirot,{
	init:function(){
		this.style = this.param.style;
		this.value = this.param.value || '';
		this.readonly = this.param.readonly;
		this.inputStyle = this.param.inputStyle;
		this.btnText = this.param.btnText || '...';
		this.btnStyle = this.param.btnStyle || '';
		this.showBtn = this.param.showBtn;
		this._updateValue(this.value);
	},
	/**
	 * 利用value更新showValue & hiddenValue
	 * value格式:showValue | hidden 以'|'分隔
	 */
	_updateValue:function(value){
		if(value.indexOf("|")>=0){
			var xx = value.split("|");
			this.showValue = xx[0];
			this.hiddenValue = xx[1];
		}else{
			this.showValue = value;
			this.hiddenValue = this._value;
		}
	},
	/**
	 * 设置value
	 */
	setValue:function(value){
		this._updateValue(value);
		$(this.id+"_show").value = this.showValue;
		$(this.id+"_hidden").value = this.hiddenValue;
	},
	/**
	 * 获取值 ，该值包括显示值和隐藏值
	 */
	getValue:function(){
		return this.showValue + "|" + this.hiddenValue;
	},
	getHiddenValue:function(){
		return this.hiddenValue;
	},
	getShowValue:function(){
		return this.showValue;
	},
	toHTML:function(){
		var html = "";
		if(this.st == '02'){
			html = "<span style='padding:5'>"+((this.showValue && this.showValue.length>0)?$escapeHtml(this.showValue):"")+"</span>";
		}else{
			html = "<span id='"+this.id+"'>" +
				"<input name='"+this.name+"_show' id='"+this.id+"_show' type='text' " +
				((this.inputStyle && this.inputStyle.length>0)?"style='"+this.inputStyle+"' ":"") +
				((this.showValue && this.showValue.length>0)?"value='"+$escapeHtml(this.showValue)+"' ":"") +
				($bool(this.readonly)?"readonly":"") +
				"/>"+
				"<input name='"+this.name+"_hidden' id='"+this.id+"_hidden' type='hidden'" +
				((this.hiddenValue && this.hiddenValue.length>0)?"value='"+$escapeHtml(this.hiddenValue)+"' ":"") +
				"/>" +
				($bool(this.showBtn) == true?"<button style='"+this.btnStyle+"'>."+this.btnText+"</button>":"") +
				"</span>";
		}
		return html;
	},

	/**
	 * 渲染完成之后，初始化下拉框数据
	 */
	onRender:function(){
		if(this.showBtn){
			var btn = this.dom.childNodes[2];
			if(btn){
				var obj = this;
				btn.attachEvent("onclick",function(e){
					obj.fireEvent("onbtnclick");
				});
			}
		}
	},
	wai:function(){
		return "TXTE";
	},
	eventEnum:function(){
		return ["onclick","onmouseover","onmouseout","onmouse","onblur",'onbtnclick'];
	}
});

/**
 * 多选下拉树
 */
var CKCOMBTREE = Class.create(AbstractEdirot,{
	init:function(){
		try{
			$import_syn(contextPath+"/script/adapter/ext/ext-base.js",this.doc);
			$importCss(contextPath + "/resources/css/ext-all.css",this.doc);
			$import_syn(contextPath+"/script/ext-all-debug.js",this.doc);
			$import_syn(contextPath+"/script/ext-extend/tree/CheckboxTreeNodeUI.js",this.doc);
			$import_syn(contextPath+"/script/ext-extend/tree/CheckboxTreeNode.js",this.doc);
			$import_syn(contextPath+"/script/ext-extend/tree/CheckboxAsyncTreeNode.js",this.doc);
			$import_syn(contextPath+"/script/ext-extend/tree/CheckboxTreeNodeLoader.js",this.doc);
			
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
			
			this.p_url = contextPath + this.param.url || '';
			this.p_width = this.param.width || 200;
			this.p_width = parseInt(this.p_width);
			this.p_listWidth = this.param.listWidth || 200;
			this.p_listWidth = parseInt(this.p_listWidth);
			this.p_listHeight = this.param.listHeight || 200; 
			this.p_listHeight = parseInt(this.p_listHeight);
			this.p_emptyText = this.param.emptyText || '';
			this.p_allowBlank = this.param.allowBlank;
			
			this.docid = this.param.docid;
		}catch(ex){
	        this.avalible = false;
		}
	},
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			html = "<div>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/>" +
						"<input name='hide_"+this.name+"'  type='text' value=''/></div>" +
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onRender:function(){
		var editor = this;
		if(!this.avalible) return;
		$import_syn(contextPath+"/script/ext-extend/form/CheckComboTree.js",this.doc);
		
		if(this.st == '02'){
			var rawValue = this.ckCombTree.getValue();
			this.dom.parentNode.innerHTML = "<span " +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					">" +
					rawValue +
					"</span>";
		}else{
			
			this.ckCombTree = new Ext.app.ComboTree({
					id: 'ct_' + this.id,
					dataUrl : this.p_url,
					width: this.p_width,
					listWidth: this.p_listWidth,
					listHeight: this.p_listHeight,
					emptyText: this.p_emptyText,
					triggerClass: 'comboTree',
					ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
					autoScroll: true,
					localData : false,
					rootVisible: this.p_rootVisible,
					allowBlank: this.p_allowBlank,
					listeners: {
						collapse: function(t,node){
							var txtValue = editor.dom.childNodes[0].childNodes[0];
							var txtHide = editor.dom.childNodes[0].childNodes[1];
							if(txtValue != null){
								txtValue.value = editor.ckCombTree.getValue();
								if(editor.ckCombTree.hideMode != "display"){
									txtHide.value = editor.ckCombTree.hideMode;
								}
							}
						},
						select:function(t,node){
							editor.onSelect(node);
						}
					}
			});
		}
		
	   	this.ckCombTree.render(this.id+"_div");
   	 	$(this.name).value=this.value;
   	 	this.ckCombTree.setData(this.id,this.value);
   	 	 
	},
	reRender:function(){
		var el = this.dom.parentNode;
		if(el){
			el.innerHTML = "";
		}
		this.renderTo(el);
	},
	getId:function(){
		return this.id;
	},
	wai:function(){
		return "CKCOMBTREE";
	},
	setValue:function(value){
		this.ckCombTree.setValue(value);
	},
	getValue:function(){
		return this.ckCombTree.getValue();
	},
	validate:function(cp, is_Not_Valid_Null){
		var value = this.ckCombTree.getValue();
		if(this.nn == "true" && (value== null || value == '') && !is_Not_Valid_Null){
			if (cp == null || cp == '') {
				alert(this.name+"不能为空");
			} else {
				alert("字段【" + cp + "】为必填项，不能为空\r\n");
			}
			this.ckCombTree.focus();
			return false;
		}
		return true;
	}
});

/**
 * 下拉树
 */
var COMBTREE = Class.create(AbstractEdirot,{
	init:function(){
		try{
			$import_syn(contextPath+"/script/adapter/ext/ext-base.js",this.doc);
			$importCss(contextPath + "/resources/css/ext-all.css",this.doc);
			$import_syn(contextPath+"/script/ext-all-debug.js",this.doc);
						
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
			
			this.p_url = contextPath + this.param.url || '';
			this.p_width = this.param.width || 200;
			this.p_width = parseInt(this.p_width);
			this.p_listWidth = this.param.listWidth || 200;
			this.p_listWidth = parseInt(this.p_listWidth);
			this.p_listHeight = this.param.listHeight || 200; 
			this.p_listHeight = parseInt(this.p_listHeight);
			this.p_emptyText = this.param.emptyText || '';
			this.p_rootVisible = this.param.rootVisible;
			this.p_allowBlank = this.param.allowBlank;
			
			this.docid = this.param.docid;
		}catch(ex){
	        this.avalible = false;
		}
	},
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			html = "<div>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/>" +
						"<input name='hide_"+this.name+"'  type='text' value=''/></div>" +
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onSelect:function(node){
		this.fireEvent("onselect", this, node);
	},
	onRender:function(){
		var editor = this;
		if(!this.avalible) return;
		$import_syn(contextPath+"/script/ext-extend/form/ComboTree.js",this.doc);
//		$import_syn(contextPath+"/jteap/system/person/script/GroupNodeUI.js",this.doc);
//		$import_syn(contextPath+"/jteap/system/role/script/RoleNodeUI.js",this.doc);
		
		if(this.st == '02'){
			var rawValue = this.param.value;
			this.dom.parentNode.innerHTML = "<span " +
					((this.style && this.style.length>0)?"style='"+this.style+"' ":"") +
					">" +
					rawValue +
					"</span>";
		}else{
			this.combTree = new Ext.app.ComboTree({
					id: 'ct_' + this.id,
					dataUrl : this.p_url,
					width: this.p_width,
					listWidth: this.p_listWidth,
					listHeight: this.p_listHeight,
					emptyText: this.p_emptyText,
					triggerClass: 'comboTree',
					autoScroll: true,
					localData : false,
					rootVisible: this.p_rootVisible,
					allowBlank: this.p_allowBlank,
					listeners: {
						collapse: function(t,node){
							var txtValue = editor.dom.childNodes[0].childNodes[0];
							var txtHide = editor.dom.childNodes[0].childNodes[1];
							if(txtValue != null){
								txtValue.value = editor.combTree.getValue();
								if(editor.combTree.hideMode != "display"){
									txtHide.value = editor.combTree.hideMode;
								}
							}
						},
						select:function(t,node){
							editor.onSelect(node);
						}
					}
			});
		   	this.combTree.render(this.id+"_div");
		   	$(this.name).value=this.value;
		    this.combTree.setValue(this.id,this.value);
			//alert(this.combTree.getValue()+"-----------");
		}
		
	},
	reRender:function(){
		var el = this.dom.parentNode;
		if(el){
			el.innerHTML = "";
		}
		this.renderTo(el);
	},
	getId:function(){
		return this.id;
	},
	wai:function(){
		return "COMBTREE";
	},
	setValue:function(value){
		this.combTree.setValue(value);
	},
	getValue:function(){
		return this.combTree.getValue();
	},
	validate:function(cp, is_Not_Valid_Null){
		var value = this.combTree.getValue();
		if(this.nn == "true" && (value== null || value == '') && !is_Not_Valid_Null){
			if (cp == null || cp == '') {
				alert(this.name+"不能为空");
			} else {
				alert("字段【" + cp + "】为必填项，不能为空\r\n");
			}
			this.combTree.focus();
			return false;
		}
		return true;
	},
	eventEnum:function(){
		return ["onselect"];
	}
});

/**
 * 附件列表
 * 
 */
var ATLT = Class.create(AbstractEdirot,{
	init:function(){
		try{
			var atlt = this;
			$import_syn(contextPath+"/script/adapter/ext/ext-base.js",this.doc);
			$importCss(contextPath + "/resources/css/ext-all.css",this.doc);
			$import_syn(contextPath+"/script/ext-all-debug.js",this.doc);
			$import_syn(contextPath+"/script/ext-extend/swfupload/swfupload.js");
			
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
			
			this.p_width = this.param.width || 400;
			this.p_width = parseInt(this.p_width);
			this.p_height = this.param.height  || 200;
			this.p_height = parseInt(this.p_height);
			
			this.p_mutiSelect = this.param.mutiSelect;			//是否支持选择多个文件
			this.p_saveToFs = this.param.saveToFs;				//是否以文件系统的方式存放，否则存入数据库
			this.p_maxFileSize = this.param.maxFileSize;		//支持上传的最大文件大小
			this.p_allowFileExt = this.param.allowFileExt;		//支持上传的文件类型
			this.p_allowFileDesc = this.param.allowFileDesc;	//文件类型描述
			this.p_maxFileNum = this.param.maxFileNum;			//最多支持上传的文件个数
			this.p_encodeFileName = this.param.encodeFileName;	//是否加密文件名
		}catch(ex){
	        this.avalible = false;
	       throw ex;
		}
	},
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			var tbarHtml = "" ;
			if(this.st != '02'){
				tbarHtml = "" +
				"	<div id='"+this.id+"_tb_div' style='vertical-align:top'>" +
				"		<span style='height:25;valign:bottom;'>" +
				"			<span id='"+this.id+"_tb_upload_div'></span>" +
				"		</span>" +
				"		<span style='padding-bottom:2;height:25;'>" +
				//"			<input type='button' value='刷新'/>" +
				"		</span>" +
				"	</div>";
			}
			
			html = "" +
				"<div id='"+this.id+"'>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/></div>" +
					tbarHtml + 
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onRender:function(){
		if(!this.avalible)
			return;
		var atlt = this;
 		$import_syn(contextPath+"/jteap/form/eform/script/eformEditorATLTAttachGrid.js",this.doc);
 		$import_syn(contextPath+"/jteap/form/eform/script/eformEditorATLTProcessWindow.js",this.doc);
 		
		this.grid = new EFormAttachGrid({
	   		id:'atlt_'+atlt.id,
	   		editorId:atlt.name,
	   		width:atlt.p_width,
	   		isEdit:(this.st != '02'),
	   		border:true,
	   		height:atlt.p_height,
	   		docid:atlt.docid,
	   		frame:false,
	   		enableHdMenu :false,
	   		hideHeaders :true,
	   		listeners:{
	   		}
   		});
   		
   		//渲染grid列表
   		this.grid.render(this.id+"_div");
   		if(this.st != '02')
   			this._renderSWFUploadButton();
	},
	/**
	 * 保存之前
	 */
	onBeforeSave:function(){
		var json = this.getValue();
		this.dom.firstChild.firstChild.value = json;
		//alert(json);
	},
	/**
	 * 取值,获取该组件的值，该值将会根据bJustDirty判断是否包含所有数据还是只包含修改过的数据
	 */
	getValue:function(){
		var data = [];
		var records = this.grid.store.getRange();
		for (var i = 0;i < records.length; i++) {
			data.push(records[i].data);
		}
		var json = "";
		if(data.length>0){
			json = Ext.encode(data);
		}
		return json;
	},
	/**
	 * 渲染SWF UPLOAD按钮
	 */
	_renderSWFUploadButton:function(){
		var divEl = this.id+"_tb_upload_div";
		var atlt = this;
		var settings = {
			flash_url : contextPath + "/script/ext-extend/swfupload/swfupload.swf",
			upload_url : contextPath + "/jteap/form/eform/EFormFjUploadServlet",
			post_params: {},
			file_size_limit : this.p_maxFileSize,
			file_types : this.p_allowFileExt,
			file_types_description : this.p_allowFileDesc,
			file_upload_limit : 0,
			file_queue_limit : this.p_maxFileNum,
			debug: false,
			// Button settings
			button_image_url: contextPath + "/script/ext-extend/swfupload/images/XPButtonUploadText_61x22.png",
			button_width: "61",
			button_height: "22",
			button_placeholder_id: divEl,
			//button_text: 'ssss',
			//button_text_style: ".theFont { font-size: 16; }",
			//button_text_left_padding: 12,
			//button_text_top_padding: 3,
			
			// The event handler functions are defined in handlers.js
			file_queued_handler : function(file){atlt._file_queued_handler(file,atlt);},
			file_queue_error_handler : function(file, errorCode, message){atlt._file_queue_error_handler(file, errorCode, message,atlt)},
			file_dialog_complete_handler :function(numFilesSelected, numFilesQueued){atlt._file_dialog_complete_handler(numFilesSelected, numFilesQueued,this,atlt);},
			upload_start_handler : function(){},
			upload_progress_handler :  function(file, bytesLoaded, bytesTotal){atlt._upload_progress_handler(file, bytesLoaded, bytesTotal,atlt);},
			upload_error_handler : function(file, errorCode, message){atlt._upload_error_handler(file, errorCode, message,atlt)},
			upload_success_handler : function(file, serverData){atlt._upload_success_handler(file, serverData,atlt);},
			upload_complete_handler : function(file){atlt._upload_complete_handler(file,atlt);},
			queue_complete_handler : function(numFilesUploaded){atlt._queue_complete_handler(numFilesUploaded,atlt)}	// Queue plugin event
		};
		var swfu = new SWFUpload(settings);
		//初始化进度条窗口
   		this.processWindow = new ProcessWindow(swfu);
	},
	wai:function(){
		return "ATLT";
	},
	/**
	 * 添加上传队列
	 */
	_file_queued_handler:function(file,atlt){

	},
	_file_queue_error_handler:function(file, errorCode, message,atlt){
		try {
			if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
				alert("文件个数过多.\n" + (message === 0 ? "超出指定的文件个数." : "你可以选择 " + (message > 1 ? "" + message + "个文件." : "1个文件.")));
				return;
			}
			switch (errorCode) {
				case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
					alert("Error Code: 文件超出指定大小, 文件名: " + file.name + ", 文件大小: " + file.size + ", Message: " + message);
					break;
				case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
					alert("Error Code:文件大小为0, 文件名: " + file.name + ", 文件大小: " + file.size + ", Message: " + message);
					break;
				case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
					alert("Error Code: 无效的文件类型, 文件名: " + file.name + ", 文件大小: " + file.size + ", Message: " + message);
					break;
				default:
					alert("Error Code: " + errorCode + ", 文件名: " + file.name + ", 文件大小: " + file.size + ", Message: " + message);
					break;
			}
		} catch (ex) {
			alert(ex);
	    }
	},

	_upload_error_handler:function(file, errorCode, message,atlt) {
		try {
			switch (errorCode) {
			case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
				alert("Upload Error: " + message);
				//this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
				alert("Upload Failed.");
				//this.debug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.IO_ERROR:
				alert("Server (IO) Error");
				//this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
				alert("Security Error");
				//this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
				alert("Upload limit exceeded.");
				//this.debug("Error Code: Upload Limit Exceeded, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
				alert("Failed Validation.  Upload skipped.");
				//this.debug("Error Code: File Validation Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
				break;
			case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
				// If there aren't any files left (they were all cancelled) disable the cancel button
				alert("Cancelled");
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
				alert("Stopped");
				break;
			default:
				alert("Unhandled Error: " + errorCode);
				//this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
				break;
			}
		} catch (ex) {
	        this.debug(ex);
	    }
	},


	/**
	 * 选择窗口完成后
	 */
	_file_dialog_complete_handler:function(numFilesSelected, numFilesQueued,swfupload,atlt) {
		try {
			if(numFilesQueued>0){
				atlt.processWindow.show();
				swfupload.startUpload();
			}
		} catch (ex)  {
		}
	},
	/**
	 * 上传过程
	 */
	_upload_progress_handler:function(file, bytesLoaded, bytesTotal,atlt) {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		var fvalue = percent/100;
		var text = "文件"+file.name+"已完成"+percent+"%";
		atlt.processWindow.updateProcess(fvalue,text);
	},
	/**
	 * 上传过程完成
	 */
	_upload_success_handler:function(file, serverData,atlt){
		//EFormAttachRec defined in eformEditorAttachGrid.js
		var responseObject = serverData.evalJSON();
		var st = ($bool(atlt.p_saveToFs) == true?"FS":"DB");
		var encodeFileName = $bool(atlt.p_encodeFileName);
		if(responseObject!=null && responseObject.success == true){
			var fileId = responseObject.fileId;
			var path = responseObject.path;
			var rec = new EFormAttachRec({id:fileId,type:file.type,path:path,name:file.name,size:file.size,isNew:true,st:st,encodeFileName:encodeFileName});
			atlt.grid.store.add(rec);
		}
	},
	_upload_complete_handler:function(file,atlt){
		//alert("上传完成");
	},
	/**
	 * 队列完成，所有上传完毕
	 */
	_queue_complete_handler:function queueComplete(numFilesUploaded,atlt) {
		atlt.processWindow.hide();
	}
});


/**
 * 从表控件
 * 从表控件支持
 * TXT、COMB、RADIO、CHECKBOX
 */
var SIGNAREA = Class.create(AbstractEdirot,{
	init:function(){
		try{
			//由于Struts2.0中ONGL存在无法解析包含"-"的变量，所以重写Ext中自动生成控件的ID生成规则，将"-"改为"_"
			if (Ext.Component){
				Ext.Component.prototype.getId = function(){
					var id = this.id || (this.id = "ext_comp_" + (++Ext.Component.AUTO_ID));
		        	return id;
		    	}
			}
	
			this.width = this.param.width || '100%';
			try{
				this.p_columns = eval(this.param.subTableCM || '[]');
			}catch(ex){
				alert('列模型定义错误');
				throw ex;
			}
			
			this.p_subTableName = this.param.subTableName;//子表表明,可带schema一起
			this.p_fkFieldName = this.param.subTableFK;//外键字段名
			this.p_pkFieldName = this.param.subTablePK;	//主键字段名
			
			this.p_width = this.param.width || 400;
			this.p_width = parseInt(this.p_width);
			this.p_height = this.param.height  || 200;
			this.p_height = parseInt(this.p_height);
			
			this.p_where = this.param.where || "";	//扩展过滤条件
			this.p_order = this.param.subTableOrder || "";
			this.p_bShowTB = this.param.showToolBar;
			if(this.p_bShowTB==null)
				this.p_bShowTB = true;
			this.docid = this.param.docid;
			
			this.isSign = false;
		}catch(ex){
	        this.avalible = false;
		}
	},
	
	toHTML:function(){
		var html = "";
		if(!this.avalible){
			html = '<div>控件渲染异常</div>';
		}else{
			html = "<DIV>" +
				"	<div style='display:none'><input name='"+this.name+"'  type='text' value=''/></div>" +
				"	<div id ='"+this.id+"_div'></div>" +
				"</div>";
		}
		return html;
	},
	onRender:function(){
		if(!this.avalible) return;
		//渲染EXT Grid
		var sub = this;
		this.csm= new Ext.grid.CheckboxSelectionModel();
		$import_syn(contextPath+"/jteap/form/eform/script/eformEditorSubGridSign.js",this.doc);
		var config = {
				width:sub.p_width,
		   		height:sub.p_height
			};
		if(this.st == '02'){
			sub.grid = new SubGrid({
		   		id:'sg_'+sub.id,
		   		width:sub.p_width,
		   		height:sub.p_height,
		   		listeners:{
		   		}
	   		});
		}else{
			sub.grid = new SubEditGrid({
		   		id:'sg_'+sub.id,
		   		width:sub.p_width,
		   		height:sub.p_height,
		   		bShowToolBar:(sub.p_bShowTB == true || sub.p_bShowTB=='true'),
		   		sm:sub.csm,
		   		listeners:{
		   		}
	   		});
		}
		
	   	sub.grid.editor = sub;
	   	sub.grid.render(sub.id+"_div");
	   	var cm = null;
	   	if(this.st=='02'){
	   		cm = sub._buildReadOnlyCm();
	   	}else{
	   		cm = sub._buildEditableCm();
	   	}
 		var ds = sub._buildDs();
 		sub.grid.updateData(cm,ds);
 		
	},
	wai:function(){
		return "SIGNAREA";
	},
	eventEnum:function(){
		return ["onsign"];
	},
	/**
	 * 创建新的记录
	 */
	createNew:function(){
		//this.grid.createNew();
		if (this.st != '02') {
			var url = contextPath + "/jteap/form/eform/eformRecSignObj.jsp"
			var dt = showModule(url, "yes",300, 230, "");
			if(typeof(dt) != "undefined"){
				this.grid.createNew();
				var args = new Array();
				args = dt.split(",");
				this.grid.selModel.selectFirstRow();
				if(dt != null){
					this.fireEvent("onsign",args);
					this.isSign = true;
				}
			}
			/**
			else{
				var store = this.grid.store;
				var item = store.getAt(store.getTotalCount()-1);
				this.grid.store.remove(item);
			}
			**/
		}
	},
	/**
	 * 取值,获取该组件的值，该值将会根据bJustDirty判断是否包含所有数据还是只包含修改过的数据
	 */
	getValue:function(bJustDirty){
		if(typeof bJustDirty == 'undefined')
			bJustDirty = false;
		var data = [];
		var records = this.grid.store.getRange(0,0);
		for (var i = 0;i < records.length; i++) {
			if(records[i].dirty==true){
				data.push(records[i].data);
			}
		}
		var json = "";
		if(data.length>0){
			json = Ext.encode(data);
			
		}
		return json;
	},
	onBeforeSave:function(){
		var json = this.getValue(true);
		this.dom.firstChild.firstChild.value = json;
	},
	/**
	 * 单独保存子表
	 */
	doSave:function(){
		if(this.docid == null || this.docid == ''){
			alert('主表没保存，请先保存主表');
			return;
		}
		if(!this.isSign){
			//alert('签名后才能编辑签名说明');
			return;
		}	
		this.isSign = false;
		var json = this.getValue();
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecSaveSubAction.do";
		var sub = this;
		AjaxRequest_Sync(url,{json:json,docid:this.docid,pkFieldName:this.p_pkFieldName,fkFieldName:this.p_fkFieldName,subTableName:this.p_subTableName},function(ajax){
			var obj = ajax.responseText.evalJSON();
			if(obj.success == true){
				alert('保存成功');
				sub.grid.store.reload();
			}else{
				alert('保存失败');
			}
		});
	},
	getSelections:function(){
		var items = this.grid.selModel.getSelections();
		return items;
	},
	/**
	 * 刷新
	 */
	doRefresh:function(){
		this.grid.store.reload();
	},
	/**
	 * 删除操作
	 */
	doDelete:function(){
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecDeleteSubAction.do";
		var ids = "";
		var items = this.grid.selModel.getSelections();
		for(var i=0;i<items.length;i++){
			var item = items[i];
			if(item.json){
				var id = eval("item.json."+this.p_pkFieldName);
				ids+=(id+",");
			}else{
				this.grid.store.remove(item);
			}
		}
		if(ids!='' && confirm("确定取消删除选中的记录数据吗？")){
			var sub = this;
			AjaxRequest_Sync(url,{ids:ids,subTableName:this.p_subTableName,pkFieldName:this.p_pkFieldName},function(ajax){
				var obj = ajax.responseText.evalJSON();
				if(obj.success == true){
					alert('删除成功');
					sub.grid.store.reload();
				}else{
					alert('删除失败');
				}
			})
		}
	},
	//构建列模型对象
	_buildEditableCm:function(){
		var tmpArray=new Array();
		tmpArray.push(this.csm);
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			if(!cols[i].hidden || cols[i].hidden==false || cols[i].hidden=='false'){
				var tmpCm={"id":cols[i].id,"header":cols[i].header,"width":cols[i].width,"sortable":cols[i].sortable || false,"dataIndex":cols[i].dataIndex};
				var editor = cols[i].editor;
				try{
					if(editor!=null && editor!='')
	//					editor = {xtype:'textfield'};
					tmpCm.editor = new Ext.ComponentMgr.create(editor);
				}catch(ex){
					alert(ex.message);
				}
				var render = cols[i].renderer;

				if(render!=null)
					tmpCm.renderer = render;
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	},//构建列模型对象
	_buildReadOnlyCm:function(){
		var tmpArray=new Array();
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			if(!cols[i].hidden || cols[i].hidden==false || cols[i].hidden=='false'){
				var tmpCm={"id":cols[i].id,"header":cols[i].header,"width":cols[i].width,"sortable":false,"dataIndex":cols[i].dataIndex};
				var render = cols[i].renderer;
				if(render!=null)
					tmpCm.renderer = render;
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	},
	//构建数据源对象
	_buildDs: function(){
		var colArray=new Array();
		var cols = this.p_columns;
		for(var i=0;i<cols.length;i++){
			var fd = cols[i].dataIndex;
			colArray.push(fd);
		}
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecGetSubDataAction.do";
		var ds = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: url
	        }),
	        baseParams :{
	        	subTableName:this.p_subTableName,
	        	fkFieldName:this.p_fkFieldName,
	        	docid:this.docid,
	        	where:this.p_where,
	        	subTableOrder:this.p_order
	        },
	        listeners:{
	        loadexception:function(a){
	        	alert('数据加载失败');
	        } 
	        },
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount'
	        }, colArray),
	        remoteSort: true
	    });
		return ds;
	}
});

/**
 * 数据源控件
 */
var DATASTORE = Class.create(AbstractEdirot,{
	init:function(){
		this.sql = this.param.sql;
		this.url = this.param.url || '';
		this.store = "";
	},
	toHTML:function(){
		var html = "<span></span>"
		return html;
	},

	/**
	 * 渲染完成之后，初始化下拉框数据
	 */
	onRender:function(){
		if(this.sql)
			this.initSql(this.sql);
	//	else if(this.url)
	///		this.initUrl(this.url);
	},
	initSql:function(sql){
		var url = contextPath + "/jteap/form/eform/EFormAction!doQueryBySqlAction.do?"+$('queryString').value;
		var ds = this;
		AjaxRequest_Sync(url,{sql:sql},function(ajax){
			var responseObj = ajax.responseText.evalJSON();
			if(responseObj.success == true){
				ds.store = responseObj.list;
			}
		})
	},
	getStore:function(){
		return this.store;
	},
	getValueByDataIndex:function(row,dataIndex){
		return eval('this.store['+row+'].'+dataIndex);
	},
	getRecordById:function(id){
		
	},
	initUrl:function(url){
		var url = contextPath + url;
		var ds = this;
		AjaxRequest_Sync(url,{sql:sql},function(ajax){
			var responseObj = ajax.responseText.evalJSON();
			if(responseObj.success == true){
				ds.store = responseObj.list;
			}
		})
	},
	wai:function(){
		return "DATASTORE";
	},
	eventEnum:function(){
		return [];
	}
});




