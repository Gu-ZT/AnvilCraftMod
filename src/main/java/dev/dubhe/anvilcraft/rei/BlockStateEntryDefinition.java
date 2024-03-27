package dev.dubhe.anvilcraft.rei;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.data.recipe.Component.BlockValue;
import dev.dubhe.anvilcraft.data.recipe.Component.TagValue;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BlockStateEntryDefinition implements EntryDefinition<BlockState>, EntrySerializer<BlockState> {
    public static final EntryType<BlockState> TYPE = EntryType.deferred(AnvilCraft.of("block_state"));
    public static final BlockStateEntryDefinition INSTANCE = new BlockStateEntryDefinition();
    public static EntryStack<BlockState> of(BlockState blockState) {
        return EntryStack.of(INSTANCE, blockState);
    }
    public static EntryStack<BlockState> of(BlockValue blockValue) {
        return of(blockValue.getBlock(), blockValue.getStates());
    }
    public static EntryStack<BlockState> of(Block block, Map<String, Comparable<?>> sStatesMap) {
        BlockState blockState = block.defaultBlockState();
        for (Map.Entry<String, Comparable<?>> entry : sStatesMap.entrySet()) {
            String sProperty = entry.getKey();
            Comparable<?> value = entry.getValue();
            Property<?> property = blockState.getBlock().getStateDefinition().getProperty(sProperty);
            if (property == null) continue;
            blockState = with(blockState, property, value);
        }
        return of(blockState);
    }
    public static List<EntryStack<BlockState>> of(TagValue tagValue) {
        List<EntryStack<BlockState>> stacks = new ArrayList<>();
        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tagValue.getBlock())) {
            stacks.add(of(blockHolder.value(), tagValue.getStates()));
        }
        return stacks;
    }
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState with(BlockState blockState, Property<?> property, Comparable<?> value) {
        return blockState.setValue((Property<T>) property, (T) value);
    }
    @Override
    public boolean supportSaving() {
        return true;
    }
    @Override
    public boolean supportReading() {
        return true;
    }
    @Override
    public CompoundTag save(EntryStack<BlockState> entry, BlockState value) {
        return NbtUtils.writeBlockState(value);
    }
    @Override
    public BlockState read(CompoundTag tag) {
        ClientLevel level = Minecraft.getInstance().level;
        HolderGetter<Block> holderGetter = level != null ? level.holderLookup(Registries.BLOCK) : RegistryAccess.EMPTY.asGetterLookup().lookupOrThrow(Registries.BLOCK);//TODO 感觉用EMPTY会出问题
        return NbtUtils.readBlockState(holderGetter, tag);
    }
    @Override
    public Class<BlockState> getValueType() {
        return BlockState.class;
    }
    @Override
    public EntryType<BlockState> getType() {
        return TYPE;
    }
    @Override
    public EntryRenderer<BlockState> getRenderer() {
        return BlockStateEntryRenderer.INSTANCE;
    }
    @Override
    public @NotNull ResourceLocation getIdentifier(EntryStack<BlockState> entry, BlockState value) {
        return BuiltInRegistries.BLOCK.getKey(value.getBlock());
    }
    @Override
    public boolean isEmpty(EntryStack<BlockState> entry, BlockState value) {
        return value.isAir();
    }
    @Override
    public BlockState copy(EntryStack<BlockState> entry, BlockState value) {
        return value;
    }
    @Override
    public BlockState normalize(EntryStack<BlockState> entry, BlockState value) {
        return value;
    }
    @Override
    public BlockState wildcard(EntryStack<BlockState> entry, BlockState value) {
        return value;
    }
    @Override
    public long hash(EntryStack<BlockState> entry, BlockState value, ComparisonContext context) {
        return value.getBlock().hashCode() ^ (context.isFuzzy() ? 0 : value.getValues().hashCode());
    }
    @Override
    public boolean equals(BlockState o1, BlockState o2, ComparisonContext context) {
        return o1.getBlock() == o2.getBlock() && (context.isFuzzy() || o1.getValues().equals(o2.getValues()));
    }
    @Override
    public @NotNull EntrySerializer<BlockState> getSerializer() {
        return this;
    }
    @Override
    public Component asFormattedText(EntryStack<BlockState> entry, BlockState value) {
        return value.getBlock().getName();
    }
    @Override
    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<BlockState> entry, BlockState value) {
        return value.getTags();
    }
}
