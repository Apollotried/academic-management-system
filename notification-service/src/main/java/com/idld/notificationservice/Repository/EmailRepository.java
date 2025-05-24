package com.idld.notificationservice.Repository;

import com.idld.notificationservice.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
