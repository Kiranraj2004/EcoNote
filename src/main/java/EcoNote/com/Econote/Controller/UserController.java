package EcoNote.com.Econote.Controller;


import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // ----------------- CREATE USER -----------------
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // ----------------- GET USER -----------------
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.getByName(username);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body("User not found");
    }

    // ----------------- UPDATE USER -----------------
    @PutMapping()
    public ResponseEntity<?> updateUser( @RequestBody User updates) {
        return userService.updateUser( updates);
    }

    // ----------------- DELETE USER -----------------
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> user = userService.getByName(username);
        if(user.isPresent()){
            return userService.deleteByName(username);
        }
        return ResponseEntity.status(404).body("User not found");
    }

    // ----------------- LIST ALL USERS (ADMIN) -----------------
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAll();
    }


}
