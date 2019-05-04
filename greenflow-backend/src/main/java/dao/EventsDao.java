package dao;

import entities.EventsEntity;
import service.events.Event;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

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

    @Transactional
    public void updateWarning(String name, boolean value) {
        int userId = userDao.getId(name);
        try{
            EventsEntity eventsEntity = em.createNamedQuery(EventsEntity.QUERY_EVENTS_BY_ID,EventsEntity.class)
                    .setParameter("user_id", userId)
                    .getSingleResult();
            eventsEntity.setWarningReports(value);
            em.merge(eventsEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("modify events problem, unathorized access");
            throw e;
        }
    }

    public Event getEvents(String username) {
        int userId=userDao.getId(username);
        try{
            EventsEntity eventsEntity = em.createNamedQuery(EventsEntity.QUERY_EVENTS_BY_ID,EventsEntity.class)
                    .setParameter("user_id", userId)
                    .getSingleResult();
            Event ret=new Event();
            ret.setMonthly(eventsEntity.IsMonthlyReports());
            ret.setWarning(eventsEntity.IsWarningReports());
            return ret;
        }catch (Exception e){
            System.out.println("getEvents problem");
            throw e;
        }
    }

    public List<Integer> getAllUserIdsWithMonthly() {
        try{
            List<Integer> ret= em.createNamedQuery(EventsEntity.QUERY_EVENTS_GET_ID_BY_MONTHLYSET,Integer.class)
                    .getResultList();
            return ret;
        }catch (Exception e){
            System.out.println("getEvents problem");
            throw e;
        }
    }
}
