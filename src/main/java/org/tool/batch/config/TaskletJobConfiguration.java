package org.tool.batch.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.tool.batch.tasklet.DemoTasklet1;
import org.tool.batch.tasklet.DemoTasklet2;


@Configuration
public class TaskletJobConfiguration {
    @Autowired
    DemoTasklet1 demoTasklet;

    @Autowired
    DemoTasklet2 demoTasklet2;

    @Bean
    public Step myStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep1", jobRepository)
                .tasklet(demoTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step myStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep2", jobRepository)
                .tasklet(demoTasklet2, transactionManager)
                .build();
    }


    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep1, Step myStep2) {
        return new JobBuilder("myJob", jobRepository)
                .start(myStep1)
                .next(myStep2)
                .build();
    }

    @Component
    public class JobRunner implements CommandLineRunner {

        private final JobLauncher jobLauncher;
        private final Job myJob;

        public JobRunner(JobLauncher jobLauncher, Job myJob) {
            this.jobLauncher = jobLauncher;
            this.myJob = myJob;
        }

        @Override
        public void run(String... args) throws Exception {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // 添加一个唯一参数
                    .toJobParameters();
            jobLauncher.run(myJob, jobParameters);
        }
    }
}