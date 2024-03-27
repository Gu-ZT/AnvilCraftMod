package dev.dubhe.anvilcraft.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BlockStateEntryRenderer implements EntryRenderer<BlockState> {
    public static final BlockStateEntryRenderer INSTANCE = new BlockStateEntryRenderer();
    //FIXME 无法渲染半透明方块（比如染色玻璃）
    //FIXME 无法渲染以方块实体渲染的方块（比如箱子和告示牌）
    @Override
    public void render(EntryStack<BlockState> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
        if (entry.isEmpty()) return;
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(bounds.getX(), bounds.getY(), 0);
        //Minecraft.getInstance().font.drawInBatch("标记", 0, 0, -1, true, poseStack.last().pose(), graphics.bufferSource(), Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        poseStack.mulPoseMatrix(new Matrix4f().scale(1, -1, 1));
        poseStack.scale(16, 16, 16);
        new ItemTransform(new Vector3f(30, 210, 0), new Vector3f(0.925f, -0.8125f, 0), new Vector3f(0.625f)).apply(false, poseStack);
        //ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        //Minecraft.getInstance().getBlockRenderer().renderBatched(entry.getValue(), BlockPos.ZERO,level,poseStack,graphics.bufferSource().getBuffer(Sheets.translucentCullBlockSheet()),false,level.getRandom());
        //Lighting.setupForFlatItems();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(entry.getValue(), poseStack, graphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        graphics.bufferSource().endBatch();
        //Lighting.setupFor3DItems();
    }
    @Override
    public @Nullable Tooltip getTooltip(EntryStack<BlockState> entry, TooltipContext context) {
        if (entry.isEmpty()) return null;
        Tooltip tooltip = Tooltip.create(
          entry.asFormattedText(context),
          Component.translatable("soundCategory.block").withStyle(ChatFormatting.YELLOW));
        if (context.getFlag().isAdvanced()) {
            tooltip.add(Component.literal(Objects.toString(entry.getIdentifier())).withStyle(ChatFormatting.DARK_GRAY));
        }
        //TODO 没有体现方块状态
        return tooltip;
    }
}
