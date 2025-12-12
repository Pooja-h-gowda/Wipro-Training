package com.taskmanager.service;

import java.util.List;

import com.taskmanager.bean.Task;

public interface TaskService {

    public List<Task> getAllTasks();
    public Task getTaskById(Integer id);
    public Task createTask(Task task);
    public Task updateTask(Integer id, Task task);
    public void deleteTask(Integer id);
    public boolean exists(Integer id);
    public List<Task> getTasksByStatus(String status);
    public List<Task> getTasksByPriority(String priority);
    public List<Task> getOverdueTasks();
}
