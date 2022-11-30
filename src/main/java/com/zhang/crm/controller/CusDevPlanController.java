package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.query.CusDevPlanQuery;
import com.zhang.crm.service.CusDevPlanService;
import com.zhang.crm.service.SaleChanceService;
import com.zhang.crm.vo.CusDevPlan;
import com.zhang.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户开发计划控制层
 */
@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Autowired
    private CusDevPlanService cusDevPlanService;

    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 页面入口
     */

    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 打开计划项开发与详情页面
     *
     * @param id
     * @return
     */
    @RequiredPermission(code = "102001")
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request) {
        //通过ID查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域中
        request.setAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 查询营销机会数据开发详情
     *
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        return cusDevPlanService.queryCusDevPlanParams(cusDevPlanQuery);
    }

    /**
     * 打开客户开发计划添加/修改页面
     *
     * @param sId
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sId, Integer id, HttpServletRequest request) {
        //将营销机会ID设置刀请求域中，给计划项页面获取
        request.setAttribute("sId", sId);

        request.setAttribute("cusDevPlan", cusDevPlanService.selectByPrimaryKey(id));
        return "cusDevPlan/add_update";
    }

    /**
     * 添加客户开发计划
     *
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("添加用户开发计划成功");
        return resultInfo;
    }


    /**
     * 修改客户开发计划
     *
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("客户开发计划修改成功");
        return resultInfo;
    }

    /**
     * 删除客户开发计划
     *
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.deleteCusDevPlan(cusDevPlan);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("客户开发计划删除成功");
        return resultInfo;
    }
}
