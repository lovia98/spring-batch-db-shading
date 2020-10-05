package com.batch.multidb.dto;

import com.batch.multidb.dao.ShardingDbDao;
import com.batch.multidb.vo.Paging;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Article extends Paging {

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
