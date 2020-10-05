package com.batch.multidb.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoopDecider implements JobExecutionDecider {

    public static final String COMPLETED = "COMPLETED";
    public static final String CONTINUE = "CONTINUE";

    @Value("${shard.db.count}")
    private int shardingCount;

    private int curruntCount = 0;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        if (curruntCount >= shardingCount - 1) {
            return new FlowExecutionStatus(COMPLETED);
        } else {
            curruntCount++;
            return new FlowExecutionStatus(CONTINUE);
        }

    }

    public int getCurruntCount() {
        return curruntCount;
    }
}
