import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Client {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket;

    public Client()
    {
        try {
            // construct a datagram socket which will be sending and receiving packets
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Send and receives packets from and to Host
     */
    public void sendAndReceive() {
        // loops through 11 total requests and changes the byte each time if i is odd or even
        for(int i=0; i<=10; i++) {
            byte readWriteInvalidByte;
            if(i==10) {
                readWriteInvalidByte = 0;
            } else if (i % 2 == 0) {
                readWriteInvalidByte = 1;
            } else {
                readWriteInvalidByte = 2;
            }
            //initializes variables and bytes
            Byte zeroByte = 0;
            String s = "test.txt";
            String netasciiName = "netascii";

            // gets the bytes from the text strings above
            byte[] sBytes = s.getBytes();
            byte[] netasciiNameBytes = netasciiName.getBytes();

            // creates and arraylist of bytes and adds the bytes from the text strings
            // ArrayList is able to manipulated and changed if it is a read or write request
            // formats the bytes in the required order
            ArrayList<Byte> byteArrayList = new ArrayList<>();
            byteArrayList.add(zeroByte);
            byteArrayList.add(readWriteInvalidByte);
            for (byte b : sBytes) {
                byteArrayList.add(b);
            }
            byteArrayList.add(zeroByte);
            for (byte x : netasciiNameBytes) {
                byteArrayList.add(x);
            }
            byteArrayList.add(zeroByte);

            // to be able to send using a packet must be converted into a byte array
            byte[] msg = new byte[byteArrayList.size()];
            for (int z = 0; z < byteArrayList.size(); z++) {
                msg[z] = byteArrayList.get(z);
            }

            // prints out the bytes that the client is going to send to the host
            System.out.println("\nClient: sending to Host containing:");
            System.out.print("Bytes: ");
            Message.printByteFromArray(msg);
            System.out.println("String: ");
            Message.printByteToString(msg);

            // construct a new datagram packet to port 23
            try {
                sendPacket = new DatagramPacket(msg, msg.length,
                        InetAddress.getLocalHost(), 23);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // prints out what is in the packet that is about to be sent to the host
            System.out.println("\nClient: Sending a packet to Host:");
            int len = sendPacket.getLength();
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(sendPacket.getData());
            System.out.print("\nString: ");
            Message.printByteToString(sendPacket.getData());

            // sends the datagram packet using the sendReceiveSocket
            try {
                sendReceiveSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("Client: Packet sent to Host.\n");

            // creates a byte array given a capacity of bytes as 100
            byte data[] = new byte[100];
            // creates new receive datagram packet
            receivePacket = new DatagramPacket(data, data.length);

            // receives the packet
            try {
                sendReceiveSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            // creates a new byte array and copys the bytes into the new array giving the new array that specific size
            byte temp[] = new byte[receivePacket.getLength()];
            for (int j = 0; j < receivePacket.getLength(); j++) {
                temp[j] = data[j];
            }

            // prints out what the client receives back from the host
            System.out.println("Client: Packet received from Host:");
            len = receivePacket.getLength();
            System.out.println("Containing: ");
            System.out.print("Bytes: ");
            Message.printByteFromArray(temp);
        }

        // close socket
        sendReceiveSocket.close();
    }

    public static void main(String args[])
    {
        Client c = new Client();
        c.sendAndReceive();
    }
}