/**
 * $ $ License.
 *
 * Copyright $ L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.l2fprod.common.propertysheet;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.l2fprod.common.beans.ExtendedPropertyDescriptor;

/**
 * PropertyDescriptorAdapter.<br>
 * 
 */
public class PropertyDescriptorAdapter extends AbstractProperty {

	private PropertyDescriptor descriptor;

	public PropertyDescriptorAdapter() {
		super();
	}

	public PropertyDescriptorAdapter(final PropertyDescriptor descriptor) {
		this();
		setDescriptor(descriptor);
	}

	public void setDescriptor(final PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public String getName() {
		return descriptor.getName();
	}

	@Override
	public String getDisplayName() {
		return descriptor.getDisplayName();
	}

	@Override
	public String getShortDescription() {
		return descriptor.getShortDescription();
	}

	@Override
	public Class getType() {
		return descriptor.getPropertyType();
	}

	@Override
	public Object clone() {
		final PropertyDescriptorAdapter clone = new PropertyDescriptorAdapter(
				descriptor);
		clone.setValue(getValue());
		return clone;
	}

	@Override
	public void readFromObject(final Object object) {
		try {
			final Method method = descriptor.getReadMethod();
			if (method != null) {
				setValue(method.invoke(object, null));
			}
		} catch (final Exception e) {
			String message = "Got exception when reading property " + getName();
			if (object == null) {
				message += ", object was 'null'";
			} else {
				message += ", object was " + String.valueOf(object);
			}
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public void writeToObject(final Object object) {
		try {
			final Method method = descriptor.getWriteMethod();
			if (method != null) {
				method.invoke(object, new Object[] { getValue() });
			}
		} catch (final Exception e) {
			String message = "Got exception when writing property " + getName();
			if (object == null) {
				message += ", object was 'null'";
			} else {
				message += ", object was " + String.valueOf(object);
			}
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public boolean isEditable() {
		return descriptor.getWriteMethod() != null;
	}

	@Override
	public String getCategory() {
		if (descriptor instanceof ExtendedPropertyDescriptor) {
			return ((ExtendedPropertyDescriptor) descriptor).getCategory();
		} else {
			return null;
		}
	}

}
