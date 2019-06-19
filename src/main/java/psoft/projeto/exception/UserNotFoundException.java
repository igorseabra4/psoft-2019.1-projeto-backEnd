package psoft.projeto.exception;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ServletException {

	public UserNotFoundException(String string) {
		super(string);
	}

}