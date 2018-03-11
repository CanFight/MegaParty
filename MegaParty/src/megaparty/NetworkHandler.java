package megaparty;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

/**
 *
 * @author Niklas
 */
public class NetworkHandler {

    private int port;
    private NetworkInterface netFace;
    private InetAddress netAddress, broadcast;
    private SocketAddress sAd;
    private MegaParty program;
    private DatagramSocket clientSocket, serverSocket;
    private volatile boolean runNet;

    public NetworkHandler(NetworkInterface netFace, InetAddress netAddress, InetAddress broadcast, MegaParty program, int port) {
        this.netFace = netFace;
        this.netAddress = netAddress;
        this.program = program;
        this.port = port;
        this.broadcast = broadcast;
        runNet = true;
        try {
            clientSocket = new DatagramSocket();
        } catch (Exception e) {
        }
        try {
            serverSocket = new DatagramSocket();
        } catch (Exception e) {
        }
        runNetHandler();
    }
    
    public void terminateSockets(){
        runNet = false;
        clientSocket.close();
        serverSocket.close();
    }

    public void broadCastServer() {
        try {
            String broadCastFixed = broadcast.toString().substring(1, broadcast.toString().length());
            InetAddress IPAddress = InetAddress.getByName(broadCastFixed);
            String sentence = "/IMS/" + port + "/";
            byte[] sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 15255);
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            System.err.println("failed to send package");
            e.printStackTrace();
        }
    }

    public void runNetHandler() {

        Thread netThread = new Thread() {
            @Override
            public void run() {
                try {
                    DatagramSocket serverSocket = new DatagramSocket(port, netAddress);
                    System.out.println("port: " + port);
                    System.out.println("IP: " + netAddress.getHostAddress());
                    String sentence;
                    DatagramPacket receivePacket;
                    byte[] receiveData = new byte[1024];
                    while (runNet) {
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        sentence = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                        //System.out.println("Recieved " + receivePacket.getLength() + ": " + sentence);
                        decodePackage(sentence, "" + receivePacket.getAddress());
                    }
                } catch (Exception e) {
                }

            }

        };
        netThread.start();
    }

    public void decodePackage(String pack, String sender) {
        int senderID = 0;
        try {
            String[] data = sender.split("\\.");
            senderID = Integer.parseInt(data[3]);
        } catch (Exception e) {
            return;
        }
        int senderListID = program.getPlayerListIdByIP(senderID);
        if (senderListID == -1) {
            program.addPlayerToIdList(senderID);
        }
        if (senderListID == -1) {
            return;
        }

        try {
            String nick;
            double facingLeft, facingRight, pushLeft, pushRight;
            String[] data = pack.split("/");
            if (data[0].length() > 16) {
                data[0] = data[0].substring(0, 16);
            }
            if (data[1].length() > 5) {
                System.err.println("[Custom Output] Extra values at data2: " + data[1]);
                data[1] = data[1].substring(0, 4);
            }
            if (data[2].length() > 5) {
                System.err.println("[Custom Output] Extra values at data3: " + data[2]);
                data[2] = data[2].substring(0, 4);
            }
            if (data[3].length() > 5) {
                System.err.println("[Custom Output] Extra values at data4: " + data[3]);
                data[3] = data[3].substring(0, 4);
            }
            if (data[4].length() > 5) {
                System.err.println("[Custom Output] Extra values at data5: " + data[4]);
                data[4] = data[4].substring(0, 4);
            }
            nick = data[0];

            facingRight = Double.parseDouble(data[1]);
            pushRight = Double.parseDouble(data[2]);
            facingLeft = Double.parseDouble(data[3]);
            pushLeft = Double.parseDouble(data[4]);

            if (pushLeft > 1) {
                pushLeft = 1;
            }

            if (pushRight > 1) {
                pushRight = 1;
            }
            program.getPlayer(senderListID).setTimeSinceLastPack(0);
            program.getPlayer(senderListID).setNickName(nick);
            program.getPlayer(senderListID).setCtrlFacingLeft(facingLeft);
            program.getPlayer(senderListID).setCtrlFacingRight(facingRight);
            program.getPlayer(senderListID).setCtrlPushLeft(pushLeft);
            program.getPlayer(senderListID).setCtrlPushRight(pushRight);

        } catch (Exception e) {
            System.err.println("[Custom Output] Failed to update player");
            System.err.println("[Custom Output] Packet: " + pack);
            e.printStackTrace();
        }

    }
}
