package com.jteap.jhtj.sjqx.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjKndJZ;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.jhtj.sjqx.model.Sjqx;
@SuppressWarnings({ "unchecked", "serial" })
public class FlTreeAction extends AbstractTreeAction {
	private TjItemKindManager tjItemKindManager;
	private SjqxManager sjqxManager;
	public TjItemKindManager getTjItemKindManager() {
		return tjItemKindManager;
	}

	public void setTjItemKindManager(TjItemKindManager tjItemKindManager) {
		this.tjItemKindManager = tjItemKindManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		List<TjItemKind> childList=new ArrayList<TjItemKind>();
		TjItemKind kind=(TjItemKind)bean;
		Set<TjItemKind> set=kind.getChildKind();
		if(set!=null){
			childList.addAll(set);
		}
		//如果不是节点数据那么就到了最底层，再判断该数据类型是否含有机组
		if(kind.getFlflag()!=0&&kind.getFlflag()!=4){
			String kid=kind.getKid();
			boolean isJz=tjItemKindManager.isIncludeJz(kid);
			if(isJz){
				List<TjKndJZ> jzList=tjItemKindManager.findTjKndJZByKid(kid);
				for(TjKndJZ jz:jzList){
					TjItemKind jzKind=new TjItemKind();
					jzKind.setId(jz.getId());
					jzKind.setKid(jz.getKid());
					jzKind.setKname(jz.getJzname());
					jzKind.setFlflag(4l);
					childList.add(jzKind);
				}
			}
		}
		return childList;
	}
	
	@Override
	public String showTreeAction() throws Exception {
		String roleid=request.getParameter("roleid");
		final List<Sjqx> qxList=this.sjqxManager.findBy("roleid", roleid);
		
		final JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				TjItemKind kind=(TjItemKind) obj;
				String id=kind.getId();
				for(Sjqx sjqx:qxList){
					if(id.equals(sjqx.getQxid())){
						map.put("checked",true);
					}
				}
				map.put("flflag", kind.getFlflag());
				map.put("kid", kind.getKid());
				map.put("ccCheck", true);
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return tjItemKindManager.getRootList();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	public HibernateEntityDao getManager() {
		return tjItemKindManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}

	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}

}
