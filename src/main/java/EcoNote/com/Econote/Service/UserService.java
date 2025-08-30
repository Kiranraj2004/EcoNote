package EcoNote.com.Econote.Service;


import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Repository.JournalEntryRepository;
import EcoNote.com.Econote.Repository.UserRepository;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@JsonSerialize
@Component
public class UserService {
    @Autowired
    public UserRepository userRepository;

    private final  PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public ResponseEntity<String> createUser(User user) {
        // Check username existence
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        // Check email existence
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role
        Set<String> roles = new HashSet<>();
        roles.add("user");
        user.setRoles(roles);

        // Save user
        userRepository.save(user);

        return new ResponseEntity<>("User Created Successfully", HttpStatus.CREATED);
    }


}
