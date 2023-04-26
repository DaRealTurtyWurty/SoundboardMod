package dev.turtywurty.soundboardmod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.soundboardmod.SoundboardMod;
import dev.turtywurty.soundboardmod.client.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class AddSoundScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + SoundboardMod.MODID + ".add_sound.title");
    private static final Component NAME_FIELD = Component.translatable("gui." + SoundboardMod.MODID + ".add_sound.name")
            .append(":");
    private static final Component SOUND_PATH_FIELD = Component.translatable(
            "gui." + SoundboardMod.MODID + ".add_sound.sound_path").append(":");

    private static final ResourceLocation TEXTURE = new ResourceLocation(SoundboardMod.MODID,
            "textures/gui/add_sound.png");

    private final Screen parent;
    private final int imageWidth = 176, imageHeight = 166;
    private int leftPos, topPos;

    private EditBox nameField;
    private EditBox soundPathField;
    private PlaySoundButton playSoundButton;
    private CreateButton createButton;

    public AddSoundScreen(Screen parent) {
        super(TITLE);

        this.parent = parent;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.parent.width - this.imageWidth) / 2;
        this.topPos = (this.parent.height - this.imageHeight) / 2;

        this.nameField = new EditBox(this.font, this.leftPos + 7, this.topPos + 37, 162, 20, Component.empty());
        this.nameField.setMaxLength(32);

        this.soundPathField = new EditBox(this.font, this.leftPos + 7, this.topPos + 74, 140, 20, Component.empty());
        this.soundPathField.setMaxLength(256);
        this.playSoundButton = new PlaySoundButton();

        this.createButton = new CreateButton();

        addRenderableWidget(this.nameField);
        addRenderableWidget(this.soundPathField);
        addRenderableWidget(this.playSoundButton);
        addRenderableWidget(this.createButton);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        // Render labels
        this.font.draw(pPoseStack, TITLE, this.leftPos + 8, this.topPos + 8, 0x404040);
        this.font.draw(pPoseStack, NAME_FIELD, this.leftPos + 8, this.topPos + 25, 0x404040);
        this.font.draw(pPoseStack, SOUND_PATH_FIELD, this.leftPos + 8, this.topPos + 62, 0x404040);

        // Render image preview
        RenderSystem.setShader(GameRenderer::getPositionTexShader);


        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        blit(pPoseStack, this.leftPos + 7, this.topPos + 100, 176, 0, 40, 40);
    }

    @Override
    public void onFilesDrop(List<Path> paths) {
        if (this.minecraft == null || paths.isEmpty()) return;

        double mouseX = Utils.getMouseX();
        double mouseY = Utils.getMouseY();

        if (Utils.isPosInArea(this.leftPos + 7, this.topPos + 62, 162, 32, mouseX, mouseY)) {
            Optional<Path> soundFile = Utils.findFirstSoundFile(paths);
            soundFile.ifPresent(path -> this.soundPathField.setValue(path.toAbsolutePath().toString()));
        }

        // TODO: Do the same for the thumbnail image
    }

    public class CreateButton extends AbstractWidget {
        private static final Component TITLE = Component.translatable(
                "gui." + SoundboardMod.MODID + ".add_sound.create");

        public CreateButton() {
            super(AddSoundScreen.this.leftPos + 7, AddSoundScreen.this.topPos + AddSoundScreen.this.imageHeight - 27,
                    162, 20, TITLE);
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            blit(poseStack, getX(), getY(), 0, getYOffset(), this.width, this.height);

            int textWidth = AddSoundScreen.this.font.width(TITLE);
            float textX = getX() + this.width / 2f - textWidth / 2f;
            float textY = getY() + this.height / 2f - AddSoundScreen.this.font.lineHeight / 2f;
            AddSoundScreen.this.font.drawShadow(poseStack, TITLE, textX, textY, 0xFFFFFF);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            defaultButtonNarrationText(pNarrationElementOutput);
        }

        private int getYOffset() {
            if (!this.active) return 166;

            return this.isHovered() ? 206 : 186;
        }
    }

    public class PlaySoundButton extends AbstractWidget {
        public PlaySoundButton() {
            super(AddSoundScreen.this.leftPos + 149, AddSoundScreen.this.topPos + 74, 20, 20, Component.empty());
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            blit(poseStack, getX(), getY(), 162, getYOffset(), this.width, this.height);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            defaultButtonNarrationText(pNarrationElementOutput);
        }

        private int getYOffset() {
            if (!this.active) return 166;

            return this.isHovered() ? 206 : 186;
        }

        @Override
        protected boolean clicked(double pMouseX, double pMouseY) {
            if (!this.active) return false;

            if (Utils.isPosInArea(getX(), getY(), this.width, this.height, pMouseX, pMouseY)) {
                String soundPath = AddSoundScreen.this.soundPathField.getValue();
                if (soundPath.isEmpty()) return false;

                // TODO: look into SoundEngine#play. Likely need to AT SoundEngine.channelAccess to create the channel handle
                return true;
            }

            return false;
        }
    }
}
