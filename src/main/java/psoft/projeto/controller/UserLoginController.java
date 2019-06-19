package psoft.projeto.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import psoft.projeto.exception.UserAlreadyExistsException;
import psoft.projeto.exception.UserNotFoundException;
import psoft.projeto.model.User;
import psoft.projeto.service.UserService;

@RestController
@RequestMapping({"/v1/auth"})
public class UserLoginController {
	private final String TOKEN_KEY = "watermelon";
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/")
	@ResponseBody
	public ResponseEntity<User> create(@RequestBody User user) throws UserAlreadyExistsException {
		User existingUser = userService.findByLogin(user.getLogin());

		if (existingUser != null)
			throw new UserAlreadyExistsException("Usuario ja existe");
		
		User newUser = userService.create(user);
		
		if (newUser == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public LoginResponse authenticate(@RequestBody User user) throws UserNotFoundException {
		User authUser = userService.findByLogin(user.getLogin());
		
		if (authUser == null)
			throw new UserNotFoundException("Usuario nao encontrado");
		
		if (!authUser.getPassword().equals(user.getPassword()))
			throw new UserNotFoundException("Senha invalida");
		
		String token = Jwts.builder().
				setSubject(authUser.getLogin()).
				signWith(SignatureAlgorithm.HS512, TOKEN_KEY).
				setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 minute
				.compact();
		
		return new LoginResponse(token);
	}
	
	@RequestMapping(value = "/withID")
	@ResponseBody
	public ResponseEntity<List<String>> usersWithID(@RequestBody List<Long> userIDs) throws UserAlreadyExistsException {
		List<String> result = new ArrayList<String>(userIDs.size());
		
		for (Long l : userIDs) {
			User user = userService.findByID(l);
			if (user != null)
				result.add(user.getName());
		}
		
		return new ResponseEntity<List<String>>(result, HttpStatus.CREATED);
	}
	
	private class LoginResponse {
		public String token;
		
		public LoginResponse(String token) {
			this.token = token;
		}
		
		public String getToken() {
			return token;
		}
	}
}
