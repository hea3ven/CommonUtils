package com.hea3ven.tools.commonutils.util;

import java.util.function.Supplier;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class SidedCall {
	public static void run(Side side, Runnable call) {
		if (FMLCommonHandler.instance().getSide() == side)
			call.run();
	}
}
