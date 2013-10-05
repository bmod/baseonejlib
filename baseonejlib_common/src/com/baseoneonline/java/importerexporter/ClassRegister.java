package com.baseoneonline.java.importerexporter;

import java.util.HashMap;

public class ClassRegister {

	private final HashMap<Class<? extends Storable>, String> typeToString = new HashMap<Class<? extends Storable>, String>();
	private final HashMap<String, Class<? extends Storable>> stringToType = new HashMap<String, Class<? extends Storable>>();

	public ClassRegister() {
	}

	public void register(Class<? extends Storable> type) {
		String name = type.getSimpleName();
		stringToType.put(name, type);
		typeToString.put(type, name);
	}

	public String getName(Storable stor) {
		Class<? extends Storable> type = stor.getClass();
		if (typeToString.containsKey(type)) {
			return typeToString.get(type);
		}
		return type.getName();
	}
}
