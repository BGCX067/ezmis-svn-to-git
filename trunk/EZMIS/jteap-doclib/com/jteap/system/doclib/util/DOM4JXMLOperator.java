package com.jteap.system.doclib.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
@SuppressWarnings({ "unchecked", "serial" ,"unused"})
public class DOM4JXMLOperator {
	private String url; // 文件地址

	private org.dom4j.Document document; // 解析成的DOM对象

	private org.dom4j.Element root; // 根节点

	/**
	 * 
	 *描述：根据文件地址打开XML文件，并且解析成DOM
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void open(String url) {
		File doc = new File(url);
		this.url = url;
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(doc);

			// 获得根节点
			root = document.getRootElement();
		} catch (org.dom4j.DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *描述：得到XML文件编码
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getXMLEncoding() {
		return document.getXMLEncoding();
	}

	/**
	 * 
	 *描述：根据xpath找到的节点，添加子节点列表
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void add(String xPath, java.util.List<Object> entries,
			String typeName) {
		for (Iterator<Object> i = entries.iterator(); i.hasNext();) {
			add(i.next(), xPath, typeName);
		}
	}

	/**
	 * 
	 *描述：根据xpath找到的节点，添加子节点
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void add(Object o, String xPath, String typeName) {
		Element el = interpretJavaBean(o, typeName);
		for (Iterator<Object> i = find(xPath).iterator(); i.hasNext();) {
			// 如果实现添加element
			// Node 和Element区别 node不一定是一个元素，但是元素一定是一个Node
			// 元素是含有一个小范围的定义，必须是包含完整信息的节点才是一个元素
			// ((Node)i.next()).add(element);
			Node node = (Node) i.next();
			if (node instanceof Element) {
				((Element) node).add(el);
			}
		}
	}

	/**
	 * 
	 *描述：根据xpath查找节点列表
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<Object> find(String xPath) {
		List<Object> list = document.selectNodes(xPath);
		return list;
	}

	/**
	 * 
	 *描述：修改VALUE值为XML认可的空值
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String changeValue(String value) {
		if (value == null || value.equals("")) {
			value = "#N/A";
		}
		return value;
	}

	/**
	 * 
	 *描述：根据XPath找到节点，并且删除该节点
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void deleteChild(String xPath) {
		for (Iterator<Object> i = find(xPath).iterator(); i.hasNext();) {
			Node node = (Node) i.next();
			// Removes this node from its parent if there is one.
			node.detach();
		}
	}

	/**
	 * 
	 *描述：找到子节点后，并且修改其 VALUE值
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void update(String xPath, String value) {
		value = this.changeValue(value);
		for (Iterator<Object> i = find(xPath).iterator(); i.hasNext();) {
			((Node) i.next()).setText(value);
		}
	}

	/**
	 * 
	 *描述：查找第i的实体以便修改其值
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void update(String xPath, String value, int i) {
		value = this.changeValue(value);
		int k = 0;
		for (Iterator<Object> iter = find(xPath).iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			if (i == k) {
				node.setText(value);
				break;
			}
			k++;
		}
	}

	/**
	 * 
	 *描述：解析JAVABEAN成为XML实体
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Element interpretJavaBean(Object o, String typeName) {
		Class<? extends Object> clazz = o.getClass();
		// 创建Elment对象
		Element element = DocumentHelper.createElement(typeName);
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		for (PropertyDescriptor prop : props) {
			try {
				if (prop.getReadMethod().invoke(o) != null
						&& !"class".equals(prop.getDisplayName())) {
					element.addAttribute(prop.getDisplayName(), prop
							.getReadMethod().invoke(o).toString());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return element;
	}

	/**
	 * 
	 *描述：替换原有实体,如果有子实体则加入子实体
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void replaceNode(Object o, String xPath, String typeName, Object subO) {
		Element parentElement = interpretJavaBean(o, typeName);
		if (subO != null) {
			parentElement.add(interpretJavaBean(subO, typeName.substring(0,
					typeName.length() - 1)));
		}
		for (Iterator<Object> iter = find(xPath).iterator(); iter.hasNext();) {
			Node el = (Node) iter.next();
			// node.getParentNode().replaceChild(element, node);
			/*
			 * 获取父节点，并且遍历父节点的子节点，替换相应节点
			 */
			List list = el.getParent().content();
			list.set(list.indexOf(el), parentElement);
			el.getParent().setContent(list);
		}

	}

	/**
	 * 
	 *描述：保存修改后的XML
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void saveToXml() {
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("UTF-8");
			XMLWriter output = new XMLWriter(new FileOutputStream(url), format);

			output.write(document);
			output.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	public void saveToHtml() {
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("gbk");
			HTMLWriter output = new HTMLWriter(new FileOutputStream(url),format);
			output.write(document);
			output.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	/**
	 * 
	 *描述：把字符串转换成文档对象
	 *时间：2010-2-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void stringToDocument(String xml){
		try {
			this.document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
