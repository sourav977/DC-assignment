/*
 * Suzuki-Kasami Broadcast Algorithm for implementing distributed mutual exclusion
 * 
 * Sourav Patnaik
 * 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class SuzukiKasami {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner myReader = null;
		File tfile = new File("");
		String absolutePath = tfile.getAbsolutePath();

		File sites = new File(absolutePath + "/src/sites.config");
		try {
			myReader = new Scanner(sites);
			int numberOfSites = 0;
			ArrayList<String> siteList = new ArrayList<String>();
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				siteList.add(data);
				numberOfSites++;
			}
			// store site Number in siteNumber, site ip address in siteIPAddress, site port in sitePort
			int[] siteNumber = new int[numberOfSites];
			String[] siteIPAddress = new String[numberOfSites];
			int[] sitePort = new int[numberOfSites];
			String[] str = null;

			for (int i = 0; i < numberOfSites; i++) {
				// split line by " " and store into respective array
				str = siteList.get(i).split(" ");
				siteNumber[i] = Integer.parseInt(str[0]);
				siteIPAddress[i] = str[1];
				sitePort[i] = Integer.parseInt(str[2]);
			}

			int currentSite = 0;
			int flag = 0;
			Scanner scanner = new Scanner(System.in);
			do {
				System.out.print("Enter site number (1-" + numberOfSites + "): ");
				while (!scanner.hasNextInt()) {
					System.out.println("Not correct site number, enter from (1-" + numberOfSites + "): ");
					scanner.next();
				}
				currentSite = Integer.parseInt(scanner.nextLine());
				if (currentSite >= 1 && currentSite <= numberOfSites) {
					flag = 1;
				} else {
					System.out.println("Out of Range site number, enter from (1-" + numberOfSites + "): ");
				}
			} while (flag == 0);

			int sitehasToken = 0;

			if (currentSite == 1) {
				sitehasToken = 1;
			}

			Site aSite = new Site(numberOfSites, currentSite, sitehasToken, siteIPAddress, sitePort);

			// Open a socket
			ListenToBroadcast listenToBroadcast = new ListenToBroadcast(aSite, sitePort[currentSite - 1]);
			listenToBroadcast.start();
			String inputQuery = "";
			System.out.println("Type quit to exit from program.\n");
			while (!inputQuery.equalsIgnoreCase("quit")) {
				System.out.println("Press ENTER to enter CS: ");
				Scanner scan_query = new Scanner(System.in);
				inputQuery = scan_query.nextLine();
				System.out.println("Site-" + currentSite + " is trying to enter Critical Section");
				if (aSite.hasToken == 1) {
					aSite.processingCS = 1;
					System.out.println("Site-" + currentSite + " has token. Executing in the Critical Section.....");

					Thread.sleep(8000);
					aSite.processingCS = 0;

					System.out.println("Site-" + currentSite + " is exiting Critical Section.");
					System.out.println("");

					exitCS(aSite, currentSite, numberOfSites, siteIPAddress, sitePort, numberOfSites);

				} else {
					System.out.println("Site-" + currentSite + " doesn't have token. So Site-" + currentSite + " is requesting token");
					aSite.requestCriticalSection();
					System.out.println("Site-" + currentSite + " is waiting for token.");

					aSite.processingCS = 1;

					while (aSite.hasToken == 0) {
						Thread.sleep(3000);
					}

					System.out.println("Site-" + currentSite + " has received token. Executing in Critical Section.....");
					Thread.sleep(8000);
					aSite.processingCS = 0;

					System.out.println("Site-" + currentSite + " is exiting Critical Section.");
					System.out.println("");

					exitCS(aSite, currentSite, numberOfSites, siteIPAddress, sitePort, numberOfSites);
				}
			}

		} catch (Exception e) {
			if (e != null) {
				System.out.println("");
			}
		}
	}

	/**
	 * @param aSite
	 * @param currentSite
	 * @param numberOfSites
	 * @param ipAddress
	 * @param port
	 * @param numOfSites
     * exitCS sends updates LN[], RN[] at each Site.
	 */
	public static void exitCS(Site aSite, int currentSite, int numberOfSites, String[] ipAddress, int[] port, int numOfSites) {
		aSite.LN[currentSite - 1] = aSite.RN[currentSite - 1];
		// Send updated LN value to all sites
		String message = "LN," + currentSite + "," + aSite.LN[currentSite - 1];
		for (int i = 0; i < numOfSites; i++) {
			if (i == currentSite - 1) {
				continue;
			}

			try {
				Socket socket = new Socket(ipAddress[i], port[i]);
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				bufferedWriter.write(message);
				bufferedWriter.flush();
				socket.close();
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

		for (int i = 0; i < numberOfSites; i++) {
			if (aSite.RN[i] == aSite.LN[i] + 1) {
				if (!aSite.tokenQueue.contains(i + 1)) {
					aSite.tokenQueue.add(i + 1);
				}
			}
		}

		if (aSite.tokenQueue.size() > 0) {
			aSite.sendToken(aSite.tokenQueue.poll());
		}
	}
}

