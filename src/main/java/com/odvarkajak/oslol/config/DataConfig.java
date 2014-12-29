
package com.odvarkajak.oslol.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
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

import java.util.Properties;

import javax.sql.DataSource;


@EnableTransactionManagement
@Configuration
@Profile("default")
@PropertySource("classpath:/application.properties")
public class DataConfig /*implements DisposableBean*/ {
    static final Logger logger = LoggerFactory.getLogger(CommonConfig.class);


    @Value("${db.driver}")
    private String dbDriver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.stateDb}")
    private String stateDb;
    @Value("${entitymanager.packages.to.scan}")
    private String packagesToScan;
    
    /*
     * inmemorySettings
    
    private EmbeddedDatabase ed;
        
    @Bean(name="hsqlInMemory")
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
    @Bean(name = "JPAMySQL")
    public DataSource hsqlInMemory() {
    	logger.debug("Enter: JPA MYSQL");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
     
        return dataSource;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){


        LocalContainerEntityManagerFactoryBean lcemfb
                = new LocalContainerEntityManagerFactoryBean();

        lcemfb.setDataSource(this.hsqlInMemory());
        lcemfb.setPackagesToScan(new String[] {packagesToScan});

        lcemfb.setPersistenceUnitName("MyPU");

        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        lcemfb.setJpaVendorAdapter(va);
        va.setDatabase(Database.HSQL);
        va.setGenerateDdl(true);
        va.setShowSql((showSql.equals("true") ? true : false));
        
        va.setDatabasePlatform(dialect);

        Properties ps = new Properties();
        
        ps.put("hibernate.dialect", dialect);
        ps.put("hibernate.hbm2ddl.auto", stateDb);
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

  /*  @Override
    public void destroy() {

        if ( this.ed != null ) {
            this.ed.shutdown();
        }

    }
    */
}
