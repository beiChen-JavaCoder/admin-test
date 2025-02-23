//package com.admin.task;
//
//import com.admin.config.NotifyThread;
//import com.admin.domain.vo.NotifyVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.nio.file.attribute.FileTime;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Component
//@Slf4j
//@EnableScheduling
//public class ScheduledTask {
//
//    public static Queue<NotifyVo> queue = new LinkedList<>();
//
//    @Resource(name = )
//    private ThreadPoolExecutor threadPoolExecutor;
//
//
//    /**
//     * 每秒钟执行一次
//     */
//    @Scheduled(cron = "0/1 * *  * * ?")
//    public void task1() {
//        log.info("task1");
//        for(int i=0; i<5; i++) {
//            NotifyVo notifyVo = queue.poll();
//            if(null != notifyVo) {
//                threadPoolExecutor.submit(new NotifyThread(notifyVo.getNotifyUrl(), notifyVo.getParamMap()));
//            }
//        }
//    }
//}
