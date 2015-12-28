/**
 * 数据存储结构
 * <root>
 * 	<table name='' cname='' sysid='' checked='true|false'>
 * 		<field name='' cnamd='' checked='true|false'/>
 * 		<field name='' cnamd='' checked='true|false'/>
 * 		<field name='' cnamd='' checked='true|false'/>
 * 		<field name='' cnamd='' checked='true|false'/>
 * 		<field name='' cnamd='' checked='true|false'/>
 *  </table>
 * </root>
 */
var oXmlDoc=XMLUtil.load_xml('<root></root>');
var oRoot=oXmlDoc.selectSingleNode("/root");

/**
 * 创建表节点 
 */
function xml_createTable(bbindexid,name,cname){
	var oTable=oXmlDoc.createNode(1,"table","");
	oRoot.appendChild(oTable);
	XMLUtil.setAttributeValue(oTable,"name",name);
	XMLUtil.setAttributeValue(oTable,"cname",cname);
	XMLUtil.setAttributeValue(oTable,"bbindexid",bbindexid);
	XMLUtil.setAttributeValue(oTable,"checked",'false');
	return oTable;
}
/**
 * 为表节点创建字段节点
 */
function xml_createField(oTableNode,fname,fcname,type){
	var oField=oXmlDoc.createNode(1,"field","");
	oTableNode.appendChild(oField);
	XMLUtil.setAttributeValue(oField,"name",fname);
	XMLUtil.setAttributeValue(oField,"cname",fcname);
	XMLUtil.setAttributeValue(oField,"type",type);	
	XMLUtil.setAttributeValue(oField,"checked","false");
	return oField;
}
/**
 * 查找表名为tname的表的对象
 */
function xml_findTable(tname){
	var oTable=oXmlDoc.selectSingleNode("//table[@name='"+tname+"']");
	return oTable;
}
/**
 * 根据表名及字段名查询字段对象
 */
function xml_findField(tname,fname){
	var oField=oXmlDoc.selectSingleNode("//table[@name='"+tname+"']//field[@name='"+fname+"']");
	return oField;
}
/**
 * 设置表的checked状态
 */
function xml_setTableChecked(tname,checked){
	var oTable=xml_findTable(tname);
	XMLUtil.setAttributeValue(oTable,"checked",''+checked);
}
/**
 * 设置字段的checked状态
 */
function xml_setFieldChecked(tname,fname,checked){
	var oField=xml_findField(tname,fname);
	XMLUtil.setAttributeValue(oField,"checked",''+checked);	
}
