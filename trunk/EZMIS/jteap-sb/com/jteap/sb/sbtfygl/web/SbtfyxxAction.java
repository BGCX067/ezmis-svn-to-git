package com.jteap.sb.sbtfygl.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.sb.sbtfygl.manager.SbtfyxxManager;
import com.jteap.sb.sbtfygl.model.Sbtfyxx;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class SbtfyxxAction extends AbstractAction {

	private SbtfyxxManager sbtfyxxManager;

	public SbtfyxxManager getSbtfyxxManager() {
		return sbtfyxxManager;
	}

	public void setSbtfyxxManager(SbtfyxxManager sbtfyxxManager) {
		this.sbtfyxxManager = sbtfyxxManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return sbtfyxxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "jz", "jzzt", "tfysj", "syr", "syyj",
				"sysj", "remark", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "jz", "jzzt", "tfysj", "syr", "syyj",
				"sysj", "remark" };
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		// String flbm = request.getParameter("flbm");
		// if (StringUtil.isNotEmpty(flbm)) {
		// HqlUtil.addWholeCondition(hql, "sbpjCatalog.flbm like '"+flbm+"%'");
		// }
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	/**
	 * 保存或修改设备基础台帐信息
	 * 
	 * @return
	 */
	public String saveOrUpdateAction() {
		try {
			Sbtfyxx sbtfyxx = new Sbtfyxx();
			String id = request.getParameter("id");
			// String sbpjCatalogId = request.getParameter("sbpjCatalogId");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date tfysj = (request.getParameter("tfysj") == "") ? null
					: dateFormat.parse(request.getParameter("tfysj"));
			Date sysj = (request.getParameter("sysj") == "") ? null
					: dateFormat.parse(request.getParameter("sysj"));
			if (StringUtil.isNotEmpty(id)) {
				sbtfyxx.setId(id);
			}
			sbtfyxx.setJz(request.getParameter("jz"));
			sbtfyxx.setJzzt(request.getParameter("jzzt"));
			sbtfyxx.setTfysj(tfysj);
			sbtfyxx.setSyr(request.getParameter("syr"));
			sbtfyxx.setSyyj(request.getParameter("syyj"));
			sbtfyxx.setSysj(sysj);
			sbtfyxx.setRemark(request.getParameter("remark"));
			sbtfyxxManager.save(sbtfyxx);

			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 更新设备停复役信息
	 * 
	 * @return
	 */
	public String updateDataAction() {
		String gridData = request.getParameter("gridData");
		List<Map<String, String>> list = JSONUtil.parseList(gridData);
		try {
			for (Map<String,String> map : list) {
				String id = map.get("id");
				Sbtfyxx sbtfyxx = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				sbtfyxx = sbtfyxxManager.get(id);
				
				sbtfyxx.setSyr(map.get("syr"));
				sbtfyxx.setSyyj(map.get("syyj"));
				sbtfyxx.setSysj(DateUtils.StrToDate(map.get("sysj"),"yyyy-MM-dd"));
				sbtfyxx.setRemark(map.get("remark"));
				sbtfyxxManager.save(sbtfyxx);
			}
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

}
