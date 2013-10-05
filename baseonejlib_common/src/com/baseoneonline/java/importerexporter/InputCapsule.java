package com.baseoneonline.java.importerexporter;

import java.util.List;

public interface InputCapsule {

	public int readInt(String key);

	public <T> List<T> readList(String key, Class<T> type);
}
