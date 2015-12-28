
/**
 * 保存全部日志记录
 */
function saveMain(all_params,year,mask){
	var json = Ext.util.JSON.encode(all_params);
	Ext.Ajax.request({
		url: link1,
		method: 'post',
		params: {json:json, year:year},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				mask.hide();
			}else{
				alert('保存失败');
			}
		}, 
		failure: function(){
			alert('数据库异常，请联系管理员');
		}
	})
}

