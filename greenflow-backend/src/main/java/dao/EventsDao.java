package dao;

import entities.EventsEntity;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Model
public class EventsDao {
    @Inject
    private EntityManager em;
    @Inject
    private UserDao userDao;

    @Transactional
    public void createEvents(String name){
        int userId = userDao.getId(name);
        EventsEntity eventsEntity = new EventsEntity();
        eventsEntity.setUser_id(userId);
        try {
            em.persist(eventsEntity);
        }catch (Exception e){
            throw e;
        }
    }

    @Transactional
    public void updateMonthly(String name, boolean value){
        int userId = userDao.getId(name);
        try{
            EventsEntity eventsEntity = em.createNamedQuery(EventsEntity.QUERY_EVENTS_BY_ID,EventsEntity.class)
                    .setParameter("user_id", userId)
                    .getSingleResult();
            eventsEntity.setMonthlyReports(value);
            em.merge(eventsEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("modify events problem, unathorized access");
            throw e;
        }
    }
}
