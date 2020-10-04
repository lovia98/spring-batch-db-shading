package com.batch.multidb.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableTransactionManagement
public class MybatisConfig {

    private final PropertiesType.HikariProperty hikariProperty;
    private final PropertiesType.DbProperty mainDb;
    private final List<PropertiesType.DbProperty> shardDbs;

    public MybatisConfig(@Qualifier("hikariProperty") PropertiesType.HikariProperty hikariProperty,
                         @Qualifier("mainProperty") PropertiesType.DbProperty mainDb,
                         @Qualifier("shardProperties") List<PropertiesType.DbProperty> shardDbs) {

        this.hikariProperty = hikariProperty;
        this.mainDb = mainDb;
        this.shardDbs = shardDbs;
    }

    /**
     * 샤딩 하지 않은 메인 디비 datasource
     * - spring batch meta table은 여기서 생성해서 씀
     */
    @Primary
    @Bean
    public HikariDataSource mainDatasource() {
        HikariDataSource db = getHikariDataSource();

        db.setJdbcUrl(mainDb.getUrl());
        db.setUsername(mainDb.getUsername());
        db.setPassword(mainDb.getPassword());

        return db;
    }

    /**
     * 샤딩 하지 않은 메인 디비 sqlSessionFactory
     */
    @Primary
    @Bean
    public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDatasource") DataSource mainDataSources,
                                                   ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mainDataSources);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/main/**.xml"));

        org.apache.ibatis.session.Configuration sessionConfiguration = new org.apache.ibatis.session.Configuration();
        sessionConfiguration.setMapUnderscoreToCamelCase(true);

        sqlSessionFactoryBean.setConfiguration(sessionConfiguration);

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 샤딩 하지 않은 메인 디비 sqlSessionTemplate
     */
    @Primary
    @Bean
    public SqlSessionTemplate mainSqlSessionTemplate(@Qualifier("mainSqlSessionFactory") SqlSessionFactory mainSqlSessionFactory) {
        return new SqlSessionTemplate(mainSqlSessionFactory);
    }

    /**
     * 샤딩 디비 dataSource
     * - 샤딩된 디비가 논리 디비로 분리 되어 있다면 메모리 낭비가 없도록 하나의 dataSource만 빈으로 등록
     */
    @Bean
    public HikariDataSource shardingDataSources() {

        HikariDataSource db = getHikariDataSource();

        if (shardDbs.size() > 0) {
            db.setJdbcUrl(shardDbs.get(0).getUrl());
            db.setUsername(shardDbs.get(0).getUsername());
            db.setPassword(shardDbs.get(0).getPassword());
        }

//        Map<String, HikariDataSource> shardingDatasourceMap = new HashMap<>();

//        for (int i = 0; i < shardDbs.size(); i++) {
//            db.setJdbcUrl(shardDbs.get(i).getUrl());
//            db.setUsername(shardDbs.get(i).getUsername());
//            db.setPassword(shardDbs.get(i).getPassword());
//            shardingDatasourceMap.put("shard-db-" + i, db);
//        }

        return db;
    }

    /**
     * 샤딩 디비 sqlSessionTemplate Bean
     *
     * @param shardingDataSources
     * @param applicationContext
     * @return
     * @throws IOException
     */
    @Bean
    public Map<String, SqlSessionTemplate> shardingSqlSession(@Qualifier("shardingDataSources") HikariDataSource shardingDataSources,
                                                              ApplicationContext applicationContext) throws Exception {

        //논리 디비로 샤딩 되어 있다는 가정으로 하나의 dataSource만 매핑하는 sqlSessionFactoryBean
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(shardingDataSources);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/shard/**.xml"));
        org.apache.ibatis.session.Configuration sessionConfiguration = new org.apache.ibatis.session.Configuration();
        sessionConfiguration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(sessionConfiguration);
        SqlSessionFactory shardingSqlSessionFactory = sqlSessionFactoryBean.getObject();

        Map<String, SqlSessionTemplate> sqlSessionTemplateMap = new HashMap<>();

        for (int i = 0; i < shardDbs.size(); i++) {
            sqlSessionTemplateMap.put("shard-sqlSession-" + i, new SqlSessionTemplate(shardingSqlSessionFactory));
        }

        return sqlSessionTemplateMap;
    }


    /**
     * 공통 dataousrce 설정값 set
     *
     * @return
     */
    private HikariDataSource getHikariDataSource() {

        HikariDataSource db = new HikariDataSource();
        db.setConnectionTimeout(hikariProperty.getConnectionTimeout());
        db.setIdleTimeout(hikariProperty.getIdleTimeout());
        db.setMaximumPoolSize(hikariProperty.getMaximumPoolSize());
        db.setMinimumIdle(hikariProperty.getMinimumIdle());
        db.setPoolName(hikariProperty.getPoolName());
        db.setValidationTimeout(hikariProperty.getValidationTimeout());

        return db;
    }

}
