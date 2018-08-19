package com.jpa.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @Author huangliujun
 * @Date 2018-08-13 1:25
 * @Description   Jpa工具类
 */
public class JPAUtil {

    private static final EntityManagerFactory factory;
    static {
        factory = Persistence.createEntityManagerFactory("myJpa");
    }
    
    public static EntityManager getEntityManager(){
        return factory.createEntityManager();
    }
    
}
