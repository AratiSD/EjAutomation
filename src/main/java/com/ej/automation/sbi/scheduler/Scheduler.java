package com.ej.automation.sbi.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ej.automation.sbi.service.ISbiService;

@Component
@Profile("sbi")
public class Scheduler {
	
	public static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	ISbiService sbiService;
	
	@Scheduled(cron = "${cron.expression}")
	public void sbiScheduler(){
		logger.info("Inside [Scheduler:sbiScheduler]");
		String sbiResult = sbiService.splitFile();
		logger.info(sbiResult);
	}

}
