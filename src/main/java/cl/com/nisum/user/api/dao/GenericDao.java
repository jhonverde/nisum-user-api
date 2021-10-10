package cl.com.nisum.user.api.dao;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class GenericDao<Entity> {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Entity> findAllEntity(Class<Entity> clazz) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(clazz);

        cq.from(clazz);

        List<Entity> resultList = getCurrentSession().createQuery(cq).getResultList();

        return resultList;
    }

    public Optional<Entity> findOneEntity(String attributeId, String value, Class<Entity> clazz) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(clazz);
        Root<Entity> entityRoot = cq.from(clazz);

        cq.where(cb.equal(entityRoot.get(attributeId), value));

        return Optional.ofNullable(getCurrentSession().createQuery(cq).uniqueResult());
    }

    public Entity saveOrUpdateEntity(Entity entity) {
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().flush();
        getCurrentSession().refresh(entity);

        return entity;
    }

    public void deleteEntity(Entity entity) {
        getCurrentSession().delete(entity);
    }
}
