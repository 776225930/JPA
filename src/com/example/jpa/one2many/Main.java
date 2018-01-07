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
	// 1创建EntitymanagerFactory
    String persistenceUnitName="jpa-1";
    Map<String,Object> properties=new HashMap<String,Object>();
    properties.put("hibernate.show_sql",true);
	EntityManagerFactory entityManagerFactory=
			//Persistence.createEntityManagerFactory(persistenceUnitName);
	          Persistence.createEntityManagerFactory(persistenceUnitName, properties);
			// 2.创建EntityManager, 类似于 Hibernate 的 SessionFactory
	EntityManager entityManager = entityManagerFactory.createEntityManager();
	// 3.开启事务
	EntityTransaction transaction = entityManager.getTransaction();
	// 4.进行持久化操作
	transaction.begin();
	Customer customer=new Customer();
	customer.setAge(12);
	customer.setEmail("aa@163.com");
	customer.setLastName("AA");
	customer.setCreateTime(new Date());
	customer.setBirth(new Date());
	

	entityManager.persist(customer);
	// 5.提交事务
	transaction.commit();
	// 6.关闭EntityManager
    entityManager.close();
	// 7.关闭EntityMamagerFactory
    entityManagerFactory.close();
    System.out.println("success!");
	}
}
