import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
/**
 Object that interprets the Dana Foods website and stores menu in a Menu object for a single day's menu
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */
public class DayMenu {
    private final Menu menu;
    private final String date;

    public DayMenu(String u) throws MalformedURLException {
        URL url = new URL(u);
        String m = retrieveMenu(url); // getting the raw html code from the link
        date = getDate(m); // fetching the date
        menu = createMenu(m); // formatting that raw code into the Menu object
    }

    public static String getDate(String m){ // simple method that searches the html code for where the date is stored
        m = m.substring(m.indexOf("Daily Specials | ") + 17);
        return m.substring(0, m.indexOf("</div>"));
    }

    public static Menu createMenu(String m) { // sorts through the html code to create MenuItems
        Menu menu = new Menu();

        while (m.contains("<span class=\"MenuSection\">")){ // while there is another food item in the code
            m = m.substring(m.indexOf("<span class=\"MenuSection\">") + 26); // substring + the # of chars to get the endpoint as index 0
            String c = m.substring(0, m.indexOf("</span>")); // course name (eg. pizza)

            m = m.substring(m.indexOf("<span class=\"ItemName\">") + 23);
            String n = m.substring(0, m.indexOf("<")); // name of dish (eg. cheese pizza)

            if (m.indexOf("<span class=\"MenuSection\">") > m.indexOf("<div class=\"ItemDescription\">")
                    && m.contains("<div class=\"ItemDescription\">")){ // if there is a description before the next menu item
                m = m.substring(m.indexOf("<div class=\"ItemDescription\">") + 29);
                String d = m.substring(0, m.indexOf("<")); // description of dish
                menu.addMenuItem(new MenuItem(c, n, d));
            }

            else // otherwise create a menu item with no description
                menu.addMenuItem(new MenuItem(c, n));
        }

        return menu;
    }

    public static String retrieveMenu(URL url){ // gets the raw html code from a url
        String m = "";
        try {
            BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                m = m.concat(line);
            }
        } catch (IOException ignored){} // catching any exception and ignoring it

        return m;
    }

    public Menu getMenu(){return menu;}

    public String getDate(){return date;}

    public String toString(){
        String s = "";

        for (int i = 0; i < menu.size(); i++){
            s = s.concat(menu.getMenuItem(i).getDish() + "\n");
            s = s.concat("\n");
        }

        return s;
    }
}
