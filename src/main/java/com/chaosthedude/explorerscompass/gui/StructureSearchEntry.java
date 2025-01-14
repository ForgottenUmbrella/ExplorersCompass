package com.chaosthedude.explorerscompass.gui;

import com.chaosthedude.explorerscompass.ExplorersCompass;
import com.chaosthedude.explorerscompass.util.StructureUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructureSearchEntry extends ObjectSelectionList.Entry<StructureSearchEntry> {

	private final Minecraft mc;
	private final ExplorersCompassScreen parentScreen;
	private final ResourceLocation structureKey;
	private final StructureSearchList structuresList;
	private long lastClickTime;

	public StructureSearchEntry(StructureSearchList structuresList, ResourceLocation structureKey) {
		this.structuresList = structuresList;
		this.structureKey = structureKey;
		parentScreen = structuresList.getParentScreen();
		mc = Minecraft.getInstance();
	}

	@Override
	public void render(PoseStack poseStack, int par1, int par2, int par3, int par4, int par5, int par6, int par7, boolean par8, float par9) {
		mc.font.draw(poseStack, Component.literal(StructureUtils.getPrettyStructureName(structureKey)), par3 + 1, par2 + 1, 0xffffff);
		mc.font.draw(poseStack, Component.translatable(("string.explorerscompass.source")).append(Component.literal(": " + StructureUtils.getPrettyStructureSource(structureKey))), par3 + 1, par2 + mc.font.lineHeight + 3, 0x808080);
		mc.font.draw(poseStack, Component.translatable(("string.explorerscompass.group")).append(Component.literal(": ")).append(Component.translatable(StructureUtils.getPrettyStructureName(ExplorersCompass.structureKeysToTypeKeys.get(structureKey)))), par3 + 1, par2 + mc.font.lineHeight + 14, 0x808080);
		mc.font.draw(poseStack, Component.translatable(("string.explorerscompass.dimension")).append(Component.literal(": " + StructureUtils.dimensionKeysToString(ExplorersCompass.dimensionKeysForAllowedStructureKeys.get(structureKey)))), par3 + 1, par2 + mc.font.lineHeight + 25, 0x808080);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			structuresList.selectStructure(this);
			if (Util.getMillis() - lastClickTime < 250L) {
				searchForStructure();
				return true;
			} else {
				lastClickTime = Util.getMillis();
				return false;
			}
		}
		return false;
	}
	
	@Override
	public Component getNarration() {
		return Component.literal(StructureUtils.getPrettyStructureName(structureKey));
	}

	public void searchForStructure() {
		mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		parentScreen.searchForStructure(structureKey);
	}
	
	public void searchForGroup() {
		mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		parentScreen.searchForGroup(ExplorersCompass.structureKeysToTypeKeys.get(structureKey));
	}

}