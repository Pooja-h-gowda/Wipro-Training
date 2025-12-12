package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name= "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	private Double amount;
	private String type;
	private LocalDateTime dateTime;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
}

