/**
  * **************
  * 文档附件列表
  * **************
  **/
AttachGrid = function(resourceId) {
	attachGrid = this
	//被修改了得记录
	this.dirtyRecords=[];
	//默认DS
	foDefaultDS = this.getDefaultDS(link13+"?id="+resourceId);
	
	var fm = Ext.form;		
	var cm = new Ext.grid.ColumnModel([{
			header: "图标",
			dataIndex: 'icon',
			width: 50,
			renderer:function(value){
				if(value == "docx" || value == "doc"){
					return "<span style='color:red;font-weight:bold;'>文档</span>";
					//return "<span style='color:red;font-weight:bold;'>文档</span><img src='./icon/word.gif' />";
				}else if(value == "xlsx" || value == "xls"){
					return "<span style='color:green;font-weight:bold;'>表格</span>";
					//return "<span style='color:green;font-weight:bold;'>表格</span><img src='./icon/excel.gif' />";
				}else{
					return "<span style='color:blue;font-weight:bold;'>其他</span>";
					//return "<span style='color:blue;font-weight:bold;'>其他</span><img src='./icon/other.gif' />";
				}
			},
			editor: new fm.TextField({
				allowBlank: false
			})
        },{
           header: "附件名称",
           dataIndex: 'name',
           width: 225,
           editor: new fm.TextField({
               allowBlank: false
           })
        },{
           header: "附件大小",
           dataIndex: 'doclibSize',
           width: 225,
           editor: new fm.TextField({
               allowBlank: false
           })
        }
    ]);
	//加载添加附件和删除附件的按钮
	var load = function () {

		 var uploadButton = new Ext.ux.UploadDialog.TBBrowseButton({
		      input_name: 'attachFile',
		      text: '添加附件',
		     //tooltip: ;,
		      iconCls: 'ext-ux-uploaddialog-addbtn',
		      handler: function onAddButtonFileSelected(){
		      			fileSelectorChanged(uploadButton);
		      			},
		      scope: this
	   	    }); 
	   	  var btn = new Ext.Toolbar.Button({text:'删除附件',handler : attachGrid.deteleOperate});
	   	 attachGrid.getTopToolbar().addItem(uploadButton);  
	   	 attachGrid.getTopToolbar().addItem(btn);     	 
	}

	AttachGrid.superclass.constructor.call(this,{
		
		//tbar : ['->',{text:'删除附件',handler : this.deteleOperate}],
		tbar : new Ext.Toolbar(),
		loadMask : true ,
		cm : cm ,
		store : foDefaultDS,
		width:535,
        height:190,
        frame:true,
        bbar:  new Ext.Toolbar(),
        selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),  //设置单行选中模式
        clicksToEdit:2
	})
	foDefaultDS.load();
	//为选择模型添加事件，操作列上不需要选择操作
	var sm=this.getSelectionModel();
    var ds=this.getStore();
    this.on("beforeedit",function(e){
    	if(e.column==1)
			return false;
    });
	this.on("afteredit",function(e){
		if(e.value!=e.originalValue){
			this.dirtyRecords.push(e.record.data);
		}
	});
	 //EditGridPanel渲染完成后调用load()方法加载"添加附加" 和"删除附件" 按钮
	this.on("render",function(){
		load();
	});
	
   	 
   	 
}


Ext.extend(AttachGrid , Ext.grid.EditorGridPanel , {
	//data : {'totalCount':Ext.decode(getLabelValue('flowVariable')).length,'list':Ext.decode(getLabelValue('flowOperate'))},
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
	            root: 'fdlist',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            "id","name","type","content","doclibSize"
	        ]),
	        remoteSort: true	
	    });
		return ds;
	},
	
	
	//添加字段
	createOperate : function() {	

		//$(attachFile.getId()).attachEvent("onchange",function(){fileSelectorChanged(attachFile.getValue());});
   		//$(attachFile.getId()).click();
    	/*var button = new Ext.ux.UploadDialog.TBBrowseButton({
	      input_name: 'attachFile',
	      text: '添加附件',
	     //tooltip: ;,
	      iconCls: 'ext-ux-uploaddialog-addbtn',
	      handler: function onAddButtonFileSelected(){
	      			fileSelectorChanged(button.getInputFile().dom.value,button);
	      			dialog.onAddButtonFileSelected(button);
	      			},
	      scope: this
   	    });
    */    	
	} ,
		
	//删除字段
	deteleOperate : function() {
	    var store = this.store;
	    var records = attachGrid.selModel.getSelections();
	    var recordsLen = records.length;                     //得到行数组的长度
	    var ids = "";
	    var id = "";
		if(attachGrid.getSelectionModel().getSelected() == null) {
			Ext.Msg.alert("提示","请先选择要删除的行!");
			return ;
		}
		Ext.MessageBox.confirm('系统提示信息', '确定要删除所选的记录吗?', function(buttonobj){
			if(buttonobj == 'yes'){
				//判断扩展字段是否拥有id，没有就直接在前台删除
				var bFlag=false;
				
				for(var i = 0; i < recordsLen; i++){
					if(records[i].get("id") == "" || records[i].get("id") == null){
						attachGrid.store.commitChanges() ;
						attachGrid.store.remove(attachGrid.getSelectionModel().getSelected()) ;
					}else{
						bFlag=true;
						var id = records[i].get("id");
						if(id == null || id == ""){
							//ids += id ;
						}else{
							if(i != 0){
								ids += ","+id;
							}else{
								ids += id ;
							}
						}
					}
				}
				if(bFlag){
					//如果有id，就直接在数据库删除
					var link=contextPath + "/jteap/doclib/DoclibAttachAction!removeAction.do";
					Ext.Ajax.request({
						url:link,
						method:'post',
						params: {ids:ids},
						success:function(ajax){
							var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
					 			alert("删除成功!");
					 			attachGrid.getStore().reload();
					 		}else{
					 			alert(responseObject.msg);
					 		}
						},
						failure:function(){
							alert("提交失败!");
						}
					});
				}
			}else{
				Ext.Msg.alert("提示","请先选择要删除的行!");
	    	}
		});
	} 
})



