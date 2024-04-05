package edu.tomokoki.configurations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = {"edu.tomokoki"})
public class SpringConfiguration {

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        SessionFactory factory = new org.hibernate.cfg.Configuration()
                .configure("my.hiber.cfg.xml")
                .buildSessionFactory();
        try (Session session = factory.getCurrentSession()) {
            Path path = Paths.get(getClass().getResource("/schema.sql").toURI());
            String createScript = Files.lines(path, StandardCharsets.UTF_8)
                    .collect(Collectors.joining(" "));
            session.beginTransaction();
            session.createNativeQuery(createScript).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Не удалось создать базу");
            System.exit(-1);
        }
        return factory;
    }
}
