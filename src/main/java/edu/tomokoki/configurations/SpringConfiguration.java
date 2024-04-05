package edu.tomokoki.configurations;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"edu.tomokoki"})
public class SpringConfiguration {

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        SessionFactory factory = new org.hibernate.cfg.Configuration()
                .configure("my.hiber.cfg.xml")
                .buildSessionFactory();
        return factory;
    }
}
