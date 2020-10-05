# Spring Batch Sharding DataSource(multi datasource) 설정 Sample

## 개요
 * 샤딩된 디비(multi database)에서 데이터를 얻어오는 동작이 필요한 경우 spring batch 설정
 
## 설정
 1. db 구조
    * 게시글을 디비를 샤딩 하여 총 3개의 디비에 동일한 스키마 생성(/sql/shard-scheme.sql 참고)
    * spring batch 메타 정보가 저장될 메인 DB 생성 (/sql/main-batch-scheme.sql 참고)  
    
 2. mybatis config
    * 메인 디비 datasource, sqlSessionFactory, sqlSessionTemplate 설정
    * 샤딩 디비는 성능을 위해 물리적 분리가 필요해지기 전까지는 모든 디비설정에 대해서 datasource를 생성하면  
     메모리 낭비가 될 수 있음으로 논리 디비 샤딩이라고 가정함.
    * 따라서 샤딩 디비의 datasource 는 하나만 빈에 등록, sqlTemplate만 샤딩 갯수에 맞춰 Map<String,sqlTemplate> 으로 빈등록
    * 샤딩 디비 select 작동을 위한 샘플로 transction 설정은 생략 하였음.
    
 3. job config
    * ItemReader 커스텀 하여 shard번호에 따라 빈으로 등록해 놓은 sqlTemplate Map 에서 샤딩디비에 맞는 sqlSession을 가져와 실행  
      (MybatigReader 소스를 참고)
    * LoopDecider 를 통해 샤딩된 디비 갯수 만큼 step 반복