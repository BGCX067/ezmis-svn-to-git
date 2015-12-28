
/**
 * 1#机组指标汇总
 */
Grid=function(jzNum){
	
    var grid=this;
    this.toolbar=new Ext.Toolbar({
		items:['<font style="color: red;">#'+jzNum+'机组小指标汇总</font>','-',{text:'导出Excel',handler:function(){
			exportMyExcel(true,jzNum);
		}}]
	});
	Grid.superclass.constructor.call(this,{
		ds: new Ext.data.Store(),
	 	cm: this.getMyColumnModel(),
		sm: this.sm,
	    margins: '2px 2px 2px 2px',
		width: 765,
		height: 450,
		frame: true,
		region: 'center',
		tbar: this.toolbar
	});	
	
}
Ext.extend(Grid, Ext.grid.GridPanel, {
	
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * default 列模型
	 */
	getMyColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		    	{id:'columncode',header: "小指标名称", width: 140, sortable: true, "dataIndex":"directiveName"},
		    	{id:'columncode',header: "单位", width: 80, sortable: true, "dataIndex":"remark"},
		    	{id:'columncode',header: "目标值", width: 100, sortable: true, "dataIndex":"mubiaozhi"},
		        {id:'columncode',header: "一值", width: 120, sortable: true, "dataIndex":"yizhi"},
		        {id:'columncode',header: "二值", width: 120, sortable: true, "dataIndex":"erzhi"},
		        {id:'columncode',header: "三值", width: 120, sortable: true, "dataIndex":"sanzhi"},
		        {id:'columncode',header: "四值", width: 120, sortable: true, "dataIndex":"sizhi"},
		        {id:'columncode',header: "五值", width: 120, sortable: true, "dataIndex":"wuzhi"},
		        {id:'columncode',header: "实际值汇总", width: 120, sortable: true, "dataIndex":"zongji"},
		        {id:'columncode',header: "对标结果", width: 120, sortable: true, "dataIndex":"dbjg"}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		var grid = this;
		this.reconfigure(ds,cm);
		this.store.reload();
	},
	
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData2:function(ds){
		var grid = this;
		var cm = grid.getMyColumnModel();
		this.reconfigure(ds,cm);
		this.store.reload();
	}
		
});