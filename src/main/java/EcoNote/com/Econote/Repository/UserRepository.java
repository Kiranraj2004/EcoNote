package EcoNote.com.Econote.Repository;

import EcoNote.com.Econote.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String userName);
    User findByEmail(String userEmail);
}
