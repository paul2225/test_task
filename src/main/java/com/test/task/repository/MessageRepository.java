package com.test.task.repository;

import com.test.task.domain.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(nativeQuery = true, value = "select * from message order by date desc LIMIT ?1")
    List<Message> findFirstNMessages(int n);
}
