package com.hea3ven.tools.commonutils.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.inventory.Slot;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SlotUtil {

	public static void onSwapCraft(Slot slot, int count) {
		Method mthd = ReflectionHelper.findMethod(Slot.class, "onSwapCraft", "func_190900_b", Integer.class);
		try {
			mthd.invoke(slot, count);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Could not call onSwapCraft");
		}
	}
}
