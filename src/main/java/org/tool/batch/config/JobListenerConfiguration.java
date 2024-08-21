package org.tool.batch.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobListenerConfiguration implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job实行开始");;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job实行结束");;
    }
}
