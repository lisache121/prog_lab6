package client;

import exceptions.ConnectionException;
import interaction.CommandRequest;
import interaction.ResponseMessage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    private InetAddress host;
    private final int port;
    private DatagramSocket socket;
    private final int MAX_ATTEMPTS = 5;
    private int TIMEOUT;
    private final int BUFFER_SIZE = 4096;


    public Client(int port, int TIMEOUT) {
        this.port = port;
        this.TIMEOUT = TIMEOUT;
    }


    public void startConnection() {
        try {
            this.host = InetAddress.getLocalHost();
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
        } catch (IOException e) {
            throw new ConnectionException("cannot create a socket");
        }
    }

    public void run() {
        int attemptCount = MAX_ATTEMPTS;
        while (attemptCount > 0) {
            try {
                startConnection();
                System.out.println("client started");
                return;
            } catch (ConnectionException e) {
                attemptCount--;
                if (attemptCount != 0) {
                    System.out.println(attemptCount + " attempts to get response from server left");
                }
                else throw new ConnectionException("socket creation failed");
            }
        }
    }

    public void sendRequest(CommandRequest request) {
        try {
            byte[] buf = serialize(request);
            int len = buf.length;
            DatagramPacket requestPacket = new DatagramPacket(buf, len, InetAddress.getLocalHost(), port);
            socket.send(requestPacket);
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while sending request");
        }
    }

    public String handleRequest(CommandRequest request) {
        try {
            sendRequest(request);
            return getResponse().getMessage();
        } catch (ConnectionException e) {
            return(e.getMessage());
        } catch (ResponseTimeoutException e) {
            return("Error — no response from the server. Try again or close the application.");
        } catch (InvalidResponseException e) {
            return("Error — invalid response from the server. Try again or close the application");
        }
    }

    private byte[] getResponsePacket() {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket responsePacket = new DatagramPacket(buf.array(), BUFFER_SIZE);
        int attemptCount = MAX_ATTEMPTS;
        while (attemptCount > 0) {
            try {
                socket.receive(responsePacket);
                return buf.array();
            } catch (SocketTimeoutException | PortUnreachableException e) {
                attemptCount--;
                if (attemptCount != 0)
                    System.out.println(attemptCount + " attempts to get response from server left");
                else throw new ResponseTimeoutException();
            } catch (IOException e) {
                throw new ConnectionException("something went wrong while getting response");
            }
        }
        throw new ConnectionException("receiving response failed");
    }

    private ResponseMessage getResponse() {
        try {
            int packetCount = (Integer) deserialize(getResponsePacket());
            ByteBuffer buf = ByteBuffer.allocate(packetCount * BUFFER_SIZE);
            while (packetCount != 0) {
                buf.put(getResponsePacket());
                packetCount--;
            }
            return (ResponseMessage) deserialize(buf.array());
        } catch (ClassNotFoundException | IOException e) {
            throw new InvalidResponseException();
        }
    }

    public  byte[] serialize(CommandRequest request) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            byteArrayOutputStream.flush();
            byte[] buf = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return buf;
        }
    }

    public Object deserialize(byte[] buf) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object response = objectInputStream.readObject();
            byteArrayInputStream.close();
            objectInputStream.close();
            return response;
        }
    }
}
