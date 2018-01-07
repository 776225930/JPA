package com.example.jpa.many2manyboth;

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
	//������ı���
	@Test
	public void testManyTomanyPersistence() {
		Item item1=new Item();
		item1.setItemName("i-1");
		
		Item item2=new Item();
		item2.setItemName("i-2");
		
		Category category1=new Category();
		category1.setCategoryName("c-1");
		
		Category category2=new Category();
		category2.setCategoryName("c-2");
		
		//���ù�����ϵ
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		 
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		//ִ�б���
		entityManager.persist(item1);
		entityManager.persist(item2);
		entityManager.persist(category1);
		entityManager.persist(category2);
	}
	//���ڹ����ļ��϶���, Ĭ��ʹ�������صĲ���.
	//ʹ��ά��������ϵ��һ����ȡ, ����ʹ�ò�ά��������ϵ��һ����ȡ, SQL �����ͬ. 
	@Test
	public void testManyToManyFind() {
		Item item=entityManager.find(Item.class, 2);
	    System.out.println("itemName: "+item.getItemName());
	    System.out.println("  "+item.getCategories().size());
//	   Category category=entityManager.find(Category.class, 1);
//	   System.out.println(category.getCategoryName());
//	   System.out.println(category.getItems().size());
	}
}
