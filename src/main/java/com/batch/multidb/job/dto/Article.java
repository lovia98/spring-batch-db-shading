package com.batch.multidb.job.dto;

import com.batch.multidb.job.dao.ShardingDbDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Article {

    private int id;
    private String title;
    private String content;

    private int shardNumber;
    private String shardDbName;

    public Article(int shardNumber) {
        this.shardNumber = shardNumber;
    }

    public String getShardDbName() {
        return ShardingDbDao.dbName(this.shardNumber);
    }
}
