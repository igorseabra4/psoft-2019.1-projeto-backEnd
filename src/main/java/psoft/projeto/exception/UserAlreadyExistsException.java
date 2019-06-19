package psoft.projeto.exception;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserAlreadyExistsException extends ServletException {

	public UserAlreadyExistsException(String string) {
		super(string);
	}

}