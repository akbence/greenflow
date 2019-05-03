package service.events;

import dao.EventsDao;
import dao.UserDao;
import rest.Input.EventsInput;
import rest.Response.EventResponse;
import service.authentication.LoggedIn;
import service.authentication.LoggedInService;

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
    private LoggedInService loggedInService;
    @Schedules ({
            @Schedule(dayOfMonth="1"),
            @Schedule(hour="0",minute = "54")
    })
    public void automaticallyScheduled() {
        fireEvent();
    }


    private void fireEvent() {
        System.out.println("fired at: " + LocalDateTime.now());
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
}