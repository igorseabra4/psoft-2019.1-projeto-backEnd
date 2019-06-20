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
		if (user.getFirstName().isEmpty())
			throw new IllegalArgumentException("Primeiro nome nao pode ser vazio");

		if (user.getLastName().isEmpty())
			throw new IllegalArgumentException("Sobrenome nao pode ser vazio");

		if (user.getEmail().isEmpty())
			throw new IllegalArgumentException("Login nao pode ser vazio");

		if (user.getPassword().length() < 6)
			throw new IllegalArgumentException("Senha deve possuir pelo menos 6 caracteres");
		
		return userDAO.save(user);
	}

	public Usuario findByEmail(String login) {
		return userDAO.findByEmail(login);
	}

	public Usuario findByID(Long id) {
		if (userDAO.existsById(id))
			return userDAO.findById(id).get();
		return null;
	}
}