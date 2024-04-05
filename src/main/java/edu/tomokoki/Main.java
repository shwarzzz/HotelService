package edu.tomokoki;

import edu.tomokoki.application.ConsoleApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                "edu.tomokoki.configurations");
        context.getBean(ConsoleApplication.class).start();
    }

    public static void addTestData() {
        System.out.println("Хотите ли вы добавить тестовые данные в базу(y/n):");

    }
}
