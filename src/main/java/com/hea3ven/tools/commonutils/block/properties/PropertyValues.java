package com.hea3ven.tools.commonutils.block.properties;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.IStringSerializable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PropertyValues<T extends Comparable<T> & IStringSerializable> extends PropertyHelper<T> {
	private Collection<T> values;
	private final Map<String, T> nameToValue;

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
		ImmutableMap.Builder<String, T> builder = ImmutableMap.builder();
		for (T value : values) {
			builder.put(value.getName(), value);
		}
		nameToValue = builder.build();
	}

	@SideOnly(Side.CLIENT)
	public Optional<T> parseValue(String value)
	{
		return Optional.<T>fromNullable(this.nameToValue.get(value));
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
