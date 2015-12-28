
/**
 * 手工清空hibernate缓存
 */
function clearHibernateCache() {
	Ext.Ajax.request({
		url : link1,
		success : function(ajax, options) {
			var responseText = ajax.responseText;
			var responseObject = Ext.util.JSON.decode(responseText);
			if(responseObject.success){
				alert("缓存清空成功");
			}else{
				alert("清空缓存是遇到不明异常");
			}
		}
	});
}