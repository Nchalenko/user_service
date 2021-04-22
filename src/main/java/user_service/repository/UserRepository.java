package user_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user_service.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findById(Long id);
    List<User> findByIdIn(List<Long> ids);
    List<User> findAll();

    @Transactional
    void deleteById(Long id);
}
