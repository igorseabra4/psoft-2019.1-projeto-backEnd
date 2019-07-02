package psoft.projeto.controller;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import psoft.projeto.exception.UserAlreadyExistsException;
import psoft.projeto.exception.UserNotFoundException;
import psoft.projeto.model.Usuario;
import psoft.projeto.service.UserService;

@RestController
@RequestMapping({"/v1/auth"})
public class UserLoginController {
	private final String TOKEN_KEY = "watermelon";
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/")
	public ResponseEntity<Usuario> create(@RequestBody Usuario user) throws UserAlreadyExistsException {
		Usuario existingUser = userService.findByEmail(user.getEmail());

		if (existingUser != null)
			throw new UserAlreadyExistsException("Já existe um usuário com o e-mail especificado. Por favor, insira outro e-mail.");
		
		Usuario newUser = userService.create(user);
		
		if (newUser == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<Usuario>(newUser, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/login", produces = "application/json", method=RequestMethod.POST)
	public ResponseEntity<LoginResponse> authenticate(@RequestBody Usuario user) throws UserNotFoundException {
		Usuario authUser = userService.findByEmail(user.getEmail());
		
		if (authUser == null)
			throw new UserNotFoundException("Não foi encontrado usuário com o e-mail especificado. Por favor, insira um e-mail válido.");
		
		if (!authUser.getPassword().equals(user.getPassword()))
			throw new UserNotFoundException("Senha inválida para o usuário especificado.");
		
		String token = Jwts.builder().
				setSubject(authUser.getEmail()).
				signWith(SignatureAlgorithm.HS512, TOKEN_KEY).
				setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 minute
				.compact();
		
		return new ResponseEntity<LoginResponse>(new LoginResponse(token, authUser.getId(), authUser.getName()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/withID")
	public ResponseEntity<String> userWithID(@RequestParam(name="id", required=true) Long userID) throws UserNotFoundException {
		Usuario user = userService.findByID(userID);
		
		if (user == null)
			throw new UserNotFoundException("Não foi encontrado usuário especificado. Por favor, insira um ID válido.");
				
		return new ResponseEntity<String>(user.getName(), HttpStatus.OK);
	}
	
	private class LoginResponse {
		public String token;
		public Long userID;
		public String userName;
		
		public LoginResponse(String token, Long userID, String userName) {
			this.token = token;
			this.userID = userID;
			this.userName = userName;
		}
		
		public String getToken() {
			return token;
		}
		
		public Long getUserID() {
			return userID;
		}
		
		public String getUserName() {
			return userName;
		}
	}
}
