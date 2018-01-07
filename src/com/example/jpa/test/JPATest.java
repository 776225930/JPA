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

	// ������hibernate��session��get����
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("---------------------------");
		System.out.println(customer);

		// ������hibernate��session��load����
	}

	@Test
	public void testGetReference() {
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
		System.out.println("---------------------------");
		System.out.println(customer);
	}

	// ������ hibernate �� save ����. ʹ��������ʱ״̬��Ϊ�־û�״̬.
	// �� hibernate �� save �����Ĳ�֮ͬ��: �������� id, ����ִ�� insert ����, �����׳��쳣.
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

	// ������ hibernate �� Session �� delete ����. �Ѷ����Ӧ�ļ�¼�����ݿ����Ƴ�
	// ע��: �÷���ֻ���Ƴ� �־û� ����. �� hibernate �� delete ����ʵ���ϻ������Ƴ� �������.
	@Test
	public void testRemove() {
		Customer customer = entityManager.find(Customer.class, 2);
		entityManager.remove(customer);
	}

	/**
	 * �ܵ���˵������hibernate session��saveOrUpdat()����
	 */
	// 1.���������һ����ʱ����
	// �ᴴ��һ���µĶ���, ����ʱ��������Ը��Ƶ��µĶ�����, Ȼ����µĶ���ִ�г־û�����. ����
	// �µĶ������� id, ����ǰ����ʱ������û�� id.
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

	// ���������һ���������, ������Ķ����� OID.
	// 1. ���� EntityManager ������û�иö���
	// 2. �������ݿ���Ҳû�ж�Ӧ�ļ�¼
	// 3. JPA �ᴴ��һ���µĶ���, Ȼ��ѵ�ǰ�����������Ը��Ƶ��´����Ķ�����
	// 4. ���´����Ķ���ִ�� insert ����.
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

	// ���������һ���������, ������Ķ����� OID.
	// 1. ���� EntityManager ������û�иö���
	// 2. �������ݿ���Ҳ�ж�Ӧ�ļ�¼
	// 3. JPA ���ѯ��Ӧ�ļ�¼,Ȼ�󷵻ظü�¼��Ӧ��һ������, ��Ȼ���������������Ը��Ƶ���ѯ���Ķ�����.
	// 4. �Բ�ѯ���Ķ���ִ�� update ����.
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

	// ���������һ���������, ������Ķ����� OID.
	// 1. ���� EntityManager �������ж�Ӧ�Ķ���
	// 2. JPA ��������������Ը��Ƶ���ѯ��EntityManager �����еĶ�����.
	// 3. EntityManager �����еĶ���ִ�� UPDATE.
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
	 * ͬ hibernate �� Session �� flush ����. 
	 */
	@Test
	public void testFlush() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("Customer:  "+customer);
		
		customer.setLastName("AA");
		entityManager.flush();
	}
	/**
	 * ͬ hibernate �� Session �� refresh ����. 
	 */
	@Test
	public void testReFresh() {
		Customer customer = entityManager.find(Customer.class, 1);
		customer = entityManager.find(Customer.class, 1);
		entityManager.refresh(customer);
	}
	/**
	 * ������һʱ, �����ȱ��� 1 ��һ��, �󱣴� n ��һ��, ��������������� UPDATE ���.
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
		
		//���ù�����ϵ
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		//ִ�б������
		entityManager.persist(order1);
		entityManager.persist(order2);
		entityManager.persist(customer);
	}
	
	//Ĭ�������ʹ���������ӵķ�ʽ��ȡn��һ�˵Ķ��� �����������һ��һ�˵Ķ���
	@Test
	public void testManyToOneFind(){
		Order order =entityManager.find(Order.class, 1);
		System.out.println("OrderName: "+order.getOrderName());
	    System.out.println(order.getCustomer().getLastName());
	}
	//����ֱ��ɾ��1��һ�ˣ���Ϊ�����Լ��
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
	/**-----------------------��������------------------------*/
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
		 //ռλ��������1��ʼ
		 query.setParameter(1, 1);
		 List<Customer> customers=query.getResultList();
	      System.out.println(customers.size());
	 }
	 //Ĭ������£����ǚH���ܲ��������򽫷���Object[]���͵Ľ��,����Object���͵�List
	 //Ҳ������ʵ�����д�����Ӧ�Ĺ�����,Ȼ����JPQL��������ö�Ӧ�Ĺ���������ʵ����Ķ���
	 @Test
	 public void testPartlyProperties(){
		 String jpql="SELECT new Customer(c.lastName,c.age) FROM Customer c WHERE c.age >?";
		 Query query=entityManager.createQuery(jpql);
		 query.setParameter(1, 1);
		 System.out.println(query.getResultList());
	 }
	//createNamedQuery ��������ʵ����ǰʹ�� @NamedQuery ��ǵĲ�ѯ���
	 @Test
	 public void testNamedQuery(){
		 Query query=entityManager.createNamedQuery("testNamedQuery");
		 query.setParameter(1, 1);
		 Customer customer=(Customer)query.getSingleResult();
		 System.out.println(customer);
	 }
	//createNativeQuery �����ڱ��� SQL
	 @Test
	 public void testNativeQuery(){
		 String sql="SELECT age FROM jpa_customer WHERE id=? ";
         Query query=entityManager.createNativeQuery(sql);
		 query.setParameter(1, 1);
		 Object object=query.getSingleResult();
		 System.out.println(object);
	 }
	 
	//ʹ�� hibernate �Ĳ�ѯ����. 
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
	  //��ѯ order �������� 2 ����Щ Customer
	    @Test
	    public void testGroupBy(){
	    	String jpql="Select o.customer FROM Order o Group By o.customer HAVING count(o.id)>2 ";
	    	Query query=entityManager.createQuery(jpql);
	    	List<Customer> customers=query.getResultList();
	    	System.out.println(customers);
	    }
	    /**
		 * JPQL �Ĺ�����ѯͬ HQL �Ĺ�����ѯ. 
		 */
	    @Test
	    public void testLeftOuterJoinFetch(){
	    	String jpql="Select Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id=?";
	    	Query query=entityManager.createQuery(jpql).setParameter(1, 1);
	    	 Customer customer=(Customer) query.getSingleResult();
	    	System.out.println(customer.getLastName());
	    }
	 
		//ʹ�� jpql �ڽ��ĺ���
		@Test
		public void testJpqlFunction(){
			String jpql = "SELECT lower(c.email) FROM Customer c";
			
			List<String> emails = entityManager.createQuery(jpql).getResultList();
			System.out.println(emails);
		}
		
		@Test
		public void testSubQuery(){
			//��ѯ���� Customer �� lastName Ϊ YY �� Order
			String jpql = "SELECT o FROM Order o "
					+ "WHERE o.customer = (SELECT c FROM Customer c WHERE c.lastName = ?)";
			
			Query query = entityManager.createQuery(jpql).setParameter(1, "YY");
			List<Order> orders = query.getResultList();
			System.out.println(orders.size());
		}
		 //����ʹ�� JPQL ��� UPDATE �� DELETE ����. 
		@Test
		public void testExecuteUpdate(){
			String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
			Query query = entityManager.createQuery(jpql).setParameter(1, "YYY").setParameter(2, 12);
			
			query.executeUpdate();
		}

		
}
