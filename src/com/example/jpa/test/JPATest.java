package com.example.jpa.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.QueryHint;

import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.jpa.helloworld.Customer;
import com.example.jpa.helloworld.Order;

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

	// 类似于hibernate中session的get方法
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("---------------------------");
		System.out.println(customer);

		// 类似于hibernate中session的load方法
	}

	@Test
	public void testGetReference() {
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
		System.out.println("---------------------------");
		System.out.println(customer);
	}

	// 类似于 hibernate 的 save 方法. 使对象由临时状态变为持久化状态.
	// 和 hibernate 的 save 方法的不同之处: 若对象有 id, 则不能执行 insert 操作, 而会抛出异常.
	@Test
	public void testPersistence() {
		Customer customer = new Customer();
		customer.setAge(15);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("bb@163.com");
		customer.setLastName("BB");
		// customer.setId(100);

		entityManager.persist(customer);
		System.out.println(customer.getId());
	}

	// 类似于 hibernate 中 Session 的 delete 方法. 把对象对应的记录从数据库中移除
	// 注意: 该方法只能移除 持久化 对象. 而 hibernate 的 delete 方法实际上还可以移除 游离对象.
	@Test
	public void testRemove() {
		Customer customer = entityManager.find(Customer.class, 2);
		entityManager.remove(customer);
	}

	/**
	 * 总的来说类似与hibernate session的saveOrUpdat()方法
	 */
	// 1.若传入的是一个临时对象
	// 会创建一个新的对象, 把临时对象的属性复制到新的对象中, 然后对新的对象执行持久化操作. 所以
	// 新的对象中有 id, 但以前的临时对象中没有 id.
	@Test
	public void testMerge1() {
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("cc@163.com");
		customer.setLastName("CC");

		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer#id:   " + customer.getId());
		System.out.println("customer2#id:   " + customer2.getId());
	}

	// 若传入的是一个游离对象, 即传入的对象有 OID.
	// 1. 若在 EntityManager 缓存中没有该对象
	// 2. 若在数据库中也没有对应的记录
	// 3. JPA 会创建一个新的对象, 然后把当前游离对象的属性复制到新创建的对象中
	// 4. 对新创建的对象执行 insert 操作.
	@Test
	public void testMerge2() {
		Customer customer = new Customer();
		customer.setAge(19);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		customer.setId(100);

		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer#id:   " + customer.getId());
		System.out.println("customer2#id:   " + customer2.getId());
	}

	// 若传入的是一个游离对象, 即传入的对象有 OID.
	// 1. 若在 EntityManager 缓存中没有该对象
	// 2. 若在数据库中也有对应的记录
	// 3. JPA 会查询对应的记录,然后返回该记录对应的一个对象, 再然后会把游离对象的属性复制到查询到的对象中.
	// 4. 对查询到的对象执行 update 操作.
	@Test
	public void testMerge3() {
		Customer customer = new Customer();
		customer.setAge(19);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("ee@163.com");
		customer.setLastName("EE");
		customer.setId(4);

		Customer customer2 = entityManager.merge(customer);
		System.out.println(customer == customer2);
	}

	// 若传入的是一个游离对象, 即传入的对象有 OID.
	// 1. 若在 EntityManager 缓存中有对应的对象
	// 2. JPA 会把游离对象的属性复制到查询到EntityManager 缓存中的对象中.
	// 3. EntityManager 缓存中的对象执行 UPDATE.
	@Test
	public void testMerge4() {
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");

		customer.setId(4);
		Customer customer2 = entityManager.find(Customer.class, 4);

		entityManager.merge(customer);

		System.out.println(customer == customer2); // false
	}
	/**
	 * 同 hibernate 中 Session 的 flush 方法. 
	 */
	@Test
	public void testFlush() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("Customer:  "+customer);
		
		customer.setLastName("AA");
		entityManager.flush();
	}
	/**
	 * 同 hibernate 中 Session 的 refresh 方法. 
	 */
	@Test
	public void testReFresh() {
		Customer customer = entityManager.find(Customer.class, 1);
		customer = entityManager.find(Customer.class, 1);
		entityManager.refresh(customer);
	}
	/**
	 * 保存多对一时, 建议先保存 1 的一端, 后保存 n 的一端, 这样不会多出额外的 UPDATE 语句.
	 */
	@Test
	public void testManyToOnePersist(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("ff@163.com");
		customer.setLastName("FF");
		
		Order order1=new Order();
		order1.setOrderName("order1");
		
		Order order2=new Order();
		order2.setOrderName("order2");
		
		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		//执行保存操作
		entityManager.persist(order1);
		entityManager.persist(order2);
		entityManager.persist(customer);
	}
	
	//默认情况下使用左外连接的方式获取n的一端的对象 和与其关联的一的一端的对象
	@Test
	public void testManyToOneFind(){
		Order order =entityManager.find(Order.class, 1);
		System.out.println("OrderName: "+order.getOrderName());
	    System.out.println(order.getCustomer().getLastName());
	}
	//不能直接删除1的一端，因为有外键约束
	@Test
	public void testManyToOneRemove(){
		//Order order =entityManager.find(Order.class, 1);
		//entityManager.remove(order);
		Customer customer = entityManager.find(Customer.class, 6);
		entityManager.remove(customer);
	}
	@Test
	public void testManyToOneUpdate(){
		Order order =entityManager.find(Order.class, 2);
        order.getCustomer().setLastName("HHH");
	}
	/**-----------------------二级缓存------------------------*/
	 @Test
	public void testSecondLevelCache(){
    	Customer customer=entityManager.find(Customer.class, 1);
    	transaction.commit();
		entityManager.close();
		
		entityManager = factory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
    	Customer customer2=entityManager.find(Customer.class, 1);
    }
    /*------------------------JPQL---------------------------*/
	 @Test
	 public void testHelloJPQL(){
		 String jpql="FROM Customer c WHERE c.age >?";
		 Query query=entityManager.createQuery(jpql);
		 //占位符索引从1开始
		 query.setParameter(1, 1);
		 List<Customer> customers=query.getResultList();
	      System.out.println(customers.size());
	 }
	 //默认情况下，弱智欻性能部分属性则将返回Object[]类型的结果,或者Object类型的List
	 //也可以在实体类中创建对应的构造器,然后在JPQL语句中利用对应的构造器返回实体类的对象
	 @Test
	 public void testPartlyProperties(){
		 String jpql="SELECT new Customer(c.lastName,c.age) FROM Customer c WHERE c.age >?";
		 Query query=entityManager.createQuery(jpql);
		 query.setParameter(1, 1);
		 System.out.println(query.getResultList());
	 }
	//createNamedQuery 适用于在实体类前使用 @NamedQuery 标记的查询语句
	 @Test
	 public void testNamedQuery(){
		 Query query=entityManager.createNamedQuery("testNamedQuery");
		 query.setParameter(1, 1);
		 Customer customer=(Customer)query.getSingleResult();
		 System.out.println(customer);
	 }
	//createNativeQuery 适用于本地 SQL
	 @Test
	 public void testNativeQuery(){
		 String sql="SELECT age FROM jpa_customer WHERE id=? ";
         Query query=entityManager.createNativeQuery(sql);
		 query.setParameter(1, 1);
		 Object object=query.getSingleResult();
		 System.out.println(object);
	 }
	 
	//使用 hibernate 的查询缓存. 
	 @Test
	 public void testQueryCache(){
		 String jpql="FROM Customer c WHERE c.age >?";
		 Query query=entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		 query.setParameter(1, 1);
		 List<Customer> customers=query.getResultList();
	      System.out.println(customers.size());
	      
	      query=entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		  query.setParameter(1, 1);
		  List<Customer> customers1=query.getResultList();
		  System.out.println(customers1.size());
	 }
	    @Test
		public void testOrderBy(){
		 String jpql="FROM Customer c WHERE c.age >? ORDER BY c.age DESC";
		 Query query=entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		 query.setParameter(1, 1);
		 List<Customer> customers=query.getResultList();
	      System.out.println(customers.size());
	 }
	  //查询 order 数量大于 2 的那些 Customer
	    @Test
	    public void testGroupBy(){
	    	String jpql="Select o.customer FROM Order o Group By o.customer HAVING count(o.id)>2 ";
	    	Query query=entityManager.createQuery(jpql);
	    	List<Customer> customers=query.getResultList();
	    	System.out.println(customers);
	    }
	    /**
		 * JPQL 的关联查询同 HQL 的关联查询. 
		 */
	    @Test
	    public void testLeftOuterJoinFetch(){
	    	String jpql="Select Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id=?";
	    	Query query=entityManager.createQuery(jpql).setParameter(1, 1);
	    	 Customer customer=(Customer) query.getSingleResult();
	    	System.out.println(customer.getLastName());
	    }
	 
		//使用 jpql 内建的函数
		@Test
		public void testJpqlFunction(){
			String jpql = "SELECT lower(c.email) FROM Customer c";
			
			List<String> emails = entityManager.createQuery(jpql).getResultList();
			System.out.println(emails);
		}
		
		@Test
		public void testSubQuery(){
			//查询所有 Customer 的 lastName 为 YY 的 Order
			String jpql = "SELECT o FROM Order o "
					+ "WHERE o.customer = (SELECT c FROM Customer c WHERE c.lastName = ?)";
			
			Query query = entityManager.createQuery(jpql).setParameter(1, "YY");
			List<Order> orders = query.getResultList();
			System.out.println(orders.size());
		}
		 //可以使用 JPQL 完成 UPDATE 和 DELETE 操作. 
		@Test
		public void testExecuteUpdate(){
			String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
			Query query = entityManager.createQuery(jpql).setParameter(1, "YYY").setParameter(2, 12);
			
			query.executeUpdate();
		}

		
}
