package com.example.jpa.one2one;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="JPA_MANAGERS")
@Entity
public class Manager {

	private Integer id;
	private String mgrName;
	
	private Department dept;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="MGR_NAME")
	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}
	//对于不维护关联关系的一方使用@OneToOne来进行映射,建议设置mapped
	@OneToOne(mappedBy="mgr",fetch=FetchType.LAZY)
	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}
}
