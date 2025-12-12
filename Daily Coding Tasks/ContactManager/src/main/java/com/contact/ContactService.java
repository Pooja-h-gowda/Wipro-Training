package com.contact;
import java.util.*;

import javax.servlet.http.HttpServlet;
public class ContactService {
	    private static List<Contact> contacts = new ArrayList<>();
	    private static int idCounter = 1;

	    public List<Contact> getAll() {
	        return contacts;
	    }

	    public boolean add(String name, String phone, String email) {
	        if(name.isEmpty() || phone.isEmpty()) return false;
	        contacts.add(new Contact(idCounter++, name, phone, email));
	        return true;
	    }

	    public Contact getById(int id) {
	        return contacts.stream()
	                .filter(c -> c.getId() == id)
	                .findFirst()
	                .orElse(null);
	    }

	    public boolean update(int id, String name, String phone, String email) {
	        Contact c = getById(id);
	        if(c == null) return false;

	        c.setName(name);
	        c.setPhone(phone);
	        c.setEmail(email);
	        return true;
	    }

	    public boolean delete(int id) {
	        return contacts.removeIf(c -> c.getId() == id);
	    }
	}



