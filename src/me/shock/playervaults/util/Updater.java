package me.shock.playervaults.util;

/*
 * Updater for Bukkit.
 *
 * This class provides the means to safetly and easily update a plugin, or check to see if it is updated using dev.bukkit.org
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import me.shock.playervaults.Main;

/**
 * Check dev.bukkit.org to find updates for a given plugin, and download the updates if needed.
 * <p>
 * <b>VERY, VERY IMPORTANT</b>: Because there are no standards for adding auto-update toggles in your plugin's config, this system provides NO CHECK WITH YOUR CONFIG to make sure the user has allowed auto-updating.
 * <br>
 * It is a <b>BUKKIT POLICY</b> that you include a boolean value in your config that prevents the auto-updater from running <b>AT ALL</b>.
 * <br>
 * If you fail to include this option in your config, your plugin will be <b>REJECTED</b> when you attempt to submit it to dev.bukkit.org.
 * <p>
 * An example of a good configuration option would be something similar to 'auto-update: true' - if this value is set to false you may NOT run the auto-updater.
 * <br>
 * If you are unsure about these rules, please read the plugin submission guidelines: http://goo.gl/8iU5l
 *
 * @author H31IX
 */

public class Updater extends Main {
	
	String newVersion = "";
	
	public String getNewVersion() {
		return this.newVersion;
	}
	public boolean getUpdate() throws Exception {
		String version = getDescription().getVersion();
		URL url = new URL("http://dev.bukkit.org/server-mods/playervaults/files.rss");
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(url.openStream());
		}
		catch(UnknownHostException e) {
			return false; //Cannot connect
		}
		BufferedReader in = new BufferedReader(isr);
		String line;
		int lineNum = 0;
		while((line = in.readLine()) != null) {
			if(line.length() != line.replace("<title>", "").length()) {
				line = line.replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("	", "").substring(1); //Substring 1 for me, takes off the beginning v on my file name "v1.3.2"
				if(lineNum == 1) {
					this.newVersion = line;
					Integer newVer = Integer.parseInt(line.replace(".", ""));
					Integer oldVer = Integer.parseInt(version.replace(".", ""));
					if(oldVer < newVer) {
						return true; //They are using an old version
					}
					else if(oldVer > newVer) {
						return false; //They are using a FUTURE version!
					}
					else {
							return false; //They are up to date!
					}
				}
				lineNum = lineNum + 1;
			}
		}
		in.close();
		return false;
	}
}