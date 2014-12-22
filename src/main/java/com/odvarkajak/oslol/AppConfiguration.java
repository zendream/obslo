
package com.odvarkajak.oslol;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.odvarkajak.oslol.config.DispatcherConfig;


@Configuration
@EnableWebMvc
@EnableWebSecurity
//@EnableWebMvcSecurity
@ComponentScan(basePackages = "com.odvarkajak.oslol")
@Import({ WebInitializer.class, DispatcherConfig.class})
public class AppConfiguration {

}
