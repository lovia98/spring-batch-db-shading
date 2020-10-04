package com.batch.multidb.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertiesType {

    @Getter
    @Setter
    @ToString
    public static class HikariProperty {
        private Long idleTimeout;
        private Integer minimumIdle;
        private Integer maximumPoolSize;
        private String poolName;
        private Long connectionTimeout;
        private Integer validationTimeout;
    }

    @Getter
    @Setter
    @ToString
    public static class DbProperty {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }
}
