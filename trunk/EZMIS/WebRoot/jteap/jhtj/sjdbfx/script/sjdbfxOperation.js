function intiDate(){
	var jzStr="";
	if(isJz=="true"){
		jzStr="机组#JZ#comboBox#"+flflag+"#7#false#.2";
	}
	//日数据
	if(flflag==1){
		searchPanel.searchDefaultFs=("对比期#SNIAN#comboBox#"+flflag+"#1#false#.17,#SYUE#comboBox#"+flflag+"#2#true#.1,当期#ENIAN#comboBox#"+flflag+"#4#false#.17,#EYUE#comboBox#"+flflag+"#5#true#.1"+(jzStr==""?"":","+jzStr)+",折线图#radioSel#radio#.2#1").split(",");
		searchPanel.searchAllFs=("对比期#SNIAN#comboBox#"+flflag+"#1#false#.17,#SYUE#comboBox#"+flflag+"#2#true#.1,当期#ENIAN#comboBox#"+flflag+"#4#false#.17,#EYUE#comboBox#"+flflag+"#5#true#.1"+(jzStr==""?"":","+jzStr)+",折线图#radioSel#radio#.2#1").split(",");		
	}else if(flflag==2){
	//月数据
		searchPanel.searchDefaultFs=("对比期#SNIAN#comboBox#"+flflag+"#1#false#.2,当期#ENIAN#comboBox#"+flflag+"#4#false#.2"+(jzStr==""?"":","+jzStr)+",折线图#radioSel#radio#.2#1").split(",");
		searchPanel.searchAllFs=("对比期#SNIAN#comboBox#"+flflag+"#1#false#.2,当期#ENIAN#comboBox#"+flflag+"#4#false#.2"+(jzStr==""?"":","+jzStr)+",折线图#radioSel#radio#.2#1").split(",");			
	}else if(flflag==3){
	//年数据
		if(jzStr!=""){
			searchPanel.searchDefaultFs=(jzStr+",折线图#radioSel#radio#.2#1").split(",");
			searchPanel.searchAllFs=(jzStr+",折线图#radioSel#radio#.2#1").split(",");	
		}	
	}
    searchPanel.addComponent();
    
    searchPanel.searchClick();
}


function renderChart(chartData,tableData,radioValue){
	var oXML=XMLUtil.load_xml(chartData);
	var path="";
	if(radioValue==1){
		path=contextPath+"/component/chart/Charts/MSLine.swf";
	}else if(radioValue==2){
		path=contextPath+"/component/chart/Charts/MSColumn2D.swf";
	}
	
	var chart = new FusionCharts(path, "chart1Id", document.body.offsetWidth-20, document.body.offsetHeight-300);
	chart.setDataXML(oXML.xml);
	chart.setTransparent(true);
	chart.render("flashcontent");
	
	createTable(tableData);
}


function createTable(tableData){
	var xmlDom=getDom(tableData);
	var datasets=xmlDom.childNodes[0];
	var tableWidth=60;
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
	var table="<table class='FBorder' width='"+tableWidth+"'><tr class='GridCellJ'><td nowrap='nowrap' width='40px' class='GridCellL'>日期</td>"+caption[0]+"</tr>";
	for(var i=0;i<contents.length;i++){
		table=table+"<tr><td nowrap='nowrap' width='60px' class='GridCellL'>"+(i==0?'对比期':'当期')+"</td>"+contents[i]+"</tr>";
	}
	table=table+"</table>";
	Ext.getDom("tablecontent").innerHTML=table;
}