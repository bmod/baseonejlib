package test;

import javax.swing.JLabel;

import com.baseoneonline.java.swing.propertySheet.BeanPropertyAdapter;
import com.baseoneonline.java.swing.propertySheet.BeanPropertyInfo;

public class TestBeanProps
{
	public static void main(String[] args)
	{

		BeanPropertyInfo info = BeanPropertyInfo.getInfo(JLabel.class);

		for (BeanPropertyAdapter prop : info.getAdaptedProperties())
		{

			System.out.println("Property: " + prop.getCategory() + ": "
					+ prop.getName());
		}

	}

}
