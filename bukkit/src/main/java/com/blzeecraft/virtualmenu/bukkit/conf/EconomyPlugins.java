package com.blzeecraft.virtualmenu.bukkit.conf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import com.blzeecraft.virtualmenu.bukkit.economy.IEconomyHook;
import com.blzeecraft.virtualmenu.bukkit.economy.VaultHook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.blzeecraft.virtualmenu.bukkit.economy.PlayerPointsHook;
import com.blzeecraft.virtualmenu.bukkit.economy.GemsEconomyHook;
@Getter
@ToString
public enum EconomyPlugins {

	VAULT("Vault", VaultHook::new), PLAYER_POINTS("PlayerPoints", PlayerPointsHook::new),
	GEMS_ECONOMY("GemsEconomy", GemsEconomyHook::new);

	private final String name;
	private final Supplier<? extends IEconomyHook> supplier;

	@Setter
	private IEconomyHook instance;
	private Set<String> currency;

	private EconomyPlugins(String name, Supplier<IEconomyHook> supplier) {
		this.name = name;
		this.supplier = supplier;
	}

	public boolean isEnable() {
		return instance != null;
	}

	public void readIsEnable(ConfigurationSection sect) {
		if (sect.getBoolean(name)) {
			IEconomyHook hook = supplier.get();
			if (hook.isPluginInstall()) {
				instance = hook;
			}
		}
	}

	public void readCurrency(ConfigurationSection sect) {
		if ("*".equals(sect.getString(name))) {
			currency = null;
		} else {
			currency = new HashSet<>(sect.getStringList(name));
		}
	}

	private static Map<String, IEconomyHook> mapCurrency = new HashMap<>();
	private static Optional<IEconomyHook> defaultEconomy = Optional.empty();

	public static void rebuildMapping() {
		defaultEconomy = Arrays.stream(values()).filter(type -> type.currency == null).findFirst()
				.map(EconomyPlugins::getInstance);
		mapCurrency = Arrays.stream(values()).flatMap(type -> {
			return type.currency.stream().map(currencyName -> {
				Map<String, IEconomyHook> map = new HashMap<>(1);
				map.put(currencyName, type.instance);
				return map;
			});
		}).reduce(new HashMap<>(), (map1, map2) -> {
			map1.putAll(map2);
			return map1;
		});
	}
	
	public static Optional<IEconomyHook> getHandlerByCurrency(String currency) {
		IEconomyHook econHook = mapCurrency.get(currency);
		if (econHook == null) {
			return defaultEconomy;
		}
		return Optional.of(econHook);
	}

}
