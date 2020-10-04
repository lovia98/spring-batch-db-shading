package com.batch.multidb.job;

import com.batch.multidb.job.dao.ShardingDbDao;
import com.batch.multidb.job.dto.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ShardingDataJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ShardingDbDao shardingDbDao;

    private static final int chunkSize = 1000;

    @Bean
    public Job shardingSampleJob() throws Exception {
        return jobBuilderFactory.get("shardingSampleJob")
                .start(shardingSampleStep())
                .build();
    }

    @Bean
    @JobScope
    public Step shardingSampleStep() throws Exception {
        return stepBuilderFactory.get("shardingSampleStep")
                .<Article, Article>chunk(chunkSize)
                .reader(shardingItemReader())
                .writer(shardingItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Article> shardingItemReader() {

        int shardNumber = 0;
        Article parameter = new Article(shardNumber);

        return CustomPagingItemReader.builder()
                .shardNumber(shardNumber)
                .shardingDbDao(shardingDbDao)
                .queryId("sharding.articles.selectArticle")
                .parameter(parameter)
                .build();
    }

    private ItemWriter<Article> shardingItemWriter() {
        return list -> {
            for (Article article: list) {
                log.info("<<<<<<<<<<<<<< current article : {} ", article);
            }
        };
    }
}
