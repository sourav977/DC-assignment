/*
 * Suzuki-Kasami Broadcast Algorithm for implementing distributed mutual exclusion
 * 
 * Sourav Patnaik
 * 
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class Site {
    String[] siteIPAddress = null;
    int[] sitePort = null;

    int numberOfSites = 0;
    int siteNumber = 0;
    int hasToken = 0;
    int seqNumber = 0;
    int processingCS = 0;
    Queue<Integer> tokenQueue = new LinkedList<>();
    int RN[];
    int LN[];

    Site(int numberOfSites, int siteNumber, int hasToken, String[] siteIPAddress, int[] sitePort) {
        this.numberOfSites = numberOfSites;
        this.siteNumber = siteNumber;
        this.hasToken = hasToken;

        this.siteIPAddress = siteIPAddress;
        this.sitePort = sitePort;

        RN = new int[this.numberOfSites];
        LN = new int[this.numberOfSites];
        for (int i = 0; i < numberOfSites; i++) {
            RN[i] = 0;
            LN[i] = 0;
        }
    }

    void updateLN(int thisSite, int value) {
        LN[thisSite - 1] = value;
    }

    void requestCriticalSection() {
        RN[siteNumber - 1]++;
        String message = "request," + siteNumber + "," + RN[siteNumber - 1];
        System.out.println("Broadcasting request to other " + (numberOfSites - 1) + " sites : ");

        for (int i = 0; i < numberOfSites; i++) {
            if (i != siteNumber - 1) {
                Socket socket = null;

                try {
                    socket = new Socket(siteIPAddress[i], sitePort[i]);
                    System.out.println(i + ". Broadcasting to the site with port :" + socket.getPort());
                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(message);
                    bufferedWriter.flush();
                    outputStream.close();
                    outputStreamWriter.close();
                    bufferedWriter.close();

                } catch (UnknownHostException e) {
                    if (e != null) {
                        System.out.println("");
                    }
                } catch (IOException e) {
                    if (e != null) {
                        System.out.println("");
                    }
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        if (e != null) {
                            System.out.println("");
                        }
                    }
                }
            }
        }
    }

    void processCriticalSectionReq(int site, int sn) {
        if (RN[site - 1] < sn) {
            RN[site - 1] = sn;
        }

        if (processingCS == 0 && hasToken == 1) {
            sendToken(site);

        } else {
            tokenQueue.add(site);
        }

    }

    void sendToken(int site) {

        if (this.hasToken == 1) {
            if (RN[site - 1] == LN[site - 1] + 1) {
                System.out.println("Sending token to site " + site);

                try {
                    Socket socket = new Socket(siteIPAddress[site - 1], sitePort[site - 1]);
                    String message = "token";
                    int tokenQueuelen = tokenQueue.size();
                    for (int i = 0; i < tokenQueuelen; i++) {
                        message += "," + tokenQueue.poll();
                    }

                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(message);
                    bufferedWriter.flush();
                    socket.close();
                    this.hasToken = 0;
                } catch (UnknownHostException e) {
                    if (e != null) {
                        System.out.println("");
                    }
                } catch (IOException e) {
                    if (e != null) {
                        System.out.println("");
                    }
                }
            }
        }

    }
}

