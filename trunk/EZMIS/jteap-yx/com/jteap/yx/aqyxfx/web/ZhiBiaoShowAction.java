package com.jteap.yx.aqyxfx.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.aqyxfx.manager.ZhiBiaoShowManager;
import com.jteap.yx.aqyxfx.model.Jznhzb;
import com.jteap.yx.aqyxfx.model.Kkxzb;
import com.jteap.yx.aqyxfx.model.Zb;


public class ZhiBiaoShowAction extends AbstractAction
{
    
    private static final long serialVersionUID = 7892073999004312282L;
    private ZhiBiaoShowManager zhiBiaoShowManager;

    @Override
    public HibernateEntityDao<?> getManager()
    {
        return zhiBiaoShowManager;
    }

    /**
     * 根据日期查找指标数据 作者:童贝 时间:Feb 23, 2011
     * 
     * @return
     * @throws Exception
     */
    public String findZbsjAction() throws Exception
    {
        String tableName = request.getParameter("tableName");
        String cxrq = request.getParameter("cxrq");
        String code = request.getParameter("code");
        // String formName=request.getParameter("formName");
        Zb zb = null;
        String[] jsonStr = null;
        if (Zb.ZB_YQ_NH_YUE.equals(code) || Zb.ZB_EQ_NH_YUE.equals(code))
        {
            zb = new Jznhzb();
            jsonStr = new String[]
            { "id", "fdl", "gdmh", "zhcydl", "sccydl", "yh", "fdl2", "gdmh2",
                    "zhcydl2", "sccydl2", "yh2", "fdl3", "gdmh3", "zhcydl3",
                    "sccydl3", "yh3", "fdl4", "gdmh4", "zhcydl4", "sccydl4",
                    "yh4", "fdl5", "gdmh5", "zhcydl5", "sccydl5", "yh5",
                    "fdl6", "gdmh6", "zhcydl6", "sccydl6", "yh6"};
        }
        else if (Zb.ZB_YQ_KKX_YUE.equals(code) || Zb.ZB_EQ_KKX_YUE.equals(code))
        {
            zb = new Kkxzb();
            jsonStr = new String[]
            { "id", "fdl", "yxxs", "lyxs", "ftcs", "ftxss", "dxkyxs","dxqptyl", 
                    "fdl2", "yxxs2", "lyxs2", "ftcs2", "ftxss2", "dxkyxs2", "dxqptyl2" ,
                    "fdl3", "yxxs3", "lyxs3", "ftcs3", "ftxss3", "dxkyxs3", "dxqptyl3" ,
                    "fdl4", "yxxs4", "lyxs4", "ftcs4", "ftxss4", "dxkyxs4", "dxqptyl4" ,
                    "fdl5", "yxxs5", "lyxs5", "ftcs5", "ftxss5", "dxkyxs5", "dxqptyl5" ,
                    "fdl6", "yxxs6", "lyxs6", "ftcs6", "ftxss6", "dxkyxs6", "dxqptyl6" };
        }
        else
        {
            zb = new Zb();
            jsonStr = new String[]
            {};
        }
        // 是否存在于表单对应的表中
        String exist = this.zhiBiaoShowManager.isExData(tableName, cxrq);
        if (StringUtils.isEmpty(exist))
        {
            // this.zhiBiaoShowManager.saveZb(formName,zb,cxrq);
            this.outputJson("{success:false}");
        }
        else
        {
            // 在data表查找数据
            zb = this.zhiBiaoShowManager.findZbsjByTablenameAndRq(tableName,
                    cxrq, zb);
            String json = JSONUtil.objectToJson(zb, jsonStr);
            outputJson("{success:true,data:" + json + "}");
        }
        return NONE;
    }

    public String showListAction() throws Exception
    {
        // 每页大小
        String limit = request.getParameter("limit");
        String formSn = request.getParameter("formSn");
        String sql = "select * from " + formSn;
        if (StringUtils.isEmpty(limit))
            limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

        // 开始索引
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start))
            start = "0";
        sql += " order by cxrq desc";
        List<Map<String, Object>> list =this.zhiBiaoShowManager.findList(sql);
        // 开始分页查询
        int startIndex = Integer.parseInt(start);
        int limitIndex = Integer.parseInt(limit) + startIndex;
        if (limitIndex > list.size())
        {
            limitIndex = list.size();
        }
        List<Map<String, Object>> pageList = list.subList(startIndex,limitIndex);
        String json = JSONUtil.listToJson(pageList, new String[]{"ID","TXR","CXRQ"});
        StringBuffer sBuffer=new StringBuffer();
        sBuffer.append("{totalCount:'" + list.size() + "',list:" + json + "}");
        this.outputJson(sBuffer.toString());
        return NONE;
    }

    public ZhiBiaoShowManager getZhiBiaoShowManager()
    {
        return zhiBiaoShowManager;
    }

    public void setZhiBiaoShowManager(ZhiBiaoShowManager zhiBiaoShowManager)
    {
        this.zhiBiaoShowManager = zhiBiaoShowManager;
    }

    @Override
    public String[] listJsonProperties()
    {
        return new String[]
        { "id" };
    }

    @Override
    public String[] updateJsonProperties()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
