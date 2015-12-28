package com.jteap.yx.runlog.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;

/**
 * 燃炉统计处理类
 */
@SuppressWarnings({"serial","unchecked"})
public class RltjAction extends AbstractAction{
	
	//时间段 0:中班;1:夜班;2:早班*/
	private static String[][] timeSubsection = {{"8:00", "16:00"}, {"16:00", "00:00"}, {"00:00", "08:00"}};
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 返回停机前一天当前班次的id
	 * @return
	 */
	public String getOldId(){
		String formSn = request.getParameter("formSn");
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		Connection con =null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select id from "+formSn+" where zbbc='"+this.doGetShift()+"' and to_char(tianxieshijian,'YYYY-MM-DD') = to_char(sysdate-1,'YYYY-MM-DD')";
		try {
			con=dataSource.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				String json = "{success:true,id:'"+rs.getString("id")+"'}";
				this.outputJson(json);
			}else{
				this.outputJson("{success:false,msg:'"+this.doGetShift()+"'}");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return NONE;
	}
	/**
	* 验证某一时间是否在某一时间段
	* @param currTime 某一时间
	* @param timeSlot 某一时间段
	* @return true/false
	*/
	public boolean isShift(final long currTime, String[] timeSlot) {
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTimeInMillis(currTime);
		String[] tmpArray = timeSlot[0].split(":");
		long startTime, stopTime;
		tempCalendar.clear(Calendar.HOUR_OF_DAY);
		tempCalendar.clear(Calendar.MINUTE);
		tempCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArray[0]));
		tempCalendar.set(Calendar.MINUTE, Integer.parseInt(tmpArray[1]));
		startTime = tempCalendar.getTimeInMillis();
		tmpArray = timeSlot[1].split(":");
		int stopHour = Integer.parseInt(tmpArray[0]), stopMinute = Integer.parseInt(tmpArray[1]);
		if (stopHour == 0) {
		tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		tempCalendar.clear(Calendar.HOUR_OF_DAY);
		tempCalendar.clear(Calendar.MINUTE);
		tempCalendar.set(Calendar.HOUR_OF_DAY, stopHour);
		tempCalendar.set(Calendar.MINUTE, stopMinute);
		stopTime = tempCalendar.getTimeInMillis();
		return ((startTime < currTime && currTime <= stopTime) ? true : false);
	}
	/**
	* 班次计算 获取上一个班次
	*  
	* @return  
	*/
	public String doGetShift() {
		String result = "";
		Calendar currCalen = Calendar.getInstance();
		long currTime = currCalen.getTimeInMillis();
		if (isShift(currTime, timeSubsection[2])) {
			result = "中班";
		} else if (isShift(currTime, timeSubsection[0])) {
			result = "夜班";
		} else if (isShift(currTime, timeSubsection[1])) {
			result = "白班";
		}
		return result;
	}

}
