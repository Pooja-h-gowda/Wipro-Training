package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name= "fixed_deposit")
@Data
public class FixedDeposit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fdId;
	private Double depositAmount;
	private Double interestRate;
	private Integer durationMonths;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

}
