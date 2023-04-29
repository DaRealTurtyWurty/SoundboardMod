package dev.turtywurty.soundboardmod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Either;
import dev.turtywurty.soundboardmod.SoundboardMod;
import dev.turtywurty.soundboardmod.client.SoundboardSound;
import dev.turtywurty.soundboardmod.client.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.joml.Vector2f;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AddSoundScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + SoundboardMod.MODID + ".add_sound.title");
    private static final Component NAME_FIELD = Component.translatable("gui." + SoundboardMod.MODID + ".add_sound.name")
            .append(":");
    private static final Component SOUND_PATH_FIELD = Component.translatable(
            "gui." + SoundboardMod.MODID + ".add_sound.sound_path").append(":");

    private static final Component VOLUME_SLIDER = Component.translatable(
            "gui." + SoundboardMod.MODID + ".add_sound.volume").append(":");
    private static final Component PITCH_SLIDER = Component.translatable(
            "gui." + SoundboardMod.MODID + ".add_sound.pitch").append(":");

    private static final ResourceLocation TEXTURE = new ResourceLocation(SoundboardMod.MODID,
            "textures/gui/add_sound.png");

    private final Screen parent;
    private final int imageWidth = 176, imageHeight = 196;
    private int leftPos, topPos;

    private InformationScrollPanel scrollPanel;
    private EditBox nameField;
    private EditBox soundPathField;
    private PlaySoundButton playSoundButton;
    private ForgeSlider volumeSlider, pitchSlider;
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

        this.scrollPanel = addRenderableWidget(new InformationScrollPanel());

        this.nameField = this.scrollPanel.addComponent(
                new EditBox(this.font, this.leftPos + 7, this.topPos + 37, 162, 20, Component.empty()));
        this.nameField.setMaxLength(32);

        this.soundPathField = this.scrollPanel.addComponent(
                new EditBox(this.font, this.leftPos + 7, this.topPos + 74, 140, 20, Component.empty()));
        this.soundPathField.setMaxLength(256);
        this.playSoundButton = this.scrollPanel.addComponent(
                new PlaySoundButton(() -> Path.of(this.soundPathField.getValue()), () -> 1F, () -> 1F));

        this.volumeSlider = this.scrollPanel.addComponent(
                new ForgeSlider(this.leftPos + 7, this.topPos + 111, 162, 20, Component.empty(), Component.literal("%"),
                        0, 100, 100, 1, 1, true));

        this.pitchSlider = this.scrollPanel.addComponent(
                new ForgeSlider(this.leftPos + 7, this.topPos + 148, 162, 20, Component.empty(), Component.literal("%"),
                        0, 100, 100, 1, 1, true));

        this.createButton = addRenderableWidget(new CreateButton());
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
        this.font.draw(pPoseStack, VOLUME_SLIDER, this.leftPos + 8, this.topPos + 99, 0x404040);
        this.font.draw(pPoseStack, PITCH_SLIDER, this.leftPos + 8, this.topPos + 136, 0x404040);

//        // Render image preview
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//
//
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
//        blit(pPoseStack, this.leftPos + 7, this.topPos + 100, 176, 0, 40, 40);
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

    public class InformationScrollPanel extends ScrollPanel {
        private final List<AbstractWidget> components = new ArrayList<>();

        public InformationScrollPanel() {
            super(AddSoundScreen.this.minecraft, 162, 140, AddSoundScreen.this.topPos + 25,
                    AddSoundScreen.this.leftPos + 7);
        }

        public <T extends AbstractWidget> T addComponent(T component) {
            this.components.add(AddSoundScreen.this.addWidget(component));
            return component;
        }

        @Override
        protected int getContentHeight() {
            return this.components.stream().mapToInt(widget -> widget.getY() + widget.getHeight()).max().orElse(0);
        }

        @Override
        protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            GuiComponent.enableScissor(this.left, this.top, this.width, this.height);

            // if the component is above the top of the screen or below the bottom, don't draw it
            this.components.stream()
                    .filter(component -> component.getY() > relativeY && component.getY() < relativeY + this.height)
                    .forEach(component -> {
                        // translate the component to be in the correct position (using the relativeY)
                        poseStack.pushPose();
                        poseStack.translate(0, -relativeY, 0);
                        component.render(poseStack, mouseX, mouseY, Minecraft.getInstance().getPartialTick());
                        poseStack.popPose();
                    });

            GuiComponent.disableScissor();
        }

        @Override
        public NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        }
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
            if (!this.active) return 196;

            return this.isHovered() ? 236 : 216;
        }
    }

    public class PlaySoundButton extends AbstractWidget {
        private final Supplier<Path> soundPath;
        private final Supplier<Float> volume;
        private final Supplier<Float> pitch;

        public PlaySoundButton(Supplier<Path> soundPath, Supplier<Float> volume, Supplier<Float> pitch) {
            super(AddSoundScreen.this.leftPos + 149, AddSoundScreen.this.topPos + 74, 20, 20, Component.empty());

            this.soundPath = soundPath;
            this.volume = volume;
            this.pitch = pitch;
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
            if (!this.active) return 196;

            return this.isHovered() ? 236 : 216;
        }

        @Override
        protected boolean clicked(double pMouseX, double pMouseY) {
            if (!this.active) return false;

            if (Utils.isPosInArea(getX(), getY(), this.width, this.height, pMouseX, pMouseY)) {
                String soundPath = AddSoundScreen.this.soundPathField.getValue();
                if (soundPath.isEmpty()) return false;

                Minecraft minecraft = AddSoundScreen.this.getMinecraft();
                System.out.println(this.soundPath.get());
                minecraft.getSoundManager()
                        .play(new SoundboardSound(this.soundPath.get(), minecraft.player.position(), this.volume.get(),
                                this.pitch.get()));
                return true;
            }

            return false;
        }
    }

    public static class TextWidget extends AbstractWidget {
        private final TextData textData;
        private final Font font = Minecraft.getInstance().font;

        public TextWidget(int pX, int pY, int pWidth, int pHeight, TextData textData) {
            super(pX, pY, pWidth, pHeight, Component.empty());
            this.textData = textData;
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            Either<Supplier<String>, Supplier<Component>> content = this.textData.content();
            poseStack.pushPose();

            Component component = content.map(text -> Component.literal(text.get()), Supplier::get);
            int xOffset = 0;
            if (this.textData.centered()) {
                xOffset += this.width / 2f - this.font.width(component) / 2f;
            }

            poseStack.translate(getX() + xOffset, getY(), 0);

            poseStack.scale(this.textData.scale().x, this.textData.scale().y, 1);

            if (this.textData.shadow()) {
                this.font.drawShadow(poseStack, component, 0, 0, this.textData.color());
            } else {
                this.font.draw(poseStack, component, 0, 0, this.textData.color());
            }

            poseStack.popPose();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.textData.content.ifLeft(text -> pNarrationElementOutput.add(NarratedElementType.TITLE, text.get()));
            this.textData.content.ifRight(text -> pNarrationElementOutput.add(NarratedElementType.TITLE, text.get()));
        }

        @Override
        protected boolean clicked(double pMouseX, double pMouseY) {
            return false;
        }
    }

    public record TextData(Either<Supplier<String>, Supplier<Component>> content, int color, Vector2f scale,
                           boolean shadow, boolean centered) {
        public static class Builder {
            private Either<Supplier<String>, Supplier<Component>> content;
            private int color = 0xFFFFFF;
            private Vector2f scale = new Vector2f(1, 1);
            private boolean shadow = false;
            private boolean centered = false;

            public Builder text(Supplier<String> text) {
                this.content = Either.left(text);
                return this;
            }

            public Builder component(Supplier<Component> component) {
                this.content = Either.right(component);
                return this;
            }

            public Builder color(int color) {
                this.color = color;
                return this;
            }

            public Builder scale(float scale) {
                this.scale = new Vector2f(scale, scale);
                return this;
            }

            public Builder scale(float x, float y) {
                this.scale = new Vector2f(x, y);
                return this;
            }

            public Builder shadow() {
                this.shadow = true;
                return this;
            }

            public Builder centered() {
                this.centered = true;
                return this;
            }

            public TextData build() {
                return new TextData(this.content, this.color, this.scale, this.shadow, this.centered);
            }
        }
    }
}
