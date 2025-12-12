package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name= "chat")
@Data
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatId;
	@Column(name = "customer_id")
	private Long customerId;
	private Long adminId;
	private String sender;
	private String message;
	private LocalDateTime timestamp;
	
}
