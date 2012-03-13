package com.baseoneonline.java.swing.propertySheet;

import javax.swing.JComboBox;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

/*
 * MyComboEditor.java
 *
 * Created on May 21, 2007, 4:09 PM
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

/**
 * 
 * @author yccheok
 */
public class EnumComboBoxPropertyEditor extends ComboBoxPropertyEditor {

	@Override
	public void setValue(final Object value) {
		final JComboBox box = (JComboBox) editor;

		// We need to remove the previous items. This is because if a
		// property panel is having more than 1 enum type property, we
		// may display wrong type of enum.
		box.removeAllItems();

		if (box.getItemCount() == 0) {
			try {
				final java.lang.reflect.Method m = value.getClass().getMethod(
						"values");
				final Enum<?>[] array = (Enum[]) m.invoke(null);
				setAvailableValues(array);
			} catch (final Exception exp) {
				throw new RuntimeException(exp);
			}

		}
		super.setValue(value);
	}

	/** Creates a new instance of EnumComboBoxPropertyEditor */
	public EnumComboBoxPropertyEditor() {
	}

}
