package service.events;

import dao.EventsDao;
import dao.UserDao;
import rest.Input.EventsInput;
import rest.Response.EventResponse;
import service.authentication.LoggedIn;
import service.authentication.LoggedInService;
import service.budget.Budget;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Date;

@Singleton
public class EventService {

    @Inject
    private EventsDao eventsDao;

    @Inject
    private MailService mailService;

    @Inject
    private LoggedInService loggedInService;


    //@Schedule(second="*/10", minute="*",hour="*", persistent=false)

    @Schedules ({
            @Schedule(dayOfMonth="1")
//            @Schedule(hour="0",minute = "52")
    })
    public void automaticallyScheduled() {
        sendMonthlyMails();
    }


    private void sendMonthlyMails() {
        try{
            mailService.sendMonthly(null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("Monthly reminder messages sent at: " + LocalDateTime.now());
    }

    public void eventsUpdate(EventsInput eventsInput) {
        String name = loggedInService.getCurrentUserName();
        eventsDao.updateMonthly(name,eventsInput.isMonthlyreport());
        eventsDao.updateWarning(name,eventsInput.isWarningreport());
    }

    public void eventsCreate(String username) {
        eventsDao.createEvents(username);
    }

    public EventResponse eventsGet() {
        String username = loggedInService.getCurrentUserName();
        EventResponse eventResponse= new EventResponse();
        Event event=eventsDao.getEvents(username);
        eventResponse.setMonthly(event.isMonthly());
        eventResponse.setWarning(event.isWarning());
        return eventResponse;
    }

    public void limitWarning(Budget budget) throws Exception{
        String username = loggedInService.getCurrentUserName();
        Event event=eventsDao.getEvents(username);
        if(event.isWarning()){
            mailService.sendWarning(username,budget);
        }
    }
}
