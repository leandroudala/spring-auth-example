package app.udala.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Value("${ssn.passwords.salt}")
    private String salt;

    public String generateHash(String password) {
        
        return "";
    }
}
