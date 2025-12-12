package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name= "loan")
@Data
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;
	private Double amount;
	private Double interestRate;
	private Integer months;
	private Double emiAmount;
	private Double remainingAmount;
	private String status;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
