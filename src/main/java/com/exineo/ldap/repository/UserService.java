package com.exineo.ldap.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.naming.InvalidNameException;
import javax.naming.Name;

import com.exineo.ldap.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User create(User user) throws InvalidNameException {
		if (user.getDn() == null)
			user.setDn(LdapUtils.emptyLdapName().add("cn=" + user.getUid()));
		User ret = findByUid(user.getUid());
		if(ret == null)
			return userRepository.save(user);
		else
			return null;
	}

	public User findByUid(String uid) {
		try {
			Optional<User> optional = userRepository.findById(LdapUtils.emptyLdapName().add("cn=" + uid));
			return optional.get();
		} catch (InvalidNameException e) {
			e.printStackTrace();
			return null;
		}
		catch(NoSuchElementException e) {
			return null;
		}
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void delete(String uid) {
		Name id;
		try {
			id = LdapUtils.emptyLdapName().add("uid=" + uid);
		} catch (InvalidNameException e) {
			id = null;
			e.printStackTrace();
		}
		if (id != null)
			userRepository.deleteById(id);
	}
}
