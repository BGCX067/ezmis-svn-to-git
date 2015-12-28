package com.jteap.lp.pkgl.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.lp.pkgl.manager.PkglManager;
import com.jteap.lp.pkgl.model.Pkgl;

/**
 * 票库管理Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unchecked", "unused", "serial" })
public class PkglAction extends AbstractAction {

	private PkglManager pkglManager;

	/**
	 * 
	 * 描述 : 保存/修改 
	 * 作者 : wangyun
	 * 时间 : Sep 27, 2010
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateAction() throws Exception {
		String pfl = request.getParameter("pfl");
		String pmc = request.getParameter("pmc");
		String gllch = request.getParameter("gllch");
		String cjbm = request.getParameter("cjbm");
		String cjr = request.getParameter("cjr");
		String cjsj = request.getParameter("cjsj");
		String isBzp = request.getParameter("isBzp");
		String pzt = request.getParameter("pzt");
		String bzsm = request.getParameter("bzsm");
		String id = request.getParameter("id");
		String nm = request.getParameter("nm");

		try {
			Pkgl pkgl = null;
			if (StringUtil.isEmpty(id)) {
				pkgl = new Pkgl();
			} else {
				pkgl = pkglManager.get(id);
			}
			pkgl.setPfl(pfl);
			pkgl.setPmc(pmc);
			pkgl.setGllch(gllch);
			pkgl.setCjbm(cjbm);
			pkgl.setCjr(cjr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dtCjsj = sdf.parse(cjsj);
			pkgl.setCjsj(dtCjsj);
			pkgl.setIsBzp(isBzp);
			pkgl.setPzt(pzt);
			pkgl.setBzsm(bzsm);
			pkgl.setNm(nm);
			
			pkglManager.save(pkgl);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 生成标准票
	 * 作者 : wangyun
	 * 时间 : 2010-9-28
	 * 异常 : Exception
	 */
	public String makeBzpAction() throws Exception {
		try {
			String ids = request.getParameter("ids");
			String[] idx = ids.split(",");
			for (String id : idx) {
				Pkgl pkgl = pkglManager.get(id);
				pkgl.setIsBzp("1");
				pkglManager.save(pkgl);
			}
			outputJson("{success:true}");
		} catch (Exception e) {
			outputJson("{success:true,msg:'数据库异常，请联系管理员!'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 内码验证
	 * 作者 : wangyun
	 * 时间 : 2010-10-26
	 * 异常 : Exception
	 */
	public String validateLpNmAction() throws Exception {
		String nm = request.getParameter("nm");
		String id = request.getParameter("id");
		Pkgl pkgl = pkglManager.findUniqueBy("nm", nm);
		
		if (pkgl != null) {
			if(StringUtils.isNotEmpty(id) && id.equals(pkgl.getId().toString())){
				outputJson("{unique:true}");
			}else{
				outputJson("{unique:false}");	
			}
		} else {
			outputJson("{unique:true}");
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 两票种类树
	 * 作者 : wangyun
	 * 时间 : 2010-10-25
	 * 异常 : Exception
	 */
	public String showLpTreeAction() throws Exception {
		List<Pkgl> lstGzp = pkglManager.findBy("pfl", "gzp");
		List<Pkgl> lstCzp = pkglManager.findBy("pfl", "czp");
		List<Pkgl> lstLsp = pkglManager.findBy("pfl", "lsp");
		StringBuffer sb = new StringBuffer();
		
		sb.append("[{'id':'gzp','text':'工作票','children':[");
		for (Pkgl gzp : lstGzp) {
			String id = gzp.getId();
			String pmc = gzp.getPmc();
			sb.append("{'id':'");
			sb.append(gzp.getNm());
			sb.append("','text':'");
			sb.append(pmc);
			sb.append("','leaf':true,'pfl':'gzp'},");
		}
		sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		sb.append("]},");
		sb.append("{'id':'czp','text':'操作票','children':[");
		for (Pkgl czp : lstCzp) {
			String id = czp.getId();
			String pmc = czp.getPmc();
			sb.append("{'id':'");
			sb.append(czp.getNm());
			sb.append("','text':'");
			sb.append(pmc);
			sb.append("','leaf':true,'pfl':'czp'},");
		}
		sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		sb.append("]},");
		sb.append("{'id':'czp','text':'临时票','children':[");
		for (Pkgl lsp : lstLsp) {
			String id = lsp.getId();
			String pmc = lsp.getPmc();
			sb.append("{'id':'");
			sb.append(lsp.getNm());
			sb.append("','text':'");
			sb.append(pmc);
			sb.append("','leaf':true,'pfl':'czp'},");
		}
		sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		sb.append("]}]");
		outputJson(sb.toString());
		return NONE;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String pfl = request.getParameter("pfl");
		String pzt = request.getParameter("pzt");
		if (StringUtil.isNotEmpty(pfl)) {
			if (!"dbzp".equals(pfl)) {
				HqlUtil.addCondition(hql, "pfl", pfl);
				HqlUtil.addCondition(hql, "isBzp", "1");
			} else {
				HqlUtil.addCondition(hql, "isBzp", "2");
			}
		}
		if (StringUtil.isNotEmpty(pzt)) {
			HqlUtil.addCondition(hql, "pzt", pzt);
		}
		System.out.println("sql:"+hql.toString());
	}

	@Override
	public HibernateEntityDao getManager() {
		return pkglManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "pfl", "pmc", "cjbm", "cjr", "cjsj", "time", "pzt", "isBzp", "gllch", "bzsm" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "pfl", "pmc", "cjbm", "cjr", "cjsj", "time", "pzt", "isBzp", "gllch", "bzsm", "nm" };
	}

	public PkglManager getPkglManager() {
		return pkglManager;
	}

	public void setPkglManager(PkglManager pkglManager) {
		this.pkglManager = pkglManager;
	}

}
