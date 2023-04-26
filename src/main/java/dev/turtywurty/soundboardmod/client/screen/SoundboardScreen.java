package dev.turtywurty.soundboardmod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.turtywurty.soundboardmod.SoundboardMod;
import dev.turtywurty.soundboardmod.client.SoundInfo;
import dev.turtywurty.soundboardmod.client.Utils;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.List;

public class SoundboardScreen extends Screen {
    public static final Component TITLE = Component.translatable(
            "gui." + SoundboardMod.MODID + ".open_soundboard.title");
    private static final ResourceLocation TEXTURE = new ResourceLocation(SoundboardMod.MODID,
            "textures/gui/soundboard_widgets.png");

    private SoundScrollPanel scrollPanel;
    private AddSoundButton addSoundButton;

    public SoundboardScreen() {
        super(TITLE);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.scrollPanel = addRenderableWidget(new SoundScrollPanel());
        this.addSoundButton = addRenderableWidget(new AddSoundButton());

        SoundInfo.SOUNDS.forEach(sound -> this.scrollPanel.addSound(new SoundWidget()));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        pPoseStack.pushPose();
        pPoseStack.scale(2f, 2f, 2f);
        pPoseStack.translate(this.width / 4f - this.font.width(TITLE) / 2f, 10, 0);
        this.font.draw(pPoseStack, TITLE, 0, 0, 0xA8A8A8);
        pPoseStack.popPose();
    }

    @Override
    public void onClose() {
        super.onClose();
        SoundInfo.saveSounds();
    }

    public class SoundScrollPanel extends ScrollPanel {
        private final List<SoundWidget> widgets = new ArrayList<>();

        public SoundScrollPanel() {
            super(SoundboardScreen.this.minecraft, SoundboardScreen.this.width,
                    (SoundboardScreen.this.height - 40) - font.lineHeight * 2, 40 + font.lineHeight * 2, 0);
        }

        @Override
        protected int getContentHeight() {
            int height = 0;
            for (SoundWidget widget : this.widgets) {
                height += widget.getHeight() + 8;
            }

            if (height < this.bottom - 8) height = this.bottom - 8;

            return height;
        }

        @Override
        protected int getScrollAmount() {
            return 8;
        }

        @Override
        protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            if (this.widgets.isEmpty()) {
                SoundboardScreen.this.font.draw(poseStack,
                        Component.translatable("gui." + SoundboardMod.MODID + ".open_soundboard.no_sounds"), 0, 0,
                        0x404040);
                return;
            }

            int x = this.left;
            int y = relativeY;
            for (SoundWidget widget : widgets) {
                widget.setPosition(x, y);
                widget.render(poseStack, mouseX, mouseY, 0);
                x += widget.getWidth() + 8;
                if (x > entryRight) {
                    x = this.left;
                    y += widget.getHeight() + 8;
                }
            }
        }

        @Override
        public NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        }

        public void addSound(SoundWidget widget) {
            this.widgets.add(widget);
        }
    }

    public static class SoundWidget extends AbstractWidget {
        public SoundWidget() {
            super(0, 0, 64, 64, Component.empty());
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            blit(poseStack, getX(), getY(), 0, isHoveredOrFocused() ? 64 : 0, this.width, this.height, 128, 128);

            if (isHovered()) {
                boolean isOver = Utils.isPosInArea(getX() + this.width - 16, getY() + this.height - 16, 16, 16, mouseX,
                        mouseY);
                blit(poseStack, getX() + this.width - 16, getY() + this.height - 16, 64, isOver ? 16 : 0, 16, 16, 128,
                        128);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            defaultButtonNarrationText(pNarrationElementOutput);
        }
    }

    public class AddSoundButton extends AbstractWidget {
        public AddSoundButton() {
            super(SoundboardScreen.this.width - 20, 2, 16, 16, Component.empty());
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            blit(poseStack, getX(), getY(), 64, isHoveredOrFocused() ? 48 : 32, this.width, this.height, 128, 128);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            defaultButtonNarrationText(pNarrationElementOutput);
        }

        @Override
        public void onClick(double pMouseX, double pMouseY) {
            super.onClick(pMouseX, pMouseY);
            SoundboardScreen.this.minecraft.pushGuiLayer(new AddSoundScreen(SoundboardScreen.this));
        }
    }
}
