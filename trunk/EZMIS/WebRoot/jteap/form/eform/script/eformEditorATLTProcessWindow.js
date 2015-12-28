var ProcessWindow=function(swfupload){
	this.swfupload = swfupload;
	this.pbar = new Ext.ProgressBar({
		text:'正在上传附件',
		value:0
	});
	var pwin = this;
	ProcessWindow.superclass.constructor.call(this,{
        title: '正在上传附件...',
        width: 400,
        height:100,
        modal:true,
        plain:true,
        autoHeight :true,
        closable :false,
        draggable :true,
        resizable :false,
        buttonAlign:'right',
        items: [this.pbar],
	    buttons:[{text:'取消',handler:function(){
	    	swfupload.cancelQueue();
	    	pwin.hide();
	    }}]
	 })
}
Ext.extend(ProcessWindow, Ext.Window, {
	updateProcess:function(value,text){
		this.pbar.updateProgress(value,text);
	}
});