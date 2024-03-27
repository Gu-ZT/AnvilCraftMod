package dev.dubhe.anvilcraft.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class AnvilCraftREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        //registry.add(BlockAnvilCategory.INSTANCE);
    }
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        //for (BlockAnvilRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ANVIL_BLOCK)) {
        //
        //}
        //registry.add(BlockAnvilDisplay.testOf());
    }
    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(BlockStateEntryDefinition.TYPE, BlockStateEntryDefinition.INSTANCE);
    }
    @Override
    public void registerEntries(EntryRegistry registry) {
        for (Block block : BuiltInRegistries.BLOCK) {
            BlockState blockState = block.defaultBlockState();
            if (blockState.getRenderShape() == RenderShape.INVISIBLE) continue;
            registry.addEntry(BlockStateEntryDefinition.of(blockState));
        }
    }
}
