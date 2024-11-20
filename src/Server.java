import java.io.*;
import java.net.*;

public class Server {
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendSocket, receiveSocket;
    public Server()
    {
        try {
            // construct a datagram socket which will be sending and receiving packets
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(69);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * receives the datagram packet and echos to and from the Host
     * @throws Exception
     */
    public void receiveAndEcho() throws Exception {
        while(true) {
            // creates a new empty byte array for the received packet
            byte data[] = new byte[100];
            receivePacket = new DatagramPacket(data, data.length);

            System.out.println("Server: Waiting for Packet.\n");

            // receives the datagram packet from the host
            try {
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }
            // gets the data from the datagram packet
            data = receivePacket.getData();
            // creates a new byte array given the length of the new array received from the packet
            byte[] temp = new byte[receivePacket.getLength()];
            // copy's the data grom the datagram packet into the new byte array
            for (int i = 0; i < receivePacket.getLength(); i++) {
                temp[i] = data[i];
            }

            // prints what the server received in the datagram packet
            System.out.println("Server: Packet received:");
            int len = receivePacket.getLength();
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(temp);
            System.out.print("\nString: ");
            Message.printByteToString(temp);

            // checks if the data from the datagram packet is valid request
            isValidRequest(temp);

            // slows down by a second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // creates a new byte array of four bytes that checks the validity of the request
            byte[] test = validReadWriteRequest(temp);

            // creates the new datagram packet with the new array
            sendPacket = new DatagramPacket(test, test.length,
                    receivePacket.getAddress(), receivePacket.getPort());

            // prints what the server is about to send to the host
            System.out.println("\nServer: Sending packet:");
            len = sendPacket.getLength();
            System.out.print("Containing: ");
            Message.printByteFromArray(test);

            // sens the datagram packet to the host
            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("\nServer: packet sent to host\n");
        }
    }

    /**
     * checks what request if it is a read or write request
     * @param byteArray that holds bytes that is used to send to and from client, host, and server
     * @return a byte[] with four bytes if it is a read or write request
     */
    private static byte[] validReadWriteRequest(byte[] byteArray) {
        if(byteArray[0]==0 && byteArray[1]==1) {
            return new byte[] {0, 3, 0, 1};
        } else if(byteArray[0]==0 && byteArray[1]==2) {
            return new byte[] {0, 4, 0, 0};
        } else {
            throw new IllegalArgumentException("Invalid request");
        }
    }
    /**
     * checks to see if the request is a valid request checking if the byte array starts, ends, and in the middle has a
     * zero byte. Also checks if the second byte is a 1 or 2.
     * @param byteArray that holds bytes that is used to send to and from client, host, and server
     * @return boolean returns true if the input is valid
     */
    private static boolean isValidRequest(byte[] byteArray) {
        boolean firstZero = byteArray[0] == 0;
        boolean middleZero = false;
        boolean secondByte = false;
        boolean lastZero = byteArray[byteArray.length-1] == 0;
        // if the byte array is null throw an exception
        if(byteArray == null) {
            throw new IllegalArgumentException("Invalid Request");
        }
        // if the first byte and last byte are not zero throw exception
        if(!firstZero || !lastZero) {
            throw new IllegalArgumentException("Invalid Request");
        }
        // returns true if the second byte is either a 1 or 2
        if(byteArray[1]==1 ||byteArray[1]==2) {
            secondByte = true;
        }
        // checks the middle of teh byte array to see if there is a zero byte
        for(int i=0; i<byteArray.length-1; i++) {
            if(byteArray[i]==0) {
                middleZero = true;
                break;
            }
        }
        // returns true if the input request is valid
        if(firstZero && secondByte && middleZero && lastZero) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Request");
        }
    }

    public static void main( String args[] ) throws Exception {
        Server c = new Server();
        c.receiveAndEcho();
    }
}