package io.github.alloffabric.artis.compat.rei;

import io.github.alloffabric.artis.Artis;
import io.github.alloffabric.artis.api.ArtisTableType;
import io.github.alloffabric.artis.block.ArtisTableBlock;
import io.github.alloffabric.artis.recipe.ShapedArtisRecipe;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArtisREIPlugin implements REIPluginV0 {
    public static final Identifier PLUGIN = new Identifier(Artis.MODID, "artis_plugin");

    public static final Map<ArtisTableType, ItemConvertible> iconMap = new HashMap<>();

    public ArtisREIPlugin() {
        for (ArtisTableBlock block : Artis.ARTIS_TABLE_BLOCKS) {
            iconMap.put(block.getType(), block);
        }
    }

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("3.0-pre");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
        for (ArtisTableType type : Artis.ARTIS_TABLE_TYPES) {
            recipeHelper.registerCategory(new ArtisCategory<>(type));
        }
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        Function<Recipe, RecipeDisplay> recipeDisplay = r -> new ArtisDisplay((Recipe) r);

        for (ArtisTableType type : Artis.ARTIS_TABLE_TYPES) {
            recipeHelper.registerRecipes(type.getId(), (Predicate<Recipe>) recipe -> {
                if (recipe.getType() == type) {
                    return true;
                }
                return false;
            }, recipeDisplay);
        }
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipeVisibilityHandler(new ArtisDisplayVisibilityHandler());

        for (ArtisTableBlock block : Artis.ARTIS_TABLE_BLOCKS) {
            recipeHelper.registerWorkingStations(block.getType().getId(), new ItemStack(block.asItem()));
        }
	}
}
