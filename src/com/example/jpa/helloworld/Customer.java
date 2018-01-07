package com.example.jpa.helloworld;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
@NamedQuery(name="testNamedQuery",query="FROM Customer c WHERE c.id=?")
@Cacheable(true)
@Table(name="JPA_CUSTOMER")
@Entity 
public class Customer {
	private Integer id;
	private String lastName;
	//https://github.com/776225930/JPA.git
	private String email;
	private int age;
	
	private Date createTime;
	private Date birth;
//	@TableGenerator(name="ID_GENERATOR",
//			table="JPA_ID_GENERATORS",
//			pkColumnName="PK_NAME",
//			pkColumnValue="CUSTOMER_ID",
//			valueColumnName="PK_VALUE",
//			allocationSize=10)
//	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="Last_Name")
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
	//工具方法,不需要映射为数据表的一列
	@Transient
	public String getInfo(){
		return "lastName=" + lastName + ", email=" + email;
	}
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public Customer(String lastName, int age) {
		super();
		this.lastName = lastName;
		this.age = age;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + ", createTime="
				+ createTime + ", birth=" + birth + "]";
	}
	
	
}
