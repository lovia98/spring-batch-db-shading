package com.batch.multidb.job;

import com.batch.multidb.job.dao.ShardingDbDao;
import com.batch.multidb.job.dto.Article;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Setter
@Builder
@RequiredArgsConstructor
public class CustomPagingItemReader extends AbstractPagingItemReader {

    private final int shardNumber;
    private final ShardingDbDao shardingDbDao;
    private final String queryId;
    private final Article parameter;

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
        results.addAll(shardingDbDao.getSqlSession(shardNumber).selectList(queryId, parameter));
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
        // Not Implemented
    }
}
