package com.jteap.wz.tjrw.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.StringSubstitution;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.fytj.manager.FytjManager;
import com.jteap.wz.sfjctj.manager.SfjcManager;
import com.jteap.wz.sfzjtj.manager.SfzjManager;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjny.model.Tjny;
import com.jteap.wz.tjrw.model.Tjrw;
import com.jteap.wz.wzda.manager.WzdaManager;


/**
 * 统计任务处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class TjrwManager extends HibernateEntityDao<Tjrw>{
	
	private SfjcManager sfjcManager;
	private SfzjManager sfzjManager;
	private FytjManager fytjManager;
	private WzdaManager wzdaManager;
	private CkglManager ckglManager;
	/**
	 * 新增一个统计任务
	 * @param nf	任务年份
	 * @param yf	任务月份
	 * @param rwlb	任务类别
	 */
	public void addTjrw(String nf,String yf,String ri,String rwlb){
		List list = this.find("from Tjrw t where t.nf = '"+nf+"' and yf = '"+yf+"' and t.rwlb = '"+rwlb+"'");
//		System.out.println("from Tjrw t where t.nf = '"+nf+"' and yf = '"+yf+"' and t.rwlb = '"+rwlb+"'");
		if(list.size()>0){
			this.updateTjrw(nf, yf,ri,rwlb);
		}else{
			//添加统计任务
			Tjrw tj = new Tjrw();
			tj.setNf(nf);
			tj.setYf(yf);
			tj.setRi(ri);
			tj.setRwlb(rwlb);
			if("sfjc".equals(rwlb)){
				tj.setRwmc("收发结存任务");
			}
			if("sfzj".equals(rwlb)){
				tj.setRwmc("收发资金任务");
			}
			if("fytj".equals(rwlb)){
				tj.setRwmc("物资费用统计任务");
			}
			tj.setZt("0");
			tj.setBz("准备统计:");
			tj.setCjsj(new Date());
			this.save(tj);
		}
	}
	/**
	 * 修改一个统计任务
	 * @param nf	任务年份
	 * @param yf	任务月份
	 * @param ri	任务日期
	 * @param rwlb	任务类别
	 */
	public void updateTjrw(String nf,String yf,String ri,String rwlb){
		List list = this.find("from Tjrw t where t.nf = '"+nf+"' and yf = '"+yf+"' and t.rwlb = '"+rwlb+"'");
		Tjrw	tj =  (Tjrw)list.get(0);
		tj.setCjsj(new Date());
		tj.setStartDt(new Date());
		tj.setZt("0");
		tj.setBz("准备统计:");
		if(StringUtils.isNotEmpty(ri)){
			tj.setRi(ri);
		}
		this.save(tj);
	}
	/**
	 * 定时查看是否有新建任务
	 */
	public void  tjTimingJob(){
		//System.out.println("来了");
		try{
			List<Tjrw> tjrwList = this.find("from Tjrw t where t.zt = '0'");
			for(Tjrw tjrw : tjrwList){
				//资金收发报表统计
				if("sfzj".equals(tjrw.getRwlb())){
					//任务开始时间
					tjrw.setStartDt(new Date());
					tjrw.setZt("1");
					this.save(tjrw);
					this.flush();
					
					sfzjManager.addSfzjtj(this.getUpTjrw("sfzj"),tjrw);
					
					tjrw.setZt("2");
					tjrw.setBz("统计完成");
					//任务结束时间
					tjrw.setEndDt(new Date());
					this.save(tjrw);
					//System.out.println("收发资金统计完毕");
				}
				//收发结存报表统计
				if("sfjc".equals(tjrw.getRwlb())){
					//任务开始时间
					tjrw.setStartDt(new Date());
					tjrw.setZt("1");
					this.save(tjrw);
					this.flush();
					
					sfjcManager.addSfjc(this.getUpTjrw("sfjc"),tjrw);
					tjrw.setZt("2");
					tjrw.setBz("统计完成");
					tjrw.setEndDt(new Date());
					this.save(tjrw);
				//	System.out.println("收发结存统计完毕");
				}
				//费用统计报表
				if("fytj".equals(tjrw.getRwlb())){
				//	System.out.println("收发费用统计开始");
					//任务开始时间
					tjrw.setStartDt(new Date());
					tjrw.setZt("1");
					this.flush();
					
					fytjManager.addFytj(this.getUpTjrw("sfjc"),tjrw);
					tjrw.setZt("2");
					tjrw.setBz("统计完成");
					//任务结束时间
					tjrw.setEndDt(new Date());
					this.save(tjrw);
				//	System.out.println("收发费用统计完毕");
				}
				//库存统计
				if("kctj".equals(tjrw.getRwlb())){
					//任务开始时间
					tjrw.setStartDt(new Date());
					tjrw.setZt("1");
					this.flush();

					wzdaManager.saveYmkc();
					ckglManager.saveYmkc();
					
					tjrw.setZt("2");
					tjrw.setRwmc("月末库存");
					tjrw.setBz("统计完成");
					//任务结束时间
					tjrw.setEndDt(new Date());
					this.save(tjrw);
					
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回上一月份统计日期
	 * @param rwlb
	 * @return
	 */
	public String getUpTjrw(String rwlb){
		List<Tjrw> list =this.find("from  Tjrw t where t.zt = '2' and t.rwlb='"+rwlb+"' order by t.nf desc,t.yf desc");
		if(list.size()>0){
			Tjrw tjrw =  (Tjrw)list.get(0);
			return tjrw.getRi();
		}else{
			return "25";
		}
	}
	public void setSfjcManager(SfjcManager sfjcManager) {
		this.sfjcManager = sfjcManager;
	}
	public void setSfzjManager(SfzjManager sfzjManager) {
		this.sfzjManager = sfzjManager;
	}
	public void setFytjManager(FytjManager fytjManager) {
		this.fytjManager = fytjManager;
	}
	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}
	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}
	
}
