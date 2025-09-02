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

import java.util.*;

@Service
@JsonSerialize
@Component
public class UserService {
    @Autowired
    public UserRepository userRepository;
    public JournalEntryRepository journalEntryRepository;

    private final  PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

//    --------------Create user------------
    public ResponseEntity<String> createUser(User user) {
        // Check required fields
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return new ResponseEntity<>("Username is required", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!user.getEmail().matches(emailRegex)) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

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


    // ---------------- UPDATE USER ----------------
    public ResponseEntity<?> updateUser(User entry) {
        User existingUser = userRepository.findByUsername(entry.getUsername());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Encode new password if provided
        if (entry.getPassword() != null && !entry.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(entry.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // ---------------- DELETE USER ----------------
    public ResponseEntity<?> deleteByName(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

//        // Delete all journal entries linked to the user
//        if (user.getJournalEntries() != null && !user.getJournalEntries().isEmpty()) {
//            journalEntryRepository.deleteAll(user.getJournalEntries());
//        }

        userRepository.deleteById(user.getId());
        return ResponseEntity.ok("User and their journal entries deleted successfully");
    }

    // ---------------- FIND USER BY USERNAME ----------------
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    // ---------------- GET USER BY NAME (Optional) ----------------
    public Optional<User> getByName(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    // ---------------- GET ALL USERS ----------------
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ---------------- CREATE ADMIN ----------------
    public ResponseEntity<?> createAdmin(User entry) {
        User existingUser = userRepository.findByUsername(entry.getUsername());

        if (existingUser == null) {
            // Create new user with both USER and ADMIN roles
            entry.setPassword(passwordEncoder.encode(entry.getPassword()));
            entry.setRoles(new HashSet<>(Arrays.asList("User", "Admin")));
            User savedAdmin = userRepository.save(entry);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
        }

        // Add ADMIN role if missing
        Set<String> roles = existingUser.getRoles();
        if (!roles.contains("Admin")) {
            roles.add("Admin");
            existingUser.setRoles(roles);
            userRepository.save(existingUser);
            return ResponseEntity.ok("Admin role added to existing user.");
        }
        return ResponseEntity.ok("User already has Admin role.");
    }

}
