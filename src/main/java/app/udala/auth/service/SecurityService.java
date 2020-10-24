package app.udala.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	
	@Value("${ssn.passwords.salt}")
	private String salt;
	
	public String hash(String password) {
		return BCrypt.hashpw(password, salt);
	}
	
	public boolean verifyHash(String password, String hashed) {
		return BCrypt.checkpw(password, hashed);
	}
}
