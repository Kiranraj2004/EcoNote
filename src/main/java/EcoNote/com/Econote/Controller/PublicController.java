package EcoNote.com.Econote.Controller;


import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")

public class PublicController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<String> HealthChecker(){
        return  ResponseEntity.ok("Hello Kiran here ");
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }


}
