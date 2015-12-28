var row=oJson.row;
var col=oJson.col;
var indexid=oJson.indexid;
var comment = oJson.comment; // 单元格上已经存在的批注
var DataSelectIndex = function() {
	var bbIOGrid=new BbIOGrid(indexid);
	bbIOGrid.getStore().reload();
	var bbSjzdGrid=new BbSjzdGrid();
	
	
	var cellCell = new Ext.form.TextField( {
		disabled : false,
		width : 38,
		allowBlank : false,
		fieldLabel : "单元格",
		name : "cell",
		readOnly : true
	});
	
	var fxCell=new Ext.form.ComboBox({
		xtype:'combo',
		store: new Ext.data.SimpleStore({fields: ["retrunValue", "displayText"],data: [['1','纵向'],['2','横向']]}),
		valueField :"retrunValue",
		displayField: "displayText",
		mode: 'local',
		triggerAction: 'all',
		blankText:'请选择类型',
		emptyText:'请选择类型',
		hiddenName:'fx',
		selectOnFocus :true,
		width : 68,
		fieldLabel: '填充方式'
	});
	
	var dnumCell = new Ext.form.TextField( {
		disabled : false,
		allowBlank : false,
		fieldLabel : "行数",
		name : "dnum",
		width : 38,
		readOnly : false,
		value:0
	});
	
	var mergeCell=new Ext.form.Checkbox({
		fieldLabel : "合并单元格",
		name:"merge"
	});

	var simpleForm = new Ext.FormPanel({
		labelAlign : 'right',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		id : 'myForm',
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 70, // 标签宽度
		monitorValid : true, // 绑定验证
		items:[{
	          	layout:'column',
		        border:false,
		        labelWidth:70,
				//标签宽度
		        labelSeparator:'：',
		        defaults:{
		        	blankText:'必填字段'
		        },
		        items:[{
					columnWidth : .2,
					layout : 'form',
					border : false,
					items : [cellCell]
				},{
					columnWidth : .3,
					layout : 'form',
					border : false,
					items : [fxCell]
				},{
					columnWidth : .2,
					layout : 'form',
					border : false,
					items : [dnumCell]
				},{
					columnWidth : .3,
					layout : 'form',
					border : false,
					items : [mergeCell]
				},{
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [bbIOGrid]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [bbSjzdGrid]
				}]
		    }],buttons : [{
				id : 'saveButton',
				formBind : true,
				text : '保存',
				handler : function() {
					save();
				}
			}, {
				text : '取消',
				handler : function() {
					window.close();
				}
			}]
	});
	
	this.getBbioGrid=function(){
		return bbIOGrid;
	}
	
	this.getBbSjzdGrid=function(){
		return bbSjzdGrid;
	}
	

	function save() {
		if(!simpleForm.form.isValid()){
			alert('数据校验失败，请检查填写的数据格式是否正确');
			return;
		}
		var ioSel=bbIOGrid.getSelectionModel().getSelections()[0];
		var sjzdSel=bbSjzdGrid.getSelectionModel().getSelections()[0];
		if(ioSel!=null&&sjzdSel!=null){
			var param={};
			param.bbindexid=indexid;
			param.bbioid=ioSel.json.id;
			param.bbsjzdid=sjzdSel.json.id;
			param.vname=ioSel.json.vname;
			param.fname=sjzdSel.json.fname;
			param.fx=fxCell.getValue();
			param.dnum=dnumCell.getValue();
			param.merge=mergeCell.getValue();
			window.returnValue = param;
			window.close();
		}else{
			alert("请选择数据!");
		}
		
/**
			Ext.Msg.show( {
				title : '操作错误',
				msg : '请先测试sql语句的正确性!',
				buttons : Ext.Msg.OK,
				animEl : 'elId',
				icon : Ext.MessageBox.ERROR
			});*/
	}
	
	
	/**设置单元格默认的值
	**/
	function setCell(){
		var strColunm=numfromCharCode(col);
		cellCell.setValue(strColunm+row);
		fxCell.setValue(1);
		initValueWithComment();
	}
	
	/**根据数字得到字母,用于根据列号得到对应的字母，显示用
	**/
	function numfromCharCode(num){
	    var result="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var star="A";//前部分
		var end="";//后部分
		var flag=0;//控制字母循环
		var flag2=0;//控制在结果里取数
		var bool=true;
		for(var i=0;i<num;i++){
			if(num<=25){
				star=String.fromCharCode(flag+65);
				end=star;
			}else{
				bool=false;
				end=String.fromCharCode(flag+65);
				if(flag==25){
					star=String.fromCharCode(result.charCodeAt(flag2));
					//alert("star:"+star);
					flag2++;
					flag=-1;
				}
				//alert("end:"+end);
			}
			flag++;
		}      
		if(bool){
			return end;
		} else{
			return (star+end);
		}
	}
	
	/**
	 * 根据注解初始化各个控件的初始值 <di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' />
	 */
	function initValueWithComment() {
		if (comment != null && comment.length > 0) {
			try {
				var oXml = getDom(comment).firstChild;
				var fx = decodeChars(getXmlAttribute(oXml, 'fx'), "',<,>,&,\"");
				fxCell.setValue(fx);
				var dnum = decodeChars(getXmlAttribute(oXml, 'dnum'), "',<,>,&,\"");
				dnumCell.setValue(dnum);
				var merge = decodeChars(getXmlAttribute(oXml, 'merge'), "',<,>,&,\"");
				mergeCell.setValue(merge);
				var vname = decodeChars(getXmlAttribute(oXml, 'vname'), "',<,>,&,\"");
				var fname = decodeChars(getXmlAttribute(oXml, 'fname'), "',<,>,&,\"");
				//默认选中
				bbIOGrid.getStore().on("load",function(store,records,options ){
					for(var i=0;i<records.length;i++){
						if(vname==records[i].json.vname){
							bbIOGrid.setDefaultFname(fname);
							bbIOGrid.getSelectionModel().selectRow(i);
						}
					}
			    });
			} catch (e) {
			}
		}
	}
	
	this.loadData=function(){
		setCell();
	}

	DataSelectIndex.superclass.constructor.call(this, {
		width : '100%',
		height : '100%',
		modal : true,
		autoScroll : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		bodyStyle : 'padding:1px;',
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : simpleForm
		}]
	});

}

Ext.extend(DataSelectIndex, Ext.Panel, {});
