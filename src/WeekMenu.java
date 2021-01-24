import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
/**
 Uses methods from the DayMenu class to manage a full weeks menus.
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */

public class WeekMenu{
    private final ArrayList<Menu> weekMenu;
    private ArrayList<String> weekDates;
    public static final ArrayList<String> days = new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday")); // global variable

    public WeekMenu(int id) throws MalformedURLException {
        String u = "https://menu2.danahospitality.ca/hsc/menu.asp?loc=48642&menu="; // HSC's url without the ID
        URL url = new URL(u + id); // creating the URL object and adding the ID fetched by the GetID method in DayMenu
        String m = DayMenu.retrieveMenu(url); // again, using DayMenu's static methods to get the raw html for current day menu
        int modifier = retrieveDay(m); // checking which day of the week it is so the id modifier can be figured out (0 for monday, 1 for tuesday etc.)
        weekMenu = retrieveWeekMenu(id - modifier, u); // calling the custom WeekMenu method to retrieve all 5, using the modifier so the link submitted is Monday's menu
    }

    private ArrayList<String> retrieveWeekDays(ArrayList<String> mS){ // retrieves dates from mS, which contains the raw HTML of all 5 day's menus
        ArrayList<String> d = new ArrayList<>();

        for (String s : mS){
            d.add(DayMenu.getDate(s)); // using the static method from DayMenu to retrieve the date from the menu
        }

        return d; // 5 dates in an ArrayList
    }

    private ArrayList<Menu> retrieveWeekMenu(int id, String u) throws MalformedURLException {
        ArrayList<Menu> menus = new ArrayList<>(); // stores the 5 menu objects
        ArrayList<String> mS = new ArrayList<>(); // stores the 5 raw HTML Strings

        for (int i = 0; i < days.size(); i++){
            URL url = new URL(u + (id+i)); // starting at the base ID and adding 1-4 to get the next day
            String m = DayMenu.retrieveMenu(url); // getting the raw html for each one using DayMenu class
            mS.add(m);
            menus.add(DayMenu.createMenu(m)); // formatting raw data into Menu
        }

        weekDates = retrieveWeekDays(mS); // calling the retrieve date method once all 5 raw HTML menus are retrieved

        return menus;
    }

    private int retrieveDay(String m){ // finds the current day so the ID offset can be found
        int day = -1; // initialized as -1 so any errors can be caught

        for (String s : days){ // for each string day name (eg. 'tuesday')
            if (m.toLowerCase().contains(s)){ // if it is found in the raw html, that is the current day
                day = days.indexOf(s); // the index of the day is the offset (eg. monday = 0, tuesday = 1, etc.)
                break;
            }
        }

        return day; // this will be subtracted from the original ID to find Monday's ID
    }

    public ArrayList<Menu> getWeekMenu(){return weekMenu;}

    public ArrayList<String> getWeekDates(){return weekDates;}

}
