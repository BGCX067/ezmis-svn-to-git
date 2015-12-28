package com.jteap.wz.xqjhsq.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;
import com.jteap.wz.xqjhsq.model.XqjhsqLog;

@SuppressWarnings(
{ "serial", "unchecked", "unused" })
public class XqjhsqLogManager extends HibernateEntityDao<XqjhsqLog>
{

    public void addFlowLog(Long pi_id, String taskName, String taskActor,
            String taskActorLoginName, String nextTaksActor,
            String nextTaksName, String taskResult, String remark,
            String xqjhsqid)
    {

        XqjhsqLog xqjhsqLog = new XqjhsqLog();
        xqjhsqLog.setPi_id(pi_id.toString());
        xqjhsqLog.setPorcessDate(new Date());
        xqjhsqLog.setTaskName(taskName);
        xqjhsqLog.setTaskActor(taskActor);
        xqjhsqLog.setTaskLoginName(taskActorLoginName);
        xqjhsqLog.setNextTaksActor(nextTaksActor);
        xqjhsqLog.setNextTaksName(nextTaksName);
        xqjhsqLog.setTaskResult(taskResult);
        xqjhsqLog.setRemark(remark);
        xqjhsqLog.setXqjhsqid(xqjhsqid);
        save(xqjhsqLog);
    }

    /**
     * 记录需求计划申请的操作日志
     * 
     * @param ti
     * @param recordMap
     * @param curUserName
     * @param curLoginName
     * @param xqjhsqDetailList
     * @param nextTaksName
     * @param curTaskName
     * @param nextActorNames
     * @param docid
     */
    public void addCzLog(Long t_id, HashMap recordMap, String curUserName,
            String curLoginName, List<XqjhsqDetail> xqjhsqDetailList,
            String nextTaksName, String curTaskName, String nextActorNames,
            String docid)
    {
        // 需求计划申请记录删除操作日志
        String createRek = "【新建操作】添加了:"; // 新建的操作说明
        String deleteRek = "【删除操作】删除了:"; // 删除的操作说明
        String updateRek = "【修改操作】将:"; // 修改的操作说明
        //需求计划申请记录新建操作日志
        List<Map<String, String>> createList = JSONUtil.parseList((String) recordMap.get("XJSJSZ"));
        // 需求计划修改操作日志
        List<Map<String, String>> updateList = JSONUtil.parseList((String) recordMap.get("XGSJSZ"));
        //需求计划删除操作日志
        List<Map<String, String>> delList = JSONUtil.parseList((String) recordMap.get("SCSJSZ"));
        
        // 记录新增操作
        for (Map<String, String> createMap : createList)
            createRek += ("," + createMap.get("WZMC") + "("+ createMap.get("XHGG") + ")"+ String.valueOf(createMap.get("SQSL")) + createMap.get("JLDW"));

        // 记录删除操作
        for (Map<String, String> delMap : delList){
            Iterator<Map<String,String>> i =updateList.iterator();
            while(i.hasNext()){
                Map m=i.next();
                String wzmc=(String)m.get("WZMC");
                String xhgg=(String)m.get("XHGG");
                if(wzmc.equals(delMap.get("wzmc"))&&xhgg.equals(delMap.get("xhgg"))){
                    i.remove();
                }
            }
            deleteRek += ("," + delMap.get("wzmc") + "(" + delMap.get("xhgg")+ ")" + delMap.get("sqsl") + delMap.get("jldw"));
        }
        // 记录修改操作
       for (int i=0;i<updateList.size();i++){
            XqjhsqDetail xqjhsqDetail=(XqjhsqDetail)xqjhsqDetailList.get(i);
            String[] updateObj ={ "wzmc", "xhgg", "sqsl", "remark" };
            // 对应的中文名称
            String[] updateCn ={ "物资名称", "型号规格", "申请数量", "备注" };
            try
            {
                for (int a = 0; a < updateObj.length; a++)
                {
                    String s = updateObj[a];
                    String cn = updateCn[a];
                    Object value = BeanUtils.getProperty(xqjhsqDetail,s);
                    Class clz = xqjhsqDetail.getClass();
                    String type = clz.getDeclaredField(s).getType().toString();
                    String update = updateList.get(i).get(s.toUpperCase());

                    if (type.equals("class java.lang.String"))
                    {
                        if (StringUtil.isEmpty((String) value)|| (String) value == "null"|| "null".equals((String) value))
                        {
                            value = "";
                        }
                        if (StringUtil.isEmpty(update)|| update == "null"|| "null".equals(update))
                        {
                            update = "";
                        }
                        if (!((String) value).equals(update))
                        {
                            updateRek += updateList.get(i).get("WZMC") + " "+ cn + " ('" + update + "'修改为：'" + value + "');";
                        }
                    }
                    else
                    {
                        if (type.equals("class java.lang.Double"))
                        {
                            if (!Double.valueOf(update).equals(Double.valueOf(value.toString())))
                            {
                                updateRek += updateList.get(i).get("WZMC")+ " " + cn + " ('" + update + "'修改为：'" + value + "');";
                            }
                        }
                        else
                        {
                            System.out.println("null");
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        createRek=("【新建操作】添加了:".equals(createRek)?"":createRek);
        deleteRek=("【删除操作】删除了:".equals(deleteRek)?"":deleteRek);
        updateRek=("【修改操作】将:".equals(updateRek)?"":updateRek);
        if (!(("").equals(createRek) && ("").equals(deleteRek)&& ("").equals(updateRek)))
            this.addFlowLog(t_id, curTaskName, curUserName, curLoginName,
                    nextActorNames, nextTaksName,createRek+ deleteRek+updateRek, "", docid);
        }
}
