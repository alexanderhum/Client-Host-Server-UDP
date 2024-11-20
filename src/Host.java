import java.io.*;
import java.net.*;

public class Host {
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendSocket, receiveSocket;
    public Host()
    {
        try {
            // construct a datagram socket which will be sending and receiving packets
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(23);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * send and receive messages from and to the server and client
     */
    public void sendAndReceive()
    {
        while(true) {
            // creates a new empty byte array for the received packet
            byte[] data = new byte[100];
            receivePacket = new DatagramPacket(data, data.length);

            System.out.println("Host: Waiting for Packet.\n");

            // receives the datagram packet
            try {
                System.out.println("Waiting...");
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }
            int port = receivePacket.getPort();
            data = receivePacket.getData();

            // creates a new byte array given the received packets length
            byte[] temp = new byte[receivePacket.getLength()];

            // copys the received packet into a new byte array
            for (int i = 0; i < receivePacket.getLength(); i++) {
                temp[i] = data[i];
            }
            int len = receivePacket.getLength();

            // prints what the host received in the datagram packet
            System.out.println("Host: Packet received:");
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(temp);
            System.out.print("\nString: ");
            Message.printByteToString(temp);

            // slows down to 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // creates a new datagram packet to send to port 69
            sendPacket = new DatagramPacket(temp, receivePacket.getLength(),
                    receivePacket.getAddress(), 69);

            // prints out the bytes and a string representation of what the host is sending
            System.out.println("\nHost: Sending packet:");
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(sendPacket.getData());
            System.out.print("\nString: ");
            Message.printByteToString(sendPacket.getData());

            // sens the datagram packet to server
            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("Host: packet sent\n");

            // creates a new byte array that the host receives from server
            byte bytesFromServer[] = new byte[100];
            receivePacket = new DatagramPacket(bytesFromServer, bytesFromServer.length);

            // receives the datagram packet from the server
            try {
                sendSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // gets the data from the received packet
            bytesFromServer = receivePacket.getData();
            // creates a new byte array given the length of the byte array from the received packet
            byte[] temp2 = new byte[receivePacket.getLength()];

            // copy's the byte array from the datagram packet into the new byte array
            for (int i = 0; i < receivePacket.getLength(); i++) {
                temp2[i] = bytesFromServer[i];
            }

            // prints out what the host receives from the server
            System.out.println("Host: Packet received from server:");
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(temp2);

            // creates a new datagram packet to be sent back to the client
            sendPacket = new DatagramPacket(temp2, receivePacket.getLength(),
                    receivePacket.getAddress(), port);

            // sends the datagram packet
            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("\nSent to Client");
        }
    }

    public static void main( String args[] ) {
        Host c = new Host();
        c.sendAndReceive();
    }
}
