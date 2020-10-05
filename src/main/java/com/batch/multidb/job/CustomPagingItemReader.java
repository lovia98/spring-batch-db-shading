package com.batch.multidb.job;

import com.batch.multidb.dao.ShardingDbDao;
import com.batch.multidb.dto.Article;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Setter
@Builder
@RequiredArgsConstructor
public class CustomPagingItemReader extends AbstractPagingItemReader {

    private final LoopDecider decider;
    private final ShardingDbDao shardingDbDao;
    private final String queryId;

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        Article parameter = new Article(decider.getCurruntCount());
        parameter.setPageSize(getPageSize());
        parameter.setSkipRows(getPage() * getPageSize());

        log.info("실행 디비 샤드 넘버 : {}, dbName: {}, startIndex: {}, pageSize: {}",
                parameter.getShardNumber(), parameter.getShardDbName(), parameter.getSkipRows(), parameter.getPageSize());
        List<Object> objectList = shardingDbDao.getSqlSession(decider.getCurruntCount()).selectList(queryId, parameter);

        results.addAll(objectList);
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
        // Not Implemented
    }
}
