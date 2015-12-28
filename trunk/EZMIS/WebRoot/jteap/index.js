/**
 * 注销
 */
function logout_Click() {
	if (window.confirm('确定要注销吗？')) {
		window.location.href = contextPath + '/LogoutAction.do';
	}
}

/***************** 动态导航菜单 ********************/

var Speed_1 = 10; //速度(毫秒)
var Space_1 = 5; //每次移动(px)
var PageWidth_1 = 107; //翻页宽度
var interval_1 = 5000; //翻页间隔时间
var fill_1 = 0; //整体移位
var MoveLock_1 = false;
var MoveTimeObj_1;
var MoveWay_1="right";
var Comp_1 = 0;
var AutoPlayObj_1=null;
function GetObj(objName){
	if(document.getElementById){
		return eval('document.getElementById("'+objName+'")')
	}else{
		return eval('document.all.'+objName)
	}
}
function AutoPlay_1(){
	clearInterval(AutoPlayObj_1);
	AutoPlayObj_1=setInterval('ISL_GoDown_1();ISL_StopDown_1();',interval_1)
}
//左边按钮
function ISL_GoUp_1(){
	if(MoveLock_1)
		return;
	clearInterval(AutoPlayObj_1);
	MoveLock_1=true;
	MoveWay_1="left";
	ISL_ScrUp_1();
}
function ISL_StopUp_1(){
	if(MoveWay_1 == "right"){
		return
	};
	clearInterval(MoveTimeObj_1);
	if((GetObj('ISL_Cont_1').scrollLeft-fill_1)%PageWidth_1!=0){
		Comp_1=fill_1-(GetObj('ISL_Cont_1').scrollLeft%PageWidth_1);
		CompScr_1()
	}else{
		MoveLock_1=false
	}
	//AutoPlay_1()
}
//左边按钮执行方法
function ISL_ScrUp_1(){
	//if(GetObj('ISL_Cont_1').scrollLeft<=0){
	//	GetObj('ISL_Cont_1').scrollLeft=GetObj('ISL_Cont_1').scrollLeft+GetObj('List1_1').offsetWidth
	//}
	if(GetObj('ISL_Cont_1').scrollLeft<PageWidth_1){
		MoveLock_1=false;
	}else{
		GetObj('ISL_Cont_1').scrollLeft-=Space_1;
	}
}
//右边按钮
function ISL_GoDown_1(){
	clearInterval(MoveTimeObj_1);
	if(MoveLock_1)
		return;
	clearInterval(AutoPlayObj_1);
	MoveLock_1=true;
	MoveWay_1="right";
	ISL_ScrDown_1();
}
function ISL_StopDown_1(){
	if(MoveWay_1 == "left"){
		return
	};
	clearInterval(MoveTimeObj_1);
	if(GetObj('ISL_Cont_1').scrollLeft%PageWidth_1-(fill_1>=0?fill_1:fill_1+1)!=0){
		Comp_1=PageWidth_1-GetObj('ISL_Cont_1').scrollLeft%PageWidth_1+fill_1;
		CompScr_1()
	}else{
		MoveLock_1=false
	}
	//AutoPlay_1()
}
//右边按钮执行方法
function ISL_ScrDown_1(){
	//if(GetObj('ISL_Cont_1').scrollLeft>=GetObj('List1_1').scrollWidth){
	//	GetObj('ISL_Cont_1').scrollLeft=GetObj('ISL_Cont_1').scrollLeft-GetObj('List1_1').scrollWidth
	//}
	var picSum = document.getElementById("picNum").value;  //图片总数
	//alert(document.body.clientWidth%107);
	var displatPicSum = Math.round((document.body.clientWidth/107));    //可视图片数
	//alert(GetObj('ISL_Cont_1').scrollLeft+"<--------->"+picSum+"<---------->"+displatPicSum);
	if(GetObj('ISL_Cont_1').scrollLeft>=107*(picSum-displatPicSum)){
		MoveLock_1=false;
	}else{
		GetObj('ISL_Cont_1').scrollLeft+=Space_1;
	}
}
function CompScr_1(){
	if(Comp_1==0){
		MoveLock_1=false;
		return
	}
	var num,TempSpeed=Speed_1,TempSpace=Space_1;
	if(Math.abs(Comp_1)<PageWidth_1/2){
		TempSpace=Math.floor(Math.abs(Comp_1/Space_1));
		if(TempSpace<1){
			TempSpace=1
		}
	}
	if(Comp_1<0){
		if(Comp_1<-TempSpace){
			Comp_1+=TempSpace;
			num=TempSpace
		}else{
			num=-Comp_1;
			Comp_1=0
		}
		GetObj('ISL_Cont_1').scrollLeft-=num;
		setTimeout('CompScr_1()',TempSpeed)
	}else{if(Comp_1>TempSpace){
		Comp_1-=TempSpace;
		num=TempSpace
	}else{
		num=Comp_1;
		Comp_1=0
	}
	GetObj('ISL_Cont_1').scrollLeft+=num;
	setTimeout('CompScr_1()',TempSpeed)
}}
function picrun_ini(){
	GetObj("List2_1").innerHTML=GetObj("List1_1").innerHTML;
	GetObj('ISL_Cont_1').scrollLeft=fill_1>=0?fill_1:GetObj('List1_1').scrollWidth-Math.abs(fill_1);
	GetObj("ISL_Cont_1").onmouseover=function(){clearInterval(AutoPlayObj_1)}
	GetObj("ISL_Cont_1").onmouseout=function(){
		//AutoPlay_1()
	}
	//AutoPlay_1();
}
/*************************************************/