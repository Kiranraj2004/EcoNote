package EcoNote.com.Econote.Controller;


import EcoNote.com.Econote.Entity.JournalEntry;
import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Service.JournalEntryService;
import EcoNote.com.Econote.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return journalEntryService.createEntry(entry, userName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.getById(id);
        return entry.isPresent() ? ResponseEntity.ok(entry.get())
                : ResponseEntity.status(404).body("Journal entry not found.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return journalEntryService.deleteById(id, userName);
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<?> deleteAll(@RequestBody List<JournalEntry> entries) {
        journalEntryService.deleteAll(entries);
        return ResponseEntity.ok("All specified journal entries have been deleted.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalEntryService.update(id, entry);
    }

    @GetMapping("/usersJournal")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user.getJournalEntries());
    }
}
