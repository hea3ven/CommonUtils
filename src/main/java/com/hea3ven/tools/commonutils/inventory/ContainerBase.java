package com.hea3ven.tools.commonutils.inventory;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public abstract class ContainerBase extends Container {

	public void addInventoryGrid(IInventory inv, int slotOff, int xOff, int yOff, int xSize, int ySize) {
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, Slot.class, inv);
	}

	public void addInventoryGrid(int slotOff, int xOff, int yOff, int xSize, int ySize,
			Class<? extends Slot> cls, Object... args) {
		try {
			Class<?>[] argsTypes = new Class[args.length + 3];
			int i = 0;
			for (; i < args.length; i++)
				argsTypes[i] = args[i].getClass();
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i] = Integer.TYPE;

			Constructor<? extends Slot> ctor =
					ConstructorUtils.getMatchingAccessibleConstructor(cls, argsTypes);
			for (int y = 0; y < ySize; ++y) {
				for (int x = 0; x < xSize; ++x) {
					Object[] objArgs = new Object[args.length + 3];
					for (i = 0; i < args.length; i++)
						objArgs[i] = args[i];
					objArgs[i++] = slotOff + x + y * xSize;
					objArgs[i++] = xOff + x * 18;
					objArgs[i] = yOff + y * 18;
					this.addSlotToContainer(ctor.newInstance(objArgs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
