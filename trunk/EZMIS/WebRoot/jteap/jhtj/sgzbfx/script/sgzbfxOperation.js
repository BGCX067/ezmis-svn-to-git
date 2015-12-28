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
    
    searchPanel.searchClick();
}


function renderChart(chartData,tableData,fields){
	var oXML=XMLUtil.load_xml(chartData);
	var path=contextPath+"/component/chart/Charts/MSColumn2D.swf";
	var chart = new FusionCharts(path, "chart1", document.body.offsetWidth-20, document.body.offsetHeight-300);
	chart.setDataXML(oXML.xml);
	chart.setTransparent(true);
	chart.render("flashcontent");
	
	createTable(tableData,fields);
}


function createTable(tableData,fields){
	var xmlDom=getDom(tableData);
	var datasets=xmlDom.childNodes[0];
	var tableWidth=100;
	var fieldList=fields.split("!");
	var caption=new Array();//存放标题
	var contents=new Array();//存放内容
	for(var i=0;i<datasets.childNodes.length;i++){
		var dataset=datasets.childNodes[i];
		var labelTd="";
		var valueTd="";
		var propertys=dataset.childNodes;
		for (var j=0;j<propertys.length;j++) {
			property=propertys[j];
			var label=property.getAttribute("label");
			var value=property.getAttribute("value");
			labelTd=labelTd+"<td align='center' class='GridCellL2'  width=80>"+label+"</td>";
			valueTd=valueTd+"<td align='center' class='GridCellL2' width=80>"+value+"</td>";
			if(i==0){
				tableWidth=tableWidth+80;
			}
		}
		if(i==0){
			caption[i]=labelTd;
		}
		contents[i]=valueTd;
	}
	var table="<table class='FBorder' width='"+tableWidth+"'><tr class='GridCellJ'><td nowrap='nowrap' width='100px' class='GridCellL'>日期/数据</td>"+caption[0]+"</tr>";
	for(var i=0;i<contents.length;i++){
		var itemAndCname=fieldList[i].split(",");
		table=table+"<tr><td nowrap='nowrap' width='100px' class='GridCellL'>"+itemAndCname[1]+"</td>"+contents[i]+"</tr>";
	}
	table=table+"</table>";
	Ext.getDom("tablecontent").innerHTML=table;
}