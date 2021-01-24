/**
 Simple node class for linked list structure - holds three strings (course, name, description)
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */
public class MenuItem {
    private final String course, name;
    private String description;
    MenuItem next, prev;

    public MenuItem(String c, String n, String d){
        course = c;
        name = n;
        description = d;
    }

    public MenuItem(String c, String n){ // method overloading for dishes that do not have descriptions
        course = c;
        name = n;
    }

    public String getCourse(){return course;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public String getDish(){ // when getting the entire dish, must check if there is a description
        if (description == null)
            return course + "\n" + name;
        else
            return course + "\n" + name + "\n" + description;
    }

    public String toString(){return getDish();}
}
