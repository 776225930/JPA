package com.example.jpa.helloworld;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "JPA_ORDERS")
@Entity
public class Order {
	private Integer id;
	private String orderName;
	private Customer customer;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    @Column(name="ORDER_NAME")
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
    //ӳ�䵥��n-1��ϵ
	//ʹ��@ManyToOne��ӳ�䵥����һ�Ĺ�����ϵ
	//ʹ��@JoinColumn ��ӳ�����
	//��ʹ��@ManyToOne��fetch�������޸�Ĭ�ϵĹ������Լ��ز���
	@JoinColumn(name="CUSTOMER_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
