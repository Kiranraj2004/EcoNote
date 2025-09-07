package EcoNote.com.Econote.Controller;


import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // ----------------- CREATE USER -----------------
//    it will be in public api end points

    // ----------------- GET USER -----------------
    @GetMapping()
    public ResponseEntity<?> getUserByName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user = userService.getByName(userName);
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
    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return  userService.deleteByName(userName);
    }
}
