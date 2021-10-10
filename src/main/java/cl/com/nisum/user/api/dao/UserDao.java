package cl.com.nisum.user.api.dao;

import cl.com.nisum.user.api.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao extends GenericDao<User> implements OperationsDao<User> {

    @Override
    public List<User> findAll() {
        return this.findAllEntity(User.class);
    }

    @Override
    public Optional<User> findOne(String attributeId, String value) {
        return this.findOneEntity(attributeId, value, User.class);
    }
    @Override
    public User saveOrUpdate(User user) {
        this.saveOrUpdateEntity(user);
        return user;
    }

    @Override
    public void delete(User user) {
        this.deleteEntity(user);
    }

}
