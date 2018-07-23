package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by baimi on 2017/10/11.
 */
public class PhoenixDataSource {
    @Autowired
    private Environment env;
    @Bean(name = "phoenixJdbcDataSource")
    @Qualifier("phoenixJdbcDataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("phoenix.url"));
        dataSource.setDriverClassName(env.getProperty("phoenix.driver-class-name"));
        dataSource.setUsername(env.getProperty("phoenix.username"));
        dataSource.setPassword(env.getProperty("phoenix.password"));
        dataSource.setDefaultAutoCommit(Boolean.valueOf(env.getProperty("phoenix.default-auto-commit")));
        return dataSource;
    }
    @Bean(name = "phoenixJdbcTemplate")
    public JdbcTemplate phoenixJdbcTemplate(@Qualifier("phoenixJdbcDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
