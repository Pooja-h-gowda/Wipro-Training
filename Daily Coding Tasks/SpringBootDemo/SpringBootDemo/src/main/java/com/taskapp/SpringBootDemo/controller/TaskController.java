package com.taskapp.SpringBootDemo.controller;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")

public class TaskController {
	
	    @PostMapping("/post")
	    public String createData() {
	        return "Post the task";
	    }

	    @GetMapping("/get")
	    public String getTask() {
	        return "Retrieving the task";
	    }

	    @PutMapping("/update")
	    public String updateData() {
	        return "Update done";
	    }

	    @DeleteMapping("/delete")
	    public String deleteData() {
	        return "Delete done";
	    }

}
