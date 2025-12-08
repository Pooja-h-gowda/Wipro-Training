package com.myfinbank.entity;
import jakarta.persistence.*;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name= "account")
@Data
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	private Double balance = 0.0;
	
	@Column(unique = true)
	private String accountNumber;
	private String accountType;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<>();
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<RecurringDeposit> recurringDeposits = new ArrayList<>();
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<FixedDeposit> fixedDeposits = new ArrayList<>();

}
