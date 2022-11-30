package com.zhang.crm.task;
import com.zhang.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务的执行
 */
@Component
public class JobTask {
    @Autowired
    private CustomerService customerService;

    //cron表达式
    //每两秒执行一次
    //@Scheduled(cron = "0/2 * * * * ?")

    //从六月开始，每个月执行一次
    @Scheduled(cron = "* * * * 6/1 ? ")
    public void job() {
        //调用需要被执行的方法
        //开始执行定时任务
        System.out.println("开始执行定时器任务");
        customerService.updateCustomerState();
        System.out.println("定时器任务执行完成");
    }
}
