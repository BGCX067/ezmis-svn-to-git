/*************************************
*初始化脚本将会在表单初始化完成后执行
*************************************/
/*************************************
*初始化脚本将会在表单初始化完成后执行
*************************************/
var tableName="TB_FORM_SJBZB_FXB";
var zrbm=["QJ","GL","DQ","RG","YZ","EZ","SZ","FZ","WZ","RY","SJ","QT"];
var jzbh=[1,2,3,4];
var xz=["CS","SJ","DLSS"];

var docid= window.parent.document.getElementById("docid").value;
var formSn = window.parent.document.getElementById("formSn").value;

var url = contextPath+"/jteap/yx/aqyxfx/FxbAction!showFjhJclDataAction.do?";
     url += "tableName=" + tableName;
     var conn = Ext.lib.Ajax.getConnectionObject().conn; 
     conn.open("post",url ,false); 
     conn.send(null); 
     var responseObject = Ext.util.JSON.decode(conn.responseText);
     if(responseObject.success == true){
           var data=responseObject.data[0];
           for(var i=0;i<data.length;i++){
                 var curObj=data[i];
                $(zrbm[i]+"_1_CS").value=curObj._1_cs;
				$(zrbm[i]+"_1_SJ").value=curObj._1_sj;
				$(zrbm[i]+"_1_DLSS").value=curObj._1_jcl;
				$(zrbm[i]+"_2_CS").value=curObj._2_cs;
				$(zrbm[i]+"_2_SJ").value=curObj._2_sj;
				$(zrbm[i]+"_2_DLSS").value=curObj._2_jcl;
				$(zrbm[i]+"_3_CS").value=curObj._3_cs;
				$(zrbm[i]+"_3_SJ").value=curObj._3_sj;
				$(zrbm[i]+"_3_DLSS").value=curObj._3_jcl;
				$(zrbm[i]+"_4_CS").value=curObj._4_cs;
				$(zrbm[i]+"_4_SJ").value=curObj._4_sj;
				$(zrbm[i]+"_4_DLSS").value=curObj._4_jcl;           
         }

     }
//计算合计
for(var j=0;j<4;j++){
	for(var t=0;t<3;t++){
		var temp1=0;
		var temp2=0.0;
		var temp3=0.0;
		//检修合计
		for(var i=0;i<4;i++){
			temp1 += parseFloat($(zrbm[i]+"_"+jzbh[j]+"_"+xz[t]).value);
		}
		$("JXHJ"+"_"+jzbh[j]+"_"+xz[t]).value=temp1;
		//发电部合计
		for(var i=4;i<9;i++){
			temp2 += parseFloat($(zrbm[i]+"_"+jzbh[j]+"_"+xz[t]).value);
		}
		$("FDHJ"+"_"+jzbh[j]+"_"+xz[t]).value=temp2;
		//全厂合计
		for(var i=0;i<zrbm.length;i++){
		   temp3 += parseFloat($(zrbm[i]+"_"+jzbh[j]+"_"+xz[t]).value);
		}
		$("QC"+"_"+jzbh[j]+"_"+xz[t]).value=temp3;
	}
}
//全厂总合计
var tempcs=0;
var tempsj=0.0;
var tempjcl=0.0;
for(var i=1;i<=4;i++){
   tempcs += parseInt($("QC"+"_"+i+"_CS").value);
   tempsj += parseFloat($("QC"+"_"+i+"_SJ").value);
   tempjcl += parseFloat($("QC"+"_"+i+"_DLSS").value);
}
$("QCHJ_CS").value=tempcs;
$("QCHJ_SJ").value=tempsj;
$("QCHJ_DLSS").value=tempjcl;
//百分占比


zrbm=["QJ","GL","DQ","RG","JXHJ","YZ","EZ","SZ","FZ","WZ","FDHJ","RY","SJ","QT"];

for(var i=0;i<zrbm.length;i++){
    var temp1=0.0;
	for(var j=0;j<jzbh.length;j++){
		temp1 += parseInt($(zrbm[i]+"_"+jzbh[j]+"_CS").value);
	}
	$(zrbm[i]+"_CS_PERCENT").value=((temp1/tempcs)*100).toFixed(2);
}
for(var i=0;i<zrbm.length;i++){
    var temp2=0.0;
	for(var j=0;j<jzbh.length;j++){
		temp2 += parseFloat($(zrbm[i]+"_"+jzbh[j]+"_DLSS").value);
	}
	$(zrbm[i]+"_DLSS_PERCENT").value=((temp2/tempjcl)*100).toFixed(2);
}


[
/*******第1列*******/
{header:'序号',dataIndex:'XH',width:50,hidden:'false',sortable:false},

{header:'物资名称',dataIndex:'WZBM',width:120,hidden:'true',sortable:false},

/*******第2列*******/
{header:'物资名称',dataIndex:'WZMC',width:120,hidden:'false',sortable:true,editor:{id:'mytriggerfield',xtype:'ext_triggerfield',readOnly:true,onTriggerClick:function(){
		   var triger = this;

          var field = this;

		  var SQBMMC = getEditor('SQBMMC').getValue();
		  var GCLB = getEditor('GCLB').getValue();
		  var GCXM = getEditor('GCXM').getValue();
          var url = contextPath+"/jteap/wz/wzlysq/selectxqjhmxIndex.jsp";
          var CCGY = getEditor('CGY');
          var paramStore= getEditor('WZLYSQMX').grid.getStore();
          new $FW({url:url,height:500,width:800,type:'T2',userIF:true,baseParam:{tmpStore:paramStore,SQBMMC:SQBMMC,GCLB:GCLB,GCXM:GCXM},callback:function(retValue){	//回调函数

			var setValues = function(r,obj){
				r.set('SQSL',obj.PZSL?obj.PZSL:0);
				r.set('WZBM',obj.WZBM?obj.WZBM:obj.id);
				r.set('JLDW',obj.JLDW?obj.JLDW:obj.jldw);
				r.set('JG',obj.JHDJ?obj.JHDJ:obj.jhdj);
				r.set('XQJHMXBM',obj.XQJHMXID?obj.XQJHMXID:obj.xqjhmxid);
				r.set('DQKC',obj.DQKC!=null?obj.DQKC:obj.dqkc);
				r.set('YFPSL',obj.YFPSL!=null?obj.YFPSL:obj.yfpsl);
				r.set('WZMC',obj.WZMC?obj.WZMC:obj.wzmc);
				r.set('LYSL',0);

			};
		var result = retValue;
		returnStore = retValue;
		if (result != null) {
			var cgmx = getEditor('WZLYSQMX').grid;
			var records = cgmx.getStore().getRange();
			var len = result.length;
			for(n=0;n<len;n++){
				if(records.length>n && len>1){
					var done = false;
					for(k=0;k<records.length;k++){
							if(records[k].get('WZBM')==''){
							//records[k].set('WZBM',result[n].json.wzdagl.id);
							//records[k].set('WZMC',result[n].json.wzdagl.wzmc);
							setValues(records[k],result[n].json);
							//records[k].set('YSSL',result[n].data.dqsl);
							done = true;
							break;
						}
					}
					if(!done){
						cgmx.editor.createNew();		                                                
						//cgmx.getStore().getAt(0).set('WZBM',result[n].json.wzdagl.id);
						//cgmx.getStore().getAt(0).set('WZMC',result[n].json.wzdaglwzmc);
						setValues(cgmx.getStore().getAt(0),result[n].json);
						//cgmx.getStore().getAt(0).set('YSSL',result[n].data.dqsl);
					}
				}else if(len==1){
					//triger.displayValue=result[0].json.wzdagl.wzmc;
					//triger.setValue(result[0].json.wzdagl.wzmc);
					//cgmx.getSelectionModel().getSelected().set('WZBM',result[0].json.wzdagl.id);
					setValues(cgmx.getSelectionModel().getSelected(),result[0].json);
					//cgmx.getSelectionModel().getSelected().set('YSSL',result[0].data.dqsl);
				}else{
					cgmx.editor.createNew();
					//cgmx.getStore().getAt(0).set('WZBM',result[n].json.wzdagl.id);
					//cgmx.getStore().getAt(0).set('WZMC',result[n].json.wzdagl.wzmc);
					setValues(cgmx.getStore().getAt(0),result[n].json);
					//cgmx.getStore().getAt(0).set('YSSL',result[n].data.dqsl);
				}
				wztmp = result[n].json;
			}            
		} else alert('没有选中物资！');	}}).show();	//模式对话框
		
/*
          var field = this;
          var url = contextPath+"/jteap/wz/wzlysq/selectxqjhmxIndex.jsp";
          var result = showModule(url, true, 800, 500);
          if (result != null) {
              this.displayValue=result.WZMC?result.WZMC:result.wzmc;
              this.setValue(result.WZMC?result.WZMC:result.wzmc);
              wztmp = result;
         } else alert('没有选中物资！');
*/
        }},

renderer:function(value,metadata,record,rowIndex,colIndex,store){
	if(returnStore!=null)
		return value;
 var bm = record.get('WZBM');
 var url = contextPath+"/jteap/wz/wzda/WzdaAction!showListAction.do?";
        var mc = '';	
	
        AjaxRequest_Sync(url,{queryParamsSql:"obj.id='"+bm+"'"},function(obj){
	var strReturn=obj.responseText;
	var responseObject=Ext.util.JSON.decode(strReturn);
	if(responseObject.totalCount==0) return '';
                //record.set('WZBM',responseObject.list[0].wzmc);
                mc  = responseObject.list[0].wzmc;
        });
        record.set('WZMC',mc);
        return mc;

}

},

/*******第3列*******/
{header:'申领数量',dataIndex:'SQSL',width:80,hidden:'false',sortable:true,align:'right',
editor:new Ext.form.NumberField({
xtype:'numberfield',
decimalPrecision:4,
minValue:0,
maxValue:999999.99,
value:0,
listeners:{focus:function(a){
        this.selectText();
    }
}
}),
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}
},

/*******第4列*******/
{header:'计量单位',dataIndex:'JLDW',width:80,hidden:'false',sortable:true},

/*******第5列*******/
{header:'价格',dataIndex:'JG',width:100,hidden:'false',sortable:true,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value){
         value = 0;
    }
    return parseFloat(value).toFixed(2);
}
},

{header:'领用数量',dataIndex:'LYSL',width:100,hidden:'true',sortable:true},



/*******第6列*******/
{header:'金额',dataIndex:'JE',width:100,hidden:'false',sortable:true,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
 return "<font color='red' style='font-weight:bold'>"+(record.get('SQSL')*record.get('JG')).toFixed(2)+"</font>";

}
},
{header:'',dataIndex:'XQJHMXBM',width:100,hidden:'true',sortable:true},
{header:'',dataIndex:'DQKC',width:100,hidden:'true',sortable:true},
{header:'ID',dataIndex:'ID',width:32,hidden:'true',sortable:false,editor:{xtype:'textfield'}},
{header:'YFPSL',dataIndex:'YFPSL',width:100,hidden:'true',sortable:true}

]















var ksrq = $("KSRQ").value;
var jsrq =$("JSRQ").value
	var url = contextPath + "/jteap/yx/aqyxfx/bkhdltjAction!showBkhdlTjAction.do";
 	var param = {ksrq:ksrq,jsrq:jsrq};
	AjaxRequest_Sync(url,param,function(ajax){
		var obj = ajax.responseText.evalJSON();
        var ary = str.split("!!");
        for(var j=0;j<ary.length-1;j++){
           $(ary[j]).value="" ;                                                
        }                 
		if(obj.success==true){
           var data = obj.data[0];
           str="";
           for (var key in data) {
                var value = eval("data."+key);
                str = str + key+"!!";
                if (value != null || value != '') {
                     if(typeof(value)=="object") {
                         $(key).value=formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
                     }else{
                           $(key).value=value;
                           }
                }
             } 
        }else{
			alert(obj.msg);
		}
	})

fillBlank();	



