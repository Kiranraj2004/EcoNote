package EcoNote.com.Econote.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@Data
@Document(collection = "journal_entries")
public class JournalEntry {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
}