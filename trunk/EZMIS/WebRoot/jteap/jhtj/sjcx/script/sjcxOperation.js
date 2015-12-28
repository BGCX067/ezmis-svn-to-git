function intiDate(){
	var jzStr="";
	if(isJz=="true"){
		jzStr="机组#JZ#comboBox#"+flflag+"#7#false#.18";
	}
	//日数据
	if(flflag==1){
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.08,#SRI#comboBox#"+flflag+"#3#true#.15,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.08,#ERI#comboBox#"+flflag+"#6#true#.15"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.08,#SRI#comboBox#"+flflag+"#3#true#.15,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.08,#ERI#comboBox#"+flflag+"#6#true#.15"+(jzStr==""?"":","+jzStr)).split(",");
	}else if(flflag==2){
	//月数据
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.15,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.15"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.18,#SYUE#comboBox#"+flflag+"#2#true#.15,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.18,#EYUE#comboBox#"+flflag+"#5#true#.15"+(jzStr==""?"":","+jzStr)).split(",");		
	}else if(flflag==3){
	//年数据
		searchPanel.searchDefaultFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.3,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.3"+(jzStr==""?"":","+jzStr)).split(",");
		searchPanel.searchAllFs=("起始日期#SNIAN#comboBox#"+flflag+"#1#false#.3,终止日期#ENIAN#comboBox#"+flflag+"#4#false#.3"+(jzStr==""?"":","+jzStr)).split(",");			
	}
    searchPanel.addComponent();
    
    var findList=findSearchPanel();
    
    var cm=buildCm(fields,keys);
	var ds=buildDs(fields,keys,findList);
	sjcxGrid.updateData(cm,ds);
}


function findSearchPanel(){
	var oPanel = searchPanel.items.get(0);
	var oItems = oPanel.items.items;
	var param="";
	Ext.each(oItems, function(oItem) {
		if (oItem.hidden == false) {
			var temp = oItem.items.items[0];
			var tempValue = temp.getValue();
			// 值不为空才作为参数
			if (tempValue != null && tempValue != "") {
				var name=temp.getName();
				if(name.indexOf("#")>=0){
					name=name.split("#")[1];
					param=param+name+","+tempValue+"!";
				}
			}
		};
	});
	if(param!=""){
		param=param.substring(0,param.length-1);
	}
	return param;
}


//构建列模型对象
function buildCm(cols,keys){
	var tmpArray=new Array();
	var keyColumns=keys.split("!");
	//加入关键字字段
	for(var i=0;i<keyColumns.length;i++){
		var datas=keyColumns[i].split(",");
		var tmpCm={};
		//条件字段
		tmpCm={"id":datas[0],"header":datas[1],"width":60,"sortable":true,"dataIndex":datas[0]};
		tmpArray.push(tmpCm);
	}
	//tmpArray.push(new Ext.grid.CheckboxSelectionModel());
	//选择的普通字段
	var columns=cols.split("!");
	for(var i=0;i<columns.length;i++){
		var datas=columns[i].split(",");
		var tmpCm={};
		//条件字段
		tmpCm={"id":datas[0],"header":datas[1],"width":60,"sortable":true,"dataIndex":datas[0]};
		tmpArray.push(tmpCm);
	}
	cm=new Ext.grid.ColumnModel(tmpArray);
	return cm;
}
//构建数据源对象
function buildDs(cols,keys,findList){
	var colArray=new Array();
	var keyColumns=keys.split("!");
	//加入关键字字段
	for(var i=0;i<keyColumns.length;i++){
		var datas=keyColumns[i].split(",");
		var columncode=datas[0];
		colArray.push(columncode);
	}
	
	var columns=cols.split("!");
	for(var i=0;i<columns.length;i++){
		var datas=columns[i].split(",");
		var columncode=datas[0];
		colArray.push(columncode);
	}
	
	var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: link8+"?kid="+kid+"&findList="+findList+"&flflag="+flflag
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        }, colArray),
        remoteSort: true	
    });
	return ds;
}