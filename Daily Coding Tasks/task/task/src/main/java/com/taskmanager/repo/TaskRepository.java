package com.taskmanager.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.bean.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByStatusIgnoreCase(String status);

    List<Task> findByPriorityIgnoreCase(String priority);
}
