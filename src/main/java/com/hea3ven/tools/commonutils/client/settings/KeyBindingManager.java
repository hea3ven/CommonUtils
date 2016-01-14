package com.hea3ven.tools.commonutils.client.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyBindingManager {
	private final Map<KeyBinding, Consumer<InputEvent.KeyInputEvent>> keyBindings = new HashMap<>();
	private final Map<Item, Function<MouseEvent, Boolean>> scrollWheelBindings = new HashMap<>();

	public void addKeyBinding(String description, int keyCode, String category,
			Consumer<KeyInputEvent> callback) {
		keyBindings.put(new KeyBinding(description, keyCode, category), callback);
	}

	public void addItemKeyBinding(final Item item, String description, int keyCode, String category,
			final Consumer<KeyInputEvent> callback) {
		addKeyBinding(description, keyCode, category, new Consumer<KeyInputEvent>() {
			@Override
			public void accept(KeyInputEvent keyInputEvent) {
				ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
				if (stack != null && stack.getItem() == item) {
					callback.accept(keyInputEvent);
				}
			}
		});
	}

	public void addScrollWheelBinding(Item item, Function<MouseEvent, Boolean> callback) {
		scrollWheelBindings.put(item, callback);
	}

	public Set<KeyBinding> getKeyBindings() {
		return keyBindings.keySet();
	}

	@SubscribeEvent
	public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
		for (Entry<KeyBinding, Consumer<InputEvent.KeyInputEvent>> entry : keyBindings.entrySet()) {
			if (entry.getKey().isPressed()) {
				entry.getValue().accept(event);
			}
		}
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		if (event.dwheel != 0 && Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (stack == null)
				return;
			for (Entry<Item, Function<MouseEvent, Boolean>> entry : scrollWheelBindings.entrySet()) {
				if (stack.getItem() == entry.getKey()) {
					if (entry.getValue().apply(event)) {
						event.setCanceled(true);
						return;
					}
				}
			}
		}
	}
}
