package com.exineo.ldap.controllers;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.validation.Valid;

import com.exineo.ldap.dto.ResponseDTO;
import com.exineo.ldap.model.User;
import com.exineo.ldap.repository.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	@Autowired
	UserService service;

	@RequestMapping(value = "/Users", method = RequestMethod.POST)
	public ResponseDTO saveUser(@Valid @RequestBody User user) {
		User ret;
		try {
			ret = service.create(user);
		} catch (InvalidNameException e) {
			e.printStackTrace();
			ret = null;
		}
		ResponseDTO dto = new ResponseDTO();
		if (ret == null)
			dto.setMessage("Nie udało się stworzyć konta użytkownika.");
		else {
			dto.setMessage("SUKCES - jebać PIS.");
			dto.addUser(ret);
		}
		return dto;
	}

	@RequestMapping(value = "/Users", method = RequestMethod.GET)
	public ResponseDTO getUsers() {
		ResponseDTO dto = new ResponseDTO();
		List<User> users = service.findAll();
		if (users == null || users.size() == 0) {
			dto.setMessage("Nie znaleziono żadnego konta");
		} else {
			int size = users.size();
			if (size == 1)
				dto.setMessage("Znaleziono jedno konto.");
			else
				dto.setMessage("Znaleziono " + users.size() + " kont.");
			dto.setUsers(users);
		}
		return dto;
	}

	@RequestMapping(value = "/Users/{uid}", method = RequestMethod.GET)
	public ResponseDTO getUser(@PathVariable(value = "uid") String uid) {
		ResponseDTO dto = new ResponseDTO();
		User user = service.findByUid(uid);
		if (user == null) {
			dto.setMessage("Brak konta.");
		} else {
			dto.setMessage("Istnieje takie konto.");
			dto.addUser(user);
		}
		return dto;
	}

	@RequestMapping(value = "/Users/{uid}", method = RequestMethod.DELETE)
	public ResponseDTO deleteUser(@PathVariable(value = "uid") String uid) {
		ResponseDTO dto = new ResponseDTO();
		User user = service.findByUid(uid);
		if (user == null)
			dto.setMessage("User nie usuniety.");
		else {
			service.delete(uid);
			dto.setMessage("Do piekła!.");
		}
		return dto;
	}

}
