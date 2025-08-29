package EcoNote.com.Econote;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Hello_World {
    @GetMapping()
    public ResponseEntity<String> HealthChecker(){
        return  ResponseEntity.ok("Hello world");
    }


}
