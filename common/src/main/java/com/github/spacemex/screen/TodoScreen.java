package com.github.spacemex.screen;

import com.github.spacemex.client.TodoStorage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TodoScreen extends Screen {
    private final List<String> todos;
    private TextFieldWidget inputField;
    private ButtonWidget addButton;
    private float scrollAmount = 0f;
    private final float maxScrollStep = 20f;
    private final int listTopPadding = 40;
    private final int listBottomPadding = 40;

    private boolean draggingScrollbar = false;
    private int dragStartY = 0;
    private float dragStartScroll = 0f;

    public TodoScreen() {
        super(Text.literal("Todo List"));
        this.todos = new ArrayList<>(TodoStorage.loadTodos());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int contentHeight = todos.size() * 25;
        int listTop = listTopPadding;
        int listBottom = this.height - listBottomPadding - 30;
        int visibleHeight = listBottom - listTop;
        int maxScroll = Math.max(0, contentHeight - visibleHeight);

        scrollAmount -= (float) (verticalAmount * maxScrollStep);

        // Clamp scrollAmount exactly like dragging
        if (scrollAmount < 0) scrollAmount = 0;
        if (scrollAmount > maxScroll) scrollAmount = maxScroll;

        this.init();
        return true;
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingScrollbar) {
            int listWidth = 200;
            int listX = (this.width - listWidth) / 2;
            int listTop = listTopPadding;
            int listBottom = this.height - listBottomPadding - 30;
            int visibleHeight = listBottom - listTop;

            int contentHeight = todos.size() * 25;
            int maxScroll = Math.max(0, contentHeight - visibleHeight);

            int scrollbarHeight = visibleHeight;
            int thumbHeight = Math.max(20, (int)((float)visibleHeight * ((float)visibleHeight / contentHeight)));

            int deltaYInt = (int) mouseY - dragStartY;
            float scrollRange = scrollbarHeight - thumbHeight;

            scrollAmount = dragStartScroll + (deltaYInt / scrollRange) * maxScroll;
            if (scrollAmount < 0) scrollAmount = 0;
            if (scrollAmount > maxScroll) scrollAmount = maxScroll;

            this.init();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int listWidth = 200;
        int listX = (this.width - listWidth) / 2;
        int listTop = listTopPadding;
        int listBottom = this.height - listBottomPadding - 30;
        int visibleHeight = listBottom - listTop;

        int scrollbarX = listX + listWidth + 4;
        int scrollbarWidth = 6;

        int contentHeight = todos.size() * 25;

        if (contentHeight > visibleHeight) {
            float scrollRatio = scrollAmount / (contentHeight - visibleHeight);
            int thumbHeight = Math.max(20, (int)((float)visibleHeight * ((float)visibleHeight / contentHeight)));
            int thumbY = listTop + (int)((visibleHeight - thumbHeight) * scrollRatio);

            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth &&
                    mouseY >= thumbY && mouseY <= thumbY + thumbHeight) {
                draggingScrollbar = true;
                dragStartY = (int) mouseY;
                dragStartScroll = scrollAmount;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        this.clearChildren();

        int listWidth = 200;
        int listX = (this.width - listWidth) / 2;
        int y = listTopPadding - (int) scrollAmount;

        int listTop = listTopPadding;
        int listBottom = this.height - listBottomPadding - 30;

        for (int i = 0; i < todos.size(); i++) {
            if (y + 20 >= listTop && y <= listBottom) {
                int finalI = i;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(todos.get(i)), b -> {
                    todos.remove(finalI);
                    saveTodos();
                    // Clamp scrollAmount so it doesn't overscroll after removal
                    int visibleHeight = listBottom - listTop;
                    int contentHeight = todos.size() * 25;
                    int maxScroll = Math.max(0, contentHeight - visibleHeight);

                    if (scrollAmount > maxScroll) scrollAmount = maxScroll;
                    if (scrollAmount < 0) scrollAmount = 0;
                    this.init();
                }).position(listX, y).size(listWidth, 20).tooltip(Tooltip.of(Text.literal("Left-Click to Remove"))).build());
            }
            y += 25;
        }

        int inputY = this.height - listBottomPadding - 20;
        inputField = new TextFieldWidget(this.textRenderer, listX, inputY, listWidth - 60, 20, Text.literal(""));
        this.addSelectableChild(inputField);

        addButton = ButtonWidget.builder(Text.literal("Add"), b -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                todos.add(text);
                saveTodos();

                int contentHeight = todos.size() * 25;
                int visibleHeight = listBottom - listTop;

                scrollAmount = Math.max(0, contentHeight - visibleHeight);
                if (scrollAmount < 0) scrollAmount = 0;

                inputField.setText("");
                this.init();
            }
        }).position(listX + listWidth - 55, inputY).size(55, 20).build();

        this.addDrawableChild(addButton);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //this.renderBackground(context,mouseX,mouseY,delta); this is why we dont have nice things :(

        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);

        int listWidth = 200;
        int listX = (this.width - listWidth) / 2;
        int listTop = listTopPadding;
        int listBottom = this.height - listBottomPadding - 30;
        int visibleHeight = listBottom - listTop;


        // Clip scroll area
        enableScissor(listX, listTop, listWidth, visibleHeight);
        super.render(context, mouseX, mouseY, delta);
        disableScissor();

        // Draw fixed widgets
        inputField.render(context, mouseX, mouseY, delta);
        addButton.render(context, mouseX, mouseY, delta);

        // Draw scrollbar background (track)
        int scrollbarX = listX + listWidth + 4;
        int scrollbarY = listTop;
        int scrollbarWidth = 6;
        int scrollbarHeight = visibleHeight;

        fill(context, scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, 0xFF555555); // dark grey track

        // Calculate scrollbar thumb height and position
        int contentHeight = todos.size() * 25;
        if (contentHeight > visibleHeight) {
            float scrollRatio = scrollAmount / (contentHeight - visibleHeight);
            int thumbHeight = Math.max(20, (int)((float)visibleHeight * ((float)visibleHeight / contentHeight)));
            int thumbY = scrollbarY + (int)((scrollbarHeight - thumbHeight) * scrollRatio);

            fill(context, scrollbarX, thumbY, scrollbarX + scrollbarWidth, thumbY + thumbHeight, 0xFFAAAAAA); // lighter thumb
        }
    }

    private void saveTodos() {
        TodoStorage.saveTodos(todos);
    }

    private void enableScissor(int x, int y, int width, int height) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double scale = mc.getWindow().getScaleFactor();
        int scissorX = (int) (x * scale);
        int scissorY = (int) ((mc.getWindow().getScaledHeight() - (y + height)) * scale);
        int scissorW = (int) (width * scale);
        int scissorH = (int) (height * scale);
        RenderSystem.enableScissorForRenderTypeDraws(scissorX, scissorY, scissorW, scissorH);
    }

    private void disableScissor() {
        RenderSystem.disableScissorForRenderTypeDraws();
    }

    private void fill(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
    }
}
