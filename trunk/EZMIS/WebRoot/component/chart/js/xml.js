/*=============================================================
1.load_xml													加载XML对象
2.getXMLNode												XML结点:获取结点值
3.GetXpathDataAttrib								XML结点:获取XPATH上级结点DATE属性值
4.getAttributeValue									指定一个节点对象指定它的一个属性的名字,得到该属性的值,
===============================================================*/

function XMLUtil(){}

//1.XMLDOM:加载XML对象
XMLUtil.load_xml=function(xmls){
  try{
    var myxml=new ActiveXObject("MSXML2.FreeThreadedDOMDocument");
    myxml.async=false;
    myxml.loadXML(xmls);
    return myxml;
  }catch(exception){
    alert(exception.description);
  }
}

//2.XML结点:获取结点值
XMLUtil.getXMLNode=function (doc, xpath){
  var retval = "";
  var value = doc.selectSingleNode(xpath);
  if (value) retval = value.text;
  return retval;
}


//3.XML结点:获取XPATH上级结点DATE属性值
XMLUtil.GetXpathDataAttrib=function (doc, xpath) {
  var retval = "";
  var value = doc.selectSingleNode(xpath);
  if (value) retval = value.parentNode.getAttribute("Date");
  return retval;
}

//4.指定一个节点对象指定它的一个属性的名字,得到该属性的值,
//如果无该属性，则反回null
//
//oNode			xml dom中的一个节点对象
//sAttName 	属性名字
XMLUtil.getAttributeValue=function(oNode,sAttName)
{	
	var attList=oNode.attributes;
	var sResult=null;
	for(i=0;i<attList.length;i++){
		if(attList(i).name==sAttName){
			sResult=attList(i).value;
			break;
		}
	}
	return sResult;
}
/**
 * 为节点指定属性和值
 */
XMLUtil.setAttributeValue=function(oNode,sAttName,sValue){
	var attList=oNode.attributes;
	var att=null;
	for(i=0;i<attList.length;i++){
		if(attList(i).name==sAttName){
			att=attList(i);
			break;
		}
	}
	if(att==null){
		var oXmlDoc=oNode.ownerDocument;
		att=oXmlDoc.createAttribute(sAttName);		
		attList.setNamedItem(att);
	}
	att.value=sValue;
}
//5.XML结点:设置指定节点的值
XMLUtil.setXMLNodeValue=function(doc, xpath,sValue){
  var retval = "";
  var oNode = doc.selectSingleNode(xpath);
  oNode.text=sValue;
  return;
}

