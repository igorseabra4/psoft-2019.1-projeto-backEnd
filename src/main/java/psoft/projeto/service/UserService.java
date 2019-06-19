package psoft.projeto.service;

import org.springframework.stereotype.Service;

import psoft.projeto.dao.UserDAO;
import psoft.projeto.model.Usuario;

@Service
public class UserService {
	private final UserDAO userDAO;
	
	UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public Usuario create(Usuario user) {
		return userDAO.save(user);
	}

	public Usuario findByLogin(String login) {
		return userDAO.findByLogin(login);
	}

	public Usuario findByID(Long id) {
		return userDAO.findById(id).get();
	}
}