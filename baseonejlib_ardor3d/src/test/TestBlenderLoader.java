package test;

import com.baseoneonline.jlib.ardor3d.framework.ResourceManager;

public class TestBlenderLoader {
	public static void main(String[] args) {
		ResourceManager.get().getModel("test/bike.blend");
	}
}
