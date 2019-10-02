package com.blzeecraft.virtualmenu.core.action;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.BiFunction;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActionManager {

	@NonNull
	private static final Map<String, BiFunction<AbstractAction, LogNode, JsonObject>> REGISTERED = new HashMap<>();
	

	public static boolean registerAction(@NonNull String name, BiFunction<AbstractAction, LogNode, JsonObject> supplier) {
		return REGISTERED.putIfAbsent(name.toLowerCase(), supplier) == null;
	}
	
	@NonNull
	public static boolean unregisterAction(String name) {
		return REGISTERED.remove(name.toLowerCase()) != null;
	}
	
	public static AbstractAction fromString(LogNode node, String s) {

	}
}
