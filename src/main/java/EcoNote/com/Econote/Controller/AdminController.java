package EcoNote.com.Econote.Controller;

import EcoNote.com.Econote.Entity.JournalEntry;
import EcoNote.com.Econote.Service.JournalEntryService;
import EcoNote.com.Econote.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;

    // ----------------- LIST ALL USERS (ADMIN) -----------------
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAll();
    }

//    getting all the journal
    @GetMapping("/getAllJournal")
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }


//    adding other's as admin from one of the admin
    @PostMapping("/createAdmin/{userName}")
    public ResponseEntity<?> createAdmin(@PathVariable String userName){
        return userService.createAdmin(userName);
    }
}
