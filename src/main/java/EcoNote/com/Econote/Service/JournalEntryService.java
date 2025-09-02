package EcoNote.com.Econote.Service;

import EcoNote.com.Econote.Entity.JournalEntry;
import EcoNote.com.Econote.Entity.User;
import EcoNote.com.Econote.Repository.JournalEntryRepository;
import EcoNote.com.Econote.Repository.UserRepository;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class JournalEntryService {

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new journal entry for a user
     */


    @Transactional
    public ResponseEntity<?> createEntry(JournalEntry entry, String userName) {
        // Validate title and content
        if (entry.getTitle() == null || entry.getTitle().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title is required.");
        }
        if (entry.getContent() == null || entry.getContent().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content is required.");
        }

        // Find user
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Set created date and save entry
        entry.setCreatedAt(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(entry);

        // Add entry to user and save user
        user.getJournalEntries().add(savedEntry);
        userRepository.save(user);

        logger.info("Journal entry created successfully for user: {}", userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    /**
     * Get a journal entry by ID
     */
    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    /**
     * Get all journal entries
     */
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    /**
     * Delete a journal entry by ID for a specific user
     */
    @Transactional
    public ResponseEntity<?> deleteById(ObjectId id, String userName) {
        try {
            User user = userService.findByUserName(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            // Find entry
            JournalEntry entry = (JournalEntry) journalEntryRepository.findById(id)
                    .orElse(null);

            if (entry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found.");
            }

            // Remove from user entries and delete entry
            user.getJournalEntries().removeIf(j -> j.getId().equals(id));
            userRepository.save(user);
            journalEntryRepository.deleteById(id);

            logger.info("Journal entry {} deleted successfully for user: {}", id, userName);
            return ResponseEntity.ok("Journal entry deleted successfully.");
        } catch (RuntimeException e) {
            logger.error("Error deleting journal entry {} for user {}: {}", id, userName, e.getMessage());
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    /**
     * Bulk delete multiple journal entries
     */
    @Transactional
    public void deleteAll(List<JournalEntry> list) {
        for (JournalEntry journal : list) {
            journalEntryRepository.deleteById(journal.getId());
        }
    }

    /**
     * Update an existing journal entry
     */
    @Transactional
    public ResponseEntity<?> update(ObjectId id, JournalEntry newEntry) {
        // Validate updated fields
        if (newEntry.getTitle() == null || newEntry.getTitle().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title is required.");
        }
        if (newEntry.getContent() == null || newEntry.getContent().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content is required.");
        }

        // Find and update entry
        JournalEntry existing = (JournalEntry) journalEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal entry not found."));

        existing.setTitle(newEntry.getTitle());
        existing.setContent(newEntry.getContent());
        existing.setCreatedAt(LocalDateTime.now());


          JournalEntry updated=journalEntryRepository.save(existing);

        logger.info("Journal entry {} updated successfully.", id);
        return ResponseEntity.ok(updated);
    }
}

