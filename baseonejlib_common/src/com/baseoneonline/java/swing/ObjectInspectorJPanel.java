package com.baseoneonline.java.swing;

/*
 * ObjectInspectorJPanel.java
 *
 * Created on June 8, 2007, 10:48 PM
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Copyright (C) 2007 Cheok YanCheng <yccheok@yahoo.com>
 */

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * 
 * @author yccheok
 */
public class ObjectInspectorJPanel extends FilteredPropertySheetPanel {

	/** Creates a new instance of ObjectInspectorJPanel */
	public ObjectInspectorJPanel() {

		getTable().setEditorFactory(new PropertyEditorRegistryEx());
		final PropertyEditorRegistry editor = (PropertyEditorRegistry) getTable()
				.getEditorFactory();
		final PropertyRendererRegistry renderer = (PropertyRendererRegistry) getTable()
				.getRendererFactory();

		editor.registerEditor(Enum.class, new EnumComboBoxPropertyEditor());

		final DefaultCellRenderer r = new DefaultCellRenderer();
		r.setShowOddAndEvenRows(false);
		renderer.registerRenderer(Enum.class, r);

		setMode(PropertySheet.VIEW_AS_FLAT_LIST);
		setToolBarVisible(false);
		setDescriptionVisible(false);
		setToolBarVisible(true);

		addPropertySheetChangeListener(listener);
	}

	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			final Property prop = (Property) evt.getSource();
			prop.writeToObject(bean);
		}
	};

	private static class PropertyEditorRegistryEx extends
			PropertyEditorRegistry {
		// We will try to get the "nearest" super class.
		@Override
		public synchronized PropertyEditor getEditor(final Class type) {
			PropertyEditor editor = super.getEditor(type);

			Class c = type;

			while (editor == null) {
				c = c.getSuperclass();

				if (c == null)
					return editor;

				editor = super.getEditor(c);
			}

			return editor;
		}
	}

	public void setBean(final Object bean) {
		this.bean = bean;

		BeanInfo beanInfo = null;

		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass());
		} catch (final IntrospectionException exp) {
			throw new RuntimeException(exp);
		}

		setBeanInfo(beanInfo);

		final Property[] properties = getProperties();
		for (int i = 0, c = properties.length; i < c; i++) {
			properties[i].readFromObject(bean);
		}

		// sheet.revalidate();
	}

	public Object getBean() {
		return bean;
	}

	private Object bean;

}
