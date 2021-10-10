package cl.com.nisum.user.api.dao;

import java.util.List;
import java.util.Optional;

public interface OperationsDao<Entity> {

    List<Entity> findAll();

    Optional<Entity> findOne(String attributeId, String value);

    Entity saveOrUpdate(Entity entity);

    void delete(Entity entity);

}
