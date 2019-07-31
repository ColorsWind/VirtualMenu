package com.blzeecraft.virtualmenu.config;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.blzeecraft.virtualmenu.logger.PluginLogger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigReader {
	
	public static <T extends IConfig> T read(Class<? extends IConfig> clazz,
			T origin, ConfigurationSection sect) throws ClassCastException {
		return read(clazz, origin, sect.getValues(false));
	}
	

	public static <T extends IConfig> T read(Class<? extends IConfig> clazz,
			T origin, Map<String, Object> sect) throws ClassCastException {
		try {
			Set<Field> fields = new HashSet<>();
			Class<?> c = clazz;
			while(c != Object.class) {
				for(Field f : c.getDeclaredFields()) {
					f.setAccessible(true);
					fields.add(f);
				}
				c = c.getSuperclass();
			}
			for(Field f : fields) {
				Node ann = f.getDeclaredAnnotation(Node.class);
				if (ann != null) {
					Object o = sect.get(ann.key());
				 	if (o != null) {
						//start
						DataWrapper dw = new DataWrapper(o);
						try {
							switch(ann.type()) {
							case DOUBLE:
								f.setDouble(origin, dw.asDouble());
								break;
							case ENCHANTMENTS:
								f.set(origin, dw.asEnchantmentMap());
								break;
							case BOOLEAN:
								f.setBoolean(origin, dw.asBoolean());
								break;
							case INT:
								f.setInt(origin, dw.asInt());
								break;
							case LONG:
								f.setLong(origin, dw.asLong());
								break;
							case STRING:
								f.set(origin, dw.asString());
								break;
							case STRING_LIST:
								f.set(origin, dw.asStringList());
								break;
							case COMMAND_LIST:
								f.set(origin, dw.asActionMap(origin));
								break;
							case REQUIRED_ITEM:
								try {
									f.set(origin, dw.asRequireItem(origin));
									//ConfigReader不会抛出异常
								} catch (NoSuchElementException | IllegalArgumentException e) {
									PluginLogger.severe(origin, "REQUIRED-ITEM", e.getMessage());
								}
								break;
							case OVERRIDE_ICON:
								try {
									f.set(origin, dw.asOverrideIcon(origin));
								} catch (IllegalArgumentException e) {
									PluginLogger.severe(origin, "OVERRIDE", e.getMessage());
								}
								break;
							}
						} catch (ClassCastException e) {
							e.printStackTrace();
							PluginLogger.severe(origin, ann.key(),  " Expect: [" + f.getType().getSimpleName() +"]" + "  Set: " + dw.getOrigin() + " (INVALID)");
						} catch (NumberFormatException e) {
							e.printStackTrace();
							PluginLogger.severe(origin, ann.key(),  " Expect: [" + f.getType().getSimpleName() +"]" + "  Set: " + dw.getOrigin() + " (INVALID NUMBER)");
						}
					}
				}
			} 
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		try {
			origin.apply();
		} catch (IllegalArgumentException e) {
			PluginLogger.severe(origin,  e.getMessage());
		}
		return origin;
	}
}
