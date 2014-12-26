
package com.odvarkajak.oslol.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.apache.commons.dbcp.*; 
//import com.mysql.jdbc.Driver;
import java.util.Properties;

import javax.sql.DataSource;


@EnableTransactionManagement
@Configuration
@Profile("default")
public class DataConfig implements DisposableBean {
    static final Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    private EmbeddedDatabase ed;

    /*@Bean(name="hsqlInMemory")
    public EmbeddedDatabase hsqlInMemory() {
        logger.debug("Enter: EmbeddedDatabase");
        if ( this.ed == null ) {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            this.ed = builder.setType(EmbeddedDatabaseType.HSQL).build();
        }
        logger.debug("Exit: EmbeddedDatabase");
        return this.ed;

    }*/
    //TODO
    @Bean(name = "hsqlInMemory")
    public DataSource hsqlInMemory() {
    	logger.debug("Enter: JDBC MYSQL");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/usersdb");
        dataSource.setUsername("root");
        dataSource.setPassword("r4nd0mS0");
     
        return dataSource;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){


        LocalContainerEntityManagerFactoryBean lcemfb
                = new LocalContainerEntityManagerFactoryBean();

        lcemfb.setDataSource(this.hsqlInMemory());
        lcemfb.setPackagesToScan(new String[] {"com.odvarkajak.oslol.domain"});

        lcemfb.setPersistenceUnitName("MyPU");

        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        lcemfb.setJpaVendorAdapter(va);
        va.setDatabase(Database.HSQL);
        va.setGenerateDdl(true);
        va.setShowSql(true);
        // va.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
        va.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");

        Properties ps = new Properties();
        // ps.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        
        ps.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        ps.put("hibernate.hbm2ddl.auto", "create");
        lcemfb.setJpaProperties(ps);

        lcemfb.afterPropertiesSet();

        return lcemfb;

    }



    @Bean
    public PlatformTransactionManager transactionManager(){

        JpaTransactionManager tm = new JpaTransactionManager();

        tm.setEntityManagerFactory(
                this.entityManagerFactory().getObject() );

        return tm;

    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Override
    public void destroy() {

        if ( this.ed != null ) {
            this.ed.shutdown();
        }

    }
}
