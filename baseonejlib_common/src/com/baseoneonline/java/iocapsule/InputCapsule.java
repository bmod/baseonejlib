package com.baseoneonline.java.iocapsule;

import java.util.List;

public interface InputCapsule {

	public List<? extends Storable> readList(String key,
			Class<? extends Storable> elementType);

	public String readString(String key);

}
