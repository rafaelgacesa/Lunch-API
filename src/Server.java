import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
/**
 Server class which takes the menus and organizes the webserver structure.
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */
public class Server {
    private HttpServer api;
    private final int port;

    public Server(DayMenu m, int port) throws IOException {
        this.port = port;
        api = HttpServer.create(new InetSocketAddress(port), 0); // creating the server to listen on the chosen port
        setDayLink(m.getMenu(), m.getDate()); // calling method to set up link structure
    }

    public Server(WeekMenu wm, int port) throws IOException {
        this.port = port;
        api = HttpServer.create(new InetSocketAddress(port), 0);
        setWeekLink(wm.getWeekMenu(), wm.getWeekDates());
    }

    public Server(DayMenu m, WeekMenu wm, int port) throws IOException { // method overloading for different types of menus
        this.port = port;
        api = HttpServer.create(new InetSocketAddress(port), 0);
        setDayLink(m.getMenu(), m.getDate());
        setWeekLink(wm.getWeekMenu(), wm.getWeekDates());
    }

    public void start(){api.start();} // public facing method to start server

    public void restart(DayMenu m, WeekMenu wm) throws IOException { // method to recreate the server with a new menu
        api.stop(0); // stopping the current menu server
        api = HttpServer.create(new InetSocketAddress(port), 0); // creating a new server (using same port)
        setDayLink(m.getMenu(), m.getDate()); // setting the link structure with the provided new menu
        setWeekLink(wm.getWeekMenu(), wm.getWeekDates()); // doing the same for the week menu
        api.start(); // starting the new server
    }

    private void setDayLink(Menu m, String d){ // sets up the link system for day menus
        api.createContext("/menu", new Handler(m.toString())); // two basic links for entire menu and date
        api.createContext("/menu/date", new Handler(d));

        for (int i = 0; i < m.size(); i++){ // iterates through the list of MenuItems and creates a link for each of their attributes
            api.createContext("/menu/" + i, new Handler(m.getMenuItem(i).getDish()));
            api.createContext("/menu/" + i + "/0", new Handler(m.getMenuItem(i).getCourse()));
            api.createContext("/menu/" + i + "/1", new Handler(m.getMenuItem(i).getName()));
            if (m.getMenuItem(i).getDescription() != null) // creating page for description
                api.createContext("/menu/" + i + "/2", new Handler(m.getMenuItem(i).getDescription()));
            else // if description is null page will be blank
                api.createContext("/menu/" + i + "/2", new Handler(""));
        }
    }

    private void setWeekLink(ArrayList<Menu> wm, ArrayList<String> wd){ // sets up the link system for week menus
        for(int i = 0; i < wm.size(); i++){ // iterating through each of the menus in the week menu
            Menu m = wm.get(i); // parameter wm stores the 5 Menu objects
            String d = wd.get(i); // parameter wd stores the 5 corresponding dates in String objects
            String day = WeekMenu.days.get(i); // the day of the week is pulled from a global variable for the link
            api.createContext("/menu/" + day, new Handler(m.toString()));
            api.createContext("/menu/" + day + "/date", new Handler(d));
            for (int x = 0; x < m.size(); x++){
                api.createContext("/menu/" + day + "/" + x, new Handler(m.getMenuItem(x).getDish()));
                api.createContext("/menu/" + day + "/" + x + "/0", new Handler(m.getMenuItem(x).getCourse()));
                api.createContext("/menu/" + day + "/" + x + "/1", new Handler(m.getMenuItem(x).getName()));
                if (m.getMenuItem(x).getDescription() != null)
                    api.createContext("/menu/" + day + "/" + x + "/2", new Handler(m.getMenuItem(x).getDescription()));
                else
                    api.createContext("/menu/" + day + "/" + x + "/2", new Handler(""));
            }
        }
    }

    public static int getWeekID(){ // method fetches the current ID of the current day's menu so the week menu class can function
        String m = "";
        int id;
        URL url;

        try {
            url = new URL("https://menu2.danahospitality.ca/hsc/menu.asp");
            BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                m = m.concat(line);
            }
        } catch (IOException ignored) {}

        m = m.substring(m.indexOf("<a class=\"button\" href=\""), m.indexOf(";print=true"));
        m = m.substring(m.indexOf("menu=") + 5);
        id = Integer.parseInt(m.substring(0, m.indexOf("&")));

        return id;
    }

    static class Handler implements HttpHandler{ // http handler method, using the Java handle() method and modifying it
        private final String response;

        public Handler(String r){response = r;} // Handler object takes the message

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRemoteAddress().getAddress() + " accessed " + httpExchange.getRequestURI()); // prints out debugging info (IP and page accessed)
            httpExchange.sendResponseHeaders(200, response.getBytes().length); // responding to GET requests with out message
            OutputStream output = httpExchange.getResponseBody();
            output.write(response.getBytes()); // writing message to the response body
            output.close();
        }
    }
}
