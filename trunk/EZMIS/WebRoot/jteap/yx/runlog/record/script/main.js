
//保存全部日志记录
function saveMain(all_params,tableId,zbbc,zbsj,mask){
	var json = Ext.util.JSON.encode(all_params);
	Ext.Ajax.request({
		url: link1,
		method: 'post',
		params: {json:json, tableId:tableId, zbbc:zbbc, zbsj:zbsj},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.location.reload();
				mask.hide();
			}else{
				alert('保存失败');
				mask.hide();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
			mask.hide();
		}
	})
}

//日志取数
function logQushu(tableId,zbsj,zbbc,mask){
	Ext.Ajax.request({
		url: link2,
		method: 'post',
		params: {tableId: tableId, zbsj: zbsj, zbbc: zbbc},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				if(responseObj.errMsg != null){
					alert(responseObj.errMsg);					
				}else{
					alert('取数成功');
				}
				mask.hide();
				window.location.reload();
			}else{
				alert('取数失败');
				mask.hide();
			}
		},
		failure: function(){
			mask.hide();
			window.location.reload();
		}
	})
}
