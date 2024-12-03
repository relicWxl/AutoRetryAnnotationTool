//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry;

import com.relic.retry.exeception.RetryConfigExistMoreThanOneInstanceException;
import com.relic.retry.exeception.RetryConfigNotExistException;
import com.thunisoft.zhsg.utils.AssertUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component("retrySpringContext")
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContext() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        AssertUtil.notNull(applicationContext, "Spring上下文为空");
        return applicationContext;
    }

    private static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    public static Object getBean(String name) {
        return containsBean(name) ? getApplicationContext().getBean(name) : null;
    }

    public static <T> T getBean(Class<T> type) {
        List<T> beanListOfType = getBeanListOfType(type);
        if (CollectionUtils.isEmpty(beanListOfType)) {
            throw new RetryConfigNotExistException();
        } else if (beanListOfType.size() > 1) {
            throw new RetryConfigExistMoreThanOneInstanceException();
        } else {
            return beanListOfType.get(0);
        }
    }

    public static <T> List<T> getBeanListOfType(Class<T> type) {
        List<T> result = new ArrayList();
        Map<String, T> beans = getApplicationContext().getBeansOfType(type);
        Iterator var3 = beans.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, T> next = (Map.Entry)var3.next();
            result.add(next.getValue());
        }

        return result;
    }
}
