package psoft.projeto.service;

import org.springframework.stereotype.Service;

import psoft.projeto.dao.UserDAO;
import psoft.projeto.model.User;

@Service
public class UserService {
	private final UserDAO userDAO;
	
	UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public User create(User user) {
		return userDAO.save(user);
	}

	public User findByLogin(String login) {
		return userDAO.findByLogin(login);
	}

	public User findByID(Long id) {
		return userDAO.findById(id).get();
	}
}