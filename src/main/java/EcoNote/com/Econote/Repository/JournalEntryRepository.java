package EcoNote.com.Econote.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JournalEntryRepository extends MongoRepository<JournalEntryRepository, ObjectId> {

}
