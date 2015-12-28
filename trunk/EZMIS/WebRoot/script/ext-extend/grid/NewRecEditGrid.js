/**
 * 只允许编辑新创建的记录的网格
 * 需要在新增的记录上添加一个新纪录的标识
 * function(){
        var p = new Plant({
            common: 'New Plant 1',
            light: 'Mostly Shade',
            price: 0,
            availDate: (new Date()).clearTime(),
            indoor: false
        });
        grid.stopEditing();
       	p.newRecord=true;	//添加一个新纪录的标识
        store.insert(0, p);
        grid.startEditing(0, 0);
    }
 */
Ext.app.NewRecEditGrid=function(config){
	Ext.app.NewRecEditGrid.superclass.constructor.call(this,config);
}

Ext.extend(Ext.app.NewRecEditGrid,Ext.grid.EditorGridPanel,{
/**
     * Starts editing the specified for the specified row/column
     * @param {Number} rowIndex
     * @param {Number} colIndex
     * 此部分直接摘抄父类源码，只是在出发编辑之前判断当前记录是否为新创建的记录
     */
    startEditing : function(row, col){
        this.stopEditing();
        if(this.colModel.isCellEditable(col, row)){
            this.view.ensureVisible(row, col, true);
            var r = this.store.getAt(row);
            var field = this.colModel.getDataIndex(col);
            var e = {
                grid: this,
                record: r,
                field: field,
                value: r.data[field],
                row: row,
                column: col,
                cancel:false
            };
            //在源码的基础上添加了“ && r.newRecord”
            if(this.fireEvent("beforeedit", e) !== false && !e.cancel && r.newRecord){
                this.editing = true;
                var ed = this.colModel.getCellEditor(col, row);
                if(!ed.rendered){
                    ed.render(this.view.getEditorParent(ed));
                }
                (function(){ // complex but required for focus issues in safari, ie and opera
                    ed.row = row;
                    ed.col = col;
                    ed.record = r;
                    ed.on("complete", this.onEditComplete, this, {single: true});
                    ed.on("specialkey", this.selModel.onEditorKey, this.selModel);
                    this.activeEditor = ed;
                    var v = r.data[field];
                    ed.startEdit(this.view.getCell(row, col), v);
                }).defer(50, this);
            }
        }
    }
});