package com.jteap.wz.yhdmx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.yhdmx.manager.YhdmxManager;

@SuppressWarnings({ "unchecked", "serial" })
public class YhdmxSelectAction extends AbstractAction {

	private YhdmxManager yhdmxManager;
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}
	
	
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String zt = request.getParameter("zt");
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(zt)){
				HqlUtil.addCondition(hql, "zt",zt);
				HqlUtil.addCondition(hql, "zt", "1", HqlUtil.LOGIC_OR);
			}
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "yhdgl.bh", "desc");
		}
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return yhdmxManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"sjhj",
			"jhje",
			"cgjhmx",
			"xqjhDetail",
			"xqjh",
			"gclb",
			"gcxm",
			"sqbm",
			"ghdw",
			"xh",
			"sl",
			"zf",
			"wzdagl",
			"wzmc",
			"xhgg",
			"kw",
			"cwmc",
			"ck",
			"ckmc",
			"yhdbh",
			"tssl",
			"fpbh",
			"dhsl",
			"cgjldw",
			"sqdj",
			"yssl",
			"id",
			"jhdj",
			"hsxs",
			"yhdgl",
			"yhdmxs",
			"bh",
			"bz",
			"gclb",
			"gcxm",
			"dhrq",
			"ysrq",
			"htbh",
			"ghdw",
			"personCgy",
			"personBgy",
			"id",
			"userName",
			"userLoginName",
			"zt",
			"rksj",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"ghdw",
			"xh",
			"sl",
			"zf",
			"wzdagl",
			"wzmc",
			"yhdbh",
			"tssl",
			"fpbh",
			"dhsl",
			"cgjldw",
			"sqdj",
			"yssl",
			"id",
			"jhdj",
			"hsxs",
			"zt",
		"time"};
	}


}
