function intiDate(){
	if(fxfs==2){
		searchPanel.searchDefaultFs=("年#NIAN#comboBox#1#false#.2,实体#ENTIY#comboBox#2#true#.2").split(",");
		searchPanel.searchAllFs=("年#NIAN#comboBox#1#false#.2,实体#ENTIY#comboBox#2#true#.2").split(",");
	    searchPanel.addComponent();
	}else{
		searchPanel.searchDefaultFs=("年#NIAN#comboBox#1#false#.2,实体#ENTIY#comboBox#2#false#.2").split(",");
		searchPanel.searchAllFs=("年#NIAN#comboBox#1#false#.2,实体#ENTIY#comboBox#2#false#.2").split(",");
	    searchPanel.addComponent();
	}
	searchPanel.searchClick();
}


function renderChart(chartData,tableData,fields){
	var oXML=XMLUtil.load_xml(chartData);
	var chart = new FusionCharts(contextPath+"/component/chart/Charts/MSCombiDY2D.swf", "chart1Id", document.body.offsetWidth-20, document.body.offsetHeight-270);
	chart.setDataXML(oXML.xml);
	chart.setTransparent(true);
	chart.render("flashcontent");
	
	createTable(tableData,fields);
}


function createTable(tableData,fields){
	var xmlDom=getDom(tableData);
	var datasets=xmlDom.childNodes[0];
	var tableWidth=150;
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
	var table="<table class='FBorder' width='"+tableWidth+"'><tr class='GridCellJ'><td nowrap='nowrap' width='100px' class='GridCellL'>&nbsp;</td>"+caption[0]+"</tr>";
	for(var i=0;i<contents.length;i++){
		var itemAndCname=fieldList[i].split(",");
		table=table+"<tr><td nowrap='nowrap' align='center' width='150px' class='GridCellL'>"+itemAndCname[1]+"</td>"+contents[i]+"</tr>";
	}
	table=table+"</table>";
	Ext.getDom("tablecontent").innerHTML=table;
}