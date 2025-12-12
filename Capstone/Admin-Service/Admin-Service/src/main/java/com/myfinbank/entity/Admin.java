package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "admin")
@Data
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;	
	@Column(unique = true)
	private String email;
	private String password;
	
}
