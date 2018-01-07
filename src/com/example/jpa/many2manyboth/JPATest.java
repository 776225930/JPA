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
	//多对所的保存
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
		
		//设置关联关系
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		 
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		//执行保存
		entityManager.persist(item1);
		entityManager.persist(item2);
		entityManager.persist(category1);
		entityManager.persist(category2);
	}
	//对于关联的集合对象, 默认使用懒加载的策略.
	//使用维护关联关系的一方获取, 还是使用不维护关联关系的一方获取, SQL 语句相同. 
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
