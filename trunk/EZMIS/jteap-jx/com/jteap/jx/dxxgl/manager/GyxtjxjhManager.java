package com.jteap.jx.dxxgl.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.BeanUtils;
import com.jteap.jx.dxxgl.model.Gyxtjxjh;
import com.jteap.jx.dxxgl.model.GyxtjxjhBj;
import com.jteap.jx.dxxgl.model.GyxtjxjhDataItem;
import com.jteap.jx.dxxgl.model.Jxsbtz;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 公用系统检修计划manager
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked", "unused"})
public class GyxtjxjhManager extends HibernateEntityDao<Gyxtjxjh> {

	private JxsbtzManager jxsbtzManager;
	private GyxtjxjhBjManager gyxtjxjhBjManager;
	private DictManager dictManager;

	/**
	 * 
	 * 描述 : 根据设备和计划年份
	 * 作者 : wangyun
	 * 时间 : Aug 18, 2010
	 * 参数 : 
	 * 		sbid ： 设备
	 * 		year ： 计划年份
	 * 返回值 : 
	 * 异常 :
	 */
	public Gyxtjxjh findBySbAndYear(String sbid, String year) {
		String hql = "from Gyxtjxjh where sbid = ? and jhnf = ?";
		Gyxtjxjh gyxtjxjh = (Gyxtjxjh) this.findUniqueByHql(hql, new Object[] {sbid, year});
		return gyxtjxjh;
	}
	
	/**
	 * 
	 * 描述 : 根据上次检修年份和设备获得上次检修时间
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 参数 : 
	 * 		scjxYear ： 上次检修年份
	 * 		sbid ： 设备id
	 * 返回值 : 
	 */
	public String findScjxsjByScjxYearAndSbid(String sbid) {
		String hql = "select scjxsj from Gyxtjxjh where sbid=? order by jhnf desc";
		List<String> lst = this.find(hql, new Object[]{sbid});
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * 描述 : 根据所属专业和计划年份获得前台展示数据集
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 参数 : 
	 * 		zyId ： 所属专业
	 * 		jhnf :  计划年份
	 * 返回值 : 
	 * 异常 :
	 */
	public List<GyxtjxjhDataItem> getGyxtjxjhData(String zyId, String jhnf) throws Exception {
		// 查找所属专业所有设备
		List<Jxsbtz> lstSb = jxsbtzManager.findBySszy(zyId);
		List<GyxtjxjhDataItem> lstDataItem = new ArrayList<GyxtjxjhDataItem>();
		GyxtjxjhDataItem gyxtDataItem = null;
		
		// 遍历设备
		for (Jxsbtz jxsbtz : lstSb) {
			String sbid = jxsbtz.getId();
			// 根据设备和计划年份得到公用检修计划
			Gyxtjxjh gyxtjxjh = this.findBySbAndYear(sbid, jhnf);
			
			List<GyxtjxjhBj> lstGyxtjxjhBj = new ArrayList<GyxtjxjhBj>();
			// 得到每个月的标记
			for (int i = 1; i < 13; i++) {
				GyxtjxjhBj gyxtjxjhBj = gyxtjxjhBjManager.findByGyxtjxjh(sbid, String.valueOf(i), jhnf);
				if (gyxtjxjhBj != null) {
					String bjys = gyxtjxjhBj.getBjys();
					Dict dict = dictManager.findDictByCatalogNameWithKey("GYJH_YS", bjys);
					GyxtjxjhBj tmp = new GyxtjxjhBj();
					BeanUtils.copyProperties(tmp, gyxtjxjhBj);
					tmp.setBjys(dict.getValue());
					lstGyxtjxjhBj.add(tmp);
				} else {
					lstGyxtjxjhBj.add(gyxtjxjhBj);
				}
			}
			
			gyxtDataItem = new GyxtjxjhDataItem();
			gyxtDataItem.setJxsbtz(jxsbtz);
			gyxtDataItem.setGyxtjxjh(gyxtjxjh);
			gyxtDataItem.setLstBj(lstGyxtjxjhBj);
			lstDataItem.add(gyxtDataItem);
		}
		
		return lstDataItem;
	}

	public JxsbtzManager getJxsbtzManager() {
		return jxsbtzManager;
	}

	public void setJxsbtzManager(JxsbtzManager jxsbtzManager) {
		this.jxsbtzManager = jxsbtzManager;
	}

	public GyxtjxjhBjManager getGyxtjxjhBjManager() {
		return gyxtjxjhBjManager;
	}

	public void setGyxtjxjhBjManager(GyxtjxjhBjManager gyxtjxjhBjManager) {
		this.gyxtjxjhBjManager = gyxtjxjhBjManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

}
