package com.example.jpa.one2many;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
	public static void main(String[] args) {
	// 1����EntitymanagerFactory
    String persistenceUnitName="jpa-1";
    Map<String,Object> properties=new HashMap<String,Object>();
    properties.put("hibernate.show_sql",true);
	EntityManagerFactory entityManagerFactory=
			//Persistence.createEntityManagerFactory(persistenceUnitName);
	          Persistence.createEntityManagerFactory(persistenceUnitName, properties);
			// 2.����EntityManager, ������ Hibernate �� SessionFactory
	EntityManager entityManager = entityManagerFactory.createEntityManager();
	// 3.��������
	EntityTransaction transaction = entityManager.getTransaction();
	// 4.���г־û�����
	transaction.begin();
	Customer customer=new Customer();
	customer.setAge(12);
	customer.setEmail("aa@163.com");
	customer.setLastName("AA");
	customer.setCreateTime(new Date());
	customer.setBirth(new Date());
	

	entityManager.persist(customer);
	// 5.�ύ����
	transaction.commit();
	// 6.�ر�EntityManager
    entityManager.close();
	// 7.�ر�EntityMamagerFactory
    entityManagerFactory.close();
    System.out.println("success!");
	}
}
