function intiDate(){
	var jzStr="";
	if(isJz=="true"){
		jzStr="机组#JZ#comboBox#"+flflag+"#7#false#.2";
	}
	//日数据
	if(flflag==1){
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.08,#SRI#comboBox#"+flflag+"#3#true#.1,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.08,#ERI#comboBox#"+flflag+"#6#true#.1"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.08,#SRI#comboBox#"+flflag+"#3#true#.1,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.08,#ERI#comboBox#"+flflag+"#6#true#.1"+(jzStr==""?"":","+jzStr)).split(",");
	}else if(flflag==2){
	//月数据
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.1,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.1"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.1,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.1"+(jzStr==""?"":","+jzStr)).split(",");		
	}else if(flflag==3){
	//年数据
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.2,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.2"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.2,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.2"+(jzStr==""?"":","+jzStr)).split(",");			
	}
    searchPanel.addComponent();
    //触发点击的方法，让页面初始化
    searchPanel.searchClick();
}


function renderChart(chartData,tableData){
	var oXML=XMLUtil.load_xml(chartData);
	var chart = new FusionCharts(contextPath+"/component/chart/Charts/MSLine.swf", "chart1Id", document.body.offsetWidth-20, document.body.offsetHeight-270);
	chart.setDataXML(oXML.xml);
	chart.setTransparent(true);
	chart.render("flashcontent");
	
	createTable(tableData[0]);
}


function createTable(tableData){
	
	var labelTd="";
	var valueTd="";
	var tableWidth=40;
	for(var i=0;i<tableData.length;i++){
		var data=tableData[i];
		for (var p in tableData[i]) {
			labelTd=labelTd+"<td align='center' class='GridCellL2'  width=80>"+p+"</td>";
			valueTd=valueTd+"<td align='center' class='GridCellL2' width=80>"+data[p]+"</td>";
			tableWidth=tableWidth+80;
		}
	}
	var table="<table class='FBorder' width='"+tableWidth+"'><tr class='GridCellJ'><td nowrap='nowrap' width='40px' class='GridCellL'>日期</td>"+labelTd+"</tr><tr><td nowrap='nowrap' width='40px' class='GridCellL'>值</td>"+valueTd+"</tr></table>";
	Ext.getDom("tablecontent").innerHTML=table;
}