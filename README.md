

## **What did we make?**


We programmed a semi-autonomous vehicle software for the smart shield car, which enables the vehicle to execute commands provided in natural language. The software accepts both .txt files and console input for the text. We also allow the application on users' computers to communicate with the car/emulator via the internet, thus allowing for more remote control. For navigating the GUI, we offer multi-language support, from the nationalities of our members around the world. Finally, there is a coordinate system, simultaneously allowing the car to internally map out its path and return to previously recorded coordinates.	


## **Why did we make it? (What problem does it solve?)**


The natural language control of the vehicles could be very useful in certain cases. For one, it lowers the barrier of entry as any English speaker could easily operate the vehicle without any prior knowledge of manual vehicle control.  We also have the function to accept .txt files and execute the commands therein would make it possible to semi-automate the vehicle operation. This could potentially be useful in scenarios where tasks are highly repeatable like transporting things in a warehouse.


## **How did we make it? (What kind of technology did we use?)**


For internet communication, we used MQTT to establish a link between the client and the car. The client is able to see the video feed, read the sensors and it can also control the car either manually or through autonomous commands. For the autonomous command execution in the car, we designed our own protocol that sends the commands with CSV format. It’s possible to send one or many commands at the same time. . The natural language processing used in the project was custom coded and does not rely on any external NLP libraries.

## **Hardware/Software Architecture**

![image](https://user-images.githubusercontent.com/90137505/170888250-3ab91a06-2f37-4888-9002-f24da0fc3478.png)


## **Set up**


Open up the code, and ensure you have downloaded or have all the specified requirements in the manual. This means Java 17 or newer, JavaFX, and internet connectivity. For intelliJ, go to run -> edit configurations. Add VM options and paste in: “--module-path "JAVA FX LIB FOLDER" --add-modules javafx.controls,javafx.fxml”, where JAVA FX LIB FOLDER is the directory to the lib folder given in the downloaded java fx file. 


## **User Manual**


### **About**
We made a software to allow users to command a vehicle in natural language. The user must enter correct credentials for a session or for a profile, which will then let them enter the steering view and allow remote access of the car.


### **System Requirements**

* OS: Windows 10/Mac OS/Linux

* Software requirements: The system is required to have Java 17 installed along with the corresponding javaFX library

* Internet Access: Required

### **Screens** 

### **Start view**
The start view has 5 “tabs” on the left hand side. They are: 

* **Log in** - After creating a profile, you can simply log in with a username and password

* **Anonymous** - If you do not want a profile, you can instead create a simple session by providing the car name, host, and port

* **Create Profile** - If you want to log in easily in the future, you can create a profile, which requires all the fields mentioned in the previous 2 tabs.

* **Settings** - We provide support in 5 languages, which includes: English, Swedish, Polish, Korean, and Persian.

* **Exit** - Closes the GUI

### **Steering View** 
Once you have logged in, the GUI switches to the steering view and offers 5 options in the form of tabs, once again on the left hand side

* **Manual** - Allows the user to manually drive using WASD

* **Command** - Allows the user to type or submit text files for commands in the form of natural language

* **Settings** - Here, the user can either turn the automatic collision detection on or off.

* **Quit Session** - The user will return to the previous start view.

* **Exit** - Closes the GUI


### **FAQ**


**Q**: The steering view screen says its loading, why don’t I have vehicular access?

**A**: The app does not detect the car and most likely, compiling and running the car should solve it. If you have already done so, reload the car and check that all entered details are correct and that you have wifi access.


**Q**: I want the car to stop at a certain distance but the car automatically pauses, why?

**A**: The car has a built collision-detection-and-break system to ensure the car doesn’t crash into any obstacles. However, this can be disabled in the settings of the steering view; just be careful!


**Q**: I want to try out the features to their full extent but the default map is boring. Is there anything I can do?

**A**: We have a custom made map exactly for this purpose! In the SMCE change the map from default to the only other option listed below it.


Youtube Link -> [https://www.youtube.com/watch?v=WvFQEkAwPns]([url](https://www.youtube.com/watch?v=WvFQEkAwPns))
