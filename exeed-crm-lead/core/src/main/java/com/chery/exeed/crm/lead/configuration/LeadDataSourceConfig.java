package com.chery.exeed.crm.lead.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.Properties;


@Configuration(value = "leadDataSourceConfig")
public class LeadDataSourceConfig {
    @Bean(name = "leadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.lead")
    public DataSource setDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "leadTransactionManager")
    public DataSourceTransactionManager setTransactionManager(@Qualifier("leadDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "leadSqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("leadDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/chery/exeed/crm/lead/mapper/*.xml"));
        PageInterceptor pi = new PageInterceptor();
        Properties p = new Properties();
        p.setProperty("helperDialect", String.valueOf("mysql"));
        pi.setProperties(p);
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = pi;
        bean.setPlugins(plugins);
        return bean.getObject();
    }

    @Bean(name = "leadSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("leadSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "leadMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("leadSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.chery.exeed.crm.lead");
        mapperScannerConfigurer.setAnnotationClass(Class.forName("org.apache.ibatis.annotations.Mapper").asSubclass(Annotation.class));
        return mapperScannerConfigurer;
    }


}