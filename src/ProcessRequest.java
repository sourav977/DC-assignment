/*
 * Suzuki-Kasami Broadcast Algorithm for implementing distributed mutual exclusion
 * 
 * Sourav Patnaik
 * 
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ProcessRequest extends Thread {

    Socket socket = null;
    Site aSite = null;

    public ProcessRequest(Socket socket, Site site) {
        this.socket = socket;
        this.aSite = site;
    }

    public void run() {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String command = "";
            String[] message = null;

            command = bufferedReader.readLine();
            if (command != null) {
                if (command.charAt(0) == 'r') {
                    message = command.split(",");
                    System.out.println("Site-"+Integer.parseInt(message[1])+" has requested for Critical Section...\n");
                    aSite.processCriticalSectionReq(Integer.parseInt(message[1]), Integer.parseInt(message[2]));
                }

                if (command.charAt(0) == 't') {
                    message = command.split(",");
                    aSite.tokenQueue.clear();
                    int length=message.length;
                    for(int i=1;i<length;i++) {
                        aSite.tokenQueue.add(Integer.parseInt(message[i]));
                    }
                    aSite.hasToken = 1;
                }

                if (command.charAt(0) == 'L') {
                    message = command.split(",");
                    System.out.println("Site-"+Integer.parseInt(message[1])+" has left the Critical Section...\n");
                    aSite.updateLN(Integer.parseInt(message[1]), Integer.parseInt(message[2]));
                    System.out.println("");
                }
            }

        } catch (Exception e) {
            if (e != null) {
				System.out.println("");
			}
        } finally {
            try {
                bufferedReader.close();
                socket.close();
            } catch (Exception e) {
                if (e != null) {
                    System.out.println("");
                }
            }
        }
    }
}

