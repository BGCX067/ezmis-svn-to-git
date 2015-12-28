
//开始编辑的列
var startEditCol = 4;

/**
 * 字段列表
 */
RightGrid=function(){
	//记录类别、岗位类别、页大小、值班时间、值班班次
	var url = link1+"?limit=10&zbsj="+dateZbsj.getValue().format("Y-m-d")+"&zbbc="+
				encodeURIComponent(comboZbbc.getValue())+"&jllb="+encodeURIComponent("记事")
				+"&gwlb="+encodeURIComponent(gwlb)+"&zzjlType="+encodeURIComponent(recordType);
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    
    //保存记录
    function beforeSave(){
    if(lastJjbsj != dateZbsj.getValue().format("Y-m-d") || lastJieBanbc != comboZbbc.getValue()){
			alert("只能对当前班次("+lastJjbsj+" "+lastJieBanbc+")进行保存操作");
			return;
		}
    	if(gwlb == "值长"){
    		setUserloginName();
    		flag = 'save';
			var lockForm = new LockFormWindow();
			lockForm.show();
		}else{
			saveRecode();
		}
		
		 
    }
    
	var gridTool = ['-',{text:'导出Excel',handler:function(){
							exportExcel(grid,true);
						}},
				   '-',{text:"<font style='font-weight:bold;color: red;'>保存记录</font>",handler:function(){
				   			beforeSave();}}
				  ];
	if(gwlb == '值长'){
		gridTool = ['-',{text:'导出Excel',handler:function(){
							exportExcel(grid,true);
						}},
				   '-',{text:"<font style='font-weight:bold;color: red;'>保存记录</font>",handler:function(){
				   			beforeSave();}},
				   '-',btnAll,'-',btnYb,'-',btnDd
				  ];
	}else if(gwlb == '零米'){
		gridTool = ['-',{text:'导出Excel',handler:function(){
							exportExcel(grid,true);
						}},
				   '-',{text:"<font style='font-weight:bold;color: red;'>保存记录</font>",handler:function(){
				   			beforeSave();}},
				   '-',btnAllLm,'-',btnJcl,'-',btnCh,'-',btnTl
				  ];
	}
	
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 10,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items: gridTool
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar: mainToolbar, 
		items: this.pageToolbar,
		clicksToEdit:1
	});	
	
	//当有用户被选择的时候，显示工具栏的删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDelRecord=mainToolbar.items.get('btnDelRecord');
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelRecord) btnDelRecord.setDisabled(true);
		}else{
			if(btnDelRecord) btnDelRecord.setDisabled(false);
		}
	});
}

var Plant = Ext.data.Record.create([
   // the "name" below matches the tag name to read, except "availDate"
   // which is mapped to the tag "availability"
   {name: 'jzh', type: 'string'},
   {name: 'jlsj', type: 'date', dateFormat: 'Y-m-d H:i:s'},
   {name: 'jlnr', type: 'string'},
   {name: 'rncolor',type: 'string'},
   {name: 'jlr', type: 'string'},
   {name: 'zzjlType', type: 'string'}
]);

Ext.extend(RightGrid, Ext.grid.EditorGridPanel, {
	
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
				"id","jzh","gwlb","jllb","zbsj","zbbc","zbzb","jlsj","jlnr","jlr","tzgw","zy","zzjlType","nrcolor","time"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
		
		//是否显示机组号, 默认显示
		var isHidJzh = false;
		//是否显示记录类型,默认隐藏
		var isHiddenJllx = true;
		//记录内容width
		var jlnrWidth = 600;
		
		if(gwlb == "值长"){
			isHidJzh = true;
			isHiddenJllx = false;
		}else if(gwlb.indexOf("零米") != -1){
			isHidJzh = true;
			isHiddenJllx = false;
		}else if(gwlb.indexOf("电气") != -1){
			startEditCol = 2;
		}
		
		var storeJz = new Ext.data.SimpleStore({
			fields: ['key','value'],
			data: [
					['#1机组','#1机组'],
					['#2机组','#2机组'],
					['#3机组','#3机组'],
					['#4机组','#4机组']
				]		
		});
		
		var storenrcolor = new Ext.data.SimpleStore({
		     fields: ['key','value'],
		     data: [
		     		['黑色','黑色'],
		     		['红色','红色'],
		     		['蓝色','蓝色'],
		     		['黄色','黄色']
		     ]
		});
		
		var storeJllx = null;
		if("值长" == gwlb){
			storeJllx = new Ext.data.SimpleStore({
				fields: ['key','value'],
				data: [
						['一般记录','一般记录'],
						['调度指令','调度指令']
					]		
			});
		}else if("零米" == gwlb){
			storeJllx = new Ext.data.SimpleStore({
				fields: ['key','value'],
				data: [
						['精处理及炉内','精处理及炉内'],
						['除灰记录','除灰记录'],
						['脱硫记录','脱硫记录']
					]		
			});
		}
	
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "编号", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'jzh',header: "机组号", width: 100, sortable: true, hidden:isHidJzh, dataIndex: 'jzh',
					editor: new Ext.form.ComboBox({
								id: "comboJz",
								valueField: "value",
								displayField: "key",
								mode: 'local',
								triggerAction: 'all',
								blankText: '请选择机组',
								emptyText: '请选择机组',
								width : 100,
								forceSelection: true,
								editable: false,
								allowBlank: true,
								store: storeJz
							})
				},
				{id:'jlsj',header: "记事时间", width: 135, sortable: true, dataIndex: 'jlsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value["time"] != null){
				    		var dt = new Date(value["time"]).format("Y-m-d H:i:s");
							return dt;         
						}else{
							return new Date(value).format("Y-m-d H:i:s");
						}
					},
					editor: new Ext.form.DateField({
							menu: new DatetimeMenu(),
							format: 'Y-m-d H:i:s',
							listeners: {
								focus: function(){
									this.setValue(new Date());
								}
							}
					})
				},
				{id:'jlnr',header: "记事内容", width: jlnrWidth, sortable: true, dataIndex: 'jlnr',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
							if(record.data.nrcolor == "" || record.data.nrcolor == null){
								return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
							}else if(record.data.nrcolor == "黄色"){
								return "<span style='color:#FF8800' ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
							}else if(record.data.nrcolor == "红色"){
								return "<span style='color:red' ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
							}else if(record.data.nrcolor == "蓝色"){
								return "<span style='color:blue' ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
							}else if(record.data.nrcolor == "黑色"){
								return "<span style='color:black' ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
							}
			    	},
					editor: new Ext.form.TextArea({
						allowBlank: false,
						height:'600px'
					})
				},{id:'jlr',header: "记事人", width: 70, sortable: true, dataIndex: 'jlr'},
				{id:'zzjlType',header: "记录类型", width: 75, sortable: true, hidden:isHiddenJllx, dataIndex: 'zzjlType',
					editor: new Ext.form.ComboBox({
								id: "comboJllx",
								valueField: "value",
								displayField: "key",
								mode: 'local',
								triggerAction: 'all',
								blankText: '请选择记录类型',
								emptyText: '请选择记录类型',
								width : 80,
								forceSelection: true,
								editable: false,
								allowBlank: true,
								store: storeJllx
							})
				},
				{id:'nrcolor',header: "内容颜色", width: 60, sortable: true, hidden:false, dataIndex: 'nrcolor',
					editor: new Ext.form.ComboBox({
								id: "comboRncolor",
								store: storenrcolor,
								valueField: "value",
								displayField: "key",
								mode: 'local',
								blankText :'请选择颜色', 
								emptyText :'请选择颜色',
								editable:false,
								forceSelection:true,  
								triggerAction:'all',  
								allowBlank: true,
								anchor:'50%'
							})
				}
				
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link3,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	},
	//添加记录
	addPlant: function(){
		var grid = this;
		var jzh = "";
		var lastjiebanren = "";
		
		if(gwlb == "1"){
			jzh = "#1机组";
		}else if(gwlb == "2"){
			jzh = "#2机组";		
		}else if(gwlb == "3"){
			jzh = "#3机组";		
		}else if(gwlb == "4"){
			jzh = "#4机组";		
		}else if(gwlb.indexOf("控") != -1){
			jzh = "#1机组";
		}
		Ext.Ajax.request({
            url:link12,
            params: {gwlb:gwlb},
            method:'post',
            success:function(ajax){
            	var responseObj = Ext.util.JSON.decode(ajax.responseText);
           		if(responseObj.success == true){
           		lastjiebanren=responseObj.username2;
           		var p = new Plant({
		            jzh: jzh,
		            jlsj: new Date(),
		            jlnr: '',
		            nrcolor: nrcolorType,
		            jlr: lastjiebanren,
		            zzjlType: recordType
	           });
		       grid.stopEditing();
		       grid.getStore().insert(0, p);
		       grid.startEditing(0, startEditCol);
           		}
           	},
           	failure:function(){
           		alert('服务器忙，请稍候操作...');
           	}
       	});

	}
	
});
