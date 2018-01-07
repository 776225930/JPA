package com.example.jpa.one2one;

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

	// ˫�� 1-1 �Ĺ�����ϵ, �����ȱ��治ά��������ϵ��һ��, ��û�������һ��, ���������� UPDATE ���.
	@Test
	public void testOneToOnePersistence() {
		Manager mgr = new Manager();
		mgr.setMgrName("M-BB");

		Department dept = new Department();
		dept.setDeptName("D-BB");

		// ���ù�����ϵ
		mgr.setDept(dept);
		dept.setMgr(mgr);
		// ִ�б������
		entityManager.persist(dept);
		entityManager.persist(mgr);
	}

	// 1.Ĭ�������, ����ȡά��������ϵ��һ��, ���ͨ���������ӻ�ȡ������Ķ���.
	// ������ͨ�� @OntToOne �� fetch �������޸ļ��ز���.
	@Test
	public void testOneToOneFind() {
		Department dept = entityManager.find(Department.class, 1);
		System.out.println(dept.getDeptName());
		System.out.println(dept.getMgr().getClass().getName());
	}

	// 1. Ĭ�������, ����ȡ��ά��������ϵ��һ��, ��Ҳ��ͨ���������ӻ�ȡ������Ķ���.
	// ����ͨ�� @OneToOne �� fetch �������޸ļ��ز���. ����Ȼ���ٷ��� SQL �������ʼ��������Ķ���
	// ��˵���ڲ�ά��������ϵ��һ��, �������޸� fetch ����.
	@Test
	public void testOneToOneFind2() {
		Manager manager = entityManager.find(Manager.class, 1);
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDept().getClass().getName());
	}
}
