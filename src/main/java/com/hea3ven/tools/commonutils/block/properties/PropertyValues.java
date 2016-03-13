package com.hea3ven.tools.commonutils.block.properties;

import java.util.Collection;

import com.google.common.collect.Lists;

import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.IStringSerializable;

public class PropertyValues<T extends Comparable<T> & IStringSerializable> extends PropertyHelper<T> {
	private Collection<T> values;

	public static <T extends Comparable<T> & IStringSerializable> PropertyValues<T> create(String name,
			Class<T> valueClass, T... values) {
		return new PropertyValues<>(name, valueClass, Lists.newArrayList(values));
	}

	public static <T extends Comparable<T> & IStringSerializable> PropertyValues<T> create(String name,
			Class<T> valueClass, Collection<T> values) {
		return new PropertyValues<>(name, valueClass, values);
	}

	protected PropertyValues(String name, Class<T> valueClass, Collection<T> values) {
		super(name, valueClass);
		this.values = values;
	}

	@Override
	public Collection<T> getAllowedValues() {
		return values;
	}

	@Override
	public String getName(T value) {
		return value.getName();
	}
}
