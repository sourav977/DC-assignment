/*
 * Suzuki-Kasami Broadcast Algorithm for implementing distributed mutual exclusion
 * 
 * Sourav Patnaik
 * 
 */

import java.net.ServerSocket;
import java.net.Socket;

public class ListenToBroadcast extends Thread {

    int port = 0;
    Site aSite = null;

    public ListenToBroadcast(Site thisSite, int port) {
        this.port = port;
        this.aSite = thisSite;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ProcessRequest(socket, aSite).start();
            }
        } catch (Exception e) {
            if (e != null) {
				System.out.println("");
			}
        }

    }

}

