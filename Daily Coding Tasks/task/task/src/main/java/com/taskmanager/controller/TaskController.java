package com.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.bean.Task;
import com.taskmanager.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody Task task) {

        if (taskService.exists(task.getId())) {
            return new ResponseEntity<>("Task with same ID already exists", HttpStatus.CONFLICT);
        }

        Task saved = taskService.createTask(task);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    
    @GetMapping("/get")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable int id) {

        Task task = taskService.getTaskById(id);

        if (task == null) {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    
    @PutMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {

        if (!taskService.exists(task.getId())) {
            return new ResponseEntity<>("Task does not exist", HttpStatus.NOT_FOUND);
        }

        Task updated = taskService.updateTask(task.getId(), task);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {

        if (!taskService.exists(id)) {
            return new ResponseEntity<>("Task doesn't exist", HttpStatus.NOT_FOUND);
        }

        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

    
    @GetMapping("/search/{status}")
    public ResponseEntity<List<Task>> searchByStatus(@PathVariable String status) {
        return new ResponseEntity<>(taskService.getTasksByStatus(status), HttpStatus.OK);
    }

    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getByPriority(@PathVariable String priority) {
        return new ResponseEntity<>(taskService.getTasksByPriority(priority), HttpStatus.OK);
    }

    
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return new ResponseEntity<>(taskService.getOverdueTasks(), HttpStatus.OK);
    }
}
