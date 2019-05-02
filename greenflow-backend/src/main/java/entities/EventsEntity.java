package entities;

import javax.persistence.*;

@Entity
@NamedQueries(
        @NamedQuery(name = "Events.getEventById", query = "select e from EventsEntity e where e.user_id = :user_id")
)
@Table(name = EventsEntity.tableName)
public class EventsEntity {
    public static final String tableName= "Event";
    public static final String QUERY_EVENTS_BY_ID = "Events.getEventById";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int user_id;

    @Column
    private boolean monthlyReports;

    @Column
    private boolean warningReports;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean IsMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(boolean monthlyReports) {
        this.monthlyReports = monthlyReports;
    }

    public boolean IsWarningReports() {
        return warningReports;
    }

    public void setWarningReports(boolean warningReports) {
        this.warningReports = warningReports;
    }
}
