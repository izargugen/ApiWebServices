package com.sid.ovli.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sid.ovli.Ttaq;

public class BtnJouerHandler implements IHandler {
	
	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		try {

			String line;
			String pidInfo = "";

			Process p = Runtime.getRuntime().exec(
					System.getenv("windir") + "\\system32\\"
							+ "tasklist.exe");

			BufferedReader input = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			while ((line = input.readLine()) != null) {
				// pidInfo+=line;

				System.out.println(pidInfo + "\t" + line);

				// Tuer le process VLC s'il y en a un en cours
				if (line.contains("vlc.exe")) {
					/*
					 * 
					 * Nom de l'image PID Nom de la sessio Numero de s
					 * Utilisation ========================= ========
					 * ================ =========== ============ System
					 * Idle Process 0 Services 0 24 Ko System 4 Services
					 * 0 324 Ko
					 */
					pidInfo = line.substring(26, 34);

					Runtime.getRuntime().exec(
							"taskkill /F /PID" + pidInfo);
					break;
				}
			}

			input.close();

			// ProcessBuilder pb = new
			// ProcessBuilder("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe",
			// "liensActifs.m3u");
			ProcessBuilder pb = new ProcessBuilder(ttaq.txtCheminVLC
					.getText() + "\\vlc.exe", "liensActifs.m3u");
			Process start = pb.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
