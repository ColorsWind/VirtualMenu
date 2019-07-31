package com.blzeecraft.virtualmenu.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionManager;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.menu.iiem.ExtendedIcon;
import com.blzeecraft.virtualmenu.menu.iiem.OverrideIcon;
import com.blzeecraft.virtualmenu.menu.iiem.RequireItem;

import lombok.Data;
import lombok.NonNull;

@Data
public class DataWrapper {
	
	@NonNull
	private final Object origin;
	
	public String asString() {
		return origin.toString();
	}
	
	public int asInt() {
			if (origin instanceof Number) {
				return ((Number)origin).intValue();
			} else {
				return Integer.parseInt(origin.toString());
			}
		
	}
	
	public double asDouble() {
		if (origin instanceof Number) {
			return ((Number)origin).doubleValue();
		} else {
			return Double.parseDouble(origin.toString());
		}
	}
	
	public long asLong() {
		if (origin instanceof Number) {
			return ((Number)origin).longValue();
		} else {
			return Long.parseLong(origin.toString());
		}
	}
	
	public boolean asBoolean() {
		if (origin instanceof Boolean) {
			return (Boolean) origin;
		} else {
			return Boolean.parseBoolean(origin.toString());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> asStringList() {
		if (origin instanceof List) {
			return (List<String>) origin;
		} else {
			String s = asString();
			String[] array = s.split(";");
			return Arrays.asList(array);
		}

	}
	
	@SuppressWarnings("unchecked")
	public Map<Enchantment, Integer> asEnchantmentMap() {
		Map<String, Integer> origin = (Map<String, Integer>) this.origin;
		Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
		for(Entry<String, Integer> en : origin.entrySet()) {
			enchantmentMap.put(Enchantment.getByKey(NamespacedKey.minecraft(en.getKey())), en.getValue());
		}
		return enchantmentMap;
	}
	
	public List<AbstractAction> asActionList(ILog parent) {
		List<AbstractAction> list = new ArrayList<>();
		ActionManager actionManager = ActionManager.getInstance();
		for(String s : asStringList()) {
			list.add(actionManager.fromString(parent, s));
		}
		return list;
	}

	public Map<ClickType, List<AbstractAction>> asActionMap(ILog parent) {
		EnumMap<ClickType, List<AbstractAction>> map = new EnumMap<>(ClickType.class);
		if (origin instanceof ConfigurationSection) {
			Map<String, Object> cMap =  ((ConfigurationSection) origin).getValues(false);
			//保证优先执行
			Object all = cMap.remove("DEFAULT");
			if (all != null) {
				DataWrapper wrapper = new DataWrapper(all);
				List<AbstractAction> list = wrapper.asActionList(parent);
				for(ClickType ty : ClickType.values()) {
					map.put(ty, list);
				}
			}
			for(Entry<String, Object> en :cMap.entrySet()) {
				ClickType type = null;
				try {
					type = ClickType.valueOf(en.getKey());
				} catch (IllegalArgumentException e) {
					throw new NoSuchElementException("Except: [ClickType]: [action] Set: " + en.getKey() + "(invalid ClickType)"); 
				}
				DataWrapper wrapper = new DataWrapper(en.getValue());
				List<AbstractAction> list = wrapper.asActionList(parent);
				map.put(type, list);
			}
		} else {
			List<AbstractAction> list = asActionList(parent);
			for(ClickType ty : ClickType.values()) {
				map.put(ty, list);
			}
		}

		return map;
	}

	public RequireItem asRequireItem(ILog parent) throws NoSuchElementException, IllegalArgumentException {
		RequireItem require = new RequireItem(parent);
		if (origin instanceof ConfigurationSection) {
			ConfigReader.read(require.getClass(), require, (ConfigurationSection)origin);
		} else {
			String s = asString();
			try {	
				StringTokenizer str = new StringTokenizer(s, ",");
				String type = str.nextToken();
				String amount = str.nextToken().replace(" ", "");
				int i = Integer.parseInt(amount);
				require.setType(type);
				require.setAmount(i);
				require.apply();
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("Except: [Material(:byte)], [amount]  Set: " + s + "(not contain \", \""); 
			} catch (NumberFormatException e) {
				throw new NumberFormatException("Except: [Material(:byte)], [amount]  Set: " + s + "(amount isn\'t int)");
			}  catch (IllegalArgumentException e) {
				throw new NoSuchElementException("Except: [Material(:byte)], [amount]  Set: " + s + "(invade Material)"); 
			}
		}
		return require;
	}

	@SuppressWarnings("unchecked")
	public TreeSet<OverrideIcon> asOverrideIcon(Object parent) {
		if (parent instanceof ExtendedIcon) {
			ExtendedIcon icon = (ExtendedIcon) parent;
			TreeSet<OverrideIcon> set = new TreeSet<>();
			if (origin instanceof List) {
				for(Object o : (List<?>)origin) {
					if (o instanceof Map) {
						OverrideIcon oi = new OverrideIcon(icon.getMenu(), icon);
						ConfigReader.read(OverrideIcon.class, oi, (Map<String, Object>)o);
						set.add(oi);
					} else {
						throw new IllegalArgumentException("Expect: List of OverideIcon Set: " + asString() + "(Sth isn\'t Map)");
					}
				}
				return set;
			} else {
				throw new IllegalArgumentException("Expect: List of OverideIcon Set: " + asString() + "(not a List)");
			}
		} else {
			throw new IllegalArgumentException("Only ExtendedIcon can have OrrideIcon");
		}
	}


	
	


}
