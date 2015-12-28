/**
 * 字段列表
 */
RightGrid=function(){
	var gridHeight = document.body.clientHeight;
	var rightGrid = this;
	var cm = new Ext.grid.ColumnModel([ 
			//{header:'编号',dataIndex:'id',sortable:true}, 
			{header: "库名", dataIndex:'name',width:150},
			{header: "计划金额",dataIndex:'jhje',width:150},
			{header: "差额金额",dataIndex:'cbje',width:150},
			{header: "实际金额",dataIndex:'sjje',width:150}
			]) ; 
			var data = [ 
			['备品库 汇总',11665783,-467207.739,11198575.261],
			['设备库 汇总',1718139,-71963.983,1646175.017],
			['备品一库 汇总',5469754,-229099.988,5240654.012],
			['小计',18853676,-768271.71,18085404.29],
			//[,,,],
			['电料库 汇总',1694072.9,730662.8876,2424735.788],
			['五金库 汇总',390915.25,164597.7724,555513.0224],
			['钢材库 汇总',416800.24,176988.0096,593788.2496],
			['仪器仪表库 汇总',76120,32809.155,108929.155],
			['工具库 汇总',23550,10150.496,33700.496],
			['综合库 汇总',287001.254,123711.6914,410712.9454],
			['加工件库 汇总',634534,273496.178,908030.178],
			['小计',3522993.644,1512416.1900,5035409.834],
			//[,,,],
			['总计',22376669.64,744144.48,23120814.12]
			] ; 
			var ds = new Ext.data.Store({ 
			proxy:new Ext.data.MemoryProxy(data), 
			reader:new Ext.data.ArrayReader({},[  
				{name:'name'},{name:'jhje'},{name:'cbje'},{name:'sjje'}
				])
			});
			/*ds.on('load',function(store,records,options){
				for(var i = 0; i < store.getCount(); i++){
	             	var record = store.getAt(i);
	             	alert(record.data.name);
	             	if(record.data.name=='小计' || record.data.name=='总计')
	             	{
	             		//rightGrid.getView().getRow(i).style.background="yellow";
	             	}
				}
			})*/
			ds.load();
			
			
			
	RightGrid.superclass.constructor.call(this,{
	 	ds: ds,
	 	cm: cm,
	    margins:'2px 2px 2px 2px',
		height:gridHeight,
		frame:true,
		region:'center',
		viewConfig: {
        	forceFit: true,
    		getRowClass : function(record,rowIndex,rowParams,store){
  				//小计和总计数据显示黄色
  				if(record.get('name')=='小计' || record.get('name') == '总计')
 				{
 				return 'x-grid-record-red';
 				}
 				else
 				{
 				return '';
 				}
 				
 				}
    	}
	});	
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
});
