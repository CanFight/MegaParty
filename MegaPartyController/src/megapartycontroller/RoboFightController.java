package megapartycontroller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author Niklas
 */
public class RoboFightController implements Runnable {

    public static int UPDATES_PER_SEC = 20;
    public static int FRAMES_PER_SEC = 20;
    private int framesC, updatesC;
    private double nsPerFrame;
    private boolean isRunning;
    private InputHandler inputHandler;
    private GameFrame frame;
    private DatagramSocket clientSocket;
    private volatile boolean runNet;
    private volatile ArrayList<String> serverList;
    private NetworkInterface netFace;
    private InetAddress netAddress;

    public RoboFightController() {
        inputHandler = new InputHandler(this);
        frame = new GameFrame(this, inputHandler);
        isRunning = true;
        runNet = true;
        serverList = new ArrayList();
        try {
            clientSocket = new DatagramSocket();
        } catch (Exception e) {
        }
        findCorrectNetworkInterface();
        checkForServer();
        new Thread(this).start();
    }

    @Override
    public void run() {

        final double nsPerUpdate = 1000000000.0 / UPDATES_PER_SEC;
        nsPerFrame = 1000000000.0 / FRAMES_PER_SEC;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;
        double unprocessedTimeFPS = 0;

        int frames = 0;
        int updates = 0;

        long frameCounter = System.currentTimeMillis();

        while (isRunning) {

            long currentTime = System.nanoTime();
            long passedTime = currentTime - lastTime;
            lastTime = currentTime;
            unprocessedTime += passedTime;
            unprocessedTimeFPS += passedTime;

            if (unprocessedTime >= nsPerUpdate) {
                unprocessedTime = 0;
                updates++;
                update();
            }

            if (unprocessedTimeFPS >= nsPerFrame) {
                unprocessedTimeFPS = 0;
                render();
                frames++;
            }

            if (System.currentTimeMillis() - frameCounter >= 1000) {
                framesC = frames;
                updatesC = updates;
                frames = 0;
                updates = 0;
                frameCounter += 1000;
            }

        }
        dispose();
    }

    public void checkForServer() {

        Thread netThread = new Thread() {
            @Override
            public void run() {
                try {
                    DatagramSocket serverSocket = new DatagramSocket(15255, netAddress);
                    String sentence;
                    DatagramPacket receivePacket;
                    byte[] receiveData = new byte[1024];
                    while (runNet) {
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        sentence = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                        String[] data = sentence.split("/");
                        if (data[1].equalsIgnoreCase("IMS")) {
                            addServerToList("" + receivePacket.getAddress(), data[2]);
                        }
                    }
                } catch (Exception e) {
                }
            }

        };
        netThread.start();
    }

    private void addServerToList(String ip, String port){
        boolean serverAlreadyOnList = false;
        for(String s: serverList){
            if(s.equalsIgnoreCase(ip + "/" + port)){
                serverAlreadyOnList = true;
            }
        }
        if(!serverAlreadyOnList){
            serverList.add(ip + "/" + port);
            frame.getServerListBox().addItem(ip);
            System.out.println("server detected: " + ip);
        }
    }
    
    private void findCorrectNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (networkInterface.getHardwareAddress() != null && !networkInterface.getDisplayName().toLowerCase().contains("vm")
                            && addr.getHostAddress().substring(0, 1).equalsIgnoreCase("1")) {
                        netFace = networkInterface;
                        netAddress = addr;
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("could not find network interface");
        }
    }

    private void update() {
        if(frame.getServerListBox().getSelectedIndex() != 0){
            String[] data = serverList.get(frame.getServerListBox().getSelectedIndex() - 1).split("/");
            frame.setIPText(data[1]);
            frame.setPortText(data[2]);
        }
        sendData();
    }

    private void render() {
        frame.draw();
    }

    public void dispose() {
        clientSocket.close();
        System.exit(0);
    }

    private void sendData() {
        if (frame.connectButton()) {
            try {
                InetAddress IPAddress = InetAddress.getByName(frame.getIp());
                String name = frame.getName();
                String facing = "0.00";
                String fireFacing = "" + inputHandler.getFireFacing();
                String length = "" + inputHandler.getResultForce();
                String fireForce = "" + inputHandler.getFireForce();
                try {
                    fireFacing = fireFacing.substring(0, 4);
                } catch (Exception e2) {
                }
                try {
                    fireForce = fireForce.substring(0, 4);
                } catch (Exception e2) {
                }
                try {
                    length = length.substring(0, 4);
                } catch (Exception e2) {
                }
                try {
                    facing = ("" + inputHandler.getResultFacing()).substring(0, 4);
                } catch (Exception e2) {
                }
                try {
                    name = name.substring(0, 16);
                } catch (Exception e2) {
                }
                String sentence = name + "/" + fireFacing + "/" + fireForce + "/" + facing + "/" + length + "/";
                System.out.println("PACK: " + sentence);
                byte[] sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, frame.getPort());
                clientSocket.send(sendPacket);
            } catch (Exception e) {
                System.err.println("failed to send package");
                e.printStackTrace();
            }
        }
    }

}
