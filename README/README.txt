#README

SYSC 3303 Assignment 2

Introduction to UDP

Author: Alexander Hum 101180821

Project Description:
The purpose of the following assignment is to develop a program that used DatagramSockets and DatagramPackets that send messages starting with the client to host to server, then back to host then server.

File Names:
Client: Client is responsible for sending requests to the host and then having to read from the host. It alternates from read and write requsts.
Host: Host acts as a relay between client and server as it relays messages from each. Prints out all of the requsts back and fourth when sending and receiving from client and server.
Server: Server receives messages from host and processes the read and write requsts determing if it is a valid requst depending on the byte array format. Server then send a new byte array to the host depedning if it is a read or write requst.
Message: Prints output for the client, host, and server.

Set up instructions:
  1. Unzip the assignment submission.
  2. Open up IntelliJ IDE
  3. Open up the project folder in Intellij IDE
  4. Click on "src" folder and open the Client java class, Host java class, and Server java class.
  5. Run the main method in each of the three classes as followed in order; Server -> Host -> Client.