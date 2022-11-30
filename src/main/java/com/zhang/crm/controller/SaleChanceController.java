package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.enums.StateStatus;
import com.zhang.crm.query.SaleChanceQuery;
import com.zhang.crm.service.SaleChanceService;
import com.zhang.crm.utils.CookieUtil;
import com.zhang.crm.utils.LoginUserUtil;
import com.zhang.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 营销机会控制层
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询（多条件的分页查询）
     * 如果flag值不为空，且值为1，表示当前查询的是客户开发计划
     * 否则查询营销机会数据
     *
     * @param saleChanceQuery
     * @return
     */
    @RequiredPermission(code = "101001")
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag,
                                                       HttpServletRequest request) {
        //判断flag的值
        if (flag != null && flag == 1) {
            //查询的是客户开发计划
            //设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());//客户开发计划，表示已经分配了
            //设置指派人（当前用户的ID）
            //从cookie中获取当前用户登录的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 返回视图，进入营销机会管理的页面
     *
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }


    /**
     * 添加营销机会操作
     *
     * @param saleChance
     * @return
     */

    @ResponseBody
    @PostMapping("add")
    @RequiredPermission(code = "101002")
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        //拿到当前登录用户设置创建人,从cookie中拿
        String userName = CookieUtil.getCookieValue(request, "userName");
        //设置用户名到saleChance中
        saleChance.setCreateMan(userName);
        //调用Service方法
        saleChanceService.addSaleChance(saleChance);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("营销机会数据添加成功");
        return resultInfo;
    }

    /**
     * 进入添加或者修改营销机会数据页面
     */

    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        if (saleChanceId != null) {
            //通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到请求域中
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 修改营销机会操作
     *
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    @RequiredPermission(code = "101004")
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        saleChanceService.updateSaleChance(saleChance);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("营销机会数据修改成功");
        return resultInfo;
    }


    /**
     * 批量删除营销机会
     *
     * @param ids
     * @return
     */

    @RequestMapping("delete")
    @ResponseBody
    @RequiredPermission(code = "101003")
    public ResultInfo deleteSaleChance(Integer[] ids) {
        saleChanceService.deleteSaleChance(ids);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("删除营销机会成功");
        return resultInfo;
    }


    /**
     * 更新营销机会的开发状态
     *
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id, devResult);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("营销机会开发状态更新成功");
        return resultInfo;
    }
}
