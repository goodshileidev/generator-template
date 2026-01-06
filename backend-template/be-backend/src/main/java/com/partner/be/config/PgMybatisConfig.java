package com.partner.be.config;

import com.partner.be.common.db.SqlOutInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@Primary
@AutoConfigureOrder(Integer.MAX_VALUE)
@MapperScan(basePackages = {
        "com.partner.be.backend.common.dao",
        "com.partner.be.backend.ui.dao",
        "com.partner.be.backend.test.dao",
        "com.partner.be.backend.demand.dao",
        "com.partner.be.backend.report.dao",
        "com.partner.be.backend.program.dao",
        "com.partner.be.backend.data.dao",
        "com.partner.be.backend.system.dao",
        "com.partner.be.backend.management.dao",
        "com.partner.be.postgres.system.dao",
        "com.partner.helper.backend.common.dao"
},
        sqlSessionFactoryRef = "pgSqlSessionFactory")
public class PgMybatisConfig {

    @Autowired
    SqlOutInterceptor sqlOutInterceptor;


    @Bean(name = "pgDataSource")
    @ConfigurationProperties(prefix = "pg.be")
    DataSource datasource() {
        return new HikariDataSource();
    }

    @Bean(name = "pgTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManagerDp(@Qualifier("pgDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "pgSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBeanDp(@Qualifier("pgDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setPlugins(sqlOutInterceptor); //加入完整sql输出log
        factory.setDataSource(dataSource);
        factory.setTypeAliasesPackage("com.partner.be.backend.*.dao");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources("classpath:mybatis/xml/*/*.xml"));
        return factory.getObject();
    }
}
