package top.datadriven.raft.core.model.util;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description: 获取spring容器
 * @author: jiayancheng && zhangchi
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/2/23 下午7:19
 * @version: 1.0.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext 重写ApplicationContextAware中的setApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.setApp(applicationContext);
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 为了规避findbugs的Write to static field from instance method潜在bug，
     * setApplicationContext实例化SpringContextUtil通过该方法为静态变量applicationContext赋值。
     *
     * @param applicationContext 上下文
     */
    public static void setApp(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据类名获取类对象
     * @param name 类名
     * @param <T> 对象
     * @return 返回对象
     * @throws BeansException 抛出获取类对象异常
     */
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据class对象获取bean
     *
     * @param clazz class信息
     * @param <T>   对象
     * @return 返回类对象
     */
    public static <T> T getBeanOfType(Class<T> clazz) {
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /**
     * 返回依赖注入的对象工程
     *
     * @return AutowireCapableBeanFactory
     */
    public static AutowireCapableBeanFactory getAutowire() {
        return applicationContext.getAutowireCapableBeanFactory();
    }
}
