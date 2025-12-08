package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name= "recurring_deposit")
@Data
public class RecurringDeposit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rdId;
	private Double monthlyAmount;
	private Integer durationMonths;
	private Double interestRate;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
}
