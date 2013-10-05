package test;

import java.io.File;
import java.io.IOException;

import com.baseoneonline.java.importerexporter.ClassRegister;
import com.baseoneonline.java.importerexporter.InputCapsule;
import com.baseoneonline.java.importerexporter.OutputCapsule;
import com.baseoneonline.java.importerexporter.Storable;
import com.baseoneonline.java.importerexporter.XMLExporter;

public class TestImporterExporter {

	public static void main(String[] args) throws IOException {
		File testFile = new File("test/testStorable.xml");

		ClassRegister register = new ClassRegister();
		register.register(TestStorable.class);
		register.register(TestVector.class);

		TestStorable outStorable = new TestStorable();

		XMLExporter exporter = new XMLExporter(register);

		exporter.write(testFile, outStorable);

	}
}

class TestStorable implements Storable {

	float someFloat = 123.456f;
	double someDouble = 123.456;
	String someString = "Hello Storable!";
	TestVector someVector = new TestVector();

	@Override
	public void read(InputCapsule cap) {
	}

	@Override
	public void write(OutputCapsule cap) {
		cap.writeFloat("someFloat", someFloat);
		cap.writeDouble("someDouble", someDouble);
		cap.writeString("someString", someString);
		cap.writeStorable("someVector", someVector);
	}
}

class TestVector implements Storable {

	float x = 1;
	float y = 2;

	@Override
	public void read(InputCapsule cap) {
	}

	@Override
	public void write(OutputCapsule cap) {
		cap.writeFloat("x", x);
		cap.writeFloat("y", y);
	}

}
