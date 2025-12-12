package com.taskmanager.bean;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity 
public class Task {
	    @Id
	    private int id;
	    private String title;
	    private String description;
	    private String assignedTo;
	    private String priority;    
	    private String status;      
	    private LocalDate dueDate;
	}



