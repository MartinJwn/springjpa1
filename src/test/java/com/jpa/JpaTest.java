package com.jpa;

import com.jpa.pojo.Customer;
import com.jpa.utils.JPAUtil;
import org.junit.Test;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

/**
 * @Author huangliujun
 * @Date 2018-08-13 0:51
 * @Description 测试jpa
 */
public class JpaTest {

    /**
     * 测试保存
     */
    @Test
    public void testSave() {
        Customer customer = new Customer();
        customer.setCustName("百度");

        //  创建实体管理器工厂,通过presistence的静态方法获取
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");

        // 通过实体管理器工厂创建实体管理器
        EntityManager entityManager = factory.createEntityManager();
        // 获取事物
        EntityTransaction transaction = entityManager.getTransaction();
        // 开启事物
        transaction.begin();
        // 保存数据
        entityManager.persist(customer);
        // 提交事物
        transaction.commit();
        // 释放资源
        entityManager.close();
        factory.close();
    }

    /**
     * 测试工具类
     */
    @Test
    public void getEntityManager() {
        Customer customer = new Customer();

        customer.setCustName("传智播客");

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();
        entityManager.close();
    }

    /**
     * 测试根据id查询
     */
    @Test
    public void testFind() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Customer customer = entityManager.find(Customer.class, 1L);
        System.out.println("customer = " + customer);
        tx.commit();
        entityManager.close();
    }

    /**
     * 验证find方法是有缓存的
     */
    @Test
    public void testFind2() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Customer customer1 = entityManager.find(Customer.class, 1L);
        Customer customer2 = entityManager.find(Customer.class, 1L);
        System.out.println("customer1 = " + customer1);
        System.out.println("customer2 = " + customer2);
        System.out.println(customer1 == customer2);   // 只发送了一条sql语句，比较结果为true
        tx.commit();
        entityManager.close();
    }

    /**
     * 查询一个使用延迟加载策略
     */
    @Test
    public void testFind3() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, 1L);
        System.out.println("customer = " + customer);
        transaction.commit();
        entityManager.close();
    }

    /**
     * 测试更新（先查出来在更新）
     */
    @Test
    public void testUpdate() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Customer customer = entityManager.find(Customer.class, 1L);
        customer.setCustName("航头");
        tx.commit();
        entityManager.close();
    }

    /**
     * 先创建对象再设置对象属性，调用更新方法
     */
    @Test
    public void testUpdate2() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustName("黑马程序员");
        customer.setCustAddress("江苏传智播客");

        // 更新
        entityManager.merge(customer);
        transaction.commit();
        entityManager.close();

    }

    /**
     * 测试删除方法
     */
    @Test
    public void testDelete() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.find(Customer.class, 1L);

        entityManager.remove(customer);

        transaction.commit();
        entityManager.close();
    }

    /**
     * 查询全部
     */
    @Test
    public void testFindAll() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        // 创建查询对象
        Query query = entityManager.createQuery("from Customer ");

        List<Customer> customerList = query.getResultList();
        customerList.forEach(customer -> System.out.println("customer = " + customer));
        tx.commit();
        entityManager.close();
    }

    /**
     * 分页查询
     */
    @Test
    public void testFindPage() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Query query = entityManager.createQuery("from Customer");

        query.setFirstResult(0);
        query.setMaxResults(2);

        List<Customer> list = query.getResultList();
        list.forEach(customer -> System.out.println("customer = " + customer));
        tx.commit();
        entityManager.close();

    }

    /**
     * 条件查询
     */
    @Test
    public void testByCondition() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createQuery("from Customer where custName like ?");
        query.setParameter(1, "%传智%");

        List<Customer> list = query.getResultList();
        list.forEach(customer -> System.out.println("customer = " + customer));
        transaction.commit();
        entityManager.close();

    }

    /**
     * 排序查询
     */
    @Test
    public void findOrder() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createQuery("from Customer order by custId desc ");
        List<Customer> list = query.getResultList();
        list.forEach(customer -> System.out.println("customer = " + customer));
        transaction.commit();
        entityManager.close();
    }

    /**
     * 统计查询
     */
    @Test
    public void testFindCount() {

        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createQuery("select count(*) from Customer");
        Long count = (Long) query.getSingleResult();
        System.out.println("total = " + count);
        transaction.commit();
        entityManager.close();

    }

}
