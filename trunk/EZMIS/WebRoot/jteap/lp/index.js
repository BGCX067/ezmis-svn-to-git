
var northHeight = 0;
var topTB = null;
var titleBar = null;
var funcPanel = null;
var rightGrid = null;
var topBackGround = null;
var mainPanelHeight = window.screen.availHeight*0.7;
var mainPanelWidth = window.screen.availWidth*0.78;

if(JTEAP_SYSTEM_THEME == "CLASSICS"){
	northHeight = 122;
	topTB = mainTb; //mainTB is defined in Mainmenu.js
	titleBar = {
    	xtype:'box',
		el:'headerDiv',
		border:false,
		anchor: 'none -80'
	};
	topBackGround = 'background-color:/'/'';
}else if(JTEAP_SYSTEM_THEME == "MODERN"){
	northHeight = 130;
	topTB = {
        xtype:'box',
        el:'headerDiv1',
        border:false,
        anchor: 'none -160'
    };
    titleBar = {
	    xtype:'box',
	    el:'headerDiv',
	    border:false,
	    anchor: 'none -180'
	    };
    //rightGrid = new RigthGrid();
    topBackGround = 'background:url(images/topBackground.gif) no-repeat left top;';
}

// 主面板
MainPanel = function() {
	MainPanel.superclass.constructor.call(this,{
		id : 'doc-body',
		//region : 'center',
		//margins : '0 5 5 -2',
		renderTo:"center",
		margins : '0 0 0 0',
		//resizeTabs : true,
		border : false,
		frame:false,
		//tabWidth : 100,
		width:mainPanelWidth,
		height:mainPanelHeight,
		//plugins : new Ext.app.TabCloseMenu(),
		//enableTabScroll : true,
		html:"<IFRAME id='portal' name='portal' scrolling='no' src= 'welcome.html' style='width:100%;height:100%' frameboder='0'></IFRAME>"
//		activeTab : 0,
//		defaults : {
//			tabWidth : 60,
//			closable : true,
//			autoScroll : true
//		},
//		items : [{
//			id : 'welcome-tab',
//			tabWidth : 60,
//			title : '欢迎',
//			html : "<IFRAME id='portal' name='portal' scrolling='no' src="
//					//+ 'system/cms/portal/index.jsp' 
//					+ 'welcome.html'
//					+ " style='width:100%;height:100%' frameborder='no'></IFRAME>",
//			closable : false,
//			autoScroll : true
//		}]
	});
};

Ext.extend(
	MainPanel,
	Ext.Panel,{
		/**
		 * 加载模块
		 * 
		 * @tabId 新增的tab的编号
		 * @tabTitle 新增tab的标题
		 * @href 模块的url
		 */
		loadFunction : function(tabId, tabTitle, href, resStyle) {
			//if(resStyle == 0) {
				href += (href.indexOf("?") >= 0) ? "&" : "?";
				href += "moduleId=" + tabId + "&" + escape(new Date()) ;
				$('portal').src = "../"+href;
			//}
				/*
			var id = 'func-' + tabId;
			var ifarmeId = 'ifarme-' + tabId;
			var tab = this.getComponent(id);
			if (tab) {
				this.setActiveTab(tab);
			} else {
				var sHtml = (typeof(href) != 'undefined')
						? "<IFRAME id='" + ifarmeId + "' src='"
								+"../"+href
								+ "'  style='width:100%;height:100%' frameborder='no'></IFRAME>"
						: "该模块正在开发中，敬请期待。。。。";
				var tab = this.add( {
					title : tabTitle,
					id : id,
					html : sHtml
				});
				this.setActiveTab(tab);
			}
			*/
			//if(isIE && ieVer< 7.0) Ext.get(ifarmeId).dom.src = href;
		}
});


/////////////////运行管理子系统首页js////////////////
//** Chrome Drop Down Menu- Author: Dynamic Drive (http://www.dynamicdrive.com)

//** Updated: July 14th 06' to v2.0
	//1) Ability to "left", "center", or "right" align the menu items easily, just by modifying the CSS property "text-align".
	//2) Added an optional "swipe down" transitional effect for revealing the drop down menus.
	//3) Support for multiple Chrome menus on the same page.

//** Updated: Nov 14th 06' to v2.01- added iframe shim technique

//** Updated: July 23rd, 08 to v2.4
	//1) Main menu items now remain "selected" (CSS class "selected" applied) when user moves mouse into corresponding drop down menu. 
	//2) Adds ability to specify arbitrary HTML that gets added to the end of each menu item that carries a drop down menu (ie: a down arrow image).
	//3) All event handlers added to the menu are now unobstrusive, allowing you to define your own "onmouseover" or "onclick" events on the menu items.
	//4) Fixed elusive JS error in FF that sometimes occurs when mouse quickly moves between main menu items and drop down menus


var cssdropdown={
disappeardelay: 250, //set delay in miliseconds before menu disappears onmouseout
dropdownindicator: '<img src="images/down.gif" border="0" />', //specify full HTML to add to end of each menu item with a drop down menu
enableswipe: 1, //enable swipe effect? 1 for yes, 0 for no
enableiframeshim: 1, //enable "iframe shim" in IE5.5/IE6? (1=yes, 0=no)

//No need to edit beyond here////////////////////////

dropmenuobj: null, asscmenuitem: null, domsupport: document.all || document.getElementById, standardbody: null, iframeshimadded: false, swipetimer: undefined, bottomclip:0,

getposOffset:function(what, offsettype){
	var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
	var parentEl=what.offsetParent;
	while (parentEl!=null){
		totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
		parentEl=parentEl.offsetParent;
	}
	return totaloffset;
},

swipeeffect:function(){
	if (this.bottomclip<parseInt(this.dropmenuobj.offsetHeight)){
		this.bottomclip+=10+(this.bottomclip/10) //unclip drop down menu visibility gradually
		this.dropmenuobj.style.clip="rect(0 auto "+this.bottomclip+"px 0)"
	}
	else
		return
	this.swipetimer=setTimeout("cssdropdown.swipeeffect()", 10)
},

css:function(el, targetclass, action){
	var needle=new RegExp("(^|\\s+)"+targetclass+"($|\\s+)", "ig")
	if (action=="check")
		return needle.test(el.className)
	else if (action=="remove")
		el.className=el.className.replace(needle, "")
	else if (action=="add" && !needle.test(el.className))
		el.className+=" "+targetclass
},

showhide:function(obj, e){
	this.dropmenuobj.style.left=this.dropmenuobj.style.top="-500px"
	if (this.enableswipe==1){
		if (typeof this.swipetimer!="undefined")
			clearTimeout(this.swipetimer)
		obj.clip="rect(0 auto 0 0)" //hide menu via clipping
		this.bottomclip=0
		this.swipeeffect()
	}
	obj.visibility="visible"
	this.css(this.asscmenuitem, "selected", "add")
},

clearbrowseredge:function(obj, whichedge){
	var edgeoffset=0
	if (whichedge=="rightedge"){
		var windowedge=document.all && !window.opera? this.standardbody.scrollLeft+this.standardbody.clientWidth-15 : window.pageXOffset+window.innerWidth-15
		this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetWidth
		if (windowedge-this.dropmenuobj.x < this.dropmenuobj.contentmeasure)  //move menu to the left?
			edgeoffset=this.dropmenuobj.contentmeasure-obj.offsetWidth
	}
	else{
		var topedge=document.all && !window.opera? this.standardbody.scrollTop : window.pageYOffset
		var windowedge=document.all && !window.opera? this.standardbody.scrollTop+this.standardbody.clientHeight-15 : window.pageYOffset+window.innerHeight-18
		this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetHeight
		if (windowedge-this.dropmenuobj.y < this.dropmenuobj.contentmeasure){ //move up?
			edgeoffset=this.dropmenuobj.contentmeasure+obj.offsetHeight
			if ((this.dropmenuobj.y-topedge)<this.dropmenuobj.contentmeasure) //up no good either?
				edgeoffset=this.dropmenuobj.y+obj.offsetHeight-topedge
		}
	}
	return edgeoffset
},

dropit:function(obj, e, dropmenuID){
	if (this.dropmenuobj!=null) //hide previous menu
		this.hidemenu() //hide menu
	this.clearhidemenu()
	this.dropmenuobj=document.getElementById(dropmenuID) //reference drop down menu
	this.asscmenuitem=obj //reference associated menu item
	this.showhide(this.dropmenuobj.style, e)
	this.dropmenuobj.x=this.getposOffset(obj, "left")
	this.dropmenuobj.y=this.getposOffset(obj, "top")
	this.dropmenuobj.style.left=this.dropmenuobj.x-this.clearbrowseredge(obj, "rightedge")+"px"
	this.dropmenuobj.style.top=this.dropmenuobj.y-this.clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+1+"px"
	this.positionshim() //call iframe shim function
},

positionshim:function(){ //display iframe shim function
	if (this.enableiframeshim && typeof this.shimobject!="undefined"){
		if (this.dropmenuobj.style.visibility=="visible"){
			this.shimobject.style.width=this.dropmenuobj.offsetWidth+"px"
			this.shimobject.style.height=this.dropmenuobj.offsetHeight+"px"
			this.shimobject.style.left=this.dropmenuobj.style.left
			this.shimobject.style.top=this.dropmenuobj.style.top
		}
	this.shimobject.style.display=(this.dropmenuobj.style.visibility=="visible")? "block" : "none"
	}
},

hideshim:function(){
	if (this.enableiframeshim && typeof this.shimobject!="undefined")
		this.shimobject.style.display='none'
},

isContained:function(m, e){
	var e=window.event || e
	var c=e.relatedTarget || ((e.type=="mouseover")? e.fromElement : e.toElement)
	while (c && c!=m)try {c=c.parentNode} catch(e){c=m}
	if (c==m)
		return true
	else
		return false
},

dynamichide:function(m, e){
	if (!this.isContained(m, e)){
		this.delayhidemenu()
	}
},

delayhidemenu:function(){
	this.delayhide=setTimeout("cssdropdown.hidemenu()", this.disappeardelay) //hide menu
},

hidemenu:function(){
	this.css(this.asscmenuitem, "selected", "remove")
	this.dropmenuobj.style.visibility='hidden'
	this.dropmenuobj.style.left=this.dropmenuobj.style.top=0
	this.hideshim()
},

clearhidemenu:function(){
	if (this.delayhide!="undefined")
		clearTimeout(this.delayhide)
},

addEvent:function(target, functionref, tasktype){
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false);
	else if (target.attachEvent)
		target.attachEvent('on'+tasktype, function(){return functionref.call(target, window.event)});
},

startchrome:function(){
	if (!this.domsupport)
		return
	this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body
	for (var ids=0; ids<arguments.length; ids++){
		var menuitems=document.getElementById(arguments[ids]).getElementsByTagName("a")
		for (var i=0; i<menuitems.length; i++){
			if (menuitems[i].getAttribute("rel")){
				var relvalue=menuitems[i].getAttribute("rel")
				var asscdropdownmenu=document.getElementById(relvalue)
				this.addEvent(asscdropdownmenu, function(){cssdropdown.clearhidemenu()}, "mouseover")
				this.addEvent(asscdropdownmenu, function(e){cssdropdown.dynamichide(this, e)}, "mouseout")
				this.addEvent(asscdropdownmenu, function(){cssdropdown.delayhidemenu()}, "click")
				try{
					menuitems[i].innerHTML=menuitems[i].innerHTML+" "+this.dropdownindicator
				}catch(e){}
				this.addEvent(menuitems[i], function(e){ //show drop down menu when main menu items are mouse over-ed
					if (!cssdropdown.isContained(this, e)){
						var evtobj=window.event || e
						cssdropdown.dropit(this, evtobj, this.getAttribute("rel"))
					}
				}, "mouseover")
				this.addEvent(menuitems[i], function(e){cssdropdown.dynamichide(this, e)}, "mouseout") //hide drop down menu when main menu items are mouse out
				this.addEvent(menuitems[i], function(){cssdropdown.delayhidemenu()}, "click") //hide drop down menu when main menu items are clicked on
			}
		} //end inner for
	} //end outer for
	if (window.createPopup && !window.XmlHttpRequest &&!this.iframeshimadded){ //if IE5.5 to IE6, create iframe for iframe shim technique
		document.write('<IFRAME id="iframeshim"  src="" style="display: none; left: 0; top: 0; z-index: 90; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)" frameBorder="0" scrolling="no"></IFRAME>')
		this.shimobject=document.getElementById("iframeshim") //reference iframe object
		this.iframeshimadded=true
	}
} //end startchrome

}
////////////////////////////////////////////////////////

/************************** 系统功能 ********************/
/**
 * 注销
 */
function logout_Click() {
	if (window.confirm('确定要注销吗？')) {
		window.location.href = contextPath + '/LogoutAction.do';
	}
}