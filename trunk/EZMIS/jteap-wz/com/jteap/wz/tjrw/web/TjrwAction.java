package com.jteap.wz.tjrw.web;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjrw.manager.TjrwManager;
/**
 * 统计任务action
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class TjrwAction extends AbstractAction{
	//统计年月处理类
	private TjnyManager tjnyManager;
	//统计任务处理类
	private TjrwManager tjrwManager;
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return tjrwManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[] {"id","rwmc","nf","yf","cjsj","time","startStr","endStr","zt","bz",};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		this.isUseQueryCache=false;
		String rwmc = request.getParameter("rwmc");
		String rwzt = request.getParameter("rwzt");
		String nf = request.getParameter("nf");
		String yf = request.getParameter("yf");
		//任务名称
		if(StringUtil.isNotEmpty(rwmc)){
			 HqlUtil.addCondition(hql,"rwmc",rwmc,HqlUtil.LOGIC_AND ,HqlUtil.TYPE_STRING_LIKE);
		}
		//任务状态
		if(StringUtil.isNotEmpty(rwzt)){
			 HqlUtil.addCondition(hql, "zt",rwzt);
		}
		//任务年份
		if(StringUtil.isNotEmpty(nf)){
			 HqlUtil.addCondition(hql, "nf",nf);
		}
		//任务月份
		if(StringUtil.isNotEmpty(yf)){
			HqlUtil.addCondition(hql, "yf",yf);
			
		}
		//按年倒序
		HqlUtil.addOrder(hql, "startDt", "desc");
	}
	/**
	 * 添加一个统计任务
	 * @return
	 * @throws Exception 
	 */
	public String addTjrwAction() throws Exception{
		
		try {
			String nf = request.getParameter("nf");
			String yf = request.getParameter("yf");
			String ri = request.getParameter("ri");
			String rwlb = request.getParameter("rwlb");
			List rwList = tjrwManager.find("from Tjrw t where t.nf= '"+nf+"' and t.yf = '"+yf+"' and t.zt<>2 and t.rwlb = '"+rwlb+"'");
			if(rwList.size()>1){
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = getOut();
				out.print("<script>alert(\"该任务正在进行，请在统计任务页面中查看！\");window.close()</script>");
				return NONE;
			}else{
				List list = tjnyManager.find("from Tjny t where t.ny = '"+nf+"年"+yf+"月' and t.bblb = '"+rwlb+"'");
				if(list.size()>0){
					this.outputJson("{success:true,msg:'y'}");
				}else{
//					System.out.println(ri);
					//年末统计
					if(StringUtils.isNotEmpty(ri)){
						Calendar date = Calendar.getInstance();
//						System.out.println(date.get(Calendar.YEAR));
						tjrwManager.addTjrw(date.get(Calendar.YEAR)+"", "12",ri, rwlb);
					}else{
						tjrwManager.addTjrw(nf, yf,"25", rwlb);
					}
					this.outputJson("{success:true}");
				}
			}
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 *修改一个统计任务
	 * @return
	 * @throws Exception 
	 */
	public String updateTjrwAction() throws Exception{
		
		try {
			Calendar date = Calendar.getInstance();
			String nf = request.getParameter("nf");
			String yf = request.getParameter("yf");
			String rwlb = request.getParameter("rwlb");
			List rwList = tjrwManager.find("from Tjrw t where t.nf= '"+nf+"' and t.yf = '"+yf+"' and t.zt<>2 and t.rwlb = '"+rwlb+"'");
			if(rwList.size()>1){
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = getOut();
				out.print("<script>alert(\"该任务正在进行，请在统计任务页面中查看！\");window.close()</script>");
				return NONE;
			}else{
				List list = tjnyManager.find("from Tjny t where t.ny = '"+nf+"年"+yf+"月' and t.bblb = '"+rwlb+"'");
				for(int i =0;i<list.size();i++){
					tjnyManager.remove(list.get(i));
				}
				tjrwManager.updateTjrw(nf, yf,"",rwlb);
				this.outputJson("{success:true}");
			}
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public void setTjrwManager(TjrwManager tjrwManager) {
		this.tjrwManager = tjrwManager;
	}

	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}

}
