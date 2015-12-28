PhotoDataView=function(fileid,zlbh){
	 this.defaultDs=this.getDefaultDS(link7+"?fileid="+fileid+"&zlbh="+zlbh);
	 var view=this;
	 var data=new Ext.DataView({
            store: this.defaultDs,
            tpl: this.getXTemplate(),
            multiSelect: true,
            autoHeight:true,
        	autoWidth:true,
            overClass:'x-view-over',
            itemSelector:'div.thumb-wrap',
            emptyText: '没有该类型的资料',
            plugins: [
                new Ext.DataView.DragSelector(),
                new Ext.DataView.LabelEditor({dataIndex: 'FILENAME'})
            ],

            prepareData: function(data){
                return data;
            },
            
            listeners: {
            	selectionchange: {
            		fn: function(dv,nodes){
            			var l = nodes.length;
            			var s = l != 1 ? 's' : '';
            			view.setTitle('当前'+l+'项被选中');
            			$("img").innerHTML="";
            			//this.setTitle('Simple DataView ('+l+' item'+s+' selected)');
            		}
            	},
            	click:function(dataview,index,node,e){
            		var imgObj=$("yx_"+node.id);
            		$("img").innerHTML="<img src='"+imgObj.src+"' width='99%' height='74%'>";
            	}
            }
        });
     
	 PhotoDataView.superclass.constructor.call(this,{
	 	id:'images-view',
        frame:true,
        width:700,
        height:150,
        autoScroll: true,
        collapsible:true,
        layout:'fit',
        title:'列表',
        items:data,
        renderTo:'imgs'
	});	
}

Ext.extend(PhotoDataView, Ext.Panel, {
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'ID'
	        }, [
			"ID","FILENAME","PAGENO","URL_IMAGE"
	        ]),
	        remoteSort: true
	    });
	    //ds.load();
		return ds;
	},
	getXTemplate:function(){
		var tpl = new Ext.XTemplate(
		'<tpl for=".">',
            '<div class="thumb-wrap" id="{FILENAME}">',
		    '<div class="thumb"><img id="yx_{FILENAME}" src="icon/extanim32.gif"></div>',
		    '<span class="x-editable">{PAGENO}</span></div>',
        '</tpl>',
        '<div class="x-clear"></div>'
		);
		return tpl;
	},
	showImage: function(){
		xx = this.defaultDs.getCount();
		alert(xx);
	}
	
});

function preLoad(obj, src, width, height, mode,
				needStatistic, type) {
					
		width = width || 100;
		height = height || 100;
		var mode = mode || 0;
		var img = new Image();
		img.onload = function() {
			this.onload = null;
			_adjust(this, width, height, mode);
			obj.src = this.src;
			obj.style.width = this.width + "px";
			obj.style.height = this.height + "px";
			obj.style.display = "";
			if (height > this.height) {
				//vMiddle(obj, height, this.height);
			}
			if (mode && this.width > width) {
				//hMiddle(obj, width, this.width);
			}
			var m = null;
			
			img.onload = null;
			img.onerror = null;
			img = null;
		}
		img.onerror = function() {
			obj.src = 'icon/extanim32.gif';
			img.onload = null;
			img.onerror = null;
			img = null;
		}
		var _st = new Date();
		img.src = src+"&ss="+Math.random(); 
	}
	
	var _scaleVar = 1000;
		function _adjust(img, width, height, bSplit) {
		if (img.width < width && img.height < height) {
			return;
		}
		var mode = img.height * width > img.width * height ? 1 : 2;
		if (bSplit) {
			mode = mode == 1 ? 2 : 1;
		}
		switch (mode) {
			case 2 :
				img.height = Math.round(img.height * width * _scaleVar
						/ img.width)
						/ _scaleVar;
				img.width = width;
				break;
			case 1 :
				img.width = Math.round(img.width * height * _scaleVar
						/ img.height)
						/ _scaleVar;
				img.height = height;
				break;
		}
	}