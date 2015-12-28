var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在保存，请稍候..."
		});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
//rightGrid.changeToListDS(link4+"?id="+id);
// *********** 功能处理 ***********//
// 初始化
function load() {
	var de = formatDate(new Date(serverDt),"yyyy-MM-dd");
	$("curDate").value=de;
	myMaskLoad.show();
	//$("curDate").value=s;
	Ext.Ajax.request({
		url : link2,
		method : 'POST',
		params : {
			id : id
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			myMaskLoad.hide();
			if (obj.success) {
				var data=obj.data[0];
				$("PZ").innerHTML=data.pz;
				$("PF").innerHTML=data.pf;
				$("QZ").innerHTML=data.qz;
				$("QF").innerHTML=data.qf;
				$("BRPZ").innerHTML=data.brpz;
				$("BRPF").innerHTML=data.brpf;
				$("BRQZ").innerHTML=data.brqz;
				$("BRQF").innerHTML=data.brqf;
				$("BYPZ").innerHTML=data.bypz;
				$("BYPF").innerHTML=data.bypf;
				$("BYQZ").innerHTML=data.byqz;
				$("BYQF").innerHTML=data.byqf;
				$("BRYG").innerHTML=parseFloat(data.brpz)-parseFloat(data.brpf);
				$("BRWG").innerHTML=parseFloat(data.brqz)-parseFloat(data.brqf);
				$("BYYG").innerHTML=parseFloat(data.bypz)-parseFloat(data.bypf);
				$("BYWG").innerHTML=parseFloat(data.byqz)-parseFloat(data.byqf);
				var chart=obj.chart;
				renderChart(chart);
			}
		}
	});
}

function renderChart(chartData){
	var oXML=XMLUtil.load_xml(chartData);
	var chart = new FusionCharts(contextPath+"/component/chart/Charts/MSLine.swf", "chart1Id", document.body.offsetWidth-5, 150);
	chart.setDataXML(oXML.xml);
	chart.setTransparent(true);
	chart.render("flashcontent");
}

/**点击单选框进行类型选择
**/
function selectType(obj){
	var curDate=$("curDate").value;
	var type=obj.value;
	Ext.Ajax.request({
		url : link2,
		method : 'POST',
		params : {
			id : id,
			curDate:curDate,
			type:type
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			myMaskLoad.hide();
			if (obj.success) {
				var data=obj.data[0];
				$("PZ").innerHTML=data.pz;
				$("PF").innerHTML=data.pf;
				$("QZ").innerHTML=data.qz;
				$("QF").innerHTML=data.qf;
				$("BRPZ").innerHTML=data.brpz;
				$("BRPF").innerHTML=data.brpf;
				$("BRQZ").innerHTML=data.brqz;
				$("BRQF").innerHTML=data.brqf;
				$("BYPZ").innerHTML=data.bypz;
				$("BYPF").innerHTML=data.bypf;
				$("BYQZ").innerHTML=data.byqz;
				$("BYQF").innerHTML=data.byqf;
				$("BRYG").innerHTML=parseFloat(data.brpz)-parseFloat(data.brpf);
				$("BRWG").innerHTML=parseFloat(data.brqz)-parseFloat(data.brqf);
				$("BYYG").innerHTML=parseFloat(data.bypz)-parseFloat(data.bypf);
				$("BYWG").innerHTML=parseFloat(data.byqz)-parseFloat(data.byqf);
				var chart=obj.chart;
				renderChart(chart);
			}
		}
	});
}

/**选择日期重新取数
**/
function reFindData(){
	var curDate=$("curDate").value;
	var type="";
	var types=document.getElementsByName("type");
	for(var i=0;i<types.length;i++){
		if(types[i].checked){
			type=types[i].value;
		}
	}
	Ext.Ajax.request({
		url : link2,
		method : 'POST',
		params : {
			id : id,
			curDate:curDate,
			type:type
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			myMaskLoad.hide();
			if (obj.success) {
				var data=obj.data[0];
				$("PZ").innerHTML=data.pz;
				$("PF").innerHTML=data.pf;
				$("QZ").innerHTML=data.qz;
				$("QF").innerHTML=data.qf;
				$("BRPZ").innerHTML=data.brpz;
				$("BRPF").innerHTML=data.brpf;
				$("BRQZ").innerHTML=data.brqz;
				$("BRQF").innerHTML=data.brqf;
				$("BYPZ").innerHTML=data.bypz;
				$("BYPF").innerHTML=data.bypf;
				$("BYQZ").innerHTML=data.byqz;
				$("BYQF").innerHTML=data.byqf;
				$("BRYG").innerHTML=parseFloat(data.brpz)-parseFloat(data.brpf);
				$("BRWG").innerHTML=parseFloat(data.brqz)-parseFloat(data.brqf);
				$("BYYG").innerHTML=parseFloat(data.bypz)-parseFloat(data.bypf);
				$("BYWG").innerHTML=parseFloat(data.byqz)-parseFloat(data.byqf);
				var chart=obj.chart;
				renderChart(chart);
				
				parent.rightGrid.changeToListDS(link4+"?id="+id+"&curDate="+curDate+"&type="+type);
			    parent.rightGrid.getStore().reload();
			}
		}
	});
	
}

function submitTopDay() {//上一天  
  var curDate=$("curDate").value;          
  d = new Date(Date.parse(curDate.replace(/-/g,   '/')));       
  d.setDate(d.getDate()-1);       
  t = [d.getYear(),d.getMonth()+1,d.getDate()];       
  $("curDate").value=t.join('-');
  reFindData();
}    
   
   
function submitNextDay(date) {//下一天  
  var curDate=$("curDate").value;           
  d = new Date(Date.parse(curDate.replace(/-/g,'/')));       
  d.setDate(d.getDate()+1);       
  t = [d.getYear(), d.getMonth()+1, d.getDate()];       
  $("curDate").value=t.join('-');   
  reFindData();  
} 