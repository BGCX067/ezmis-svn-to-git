
//表名称
var txtTableName= new Ext.form.TextField({
	id: 'txtTableName',
	renderTo: 'divTableName',
	maxLength: 50,
	width: 200,
	maxLengthText: '最长50个字符',
	blankText: '请输入表名称',
	allowBlank: false
});

//所属分类
var txtLogCatalogName= new Ext.form.TextField({
	id: 'txtLogCatalogName',
	renderTo: 'divLogCatalogName',
	maxLength: 50,
	width: 200,
	maxLengthText: '最长50个字符',
	blankText: '请输入所属分类',
	readOnly: true,
	allowBlank: false
});

//采样点
var txtCaiyangdian= new Ext.form.TextField({
	id: 'txtCaiyangdian',
	renderTo: 'divCaiyangdian',
	maxLength: 64,
	width: 200,
	maxLengthText: '最长64个字符',
	blankText: '请输入采样点',
	allowBlank: false
});

//排序号 
var txtSortno= new Ext.form.NumberField({
	id: 'txtSortno',
	renderTo: 'divSortno',
	value: 0,
	minValue: 0,
	width: 200,
	blankText: '请输入排序号',
	allowBlank: false
});

//表编号
var txtTableCode= new Ext.form.TextField({
	id: 'txtTableCode',
	renderTo: 'divTableCode',
	maxLength: 30,
	width: 200,
	maxLengthText: '最长30个字符',
	blankText: '请输入表编号',
	readOnly: true,
	allowBlank: false
});

//说明
var txtRemark= new Ext.form.TextField({
	id: 'txtRemark',
	renderTo: 'divRemark',
	maxLength: 100,
	width: 200,
	maxLengthText: '最长100个字符',
	blankText: '请输入说明'
});

//ID
var id = window.dialogArguments.id;    	
//所属分类ID
var logCatalogId = window.dialogArguments.logCatalogId;
//所属分类名称
txtLogCatalogName.setValue(window.dialogArguments.logCatalogName);
//表编号
if(window.dialogArguments.tableCode == null){
	txtTableCode.setValue(uuid);
}else{
	txtTableCode.setValue(window.dialogArguments.tableCode);
}

//修改时赋值
if(id != null){
	//表中文名
	txtTableName.setValue(window.dialogArguments.tableName);
	//采样点
	txtCaiyangdian.setValue(window.dialogArguments.caiyangdian);
	//排序号
	txtSortno.setValue(window.dialogArguments.sortno);
	//说明
	txtRemark.setValue(window.dialogArguments.remark);
}

var tableId = window.dialogArguments.id;

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="指标名称#columnName#textField,SIS代码#sisCedianbianma#textField,计量单位#jiliangdanwei#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="指标名称#columnName#textField,SIS代码#sisCedianbianma#textField,计量单位#jiliangdanwei#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
searchPanel.width = 822;

var mainTool = new Ext.Toolbar({
	height:26,
	items:["<font style='font-size:14px;'>行数据列表</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
			{id:'btnAdd',text:'添加行',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',
				listeners: {
					click: function(){
						if(id != null){
							var obj = {};
							obj.tableId = id;
							obj.grid = columnInfoGrid;
							var url = contextPath + '/jteap/yx/runlog/set/columnInfoSet.jsp';
							showIFModule(url,"添加行","true",600,220,obj);
						}else{
							if(window.confirm("创建行需先创建表,是否创建表?")){						
								save(false);
							}
						}
					}
				} 
			},
			{id:'btnModi',text:'修改行',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',disabled:true,
				listeners: {
					click: function(){
						var select = columnInfoGrid.getSelectionModel().getSelections()[0];
						var obj = {};
						obj.id = select.get("id");
						obj.tableId = select.data.tableId;
						obj.columnCode = select.data.columnCode;
						obj.columnName = select.data.columnName;
						obj.edingzhi = select.data.edingzhi;
						obj.jiliangdanwei = select.data.jiliangdanwei;
						obj.sisCedianbianma = select.data.sisCedianbianma;
						obj.sortno = select.data.sortno;
						obj.dataTableCode = select.data.dataTableCode;
						obj.jizu = select.data.jizu;
						obj.grid = columnInfoGrid;
						var url = contextPath + '/jteap/yx/runlog/set/columnInfoSet.jsp?modi=m';
						showIFModule(url,"修改行","true",600,220,obj);
					}
				}
			},
			{id:'btnDel',text:'删除行',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',disabled:true,
				listeners: {
					click: function(){
						var select = columnInfoGrid.getSelectionModel().getSelections();
    					columnInfoGrid.deleteSelect(select);
					}
				} 
			}]
});

//grid
var columnInfoGrid=new ColumnInfoGrid();
columnInfoGrid.getStore().reload();

//默认选中第一个输入项
txtTableName.focus();

/**
 * 保存
 */
var save = function(isClose){
	/** 数据验证 */
	//表名称
	if(!txtTableName.validate()){
		alert('请输入正确的表名称');
		txtTableName.focus(true);
		return;
	}
	//采样点
	if(!txtCaiyangdian.validate()){
		alert("请输入正确的采样点");
		txtCaiyangdian.focus(true);
		return;
	}else{
		var tempCai = txtCaiyangdian.getValue().trim();
		if(isNaN(tempCai)){
			tempCai = tempCai.split(",");
			for(var i=0; i<tempCai.length; i++){
				if(tempCai[i].trim() == "" || isNaN(tempCai[i]) || parseInt(tempCai[i]) == 'NaN'){
					alert("采样点 必须为以逗号分隔开的时间点, 如: 0,1,2,3,4,5");
					txtCaiyangdian.focus(true);
					return;
				}else if(parseInt(tempCai[i]) < 0 || parseInt(tempCai[i]) > 23){
					alert("采样点 必须在0~23之间");
					txtCaiyangdian.focus(true);
					return;
				}else if(tempCai[i].indexOf(".") != -1){
					alert("采样点 必须为正整数");
					txtCaiyangdian.focus(true);
					return;
				}
			}
			
			//判断采样点是否为 升序
			for(var i=0; i<tempCai.length; i++){
				if(parseInt(tempCai[i]) > parseInt(tempCai[i+1])){
					alert("采样点 必须为升序的时间点, 如: 0,1,2,3,4,5");
					txtCaiyangdian.focus(true);
					return;
				}
			}
		}else if(parseInt(tempCai) < 0 || parseInt(tempCai) > 23){
			alert("采样点 必须在0~23之间");
			txtCaiyangdian.focus(true);
			return;
		}else if(tempCai.indexOf(".") != -1){
			alert("采样点 必须为正整数");
			txtCaiyangdian.focus(true);
			return;
		}
		
	}
	//排序号
	if(!txtSortno.validate()){
		alert("请输入排序号");
		return;
	}else{
		if(txtSortno.getValue().toString().indexOf(".") != -1){
			alert("排序号必须是正整数");
			txtSortno.focus(true);
			return;
		}
	}
	
	/** 保存 */
	Ext.Ajax.request({
		url: link6,
		method: 'post',
		params: {id:id, tableName:txtTableName.getValue().trim(), 
				caiyangdian:txtCaiyangdian.getValue().trim(), sortno:txtSortno.getValue().toString().trim(),
				tableCode:txtTableCode.getValue(), remark:txtRemark.getValue().trim(),
				logCatalogName:txtLogCatalogName.getValue().trim(), logCatalogId: logCatalogId},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				if(isClose == true){
					/** 根据表编号、采样点创建对应的记录物理表 */
					Ext.Ajax.request({
						url: link12,
						method: 'post',
						params: {tableCode: txtTableCode.getValue().trim(), 
								caiyangdian: txtCaiyangdian.getValue().trim()},
						success: function(ajax){
							eval("responseObject="+ajax.responseText);
							if(responseObject.success == true){
								alert('保存成功');
								window.close();
							}else{
								alert('保存失败');
							}
						},
						failure: function(){
							alert('服务器忙,请稍后再试...');
						}
					})
				}else{
					id = responseObj.id;
					var obj = {};
					obj.tableId = id;
					var url = contextPath + '/jteap/yx/runlog/set/columnInfoSet.jsp';
					showIFModule(url,"添加行","true",600,220,obj);
					var url = link8+"?limit=20&tableId="+id;
					columnInfoGrid.changeToListDS(url);
					columnInfoGrid.getStore().reload();
				}
			}else{
				alert('保存失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
	
}
