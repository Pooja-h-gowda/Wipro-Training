package com.taskmanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.bean.Task;
import com.taskmanager.repo.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task updateTask(Integer id, Task task) {
        Task existing = taskRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setAssignedTo(task.getAssignedTo());
        existing.setPriority(task.getPriority());
        existing.setStatus(task.getStatus());
        existing.setDueDate(task.getDueDate());

        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    // NEW method for your controller
    @Override
    public boolean exists(Integer id) {
        return taskRepository.existsById(id);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatusIgnoreCase(status);
    }

    @Override
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriorityIgnoreCase(priority);
    }

    @Override
    public List<Task> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        return taskRepository.findAll().stream()
                .filter(t -> t.getDueDate().isBefore(today)
                        && !t.getStatus().equalsIgnoreCase("completed"))
                .toList();
    }
}
