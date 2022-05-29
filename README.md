

##**What did we make?**


We programmed a semi-autonomous vehicle software for the smart shield car, which enables the vehicle to execute commands provided in natural language. The software accepts both .txt files and console input for the text. We also allow the application on users' computers to communicate with the car/emulator via the internet, thus allowing for more remote control. For navigating the GUI, we offer multi-language support, from the nationalities of our members around the world. Finally, there is a coordinate system, simultaneously allowing the car to internally map out its path and return to previously recorded coordinates.	


##**Why did we make it? (What problem does it solve?)**


The natural language control of the vehicles could be very useful in certain cases. For one, it lowers the barrier of entry as any English speaker could easily operate the vehicle without any prior knowledge of manual vehicle control.  We also have the function to accept .txt files and execute the commands therein would make it possible to semi-automate the vehicle operation. This could potentially be useful in scenarios where tasks are highly repeatable like transporting things in a warehouse.


##**How did we make it? (What kind of technology did we use?)**


For internet communication, we used MQTT to establish a link between the client and the car. The client is able to see the video feed, read the sensors and it can also control the car either manually or through autonomous commands. For the autonomous command execution in the car, we designed our own protocol that sends the commands with CSV format. It’s possible to send one or many commands at the same time. . The natural language processing used in the project was custom coded and does not rely on any external NLP libraries.
Hardware/Software Architecture

![image](https://user-images.githubusercontent.com/90137505/170888250-3ab91a06-2f37-4888-9002-f24da0fc3478.png)


##**Set up**


Open up the code, and ensure you have downloaded or have all the specified requirements in the manual. This means Java 17 or newer, JavaFX, and internet connectivity. For intelliJ, go to run -> edit configurations. Add VM options and paste in: “--module-path "JAVA FX LIB FOLDER" --add-modules 

Youtube Link -> https://www.youtube.com/watch?v=U1VlXerkwFc
