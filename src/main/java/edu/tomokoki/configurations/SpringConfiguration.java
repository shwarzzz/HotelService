package edu.tomokoki.configurations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@ComponentScan(basePackages = {"edu.tomokoki"})
public class SpringConfiguration {

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        SessionFactory factory = new org.hibernate.cfg.Configuration()
                .configure("my.hiber.cfg.xml")
                .buildSessionFactory();
        try (Session session = factory.getCurrentSession();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
            byte[] bytes = inputStream.readAllBytes();
            String createScript = new String(bytes, StandardCharsets.UTF_8);
            session.beginTransaction();
            session.createNativeQuery(createScript).executeUpdate();
            session.getTransaction().commit();
        } catch (IOException e) {
            System.out.println("Не удалось создать базу");
            System.exit(-1);
        }
        return factory;
    }
}
