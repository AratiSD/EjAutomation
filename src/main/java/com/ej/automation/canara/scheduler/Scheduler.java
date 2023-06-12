package com.ej.automation.canara.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ej.automation.canara.service.ICanaraService;

@Component
@Profile("canara")
public class Scheduler {
	
	public static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	ICanaraService canaraService;
	
	/*
	 * @Scheduled(cron = "${cron.schedule.splitFiles}") public void
	 * splitFilesScheduler(){ logger.info("Inside [Scheduler:splitFilesScheduler]");
	 * String sbiResult = canaraService.splitFile(); logger.info(sbiResult); }
	 */
	
	@Scheduled(cron="${cron.schedule.renameFiles}")
	public void renameFilesScheduler() {
		logger.info("Inside [Scheduler:renameFilesScheduler]");
		String result = canaraService.renameFile();
		logger.info(result);
	}
	

}
