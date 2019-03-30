package com.ruoyi.quartz.task;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Slf4j
@Component("ryTask")
public class RyTask {
    public void ryParams(String params) {
        log.info("执行有参方法：" + params);
    }

    public void ryNoParams() {
        log.info("执行无参方法");
    }
}
