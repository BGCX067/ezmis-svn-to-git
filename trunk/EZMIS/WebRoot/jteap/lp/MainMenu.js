	//主工具栏
   var mainTb = new Ext.Toolbar({
		listeners : {
			render : function(tb) {
				getRootMenu(tb);
			}
		}
    });
    
	//点击菜单事件
   function onItemClick(item){
	   if(!item.leaf){
			
		}else if(item.leaf && item.ctype == "Ext.menu.Item"){//叶子菜单时打开模块
		  	mainPanel.loadFunction(item.id,item.text,item.url,null);
		}
    }
    //加载菜单
    function loadMenu(item){
    	if(!item.loaded){
    		Ext.Ajax.request({
					url:link4,
					success:function(ajax){
				 		var responseText=ajax.responseText;	
				 		var objArr = Ext.util.JSON.decode(responseText);
				 		var menu ;
				 		if( item.isRoot || item.ctype == "Ext.menu.Item"){
				 			menu = item.menu;
				 		}else {
				 			menu  = item;
				 		}
				 		for(var i= 0;i<objArr.length;i++){
				 			if(objArr[i].leaf){ //添加叶子菜单
					 			menu.add({
					 						id:objArr[i].id,
					 						leaf:objArr[i].leaf,
					 						isRoot:false,
					 						text: objArr[i].text,
					 						url:objArr[i].link,
					 						icon :objArr[i].icon,
					 						handler: onItemClick,
					 						href :'javascript:void(0);'
									})
					 			}else{
					 				menu.add({//添加目录菜单
					 						id:objArr[i].id,
					 						leaf:objArr[i].leaf,
					 						isRoot:false,
					 						text: objArr[i].text,
					 						url:objArr[i].link,
					 						icon :objArr[i].icon,
					 						handler: onItemClick,
					 						menu:new Ext.menu.Menu({
						       						 id: objArr[i].id,
						       						  loaded:false,
						       						 listeners :{
							 							beforeshow:loadMenu
							 						}
											  })
					 						})
					 			}
					 	}
					},
				 	failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {node:item.id}		
				});
				item.loaded = true;
    	}
    }
    
    function hideMenu(menu){
    	menu.hide(true);
    }
    
    //加载根菜单
    function getRootMenu(tb){
    	    var moduleId = $("moduleId").value;
    		//alert(moduleId);
    		var item = {};
    		item.id = "rootNode";
    	  	Ext.Ajax.request({
				url:link14+"?moduleId="+moduleId,
//    	  		url:link4,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var objArr = Ext.util.JSON.decode(responseText);
			 		for(var i= 0;i<objArr.length;i++){
			 			tb.add({
			 						id:objArr[i].id,
			 						leaf:objArr[i].leaf,
			 						text: objArr[i].text,
			 						isRoot:true,
			 						iconCls:objArr[i].cls,
			 						handler: onItemClick,
            						menu: new Ext.menu.Menu({
						       						 id: objArr[i].id,
						       						 loaded:false,
						       						 listeners :{
							 							beforeshow:loadMenu
							 						}
											  })
			 			 })
					}
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {node:item.id}		
			});
    }