package com.example.jpa.one2manyboth;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Table(name = "JPA_CUSTOMER")
//@Entity
public class Customer {
	private Integer id;
	private String lastName;

	private String email;
	private int age;

	private Date createTime;
	private Date birth;

	private Set<Order> orders = new HashSet<>();


	// @TableGenerator(name="ID_GENERATOR",
	// table="JPA_ID_GENERATORS",
	// pkColumnName="PK_NAME",
	// pkColumnValue="CUSTOMER_ID",
	// valueColumnName="PK_VALUE",
	// allocationSize=10)
	// @GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "Last_Name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getCreateTime() {
		return createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	//注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用 @JoinColumn 属性了. 
	//@JoinColumn(name="CUSTOMER_ID")
    @OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.REMOVE},mappedBy="customer")
	public Set<Order> getOrders() {
		return orders;
	}
	
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	// 工具方法,不需要映射为数据表的一列
	@Transient
	public String getInfo() {
		return "lastName=" + lastName + ", email=" + email;
	}

}
