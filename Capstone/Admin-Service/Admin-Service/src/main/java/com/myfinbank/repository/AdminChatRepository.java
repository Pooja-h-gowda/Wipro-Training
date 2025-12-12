package com.myfinbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.Chat;
import java.util.List;

public interface AdminChatRepository extends JpaRepository<Chat, Long> {

    // Retrieves all chat messages based on the given customer ID
    List<Chat> findByCustomerId(Long customerId);

    // Retrieves all chat messages based on the given admin ID
    List<Chat> findByAdminId(Long adminId);

}
