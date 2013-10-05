package com.baseoneonline.java.importerexporter;

public class ImportExportUtils {

	public static String storableName(Storable storable) {
		return storable.getClass().getName();
	}

}
