package com.hea3ven.tools.commonutils.item.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeBuilder<T extends RecipeBuilder> {
	private boolean shaped = true;
	private ItemStack output;
	private int outputSize = 1;
	private String[] ingredients;

	public IRecipe build() {
		if (ingredients == null)
			throw new IllegalStateException("No ingredients were defined");

		ItemStack result = output.copy();
		result.stackSize = outputSize;
		if (shaped) {
			Map<Character, Boolean> mappings = new HashMap<>();
			List<Object> processedIngredients = new ArrayList<>();
			int i = ingredients.length - 1;
			while (i >= 0) {
				Object ingredient = parseIngredient(ingredients[i]);
				if (ingredient == null)
					break;
				if (ingredients[i - 1].length() != 1)
					throw new IllegalStateException("An ingredient mapping is more than 1 character long");

				char mapping = ingredients[i - 1].charAt(0);
				mappings.put(mapping, false);
				processedIngredients.add(0, ingredient);
				processedIngredients.add(0, mapping);

				i -= 2;
			}
			for (; i >= 0; i--) {
				for (char c : ingredients[i].toCharArray()) {
					if (c == ' ')
						continue;
					if (!mappings.containsKey(c))
						throw new IllegalStateException("Missing maping in the recipe");
					mappings.put(c, true);
				}
				processedIngredients.add(0, ingredients[i]);
			}
			if (mappings.containsValue(false))
				throw new IllegalStateException("Extra mapping in the recipe");

			return new ShapedOreRecipe(result, processedIngredients.toArray());
		} else {
			List<Object> processedIngredients = new ArrayList<>();
			for (String ingredient : ingredients) {
				processedIngredients.add(parseIngredient(ingredient));
			}
			return new ShapelessOreRecipe(result, processedIngredients.toArray());
		}
	}

	protected Object parseIngredient(String ingredient) {
		if (ingredient.indexOf(':') != -1)
			return ingredient;
		Block block = Block.getBlockFromName(ingredient);
		if (block != null)
			return block;
		Item item = Item.getByNameOrId(ingredient);
		if (item != null)
			return item;
		if (isOreDict(ingredient))
			return ingredient;
		return null;
	}

	protected boolean isOreDict(String ingredient) {
		return OreDictionary.doesOreNameExist(ingredient);
	}

	public T shaped(boolean shaped) {
		this.shaped = shaped;
		return (T) this;
	}

	public T output(ItemStack stack) {
		output = stack;
		return (T) this;
	}

	public T output(Item item) {
		return output(new ItemStack(item));
	}

	public T output(Block block) {
		return output(new ItemStack(block));
	}

	public T outputAmount(int size) {
		outputSize = size;
		return (T) this;
	}

	public T ingredients(String... ingredients) {
		this.ingredients = ingredients;
		return (T) this;
	}
}
