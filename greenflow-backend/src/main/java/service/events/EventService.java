package service.events;

import dao.EventsDao;
import dao.UserDao;
import dao.transactions.TransactionDao;
import rest.Request.EventsInput;
import rest.Response.EventResponse;
import service.authentication.LoggedInService;
import service.budget.Budget;
import service.transaction.Transaction;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class EventService {

    @Inject
    private EventsDao eventsDao;

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private UserDao userDao;

    @Inject
    private MailService mailService;

    @Inject
    private LoggedInService loggedInService;


    //@Schedule(second="*/10", minute="*",hour="*", persistent=false)

    @Schedules ({
            @Schedule(dayOfMonth="1")
    })
//    @Schedule(second="*/10", minute="*",hour="*", persistent=false)
    public void automaticallyScheduled() throws Exception{
        sendMonthlySummary();
    }


    private void sendMonthlySummary() throws  Exception{
        List<Integer> userIds = eventsDao.getAllUserIdsWithMonthly();
        for (Integer userId : userIds){
            String username = userDao.getName(userId);
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            List<Transaction> transactions = transactionDao.getMonthlyTransactions(username,lastMonth.getYear(),lastMonth.getMonthValue());
            mailService.sendMonthly(transactions);
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

    public void sendLimitWarning(Budget budget) throws Exception{
        String username = loggedInService.getCurrentUserName();
        Event event=eventsDao.getEvents(username);
        if(event.isWarning()){
            mailService.sendWarning(username,budget);
        }
    }
}
