package com.hea3ven.tools.commonutils.inventory;

import java.lang.reflect.Constructor;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public abstract class ContainerBase extends Container {

	protected void addInventoryGrid(IInventory inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		addInventoryGrid(inv, slotOff, xOff, yOff, xSize, ySize, Slot.class);
	}

	protected void addInventoryGrid(IInventory inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize, Class<? extends Slot> cls) {
		try {
			Constructor<? extends Slot> ctor = cls.getConstructor(IInventory.class, Integer.TYPE,
					Integer.TYPE, Integer.TYPE);
			for (int y = 0; y < xSize; ++y) {
				for (int x = 0; x < ySize; ++x) {
					this.addSlotToContainer(ctor.newInstance(inv, slotOff + x + y * ySize,
							xOff + x * 18, yOff + y * 18));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
