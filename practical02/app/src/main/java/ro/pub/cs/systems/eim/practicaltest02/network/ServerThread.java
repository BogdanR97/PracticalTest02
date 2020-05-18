package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;


public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;

    private Integer serverPort;

    public ServerThread(EditText serverPortEditText) {
        this.serverPort = Integer.parseInt(serverPortEditText.getText().toString());
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v(Constants.TAG, "accept()-ed: " + socket.getInetAddress());
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, this);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
