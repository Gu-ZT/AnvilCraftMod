package dev.dubhe.anvilcraft.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.dubhe.anvilcraft.AnvilCraft;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class RecordMaterialButton extends Button {
    private boolean record;
    private static final ResourceLocation YES = AnvilCraft.of("textures/gui/container/button_yes.png");
    private static final ResourceLocation NO = AnvilCraft.of("textures/gui/container/button_no.png");
    private static final MutableComponent defaultMessage = Component.translatable("screen.anvilcraft.button.record", Component.translatable("screen.anvilcraft.button.off"));

    public RecordMaterialButton(int x, int y, OnPress onPress, boolean record) {
        super(x, y, 16, 16, defaultMessage, onPress, (var) -> defaultMessage);
        this.record = record;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.isHovered()) {
            List<Component> components = new ArrayList<>() {{
                this.add(getMessage());
                this.add(Component.translatable("screen.anvilcraft.button.record.tooltip").withStyle(ChatFormatting.GRAY));
            }};
            guiGraphics.renderTooltip(Minecraft.getInstance().font, components, Optional.empty(), mouseX, mouseY);
        }
    }

    public void setRecord(boolean record) {
        this.record = record;
        this.setMessage(Component.translatable("screen.anvilcraft.button.record", Component.translatable("screen.anvilcraft.button." + (this.isRecord() ? "on" : "off"))));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation location = this.record ? RecordMaterialButton.YES : RecordMaterialButton.NO;
        this.renderTexture(guiGraphics, location, this.getX(), this.getY(), 0, 0, 16, this.width, this.height, 16, 32);
    }

    @Override
    public void renderTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uOffset, int vOffset, int textureDifference, int width, int height, int textureWidth, int textureHeight) {
        int i = vOffset;
        if (this.isHovered()) {
            i += textureDifference;
        }
        RenderSystem.enableDepthTest();
        guiGraphics.blit(texture, x, y, uOffset, i, width, height, textureWidth, textureHeight);
    }

    public boolean next() {
        return !this.record;
    }
}
