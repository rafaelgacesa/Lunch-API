import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 Driver class that fetches the menus and starts the server.
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */
public class Main {
    private static int dayOfMenu;

    public static void main(String[] args) throws IOException {
        // get and store the day of week of the original menu
        Calendar calendar = Calendar.getInstance();
        dayOfMenu = calendar.get(Calendar.DAY_OF_WEEK);

        System.out.println(Server.getWeekID());
        Server server = new Server(new DayMenu("https://menu2.danahospitality.ca/hsc/menu.asp?loc=48642&menu=111551"),
                // actual link for when school exists: https://menu2.danahospitality.ca/hsc/menu.asp?loc=48642
                new WeekMenu(111553), // week menu using random ID from a while ago
                // use Server.getWeekID() instead of set value when school exists
                80); // creating the server with a menu, weekmenu on port 80
        server.start();

        Timer timer = new Timer();
        TimerTask restart = new TimerTask() { // task to restart the server if it is a new day
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
                // check if it is a new day that is not a weekend day (server will run friday menu until monday)
                if((currentDay != dayOfMenu) && !(currentDay == Calendar.SATURDAY || currentDay == Calendar.SUNDAY)){
                    try {
                        // if it is a new day, restart the server using a new fetched day menu and week menu
                        server.restart(
                                new DayMenu("https://menu2.danahospitality.ca/hsc/menu.asp?loc=48642"),
                                new WeekMenu(Server.getWeekID()));
                    } catch (IOException ignored) {}
                    dayOfMenu = currentDay; // updating the current date of menu
                    System.out.println("Menu refreshing...");
                }
            }
        };
        timer.schedule(restart, 0, (60 * 60 * 1000)); // scheduling the task to run every 60 minutes
    }
}