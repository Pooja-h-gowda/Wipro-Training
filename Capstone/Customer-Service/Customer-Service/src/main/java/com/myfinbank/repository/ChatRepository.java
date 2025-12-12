package com.myfinbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.Chat;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	
    // Retrieves all chat messages for a specific customer
    List<Chat> findByCustomer_Id(Long customerId); 
	  
    // Counts unread chat messages sent by admin for a specific customer
    int countByCustomer_IdAndSenderAndReadStatus(Long customerId, String sender, boolean readStatus);

}
