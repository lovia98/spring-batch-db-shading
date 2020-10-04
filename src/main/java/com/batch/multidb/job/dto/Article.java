package com.batch.multidb.job.dto;

import com.batch.multidb.job.dao.ShardingDbDao;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Article {

    private int shardNumber;
    private String shardDbName;

    private int id;
    private String title;
    private String content;

    public Article(int shardNumber) {
        this.shardNumber = shardNumber;
    }

    public String getShardDbName() {
        return ShardingDbDao.dbName(this.shardNumber);
    }
}
