
var dict_dqgzgl=$dictList("dqgzgl");
var dict_dqgzzy=$dictList("dqgzzy");

/**
 * 数据源-工作规律
 */
var storeGzgl =  new Ext.data.Store({
	data: {rows:dict_dqgzgl},
	reader: new Ext.data.JsonReader( {
		root: 'rows',
		id: 'id'
	}, ['key', 'value', 'id'])
});

/**
 * 数据源-定期工作专业
 */
var storeDqgzzy =  new Ext.data.Store({
	data: {rows:dict_dqgzzy},
	reader: new Ext.data.JsonReader( {
		root: 'rows',
		id: 'id'
	}, ['key', 'value', 'id'])
});

/**
 * 负责部门
 */
var comboFzbm = new Ext.app.ComboTree( {
	id : 'comboFzbm',
	name : 'comboFzbm',
	renderTo: 'divFzbm',
	dataUrl : link7,
	listWidth:150,
	blankText:'请选择部门',
	emptyText:'请选择部门',
	triggerClass:'comboTree',
	anchor : '90%',
	autoScroll:true,
	localData : false,
	rootVisible : false,
	allowBlank: false,
	listeners : {
		select : function(t, node) {
			this.hiddenName = node.id;
		}
	}
});

/**
 * 负责岗位
 */
var comboFzgw = new Ext.app.ComboTree( {
	id : 'comboFzgw',
	name : 'comboFzgw',
	renderTo: 'divFzgw',
	dataUrl : link8,
	listWidth:150,
	blankText:'请选择岗位',
	emptyText:'请选择岗位',
	triggerClass:'comboTree',
	anchor : '90%',
	autoScroll:true,
	localData : false,
	rootVisible : false,
	allowBlank: false,
	listeners : {
		select : function(t, node) {
			this.hiddenName = node.id;
		}
	}
});

/**
 * 工作规律
 */
var comboGzgl = new Ext.form.ComboBox({
	id: 'comboGzgl',
    renderTo: 'divGzgl',
    store: storeGzgl,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择规律',
	emptyText: '请选择规律',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});

/**
 * 定期工作专业
 */
var comboDqgzzy = new Ext.form.ComboBox({
	id: 'comboDqgzzy',
    renderTo: 'divDqgzzy',
    store: storeDqgzzy,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择专业',
	emptyText: '请选择专业',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});

/**
 * 实验及切换项目
 */
var txtDqgznr = new Ext.form.TextArea({
	id: 'textDqgznr',
	renderTo: 'divDqgznr',
	maxLength: 800,
	width: 410,
	maxLengthText: '最长50个字符',
	blankText: '请输入实验及切换项目',
	allowBlank: false
});

var id = (window.dialogArguments).id;
var catalogId = (window.dialogArguments).catalogId;
if(id != null){
	Ext.Ajax.request({
		url: link9,
		method: 'post',
		params: {id:id},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				var data = responseObj.data[0];
				//班次
				var bc = data.bc.split(",");
				for(var i=0; i<bc.length; i++){
					if(bc[i] == "夜班"){
						document.getElementById("ckYeB").checked = true;
					}
					if(bc[i] == "白班"){
						document.getElementById("ckBaiB").checked = true; 
					}
					if(bc[i] == "中班"){
						document.getElementById("ckZhongB").checked = true; 
					}
				}
				//实验及切换项目
				txtDqgznr.setValue(data.dqgzNr);
				//定期工作专业
				comboDqgzzy.setValue(data.dqgzzy);
				//负责部门
				comboFzbm.setValue('comboFzbm',data.fzbm);
				//负责岗位
				comboFzgw.setValue('comboFzgw',data.fzgw);
				//工作规律
				comboGzgl.setValue(data.gzgl);
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}

/**
 * 保存
 */
function save(jx){
	/** 数据验证 */
	//负责部门
	if(!comboFzbm.validate()){
		alert("请选择负责部门");
		comboFzbm.focus();
		return;
	}
	//负责岗位
	if(!comboFzgw.validate()){
		alert("请选择负责岗位");
		comboFzgw.focus();
		return;
	}
	//工作规律
	if(!comboGzgl.validate()){
		alert("请选择工作规律");
		comboGzgl.focus();
		return;
	}
	//定期工作专业
	if(!comboDqgzzy.validate()){
		alert("请选择定期工作专业");
		comboDqgzzy.focus();
		return;
	}
	
	//班次
	var banCi = "";
	if(ckYeB.checked){
		banCi += "夜班,";
	}
	if(ckBaiB.checked){
		banCi += "白班,";
	}
	if(ckZhongB.checked){
		banCi += "中班";
	}
	if(banCi == ""){
		alert("请选择班次");
		return;
	}
	
	//定期工作内容
	if(!txtDqgznr.validate()){
		alert("请输入实验及切换项目");
		txtDqgznr.focus(true);
		return;
	}
	
	/** 保存定期工作设置 */	
	Ext.Ajax.request({
		url: link6,
		method: 'post',
		params: {id:id, dqgzCatalogId:catalogId, fzbm:comboFzbm.value, gzgl:comboGzgl.getValue(),
				dqgzzy:comboDqgzzy.getValue(), bc:banCi, dqgzNr:txtDqgznr.getValue(),
				fzgw:comboFzgw.value},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				if(window.dialogArguments.grid != null){
					window.dialogArguments.grid.getStore().reload();
				}
				if(jx == null){
					window.close();
				}else{
					window.location = "setting.jsp";
				}
			}else{
				alert('保存失败,请联系管理员...');
				window.close();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}
