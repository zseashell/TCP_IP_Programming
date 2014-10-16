package beyond_the_basics;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class MultiCastReceiver {
    
    public static void main(String[] args) throws Exception {
        
        if (args.length != 2) {
            throw new IllegalArgumentException("Parameter(s): <Multicast Address> <Port>");
        }
        
        InetAddress address = InetAddress.getByName(args[0]);   // Multicast Address
        if (!address.isMulticastAddress()) {  // Test if multicast address
            throw new IllegalArgumentException("Not a multicast address!");
        }
        
        int port = Integer.parseInt(args[1]);
        
        System.out.println("Start waiting on multicast IP:" + address + " and port:" + port);
        
        MulticastSocket socket = new MulticastSocket(port);  // for receiving
        socket.joinGroup(address);
        
        socket.setSoTimeout(10000);
        
        // Receive a Datagram
        DatagramPacket packet = new DatagramPacket(new byte[100], 100);
        socket.receive(packet);
        
        byte[] msgBytes = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
        String message = new String(msgBytes);
        System.out.println("Recevied message from " + packet.getAddress() + " - " + msgBytes.length + " bytes: " + message);
      
        socket.close();
    }

}
