package com.blzeecraft.virtualmenu.net;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;

public class UpdateChecker implements Runnable, Listener {
	
	public static final String URL = "https://raw.githubusercontent.com/ColorsWind/VirtualMenu/master/resource/plugin.yml";
	public static final String  DOWNLOAD = "https://github.com/ColorsWind/VirtualMenu/releases";
	public static final String MCBBS = "https://www.mcbbs.net/thread-894621-1-1.html";
	private final AtomicReference<YamlConfiguration> yaml = new AtomicReference<>();
	private final AtomicBoolean inform = new AtomicBoolean(false);
	private final VirtualMenuPlugin pl;
	
	public UpdateChecker(VirtualMenuPlugin pl) {
		this.pl = pl;
	}
	
	public void startTask() {
		Bukkit.getScheduler().runTaskAsynchronously(pl, this);
	}
	
	public void register() {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@Override
	public void run() {
		try {
			URL url = new URL(URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			this.yaml.set(YamlConfiguration.loadConfiguration(reader));
			reader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bukkit.getScheduler().runTask(pl, () -> Bukkit.getConsoleSender().sendMessage(getReport().toArray(new String[0])));
	}
	
	/**
	 * 判定插件是否需要更新
	 * @param origin
	 * @param current
	 * @return
	 */
	public boolean isOutdate(String origin, String current) {
		if (origin.equalsIgnoreCase(current)) {
			return false;
		}
		StringTokenizer o_str = new StringTokenizer(origin);
		StringTokenizer c_str = new StringTokenizer(current);
		if (o_str.countTokens() != c_str.countTokens()) {
			return true;
		}
		while(o_str.hasMoreTokens()) {
			if(!compare(o_str.nextToken(), c_str.nextToken())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 比较两个小版本
	 * @param origin
	 * @param current
	 * @return true 如果origin较新或一样，false如果current较新
	 */
	private boolean compare(String origin, String current) {
		if (origin.equalsIgnoreCase(current)) {
			return true;
		}
		try {
			int o = Integer.parseInt(origin);
			int c = Integer.parseInt(current);
			if (o >= c) {
				return true;
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

	private List<String> getReport() {
		YamlConfiguration yaml = this.yaml.get();
		ArrayList<String> report = new ArrayList<>();
		report.add("§b[§dVirtualMenu§b] ~~~~VirtualMenu检查更新~~~");
		if (yaml == null) {
			report.add("§c无法获取更新，本地插件版本为" + pl.getDescription().getVersion());
			report.add("§c请检查服务器是否能连接github");
			report.add("§c手动检查更新: " + DOWNLOAD );
			report.add("§cMCBBS链接: " + MCBBS);
		} else {
			String origin = pl.getDescription().getVersion();
			String current = yaml.getString("version");
			report.add("§b成功检查插件更新");
			report.add("§b本地插件版本为" + pl.getDescription().getVersion() + " 最新版本版本为: " + current);
			if (isOutdate(origin, current)) {
				this.inform.set(true);
				report.add("§b插件需要更新");
				report.add("§b更新地址: " + DOWNLOAD );
				report.add("§bMCBBS链接: " + MCBBS);
				if (yaml.isSet("changelog")) {
					report.add("§b更新日志");
					int index = 1;
					for(String s : yaml.getStringList("changelog")) {
						report.add("§a" + index + "." + s);
						index++;
					}
				}
			} else {
				report.add("§a插件目前为最新版本" + origin);
			}
		}
		return report;
	}
	
	@EventHandler
	public void handle(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("virtualmenu.admin") && inform.get()) {
			p.sendMessage(getReport().toArray(new String[0]));
		}
	}

}
