package com.example.jpa.one2many;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JPATest {
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	private EntityTransaction transaction;

	@Before
	public void init() {
		String persistenceUnitName = "jpa-1";
		factory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = factory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void destroyed() {
		transaction.commit();
		entityManager.close();
		factory.close();
	}

	@Test
	public void test() {
	}

	// 单向 1-n 关联关系执行保存时, 一定会多出 UPDATE 语句.
	// 因为 n 的一端在插入时不会同时插入外键列.
	@Test
	public void testOneToManyPersist() {
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("yy@163.com");
		customer.setLastName("YY");

		Order order1 = new Order();
		order1.setOrderName("o-YY-1");

		Order order2 = new Order();
		order2.setOrderName("o-YY-2");

		// 设置关联关系
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		// 执行保存操作
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}

	// 默认对关联的多的一方使用懒加载的加载策略.
	// 可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
	@Test
	public void testOneToManyFind() {
		Customer customer = entityManager.find(Customer.class, 7);
		System.out.println(customer.getLastName());
		System.out.println(customer.getOrders().size());
	}
	//默认情况下, 若删除 1 的一端, 则会先把关联的 n 的一端的外键置空, 然后进行删除. 
	//可以通过 @OneToMany 的 cascade 属性来修改默认的删除策略.
	@Test
	public void testOneToManyRemove() {
		Customer customer = entityManager.find(Customer.class, 6);
		entityManager.remove(customer);
	}
	@Test
	public void testOneToManyUpdate() {
		Customer customer = entityManager.find(Customer.class, 8);
		customer.getOrders().iterator().next().setOrderName("O-XXX-8");
	}
}
