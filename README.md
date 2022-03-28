**What are you going to make?**

We are programming a semi-autonomous vehicle software for the smart shield car, which will enable the vehicle to execute commands provided in natural language. In its most basic form the software will accept .txt files or console input of the text, but the end goal is to include operation by voice commands. We also plan to allow the application on users' computers to communicate with the car/emulator via the internet, thus allowing for more remote control.

**Why will you make it?**
**What problem does it solve?**

The natural language control of the vehicles could be very useful in certain cases. For one, it lowers the barrier of entry as any English speaker could easily operate the vehicle without any prior knowledge of manual vehicle control. Another benefit is that the speech recognition function can make the software usable for people with disabilities. Lastly, the function to accept .txt files and execute the commands therein would make it possible to semi-automate the vehicle operation. This could potentially be useful in scenarios where tasks are highly repeatable like transporting things in a warehouse. 

**How are you going to make it?**
**What kind of technology are you going to use?**

For the speech to text we are planning to either use an open source speech-to-text toolkit like coqui-ai or a more sophisticated commercial product (we would use a free trial version). For internet communication, we are going to utilize socket programming and communicate through a TCP connection. For natural language processing, we will likely use a java nlp library like OpenNLP or something similar. There is also a possibility that we will code a basic command recognition ourselves or do a mix of both options.
