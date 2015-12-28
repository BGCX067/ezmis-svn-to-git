
package com.jteap.yx.aqyxfx.web;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.aqyxfx.manager.FxbManager;
import com.jteap.yx.aqyxfx.model.Zb;


@SuppressWarnings({"serial","unchecked"})
public class FxbAction extends AbstractAction {
    private FxbManager fxbManager;
    @Override
    public HibernateEntityDao getManager() {
        return null;
    }
    
    public String showFjhJclDataAction() throws Exception{
        String tableName=request.getParameter("tableName");
        String ksrq=request.getParameter("ksrq");
        String jsrq=request.getParameter("jsrq");
        List<Zb> zbList=fxbManager.showFjhJclDataByRq(tableName, ksrq,jsrq);
        String json=JSONUtil.listToJson(zbList,this.listJsonProperties());
        outputJson("{success:true,data:["+json+"]}");
        return NONE;
    }
    
    @Override
    public String showListAction() throws Exception {
        // 每页大小
        String limit = request.getParameter("limit");
        String formSn= request.getParameter("formSn");
        String sql="select * from "+formSn;
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";
            sql += " order by jsrq desc";
        // 开始分页查询
        Page page = this.fxbManager.pagedQueryTableData(sql, Integer.parseInt(start), Integer.parseInt(limit));
        List<Map> obj = (List<Map>) page.getResult();
        
        String json=JSONUtil.listToJson(obj);
        long totalCount=page.getTotalCount();
        StringBuffer dataBlock = new StringBuffer();
        dataBlock.append("{totalCount:'" + totalCount + "',list:"
                + json + "}");
        this.outputJson(dataBlock.toString());
        return NONE;
    }

    @Override
    public String[] listJsonProperties() {
        return new String[]{"_1_cs","_1_sj","_1_jcl","_2_cs","_2_sj","_2_jcl","_3_cs","_3_sj","_3_jcl","_4_cs","_4_sj","_4_jcl"};
    }

    @Override
    public String[] updateJsonProperties() {
        return null;
    }

    public FxbManager getFxbManager()
    {
        return fxbManager;
    }

    public void setFxbManager(FxbManager fxbManager)
    {
        this.fxbManager = fxbManager;
    }



}
