/**
 Stores MenuItem objects in a linked list format.
 ICS3U - Dr. Lenarcic
 @author Rafael Gacesa
 @version 05/02/20
 */
public class Menu {
    private MenuItem head;

    public Menu(){head = null;}

    public void addMenuItem(MenuItem i){ // to add menu items to the list
        MenuItem temp;

        if (head == null) // if there is no items in the list, the item being added is the head
            head = i;

        else { // otherwise, iterate through list until an empty spot is found
            temp = head;
            while (temp.next != null){
                temp = temp.next;
            }
            temp.next = i; // setting up next and previous references
            temp.next.prev = temp;
        }
    }

    public int size(){ // counts number of menu items in menu
        MenuItem temp;
        temp = head;

        int i = 1;
        while (temp.next != null){
            temp = temp.next;
            i++;
        }

        return i;
    }

    public MenuItem getMenuItem(int x){ // gets a specific item by iterating through menu x amount of times
        MenuItem temp = head;
        for (int i = 0; i < x; i++)
            temp = temp.next;
        return temp;
    }

    public String toString(){ // simple toString which appends the menu with new lines in between
        String s = "";
        MenuItem temp;
        temp = head;
        s = s.concat(temp.getDish() + "\n\n");

        while (temp.next != null){
            temp = temp.next;
            s = s.concat(temp.getDish() + "\n\n");
        }

        return s;
    }
}
