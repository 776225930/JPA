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

	//����˫�� 1-n �Ĺ�����ϵ, ִ�б���ʱ
	//���ȱ��� n ��һ��, �ٱ��� 1 ��һ��, Ĭ�������, ���� n �� UPDATE ���.
	//���ȱ��� 1 ��һ��, ����� n �� UPDATE ���
	//�ڽ���˫�� 1-n ������ϵʱ, ����ʹ�� n ��һ����ά��������ϵ, �� 1 ��һ����ά������ϵ
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

		// ���ù�����ϵ
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		// ִ�б������
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}

	// Ĭ�϶Թ����Ķ��һ��ʹ�������صļ��ز���.
	// ����ʹ�� @OneToMany �� fetch �������޸�Ĭ�ϵļ��ز���
	//ע��: ���� 1 ��һ�˵� @OneToMany ��ʹ�� mappedBy ����, �� @OneToMany �˾Ͳ�����ʹ�� @JoinColumn ������. 
	@Test
	public void testOneToManyFind() {
		Customer customer = entityManager.find(Customer.class, 7);
		System.out.println(customer.getLastName());
		System.out.println(customer.getOrders().size());
	}
	//Ĭ�������, ��ɾ�� 1 ��һ��, ����Ȱѹ����� n ��һ�˵�����ÿ�, Ȼ�����ɾ��. 
	//����ͨ�� @OneToMany �� cascade �������޸�Ĭ�ϵ�ɾ������.
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
