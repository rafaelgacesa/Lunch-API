# LunchAPI
A simple API for access to the lunch information for HSC, providing the data for the current day as well as each day in the current week.
It functions by pulling the data from the Dana Foods menu servers.
The link structure works as follows:

**Today's menu:**

/menu - entire menu 

/menu/date - today's date

/menu/0 - specific dish

/menu/0/0 - specific text


**Week's menu:**

/menu/monday - monday's menu

/menu/monday/date - monday's date

/menu/monday/0 - specific dish

/menu/monday/0/0 - specific text

**/menu/x/y**

x:

0 - Junior/Montessori Snack

1 - Soup/Bowl

2 - Main Meal

3 - Veg. Main Meal

4 - Panini/Sandwich

5 - Pizza

6 - Junior/Montessori Meal


y:

0 - Course

1 - Name

2 - Description (optional)
