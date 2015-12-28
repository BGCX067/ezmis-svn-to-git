/*     MyValidator, version 1.0
 *    (c) 2006 rocky.shen <6662714258@163.com>
 */
//alert("myValidator loading");
/*
 *  MyValidator   the most usefull class
 */
var MyValidator = Class.create();
//alert(typeof MyValidator);
MyValidator.prototype = {
	initialize:function () {
		this.validations = new Array();
		this.popError =null;
		this.errorIds = null;
		this.errors = null;
	}, 
	addValidation:function (validation) {
		if (validation instanceof Validation) {
			if($(validation.id)){
				this.validations[validation.id] = validation;
			}
		}
	}, 
	getTipId:function (id) {
		return id + "Tip";
	}, 
	getMessageBody:function (id, message) {
		return "<span class='message' id='" + id + "'>" + (message == null ? "" : "") + "</span>";
	}, 
	getErrorBody:function (id, error, status) {
		if(status  == 'true'){
			return "<span class='error1' id='" + id + "'>" + error + "</span>";
		}else if(status  == 'false'){
			return "<span class='error2' id='" + id + "'>" + error + "</span>";
		}
	}, 
	handleError:function (event) {
		var id = Event.element(event).id;
		var validation = this.validations[id];
		var element = $(this.getTipId(id));
		if (validation.validate()) {
			element.innerHTML = this.getMessageBody(id, validation.message);
		} else {
			element.innerHTML = this.getErrorBody(id, validation.error, validation.status);
		}
	}, 
	handleMessage:function (event) {
		var id = Event.element(event).id;
		var validation = this.validations[id];
		if (validation.message != null) {
			var element = $(this.getTipId(id));
			element.innerHTML = this.getMessageBody(this.getTipId(id), validation.message);
		}
	}, 
	handleCheckError:function (event) {
		var id = Event.element(event).vid;
		var validation = this.validations[id];
		var element = $(this.getTipId(id));
		if (validation.validate()) {
			element.innerHTML = this.getMessageBody(id, validation.message);
		} else {
			element.innerHTML = this.getErrorBody(id, validation.error, validation.status);
		}
	}, 
	handleCheckMessage:function (event) {
		var id = Event.element(event).vid;
		var validation = this.validations[id];
		if (validation.message != null) {
			var element = $(this.getTipId(id));
			element.innerHTML = this.getMessageBody(this.getTipId(id), validation.message);
		}
	},
	setUp:function (mode) {
		mode=mode||1;
		for (id in this.validations) {
			var validation = this.validations[id];
			var element = $(id);
			if (validation instanceof CheckboxRangeValidation||validation instanceof RadioRangeValidation) {
				if(mode==1){
					var groups = document.getElementsByName(validation.name);
					var count = groups.length;
					for(var i=0; i<count; i++){
						var ele=groups[i];
						ele.setAttribute("vid",validation.id);					
						ele.onblur = this.handleCheckError.bindAsEventListener(this);
						ele.onfocus = this.handleCheckMessage.bindAsEventListener(this);	
					}
				}

				new Insertion.After(groups[count-1].parentNode, this.getMessageBody(this.getTipId(id), validation.message));
			}else if (validation instanceof Validation){	
				if(mode==1){
					element.onblur = this.handleError.bindAsEventListener(this);
					element.onfocus = this.handleMessage.bindAsEventListener(this);	
				}					
				new Insertion.After(id, this.getMessageBody(this.getTipId(id), validation.message));
			}
		}
	},
	mySetUp:function(mode) {
		mode=mode||1;
		for (id in this.validations) {
			var validation = this.validations[id];
			var element = $(id);
			if (validation instanceof CheckboxRangeValidation || validation instanceof RadioRangeValidation) {
				if(mode==1){
					if(typeof(element) == "undefined") {
						continue;
					} else {
						var groups = document.getElementsByName(validation.name);
						var count = groups.length;
						for(var i=0; i<count; i++){
							var ele=groups[i];
							ele.setAttribute("vid",validation.id);					
							ele.onblur = this.handleCheckError.bindAsEventListener(this);
							ele.onfocus = this.handleCheckMessage.bindAsEventListener(this);	
						}
					}
				}		
					
				new Insertion.After(groups[count-1].parentNode, this.getMessageBody(this.getTipId(id), validation.message));
			}else if (validation instanceof Validation){	
				if(mode==1){
					if(typeof(element) == "undefined"){
						continue;
					}else{
						element.onblur = this.handleError.bindAsEventListener(this);
						element.onfocus = this.handleMessage.bindAsEventListener(this);	
					}
				}					
				new Insertion.After(id, this.getMessageBody(this.getTipId(id), validation.message));
			}
		}
	},
	myRemove:function(mode) {
		mode=mode||1;
		for (id in this.validations) {
			var validation = this.validations[id];
			var element = $(id);
			if (validation instanceof CheckboxRangeValidation || validation instanceof RadioRangeValidation) {
				if(mode==1){
					var groups = document.getElementsByName(validation.name);
					var count = groups.length;
					for(var i=0; i<count; i++){
						var ele=groups[i];
						ele.setAttribute("vid",validation.id);					
						ele.onblur = function empty(){};
						ele.onfocus = function empty(){};
					}
				}
				document.getElementById(this.getTipId(id)).style.display="none";
			}else if (validation instanceof Validation){	
				if(mode==1){
					element.onblur = function empty(){};
					element.onfocus = function empty(){};
				}
				document.getElementById(this.getTipId(id)).style.display="none";
			}
		}
		this.validations = new Array();
	},
	isPassed:function (mode) {
		this.popError ="\n";
		this.errorIds = new Array();
		this.errors = new Array();
		this.status = new Array();
		var isPassed=true;
		var validation=null;
		for (id in this.validations) {
			validation = this.validations[id];
			if (validation instanceof Validation) {
				if(!validation.validate()){
					this.errorIds[this.errorIds.length] = validation.id;
					this.errors[this.errors.length] =(this.errors.length+1) + ". " + validation.error;
					this.status[this.status.length] = validation.status;
					isPassed = false;
					//this.popError =this.popError + this.errors.join("\n");
				}
			}
		}
		
		if(this.errors.length > 0){
			var flagA = 0;        //强制性验证计数
			var flagB = 0;        //非强制性验证计数
			for(var i =0; i < this.status.length; i++){
				if(this.status[i]  == 'true'){
					flagA++;
				}else{
					flagB++;
				}
			}
			mode = mode || 1;
			var errCount = this.errorIds.length;
			switch(mode){
			case 1 :
				var element=null;
				for (id in this.validations) {
						var validation = this.validations[id];
						element = $(this.getTipId(id));
						if (validation instanceof Validation){
							element.innerHTML = this.getMessageBody(id, validation.message);
						}
				}
				for(var i=0;i<errCount;i++){
					element = $(this.getTipId(this.errorIds[i]));
					element.innerHTML = this.getErrorBody(this.errorIds[i], this.validations[this.errorIds[i]].error, this.validations[this.errorIds[i]].status);
				}
				/*******mode1添加强制非强制验证********/
				if(flagA > 0){
					isPassed = false;
					this.popError =this.popError + this.errors.join("<br />");
				}else if(flagA == 0 && flagB >0 && isPassed){
					//if(confirm(this.popError + this.errors.join("<br />"))){
					if(confirm("数据异常，可能有误，是否继续保存?")){
						alert("aaaaaaa");
						isPassed = true;
					}else{
						isPassed = false;
						this.popError =this.popError + this.errors.join("<br />");	
					}
				}else{
					isPassed = true;
				}
				break;
			case 2 :
				if(flagA > 0){
					isPassed = false;
					this.popError =this.popError + this.errors.join("<br />");
				}else if(flagA == 0 && flagB >0 && isPassed){
					//if(confirm(this.popError + this.errors.join("<br />"))){
					if(confirm("数据异常，可能有误，是否继续保存?")){
						isPassed = true;
					}else{
						isPassed = false;
						this.popError =this.popError + this.errors.join("<br />");	
					}
				}else{
					isPassed = true;
					//this.popError =this.popError + this.errors.join("\n");	
				}
				break;
			default :
				this.popError =this.popError + this.errors.join("<br />");								
			}
		}
		return isPassed;  
	}
};

/**
*	the base 'abstract' validation class
*/
var Validation = Class.create();
Validation.prototype = {
	initialize:function () {
		this.id = null;
		this.message = null;
		this.error = null;
		this.require = false;
		this.params = new Array();
	}, 
	validate:function () {
		return false;
	}
};

/*
 *  CheckboxRangeValidation class
 */
var CheckboxRangeValidation = Class.create();
CheckboxRangeValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function(id,require,status,name, message, error,min,max){
		this.id = id;
		this.message = message;
		this.status = status;
		this.error = error;
		this.min=min;
		this.max=max;
		this.name=name;
		if (require=="true") {
			this.require = true;
		}else{
			this.require = false;
		}
	}, 
	validate:function () {	
		return checkboxRange(this.require,this.name,this.min, this.max);
	}
}
);

/*
 *  RadioRangeValidation class
 */
var RadioRangeValidation = Class.create();
RadioRangeValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function(id,require,status,name, message, error,min,max){
		this.id = id;
		this.status = status;
		this.message = message;
		this.error = error;
		this.min=min;
		this.max=max;
		this.name=name;
		if (require=="true") {
			this.require = true;
		}else{
			this.require = false;
		}
	}, 
	validate:function () {	
		//alert(111);
		return radioRange(this.require,this.name, this.min, this.max);
	}
}
);

/*
 *  SelectRangeValidation class
 */
var SelectRangeValidation = Class.create();
SelectRangeValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function(id,require,status,name, message, error,min,max){
		this.id = id;
		this.status = status;
		this.message = message;
		this.error = error;
		this.min=min;
		this.max=max;
		this.name=name;
		if (require=="true"){
			this.require = true;
		}else{
			this.require = false;
		}
	}, 
	validate:function () {			
		return selectRange(this.require,this.name,this.id, this.min, this.max);
	}
}
);


/*
 *  BjghSelectRangeValidation class
 */
var BjghSelectRangeValidation = Class.create();
BjghSelectRangeValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function(id,require,status,name, message, error,min,max){
		this.id = window.frames["addXmFrame"].document.getElementById(id);
		this.status = status;
		this.message = message;
		this.error = error;
		this.min=min;
		this.max=max;
		this.name=name;
		if (require=="true"){
			this.require = true;
		}else{
			this.require = false;
		}
	}, 
	validate:function () {			
		return MySelectRange(this.require,this.name,this.id, this.min, this.max);
	}
}
);


/*
 *  InputRangeCustomValidation class
 */
var InputRangeCustomValidation = Class.create();
InputRangeCustomValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, status,message, error,lenType,min,max,reg,regObj) {
		this.id = id;
		this.message = message;
		this.status = status;
		this.error = error;
		this.lenType=lenType;
		this.min=min;
		this.max=max;
		this.reg=reg;
		if (require=="true") {
			this.require = true;
		}
		if(regObj != null){
			this.regObj = regObj;
		}
	}, 
	validate:function () {
		var flag=true;
		if(this.regObj != null){
			if(this.require==true){
				if($F(this.id) == this.regObj){
					return true;
				}else{
					switch (this.lenType){
						case "lenB":flag=isInRange(lenB($F(this.id)),this.min, this.max);break;
						case "len":flag=isInRange(len($F(this.id)),this.min, this.max);break;
						case "value":flag=isInRange($F(this.id),this.min, this.max);break;
						default:flag=true;break;		
					}
				}
			}
		}else {
			if(this.require==true){
				switch (this.lenType){
					case "lenB":flag=isInRange(lenB($F(this.id)),this.min, this.max);break;
					case "len":flag=isInRange(len($F(this.id)),this.min, this.max);break;
					case "value":flag=isInRange($F(this.id),this.min, this.max);break;
					default:flag=true;break;		
				}
			}
		}
		if (this.require==false) {
			if(isEmpty($F(this.id))){return true;}
		}
		return flag&&isTrue($F(this.id),this.reg);
	}
}
);

 /*
 *  BjghInputRangeCustomValidation class
 */
var BjghInputRangeCustomValidation = Class.create();
BjghInputRangeCustomValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, status,message, error,lenType,min,max,reg) {
		this.id = window.frames["addXmFrame"].document.getElementById(id).value;
		this.message = message;
		this.status = status;
		this.error = error;
		this.lenType=lenType;
		this.min=min;
		this.max=max;
		this.reg=reg;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==false) {
			if(isEmpty(this.id)){return true;}
		}
		var flag=true;
		switch (this.lenType){
			case "lenB":flag=isInRange(lenB(this.id),this.min, this.max);break;
			case "len":flag=isInRange(len(this.id),this.min, this.max);break;
			case "value":flag=isInRange(this.id,this.min, this.max);break;
			default:flag=true;break;		
		}
		return flag&&isTrue(this.id,this.reg);
	}
}
);
 
 
 
 
 
 
 
 
/**
* MatchedValidation class
*/
var MatchedValidation = Class.create();
MatchedValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require,matchedId, message, error) {
		this.id = id;
		this.message = message;
		this.error = error;
		this.params[0] = matchedId;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {
			if(isEmpty($F(this.id))){return false;}
			return isMatch($F(this.id), $F(this.params[0]));
		}else{
			if(!isEmpty($F(this.id))){
				return isMatch($F(this.id), $F(this.params[0]));
			}
			return true;
		}
	}
}
);

/**
* MyMathedValidation class
* (参数列表:"id"表示A集合,"sign"表示标示符(包括'==','>','<','>=','<=','!='),"coefficient"表示系数(可为空),
* "matchedId"表示B集合,"require"表示是否必要,"status"表示是否强制性验证,"message"表示初始提示信息,"error"表示错误提示信息)
*/
var MyMathedValidation = Class.create();
MyMathedValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign, coefficient, matchedId, require, status, message, error, rule1, rule2) {
		this.id = id;
		this.message = message;
		this.error = error;
		this.finalError=error;
		this.require = true;
		if(matchedId == "" || matchedId == null){
			this.matchedId = 1;
		}else{
			this.matchedId = matchedId;
		}
		this.status = status;
		this.finalStatus = status;
		if(sign == '==' || sign == '>' || sign == '<' || sign == '>=' || sign == '<=' || sign == '!='){
			this.sign = sign;
		}
		if(coefficient == "" || coefficient == null){
			this.coefficient = 1;
		}else{
			this.coefficient = coefficient;
		}
		if(rule1 != null){
			this.rule1 = rule1;
		}
		if(rule2 != null){
			this.rule2 = rule2;
		}
	}, 
	validate:function () {
		if(this.rule1 != null){
			if (!this.rule1.validate())
			{
               this.error = this.rule1.error;
               this.status = this.rule1.status;
			   return false;
			}		
		}
		if(this.rule2 != null){
			if (!this.rule2.validate())
			{
               this.error = this.rule2.error;
               this.status = this.rule2.status;
			   return false;
			}		
		}
		this.error = this.finalError;
		this.status = this.finalStatus;
		var newId = this.id.split(",");
		var newMatchedId = "";
		var str1 = 0;
		var str2 = 0;
		for(var i = 0; i < newId.length;i++){
			str1 += Number(document.getElementById(newId[i]).value);
		}
		if(this.matchedId == 1){
			str2 = 1;
		}else{
			newMatchedId = this.matchedId.split(",");	
			for(var i = 0; i < newMatchedId.length;i++){
				if(document.getElementById(newMatchedId[i]).value == null || document.getElementById(newMatchedId[i]).value == '') {
					continue;
				}
				str2 += Number(document.getElementById(newMatchedId[i]).value);
			}
			str2 = Number(str2.toFixed(1));
		}
		if (this.require==true) {
			if(isEmpty($F(this.id))){return false;}
			return isMyMatch(str1,str2,this.coefficient,this.sign);
		}else{
			if(!isEmpty($F(this.id))){
				return isMyMatch(str1, str2,this.coefficient,this.sign);
			}
			return true;
		}
	}
}
);


/**
* MyMathedOfLogicValidation class
* 【主要用途是校验一个值是否在一个区间范围以内,例如:x<A<Y <====> A>X && A<Y是否成立】	
* (参数列表:"id1"表示A集合,"sign1"表示标示符(包括'==','>','<','>=','<=','!='),"coefficient1"表示系数(可为空),
* "matchedId1"表示B集合,"flag"表示逻辑运算符('||','&&'),"id2"表示A集合,"sign2"表示标示符(包括'==','>','<','>=','<=','!='),"coefficient2"表示系数(可为空),
* "matchedId2"表示C集合,"require"表示是否必要,"status"表示是否强制性验证,"message"表示初始提示信息,"error"表示错误提示信息)
*/
var MyMathedOfLogicValidation = Class.create();
MyMathedOfLogicValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign1, coefficient1, matchedId1, flag, id2, sign2, coefficient2, matchedId2, require, status, message, error, rule1, rule2) {
		this.id = id;
		this.id2 = id2;
		this.message = message;
		this.error = error;
		this.finalError=error;
		if(flag == "||" || flag == "&&"){
			this.flag = flag;
		}
		this.require = true;
		if(matchedId1 == "" || matchedId1 == null){
			this.matchedId1 = 1;
		}else{
			this.matchedId1 = matchedId1;
		}
		if(matchedId2 == "" || matchedId2 == null){
			this.matchedId2 = 1;
		}else{
			this.matchedId2 = matchedId2;
		}
		this.status = status;
		this.finalStatus = status;
		if(sign1 == '==' || sign1 == '>' || sign1 == '<' || sign1 == '>=' || sign1 == '<=' || sign1 == '!='){
			this.sign1 = sign1;
		}
		if(sign2 == '==' || sign2 == '>' || sign2 == '<' || sign2 == '>=' || sign2 == '<=' || sign2 == '!='){
			this.sign2 = sign2;
		}
		if(typeof(coefficient1) == "number" && coefficient1 == 0){
			this.coefficient1 == 0;
		}else if(coefficient1 == "" || coefficient1 == null){
			this.coefficient1 = 1;
		}else{
			this.coefficient1 = coefficient1;
		}
		if(coefficient2 == null){
			this.coefficient2 = 1;
		}else{
			this.coefficient2 = coefficient2;
		}
		if(rule1 != null){
			this.rule1 = rule1;
		}
		if(rule2 != null){
			this.rule2 = rule2;
		}
	}, 
	validate:function () {
		if(this.rule1 != null){
			if (!this.rule1.validate())
			{
               this.error = this.rule1.error;
               this.status = this.rule1.status;
			   return false;
			}		
		}
		if(this.rule2 != null){
			if (!this.rule2.validate())
			{
               this.error = this.rule2.error;
               this.status = this.rule2.status;
			   return false;
			}		
		}
		this.error = this.finalError;
		this.status = this.finalStatus;
		var newId1 = this.id.split(",");
		var newId2 = this.id2.split(",");
		var newMatchedId1 = "";
		var newMatchedId2 = "";
		var str1 = 0;
		var str2 = 0;
		var str3 = 0;
		var str4 = 0;

		for(var i = 0; i < newId1.length;i++){
			str1 += Number(document.getElementById(newId1[i]).value);
		}
		if(this.matchedId1 == 1){
			str2 = 1;
		}else{
			newMatchedId1 = this.matchedId1.split(",");	
			for(var i = 0; i < newMatchedId1.length;i++){
				str2 += Number(document.getElementById(newMatchedId1[i]).value);
			}
		}
		str2 = Number(str2.toFixed(1));
		
		for(var i = 0; i < newId2.length;i++){
			str3 += Number(document.getElementById(newId2[i]).value);
		}
		if(this.matchedId2 == 1){
			str4 = 1;
		}else{
			newMatchedId2 = this.matchedId2.split(",");	
			for(var i = 0; i < newMatchedId2.length;i++){
				str4 += Number(document.getElementById(newMatchedId2[i]).value);
			}
		}
		str4 = Number(str4.toFixed(1));
		if(typeof(this.coefficient1) == "undefined"){
			this.coefficient1 = 0;
		}
		if (this.require==true) {
			if(isEmpty($F(this.id))){return false;}
			return isMyMatchOfLogic(str1,str2,this.coefficient1,this.sign1,this.flag,str3,str4,this.coefficient2,this.sign2);
		}else{
			if(!isEmpty($F(this.id))){
				return isMyMatchOfLogic(str1,str2,this.coefficient1,this.sign1,this.flag,str3,str4,this.coefficient2,this.sign2);
			}
			return true;
		}
	}
}
);

//验证精度纬度范围(必须满足小数点3位及以上)
var MathedOfXxJWDValidation = Class.create();
MathedOfXxJWDValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign1, coefficient1, matchedId1, flag, id2, sign2, coefficient2, matchedId2, require, status, message, error) {
		this.id = id;
		this.id2 = id2;
		this.message = message;
		this.error = error;
		if(flag == "||" || flag == "&&"){
			this.flag = flag;
		}
		this.require = true;
		if(matchedId1 == "" || matchedId1 == null){
			this.matchedId1 = 1;
		}else{
			this.matchedId1 = matchedId1;
		}
		if(matchedId2 == "" || matchedId2 == null){
			this.matchedId2 = 1;
		}else{
			this.matchedId2 = matchedId2;
		}
		this.status = status;
		if(sign1 == '==' || sign1 == '>' || sign1 == '<' || sign1 == '>=' || sign1 == '<=' || sign1 == '!='){
			this.sign1 = sign1;
		}
		if(sign2 == '==' || sign2 == '>' || sign2 == '<' || sign2 == '>=' || sign2 == '<=' || sign2 == '!='){
			this.sign2 = sign2;
		}
		if(coefficient1 == "" || coefficient1 == null){
			this.coefficient1 = 1;
		}else{
			this.coefficient1 = coefficient1;
		}
		//if(coefficient2 == "" || coefficient2 == null){
		if(coefficient2 == null){
			this.coefficient2 = 1;
		}else{
			this.coefficient2 = coefficient2;
		}
	}, 
	validate:function () {
		var newId1 = this.id.split(",");
		var newId2 = this.id2.split(",");
		var newMatchedId1 = "";
		var newMatchedId2 = "";
		var str1 = 0;
		var str2 = 0;
		var str3 = 0;
		var str4 = 0;

		for(var i = 0; i < newId1.length;i++){
			str1 += parseFloat(document.getElementById(newId1[i]).value);
		}
		if(this.matchedId1 == 1){
			str2 = 1;
		}else{
			newMatchedId1 = this.matchedId1.split(",");	
			for(var i = 0; i < newMatchedId1.length;i++){
				str2 += parseFloat(document.getElementById(newMatchedId1[i]).value);
			}
		}
		
		for(var i = 0; i < newId2.length;i++){
			str3 += parseFloat(document.getElementById(newId2[i]).value);
		}
		if(this.matchedId2 == 1){
			str4 = 1;
		}else{
			newMatchedId2 = this.newMatchedId2.split(",");	
			for(var i = 0; i < newMatchedId2.length;i++){
				str4 += parseFloat(document.getElementById(newMatchedId2[i]).value);
			}
		}
		
		if (this.require==true) {
			if(isEmpty($F(this.id)) || !isTrue($F(this.id),regObj["_swxs"])){return false;}
			return isMyMatchOfLogic(str1,str2,this.coefficient1,this.sign1,this.flag,str3,str4,this.coefficient2,this.sign2);
		}else{
			if(!isEmpty($F(this.id))){
				return isMyMatchOfLogic(str1,str2,this.coefficient1,this.sign1,this.flag,str3,str4,this.coefficient2,this.sign2);
			}
			return true;
		}
	}
}
);

//the validation below is not needed just left for learn 
/**
* the RequiredValidation class
*/
var OnlyRequiredValidation = Class.create();
OnlyRequiredValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, message, error) {		
		this.id = id;
		this.message = message;
		this.error = error;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {
			return !isEmpty($F(this.id));
		}
		return true;
	}
}
);
/*
 * EmailValidation class
 */
var EmailValidation = Class.create();
EmailValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, message, error) {
		this.id = id;
		this.message = message;
		this.error = error;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {
			//if(isEmpty($F(this.id))){return false;}
			return isEmail($F(this.id));
		}else{
			if(!isEmpty($F(this.id))){
				return isEmail($F(this.id));
			}
			return true;
		}
	}
}
);


/*
 * EnglishValidation class
 */
var EnglishValidation = Class.create();
EnglishValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, message, error) {
		this.id = id;
		this.message = message;
		this.error = error;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {
			if(isEmpty($F(this.id))){return false;}
			return isLetter($F(this.id));
		}else{
			if(!isEmpty($F(this.id))){
				return isLetter($F(this.id));
			}
			return true;
		}
	}
}
);

/*
 * ChineseValidation class
 */
var ChineseValidation = Class.create();
ChineseValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id,require, message, error) {
		this.id = id;
		this.message = message;
		this.error = error;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {			
			return isChinese($F(this.id));
		}else{
			if(!isEmpty($F(this.id))){
				return isChinese($F(this.id));
			}
			return true;
		}
	}
}
);
/*
 * CustomValidation class
 */
var CustomValidation = Class.create();
CustomValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, require, status, message, error,reg) {
		this.id = id;
		this.message = message;
		this.error = error;
		this.status = status;
		this.reg=reg;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==false) {
			if(isEmpty($F(this.id))){return true;}
		}		
		return isTrue($F(this.id),this.reg);
	}
}
);

/*
 * DateValidation class
 */
var DateValidation = Class.create();
DateValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign, matchedId, require, status, message, error, rule1, rule2) {
		this.id = id;
		if(strDateTime(matchedId)){
			this.matchedId = matchedId;
		}else{
			this.matchedId = $F(matchedId);
		}
		this.status = status;
		this.finalStatus = status;
		if(sign == '==' || sign == '>' || sign == '<' || sign == '>=' || sign == '<=' || sign == '!='){
			this.sign = sign;
		}
		this.message = message;
		this.error = error;
		this.finalError=error;
		if (require=="true") {
			this.require = true;
		}
		if(rule1 != null){
			this.rule1 = rule1;
		}
		if(rule2 != null){
			this.rule2 = rule2;
		}
	}, 
	validate:function () {
		if(this.rule1 != null){
			if (!this.rule1.validate())
			{
               this.error = this.rule1.error;
               this.status = this.rule1.status;
			   return false;
			}		
		}
		if(this.rule2 != null){
			if (!this.rule2.validate())
			{
               this.error = this.rule2.error;
               this.status = this.rule2.status;
			   return false;
			}		
		}
		this.error = this.finalError;
		this.status = this.finalStatus;
		if (this.require==true) {
			if(isEmpty($F(this.id)) && isEmpty(this.matchedId)){return false;}
			return isStartEndDate($F(this.id), this.sign, this.matchedId);
		}else{
			if(!isEmpty($F(this.id)) && !isEmpty(this.matchedId)){
				return isStartEndDate($F(this.id),this.sign,this.matchedId);
			}
			return true;
		}
	}
}
);

/*
 * DateOfLogicValidation class
 * 验证时间是否在一个范围值内
 */
 var DateOfLogicValidation = Class.create();
DateOfLogicValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign1, matchedId1, flag, id2, sign2, matchedId2, require, status, message, error, rule1, rule2) {
		this.id = id;
		this.id2 = id2;
		if(sign1 == '==' || sign1 == '>' || sign1 == '<' || sign1 == '>=' || sign1 == '<=' || sign1 == '!='){
			this.sign1 = sign1;
		}
		if(sign2 == '==' || sign2 == '>' || sign2 == '<' || sign2 == '>=' || sign2 == '<=' || sign2 == '!='){
			this.sign2 = sign2;
		}
		if(strDateTime(matchedId1)){
			this.matchedId1 = matchedId1;
		}else{
			this.matchedId1 = $F(matchedId1);
		}
		if(strDateTime(matchedId2)){
			this.matchedId2 = matchedId2;
		}else{
			this.matchedId2 = $F(matchedId2);
		}
		if(flag == "||" || flag == "&&"){
			this.flag = flag;
		}
		this.status = status;
		this.message = message;
		this.error = error;
		this.finalError=error;
		if(rule1 != null){
			this.rule1 = rule1;
		}
		if(rule2 != null){
			this.rule2 = rule2;
		}
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if(this.rule1 != null){
			if (! this.rule1.validate())
			{
               this.error = this.rule1.error;
			   return false;
			}		
		}
		if(this.rule2 != null){
			if (! this.rule2.validate())
			{
               this.error = this.rule2.error;
			   return false;
			}		
		}
		this.error = this.finalError;
		if (this.require==true) {
			if(isEmpty($F(this.id)) && isEmpty(this.matchedId)){return false;}
			return isStartEndDateOfLogic($F(this.id), this.sign1, this.matchedId1, this.flag, $F(this.id2), this.sign2, this.matchedId2);
		}else{
			if(!isEmpty($F(this.id)) && !isEmpty(this.matchedId)){
				return isStartEndDateOfLogic($F(this.id), this.sign1, this.matchedId1, this.flag, $F(this.id2), this.sign2, this.matchedId2);
			}
			return true;
		}
	}
}
);
 


/*
 * BjghDateValidation class
 */
var BjghDateValidation = Class.create();
BjghDateValidation.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign, matchedId, require, status, message, error) {
		this.id = window.frames["addXmFrame"].document.getElementById(id).value;
		if(strDateTime(matchedId)){
			this.matchedId = matchedId;
		}else{
			this.matchedId = frames["addXmFrame"].document.getElementById(matchedId).value;
		}
		this.status = status;
		if(sign == '==' || sign == '>' || sign == '<' || sign == '>=' || sign == '<=' || sign == '!='){
			this.sign = sign;
		}
		this.message = message;
		this.error = error;
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if (this.require==true) {
			if(isEmpty(this.id) && isEmpty(this.matchedId)){return false;}
			return isStartEndDate(this.id, this.sign, this.matchedId);
		}else{
			if(!isEmpty(this.id) && !isEmpty(this.matchedId)){
				return isStartEndDate(this.id,this.sign,this.matchedId);
			}
			return true;
		}
	}
}
);

var DateValidation_AllowEmpty = Class.create();
DateValidation_AllowEmpty.prototype = Object.extend(new Validation(), 
{
	initialize:function (id, sign, matchedId, require, status, message, error, rule1, rule2) {
		this.id = id;
		if(strDateTime(matchedId)){
			this.matchedId = matchedId;
		}else{
			this.matchedId = $F(matchedId);
		}
		this.status = status;
		if(sign == '==' || sign == '>' || sign == '<' || sign == '>=' || sign == '<=' || sign == '!='){
			this.sign = sign;
		}
		this.message = message;
		this.error = error;
		this.finalError=error;
		if(rule1 != null){
			this.rule1 = rule1;
		}
		if(rule2 != null){
			this.rule2 = rule2;
		}
		if (require=="true") {
			this.require = true;
		}
	}, 
	validate:function () {
		if(this.rule1 != null){
			if (! this.rule1.validate())
			{
               	this.error = this.rule1.error;
			   return false;
			}		
		}
		if(this.rule2 != null){
			if (! this.rule2.validate())
			{
               this.error = this.rule2.error;
			   return false;
			}		
		}
		this.error = this.finalError;
		if (this.require==true) {
			if(isEmpty($F(this.id)) || isEmpty(this.matchedId)){return true;}
			return isStartEndDate($F(this.id), this.sign, this.matchedId);
		}else{
			if(!isEmpty($F(this.id)) && !isEmpty(this.matchedId)){
				return isStartEndDate($F(this.id),this.sign,this.matchedId);
			}
			return true;
		}
	}
}
);
 
/*------------------- below the common used function------start-------------------------*/



function strDateTime(str){
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if(r==null)return false;
	var d= new Date(r[1], r[3]-1, r[4]);
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}

// returns true if the string is empty
function isEmpty(str) {
	return (str == null) || (str.length == 0);
}
// returns true if the string is a valid email
function isEmail(str) {
	if (isEmpty(str)) {
		return false;
	}
	var re = /^[^\s()<>@,;:\/]+@\w[\w\.-]+\.[a-z]{2,}$/i;
	return re.test(str);
}
// returns true if the string only contains characters A-Z or a-z
function isLetter(str) {
	var re = /[^a-zA-Z]/g;
	if (re.test(str)) {
		return false;
	}
	return true;
}
function isChinese(str){
	return  /^[\u0391-\uFFE5]+$/.test(str);
}
// returns true if the string only contains characters 0-9
function isNumeric(str) {
	var re = /[\D]/g;
	if (re.test(str)) {
		return false;
	}
	return true;
}
// returns true if the string only contains characters A-Z, a-z or 0-9
function isLetterAndNumeric(str) {
	var re = /[^a-zA-Z0-9-]/g;
	if (re.test(str)) {
		return false;
	}
	return true;
}

// returns true if the string is a US phone number formatted as...// (000)000-0000, (000) 000-0000, 000-000-0000, 000.000.0000, 000 000 0000, 0000000000
function isPhoneNumber(str) {
	var re = /^\(?[2-9]\d{2}[\)\.-]?\s?\d{3}[\s\.-]?\d{4}$/;
	return re.test(str);
}
// returns true if the string is a valid date formatted as...// mm dd yyyy, mm/dd/yyyy, mm.dd.yyyy, mm-dd-yyyy

function isDate(str) {
	var re = /^(\d{4})[\s\.\/-](\d{1,2})[\s\.\/-](\d{1,2})$/;
	if (!re.test(str)) {
		return false;
	}
	var result = str.match(re);
	var y = parseInt(result[1]);
	var m = parseInt(result[2]);
	var d = parseInt(result[3]);
	if (m < 1 || m > 12 || y < 1900 || y > 2100) {
		return false;
	}
	if (m == 2) {
		var days = ((y % 4) == 0) ? 29 : 28;
	} else {
		if (m == 4 || m == 6 || m == 9 || m == 11) {
			var days = 30;
		} else {
			var days = 31;
		}
	}
	return (d >= 1 && d <= days);
}
// returns true if "str1" is the same as the "str2"
function isMatch(str1, str2) {
	return str1 == str2;
}
// return true if "str1" satisfy the conditions "str2" 
function isMyMatch(str1, str2, coefficient,sgin) {
	if(str2 == null){
		str2 = 1;
	}else{
		str2 = str2;
	}
	if(sgin == '=='){
		if(str1==str2*coefficient){
			return true;
		}else
			return false;
	}else if(sgin == '>'){
		if(str1>str2*coefficient){
			return true;
		} else
			return false;
	}else if(sgin == '<'){
		if(str1<str2*coefficient){
			return true;
		}else 
			return false;
	}else if(sgin == '>='){
		if(str1>=str2*coefficient){
			return true;
		}else
			return false;
	}else if(sgin == '<='){
		if(str1<=str2*coefficient){
			return true;
		}else
			return false;
	}else if(sgin == '!='){
		if(str1!=str2*coefficient){
			return true;
		}else
			return false;
	}
}

// return true if "str1,str2" satisfy the conditions "str3,str4" 
function isMyMatchOfLogic(str1, str2, coefficient1,sgin1, flag, str3, str4, coefficient2,sgin2) {
	if(str2 == null){
		str2 = 1;
	}else{
		str2 = str2;
	}
	if(str4 == null){
		str4 = 1;
	}else{
		str4 = str4;
	}
	if(flag == '&&'){
		if(sgin1 == '>' && sgin2 == '<'){
			if(str1>str2*coefficient1 && str3<str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '>' && sgin2 == '<='){
			if(str1>str2*coefficient1 && str3<=str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '>=' && sgin2 == '<'){
			if(str1>=str2*coefficient1 && str3<str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '>=' && sgin2 == '<='){
			if(str1>=str2*coefficient1 && str3<=str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '<' && sgin2 == '>'){
			if(str1<str2*coefficient1 && str3>str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '<' && sgin2 == '>='){
			if(str1<str2*coefficient1 && str3>=str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '<=' && sgin2 == '>'){
			if(str1<=str2*coefficient1 && str3>str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}else if(sgin1 == '<=' && sgin2 == '>='){
			if(str1<=str2*coefficient1 && str3>=str4*coefficient2){
				return true;
			}else{
				return false;
			}
		}
	}else if(flag == '||'){
		if((sgin1 == '>' || sgin1 == '>=' || sgin1 == '<' || sgin1 == '<=' || sgin1 == '==') || (sgin2 == '>' || sgin2 == '>=' || sgin2 == '<' || sgin2 == '<=' || sgin2 == '==')){
			if(str1>str2*coefficient1 || str3==str4*coefficient2){
				return true;
			}else{
				return false;
			}
//			if((str1>str2*coefficient1 || str3>str4*coefficient2) || (str1>str2*coefficient1 || str3>=str4*coefficient2) ||
//				(str1>str2*coefficient1 || str3<str4*coefficient2) || (str1>str2*coefficient1 || str3<=str4*coefficient2) || (str1>str2*coefficient1 || str3==str4*coefficient2) ||
//				(str1>=str2*coefficient1 || str3>str4*coefficient2) || (str1>=str2*coefficient1 || str3>=str4*coefficient2) ||
//				(str1>=str2*coefficient1 || str3<str4*coefficient2) || (str1>=str2*coefficient1 || str3<=str4*coefficient2) || (str1>=str2*coefficient1 || str3==str4*coefficient2) ||
//				(str1<str2*coefficient1 || str3>str4*coefficient2) || (str1<str2*coefficient1 || str3>=str4*coefficient2) ||
//				(str1<str2*coefficient1 || str3<str4*coefficient2) || (str1<str2*coefficient1 || str3<=str4*coefficient2) || (str1<str2*coefficient1 || str3==str4*coefficient2) ||
//				(str1<=str2*coefficient1 || str3>str4*coefficient2) || (str1<=str2*coefficient1 || str3>=str4*coefficient2) ||
//				(str1<=str2*coefficient1 || str3<str4*coefficient2) || (str1<=str2*coefficient1 || str3<=str4*coefficient2) || (str1<=str2*coefficient1 || str3==str4*coefficient2) ||
//				(str1==str2*coefficient1 || str3>str4*coefficient2) || (str1==str2*coefficient1 || str3>=str4*coefficient2) ||
//				(str1==str2*coefficient1 || str3<str4*coefficient2) || (str1==str2*coefficient1 || str3<=str4*coefficient2) || (str1==str2*coefficient1 || str3==str4*coefficient2)){
//				return true;
//			} else{
//				return false;
//			}
		}
	}
}

//return true if the startDate lessthan endDate
function isStartEndDate(startDate,sign,endDate){ 
	var allStartDate;
	var allEndDate;
	if(startDate.length>0&&endDate.length>0){ 
		var startDateTemp = startDate.split(" ");   
		var arrStartDate = startDateTemp[0].split("-");  
		allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]).getTime();   
		if(strDateTime(endDate)){
			allEndDate = endDate;
		}
		var endDateTemp = endDate.split(" ");   
		var arrEndDate = endDateTemp[0].split("-"); 
		allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]).getTime();	  
		if(sign == ">"){
			if(allStartDate > allEndDate){   
				return true;   
			}else
				return false;
		}else if(sign == ">="){
			if(allStartDate >= allEndDate){   
				return true;   
			}else
				return false;
		}else if(sign == "=="){
			if(allStartDate == allEndDate){   
				return true;   
			}else
				return false;
		}else if(sign == "<"){
			if(allStartDate < allEndDate){   
				return true;   
			}else
				return false;
		}else if(sign == "<="){
			if(allStartDate <= allEndDate){ 
				return true;   
			}else
				return false;
		}else if(sign == "!="){
			if(allStartDate != allEndDate){   
				return true;   
			}else
				return false;
		}
	}    
}  

//return true if the startDate lessthan maxDate and startDate greaterthan mixDate
function isStartEndDateOfLogic(startDate,sgin1,matchedDate1,flag,startDate2,sgin2,matchedDate2){
	var allStartDate;
	var allStratDate2;
	var allMatchedDate1;
	var allMatchedDate2;
	if(startDate.length>0 && matchedDate1.length>0 && startDate2.length>0 && matchedDate2.length>0){ 
		var startDateTemp = startDate.split(" ");   
		var arrStartDate = startDateTemp[0].split("-");  
		allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]).getTime();   
		
		var startDateTemp2 = startDate2.split(" ");   
		var arrStartDate2 = startDateTemp2[0].split("-");  
		allStratDate2 = new Date(arrStartDate2[0],arrStartDate2[1],arrStartDate2[2]).getTime();   
		
		if(strDateTime(matchedDate1)){
			allMatchedDate1 = matchedDate1;
		}
		var endDateTemp = matchedDate1.split(" ");   
		var arrEndDate = endDateTemp[0].split("-"); 
		allMatchedDate1 = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]).getTime();	  
		
		if(strDateTime(matchedDate2)){
			allMatchedDate2 = matchedDate2;
		}
		var endDateTemp2 = matchedDate2.split(" ");   
		var arrEndDate2 = endDateTemp2[0].split("-"); 
		allMatchedDate2 = new Date(arrEndDate2[0],arrEndDate2[1],arrEndDate2[2]).getTime();
		
		if(flag == "&&"){
			if(sgin1 == '>' && sgin2 == '<'){
				if(allStartDate>allMatchedDate1 && allStratDate2<allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '>' && sgin2 == '<='){
				if(allStartDate>allMatchedDate1 && allStratDate2<=allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '>=' && sgin2 == '<'){
				if(allStartDate>=allMatchedDate1 && allStratDate2<allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '>=' && sgin2 == '<='){
				if(allStartDate>=allMatchedDate1 && allStratDate2<=allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '<' && sgin2 == '>'){
				if(allStartDate<allMatchedDate1 && allStratDate2>allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '<' && sgin2 == '>='){
				if(allStartDate<allMatchedDate1 && allStratDate2>=allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '<=' && sgin2 == '>'){
				if(allStartDate<=allMatchedDate1 && allStratDate2>allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}else if(sgin1 == '<=' && sgin2 == '>='){
				if(allStartDate<=allMatchedDate1 && allStratDate2>=allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}
		}else if(flag == "||"){
			if((sgin1 == '>' || sgin1 == '>=' || sgin1 == '<' || sgin1 == '<=' || sqin1 == '==') || (sgin2 == '>' || sgin2 == '>=' || sgin2 == '<' || sgin2 == '<=' || sgin2 == '==')){
				if(allStartDate>allMatchedDate1 || allStratDate2==allMatchedDate2){
					return true;
				}else{
					return false;
				}
			}
		}
	}    
}



// returns true if the string contains only whitespace  ps: cannot check a password type input for whitespace?
function isWhitespace(str) { 
	var re = /[\S]/g;
	if (re.test(str)) {
		return false;
	}
	return true;
}

// removes any whitespace with replacement from the string and returns the result
function stripWhitespace(str, replacement) {// NOT USED IN FORM VALIDATION
	if (replacement == null) {
		replacement = "";
	}
	var result = str;
	var re = /\s/g;
	if (str.search(re) != -1) {
		result = str.replace(re, replacement);
	}
	return result;
}



function isTrue(value, reg){
		return reg.test(value);
}

function lenB(str){
		return str.replace(/[^\x00-\xff]/g,"**").length;
}

function len(str){
		return str.length;
}
function isInRange(len,min, max){
		//alert(len);
		//alert(min)
		//alert(max);
		min = min || 0;
		max = max || Number.MAX_VALUE;
		//alert(min <= len && len <= max);
		return min <= len && len <= max;
}
function compare(op1,operator,op2){
		switch (operator) {
			case "NotEqual":
				return (op1 == op2);
			case "Equal":
				return (op1 != op2);
			case "GreaterThan":
				return (op1 > op2);
			case "GreaterThanEqual":
				return (op1 >= op2);
			case "LessThan":
				return (op1 < op2);
			case "LessThanEqual":
				return (op1 <= op2);
			default:
				return (op1 == op2);            
		}
}
/**
function selectRange(require,name,id, min, max){
	var groups = document.getElementById(id);
	var hasChecked = 0;
	min = require==true?min || 1: 0;
	max = max || groups.options.length;
	//alert(min+"min\\max:==>"+groups.options.length)
	for(var i=groups.options.length-1;i>=0;i--){
		var opt =groups[i];
		if(opt.selected&&opt.value!="") hasChecked++;
	}
	return min <= hasChecked && hasChecked <= max;
}
**/

function selectRange(require,name,id, min, max){
	var groups = document.getElementById(id);
	var hasChecked = 0;
	var flag = false;
	//min = require==true?min || 1: 0;
	//max = max || groups.options.length;
	//alert(min+"min\\max:==>"+groups.options.length)
	if(require == false){
		flag = true;
	}else{
		for(var i = 0; i < groups.options.length; i++) {
			var opt =groups[i];
			if(opt.selected == true){
				if(opt.value != ""){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
	}
	return flag;
}

function MySelectRange(require,name,id, min, max){
	var groups = id;
	var hasChecked = 0;
	var flag = false;
	//min = require==true?min || 1: 0;
	//max = max || groups.options.length;
	//alert(min+"min\\max:==>"+groups.options.length)
	if(require == false){
		flag = true;
	}else{
		for(var i = 0; i < groups.options.length; i++) {
			var opt =groups[i];
			if(opt.selected == true){
				if(opt.value != ""){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
	}
	return flag;
}
//the checkbox must checked in the right number if require=true ,else  must less than the groups's length

function radioRange(require,name, min, max){
	var groups = document.getElementsByName(name);
	var flag = false;
	//var hasChecked = 0;
	//min = require==true?min || 1: 0;
	//max = max || groups.length;
	//alert(min+"min\\max:==>"+max)
	if(require == false){
		flag = true;
	}else{
		for(var i=0; i<groups.length; i++){
			if(groups[i].checked){
				if(groups.value != ""){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
	}
	return flag;
}
function checkboxRange(require,name, min, max){
	var groups = document.getElementsByName(name);
	var flag = false;
	//var hasChecked = 0;
	//min = require==true?min || 1: 0;
	//max = max || groups.length;
	//alert(min+"min\\max:==>"+max)
	if(require == false){
		flag = true;
	}else{
		for(var i=0; i<groups.length  ; i++){
			if(groups[i].checked){
				if(groups.value != ""){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
	}
	return flag;
}


/*------------------- below the common used function------end-------------------------*/

/*--------------------------set the html object the prepare value-------start-------------------------*/

function setSelect(selectId,value,disabled){
	var selectH = document.getElementById(selectId);
	var len=selectH.options.length;
	for(var i=0; i<len; i++){
		var opt=selectH[i];
		if(opt.value==value){
			selectH.selectedIndex=i;			
			break;
		}			
		if(opt.childNodes[0].nodeValue==value){
			selectH.selectedIndex=i;
			break;
		}
	}
	selectH.disabled=disabled;
}

function setRadio(name,value,disabled){
	var elements = document.getElementsByName(name);
	for(var i=0; i<elements.length; i++){
		var ele=elements[i];	
		if(value==ele.value){
			ele.setAttribute("checked","true");
			break;
		}
	}
	for(var i=0; i<elements.length; i++){
		var ele=elements[i];
		ele.setAttribute("disabled",disabled);
	}
}

function setCheckbox(name,array,disabled){
	var elements = document.getElementsByName(name);	
	for(var i=0; i<elements.length; i++){
		var ele = elements[i];
		ele.setAttribute("disabled",disabled);
		if(isInArray(array,parseInt(ele.value))){
			ele.setAttribute("checked","checked");
		}
	}	
}

function isInArray(array,value){
	for(var i = 0; i< array.length; i++){
		if(value==array[i]){return true;}
	}
	return false;
}

/*--------------------------set the html object the prepare value---end-----------------------------*/


/*-------------------------- useful functions but some of them only validate in IE---start--------*/

function refuseWithReg(obj,myreg){
	var pos = getPos(obj);

	if(myreg.test(obj.value)){
	 	pos = pos - 1;
	}
	if(obj.value.match){
		obj.value = obj.value.replace(myreg,'');
	}
	setCursor(obj,pos);
}
function getPos(obj) {
        obj.focus();
        var workRange=document.selection.createRange();
        obj.select();
        var allRange=document.selection.createRange();
        workRange.setEndPoint("StartToStart",allRange);
        var len=workRange.text.length;
        workRange.collapse(false);
        workRange.select();

        return len;
}
function setCursor(obj,num){
        range=obj.createTextRange(); 
        range.collapse(true); 
        range.moveStart('character',num); 

        range.select();
}
function trimAllElements(formobj){
	var count = formobj.elements.length;
	for(var i=0;i<count;i++){
		var obj = formobj.elements[i];
		if(obj.tagName == "INPUT" || obj.tagName == "TEXTAREA"){
			obj.value = trim(obj.value);
		}
	}
}
function LTrim( value ) {	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");	
}
function RTrim( value ) {	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");	
}
// Removes leading and ending whitespaces
function trim( value ) {
	return LTrim(RTrim(value));
}

function getCurrentDate(){
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth()+1;
	var date = myDate.getDate();
	if(month < 10){
		month = "0"+month;
	}else{
		month = month;
	}
	if(date < 10){
		date = "0"+date;
	}else{
		date = date;
	}
	var currentDate = year+"-"+month+"-"+date;
	return currentDate;
}

var regObj={
	_require : /.+/,
	_email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	_phone : /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/,
	_mobile : /^1\d{10}$/,
	_url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
	_idCard : /^\d{15}(\d{2}[A-Za-z0-9])?$/,
	_currency : /^\d+(\.\d+)?$/,
	_number : /^\d+$/,
	_zip : /^\d{6}$/,
	_qq : /^[1-9]\d{4,8}$/,
	_integer : /^[-\+]?\d+$/,
	_double : /^[-\+]?\d+(\.\d+)?$/,
	_english : /^[A-Za-z]+$/,
	_chinese :  /^[\u0391-\uFFE5]+$/,
	_unSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
	_date: /(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)/,
	_percentage: /^\d+(\.\d+)?%$/,
	_zzs: /^[0-9]*[1-9][0-9]*$/,               //正整数
	_swsz: /^\d{4}$/,                          //4位数字
	_ffzs: /^\d+$/,                            //非负整数（正整数 + 0）
	_jxnd: /^[1-9]\d{3}$/,                     //建校年代 ("建校年代"建议1000以后)
	_swxs: /^[0-9]+\.?[0-9]{3,7}$/,           //保留三位以上小数(73~135) 3~7位小数
	_ywxs: /^\d+[\.\d]?\d{0,1}$/               //保留一位小数
}
 
/*-------------------------- useful functions but some of them only validate in IE---start--------*/

