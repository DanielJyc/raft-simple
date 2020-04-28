package top.datadriven.raft.web;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/28 10:05 下午
 * @version: 1.0.0
 */
public class AppMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath*:spring/spring-content.xml"}
        );
        context.start();

        System.out.println("starting...");
    }
}
