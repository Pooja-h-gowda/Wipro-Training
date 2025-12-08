package com.myfinbank.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name= "chat")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatId;
	private Long adminId;
	private String sender;
	private String message;
	private LocalDateTime timestamp;
	@Column(name = "read_status")
    private boolean readStatus = false;
		
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "customer_id")
	private Customer customer;

}
