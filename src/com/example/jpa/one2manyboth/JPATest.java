package com.example.jpa.one2manyboth;

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

	//若是双向 1-n 的关联关系, 执行保存时
	//若先保存 n 的一端, 再保存 1 的一端, 默认情况下, 会多出 n 条 UPDATE 语句.
	//若先保存 1 的一端, 则会多出 n 条 UPDATE 语句
	//在进行双向 1-n 关联关系时, 建议使用 n 的一方来维护关联关系, 而 1 的一方不维护关联系
	@Test
	public void testOneToManyBothPersist() {
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("zz@163.com");
		customer.setLastName("ZZ");

		Order order1 = new Order();
		order1.setOrderName("o-YY-1");

		Order order2 = new Order();
		order2.setOrderName("o-YY-2");

		// 设置关联关系
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		// 执行保存操作
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}

	// 默认对关联的多的一方使用懒加载的加载策略.
	// 可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
	//注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用 @JoinColumn 属性了. 
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
