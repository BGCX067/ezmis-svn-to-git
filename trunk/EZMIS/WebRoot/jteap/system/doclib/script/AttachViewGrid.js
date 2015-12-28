/**
  * **************
  * 文档附件列表
  * **************
  **/
AttachViewGrid = function(resourceId) {
	attachGrid = this
	//被修改了得记录
	this.dirtyRecords=[];
	//默认DS
	foDefaultDS = this.getDefaultDS(link13+"?id="+resourceId);
	
	var fm = Ext.form;		
	var cm = new Ext.grid.ColumnModel([{
			header: "图标",
			dataIndex: 'type',
			width: 24,
			renderer:function(value){
				if(value == "docx" || value == "doc"){
					return "<img align='absmiddle' src='./icon/word.gif' />";
				}else if(value == "xlsx" || value == "xls"){
					return "<img align='absmiddle'  src='./icon/excel.gif' />";
				}else if(value == "gif" || value == "jpg" || value=="bmp"){
					return "<img align='absmiddle'  src='./icon/icon_14.gif' />";
				}else if(value == "pdf"){
					return "<img align='absmiddle'  src='./icon/icon_17.gif' />";
				}else if(value == "zip" || value=="rar"){
					return "<img align='absmiddle'  src='./icon/icon_15.gif' />";
				}else if(value == "txt"){
					return "<img align='absmiddle'  src='./icon/icon_16.gif' />";
				}else{
					return "<img align='absmiddle'  src='./icon/other.gif' />";		
				}
			},
			editor: new fm.TextField({
				allowBlank: false
			})
        },{
           header: "附件名称",
           dataIndex: 'name',
           width: 300,
           editor: new fm.TextField({
               allowBlank: false
           })
        },{
           header: "附件大小",
           dataIndex: 'doclibSize',
           width: 80,
           editor: new fm.TextField({
               allowBlank: false
           })
        },{
           header: "附件操作",
           dataIndex: 'type',
           width: 80,
          renderer:function(value){
				if(value == "docx" || value == "doc"){
					return "<span style='color:red;font-weight:bold;'><a href='#' onclick ='downloadAttach();'>下载</a>&nbsp;&nbsp;<a href='#' onclick ='openAttach();'>编辑</a></span>";
				}else if(value == "xlsx" || value == "xls"){
					return "<span style='color:green;font-weight:bold;'><a href='#' onclick ='downloadAttach();'>下载</a>&nbsp;&nbsp;<a href='#' onclick ='openAttach();'>编辑</a></span>";
				}else{
					return "<span style='color:blue;font-weight:bold;'><a href='#' onclick ='downloadAttach();'>下载</a></span>";
				}
			},
           editor: new fm.TextField({ 	  
               allowBlank: false
           })
        }
    ]);
	//加载添加附件和删除附件的按钮
	var load = function () {
	}
	
	AttachViewGrid.superclass.constructor.call(this,{
		loadMask : true ,
		cm : cm ,
		store : foDefaultDS,
		autoWidth:true,
		height:180,
		width:542,
        margins:'2px 2px 2px 2px',
        frame:true,
        tbar:["<span style='margin-top:5px;margin-left:5px;color: #114581;font:bolder 9pt 仿宋;line-height: 130%;'>附件列表</span>"],
        selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),  //设置单行选中模式
        clicksToEdit:2
	})
	foDefaultDS.load();
	//为选择模型添加事件，操作列上不需要选择操作
	var sm=this.getSelectionModel();
    var ds=this.getStore();
    this.on("beforeedit",function(e){
    	//if(e.column==1)
		//	return false;
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
	
//下载文档附件
function downloadAttach (){

	var select=attachGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var link=contextPath + "/jteap/doclib/DoclibAttachAction!downloadAttach.do?id="+id;
	downloadFrame = document.createElement("iframe"); //通过Iframe 的src  属性调用下载文件的action方法无刷新的下载文件
	downloadFrame.id = "downloadFrame" ;
	downloadFrame.src = link ;
	downloadFrame.style.display = "none";
	document.body.appendChild(downloadFrame);	 
}

function openAttach (){

	var select=attachGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var url=contextPath + "/jteap/system/doclib/excelForm.jsp?attachId="+id ;
	/*
	downloadFrame = document.createElement("iframe"); //通过Iframe 的src  属性调用下载文件的action方法无刷新的下载文件
	downloadFrame.id = "downloadFrame" ;
	downloadFrame.src = link ;
	downloadFrame.style.display = "none";
	//document.body.appendChild(downloadFrame);	 
	window.open(link);
	*/
	var result=showModule(url,true,800,600);
	
}

	

Ext.extend(AttachViewGrid , Ext.grid.EditorGridPanel , {
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



