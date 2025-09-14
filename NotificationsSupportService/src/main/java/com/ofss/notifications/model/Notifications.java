package com.ofss.notifications.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Notifications {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ModuleName moduleName;
	public enum ModuleName{
		USER,
		TICKET,
		TRANSACTION,
		ACCOUNT
	}
	
	@Column
	public long moduleId;
	
	@Column
	private String email;
	
	@Column
	public String message;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Status status;
//	public enum Status{
//		RECEIVED,
//		READ
//	}
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	public Status getStatus() {
//		return status;
//	}
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//	public Notifications(Integer id, String message, Status status) {
//		super();
//		this.id = id;
//		this.message = message;
//		this.status = status;
//	}
//	public Notifications() {
//		super();
//	}
//	
//	
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	@Override
//	public String toString() {
//		return "Notifications [id=" + id + ", message=" + message + ", status=" + status + "]";
//	};

}
