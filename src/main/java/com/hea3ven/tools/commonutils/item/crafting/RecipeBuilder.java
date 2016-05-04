package com.hea3ven.tools.commonutils.item.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeBuilder<T extends RecipeBuilder> {
	private static final Pattern itemPattern = Pattern.compile("((\\d+)x)?(\\w*:\\w*)(@(\\d+))?");

	private boolean shaped = true;
	private ItemStack output;
	private String outputIngredient;
	private int outputSize = 1;
	private String[] ingredients;

	public IRecipe build() {
		if (ingredients == null)
			throw new IllegalStateException("No ingredients were defined");

		if (outputIngredient == null && output == null) {
			StringBuilder inputs = new StringBuilder();
			for (String ingredient : ingredients) {
				if (inputs.length() != 0)
					inputs.append(", ");
				inputs.append(ingredient);
			}
			throw new IllegalStateException("No output defined for recipe with inputs " + inputs.toString());
		}
		ItemStack result = (outputIngredient != null) ? parseStack(outputIngredient) : output.copy();
		if (result.stackSize == 1)
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

			return createShapedRecipe(result, processedIngredients.toArray());
		} else {
			List<Object> processedIngredients = new ArrayList<>();
			for (String ingredient : ingredients) {
				processedIngredients.add(parseIngredient(ingredient));
			}
			return createShapelessRecipe(result, processedIngredients.toArray());
		}
	}

	protected Object parseIngredient(String ingredient) {
		ItemStack stack = parseStack(ingredient);
		if (stack != null)
			return stack;
		if (isOreDict(ingredient))
			return ingredient;
		return null;
	}

	private ItemStack parseStack(String ingredient) {
		Block block = Block.getBlockFromName(ingredient);
		if (block != null)
			return new ItemStack(block);

		Matcher match = itemPattern.matcher(ingredient);
		if (match.matches()) {
			Item item = Item.getByNameOrId(match.group(3));
			int size = match.group(2) != null ? Integer.parseInt(match.group(2)) : 1;
			int meta = match.group(5) != null ? Integer.parseInt(match.group(5)) : 0;
			return new ItemStack(item, size, meta);
		}
		return null;
	}

	protected boolean isOreDict(String ingredient) {
		return OreDictionary.doesOreNameExist(ingredient);
	}

	protected IRecipe createShapedRecipe(ItemStack result, Object[] inputs) {
		return new ShapedOreRecipe(result, inputs);
	}

	protected IRecipe createShapelessRecipe(ItemStack result, Object[] inputs) {
		return new ShapelessOreRecipe(result, inputs);
	}

	@SuppressWarnings("unchecked")
	public T shaped(boolean shaped) {
		this.shaped = shaped;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T output(String ingredient) {
		outputIngredient = ingredient;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	public T outputAmount(int size) {
		outputSize = size;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T ingredients(String... ingredients) {
		this.ingredients = ingredients;
		return (T) this;
	}
}
