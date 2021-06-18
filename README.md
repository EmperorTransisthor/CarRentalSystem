# Car Rental System
**Introduction** <br />
This is final project for Java Programming subject. It uses **log4j**, **JDBC** and **Swing** frameworks. I've designed windows using JFormDesigner. Unfortunately code was prepaired in hurry, thus I didn't have time to polish it well. It barely has neccessary error handling like blank textfields, negative values in price and earlier dates than reservation date. <br />

**Fast overview** <br />
I have been experimenting with Adapter pattern what resulted in JFrameSet class, which takes ```JPanel``` as an argument alongside title of the frame and returns ```JFrame``` with input JPanel aggregated. Main menu is a class where a user can open various windows of different sections of a code. It manages different frames which are singletons.

**Database** <br />
Database used in project is partial implementation of the final project for Databases subject. It was vastly modified to simplify the usage as from a certain point I intended to make things as easy as possible since I was short on time with deadlines, database is still in at least 4NF though. <br />

**GUI** <br />
Layout is very primitive, many features are not the way they should be, like not making connection to DB global (it would save dozens of code lines). <br />

**Future plans** <br />
Room for improvement is so vast, that it is hard to even just point them. The first thing I would change is the structure of the program and idea behind renting and returning the car. Now, availability flag serves no purpose as it is so poorly designed that the program takes only rental orders when considering car availability.
