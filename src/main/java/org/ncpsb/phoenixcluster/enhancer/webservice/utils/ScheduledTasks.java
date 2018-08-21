package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.ncpsb.phoenixcluster.enhancer.webservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    MailService mailService;
//    @Scheduled(fixedRate = 10*60*1000)//10Min
    @Scheduled(fixedRate = 60*1000)//60s
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        mailService.checkAnalysisToSendEmail();
    }
}
