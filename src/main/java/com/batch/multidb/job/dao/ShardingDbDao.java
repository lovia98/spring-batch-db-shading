package com.batch.multidb.job.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShardingDbDao {

    private final Map<String, SqlSessionTemplate> sqlSession;
    public static final Map<Integer, String> shardDbNameMap = new HashMap<>();

    public ShardingDbDao(@Qualifier("shardingSqlSession") Map<String, SqlSessionTemplate> sqlSession) {
        this.sqlSession = sqlSession;

        for (int i = 0;  i < sqlSession.size(); i++) {
            shardDbNameMap.put(i, "bbs_" + i);
        }
    }

    public static String dbName(int shardNumber) {
        return shardDbNameMap.get(shardNumber);
    }

    public SqlSession getSqlSession(int shardNumber) {
        return sqlSession.get("shard-sqlSession-" + shardNumber);
    }
}
