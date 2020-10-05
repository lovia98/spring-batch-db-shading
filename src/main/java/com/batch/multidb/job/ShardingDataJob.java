package com.batch.multidb.job;

import com.batch.multidb.job.dao.ShardingDbDao;
import com.batch.multidb.job.dto.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ShardingDataJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ShardingDbDao shardingDbDao;
    private final LoopDecider decider;

    private static final int chunkSize = 1000;

    @Bean
    public Job shardingSampleJob() throws Exception {

        FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("shardLootFlow");

        Flow flow = flowBuilder
                .start(shardingSampleStep())
                .next(decider)
                .on(LoopDecider.CONTINUE)
                .to(shardingSampleStep())
                .from(decider)
                .on(LoopDecider.COMPLETED)
                .end()
                .build();

        return jobBuilderFactory.get("shardingSampleJob")
                .incrementer(new RunIdIncrementer()) //jobId 발급
                .start(flow)
                .end()
                .build();
    }

    @Bean
    public Step startSampleStep() throws Exception {
        return stepBuilderFactory.get("startShardingSampleStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> start Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step shardingSampleStep() throws Exception {
        return stepBuilderFactory.get("shardingSampleStep")
                .<Article, Article>chunk(chunkSize)
                .reader(shardingItemReader(null))
                .writer(shardingItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ItemReader<Article> shardingItemReader(LoopDecider decider) {
        return CustomPagingItemReader.builder()
                .decider(decider)
                .shardingDbDao(shardingDbDao)
                .queryId("sharding.articles.selectArticle")
                .build();
    }

    private ItemWriter<Article> shardingItemWriter() {
        return list -> {
            for (Article article : list) {
                log.info("<<<<<<<<<<<<<< current article : {} ", article);
            }
        };
    }
}
