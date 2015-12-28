// some data used in the examples
Ext.namespace('Ext.exampledata');

Ext.exampledata.states = [
	{value:'1',namex:"tanchang"},
	{value:'2',namex:"tanchang2"},
	{value:'3',namex:"tanchang3"}
];

Ext.namespace('Ext.example');
 

Ext.example.Store = new Ext.data.JsonStore({
   	fields: [{name:'value'}, {name:'namex'}],
    data : Ext.exampledata.states,

	/* needed to render in the grid... */
	getNamesByIds: function(keyString) { 
		if(keyString==undefined) return null;
		var keys = keyString.split(','); 
	   	var text  = ''; 
	   	for(var i=0; i<keys.length; i++)
		{  
	    	var item = Ext.example.Store.query('value', keys[i]).items[0]; 
	    	if(item != undefined) text += (text!='' ? '; ':'') + item.data.json.namex; 
	   	} 
    	return text;
	}
});


Ext.example.StateMultiSelect = function(config) {
    Ext.example.StateMultiSelect.superclass.constructor.call(this, config);
};
Ext.extend(Ext.example.StateMultiSelect, Ext.form.MultiSelectField, {
	store: Ext.example.Store 
	,valueField:'value'
	,displayField:'namex'
	,mode: 'local'
});
