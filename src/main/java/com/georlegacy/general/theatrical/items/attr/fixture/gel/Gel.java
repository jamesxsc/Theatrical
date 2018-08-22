package com.georlegacy.general.theatrical.items.attr.fixture.gel;

import com.georlegacy.general.theatrical.entities.core.IHasModel;
import com.georlegacy.general.theatrical.init.TheatricalItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Gel extends Item implements IHasModel {

    public Gel() {
        this
                .setRegistryName("theatrical", "gel")
                .setUnlocalizedName("gel")
                .setMaxStackSize(64)
                .setMaxDamage(0)
                .setHasSubtypes(true).setCreativeTab(
            com.georlegacy.general.theatrical.tabs.base.CreativeTabs.gelsTab);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return this.getUnlocalizedName() + "." + GelType.getGelType(stack.getMetadata()).getId();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        for (GelType gelType : GelType.values()) {
            itemStacks.add(new ItemStack(this,
                1,
                gelType.getId()
            ));
        }
        items.addAll(itemStacks);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        GelType gelType = GelType.getGelType(stack.getMetadata());
        return this.getUnlocalizedName() + "." + gelType.getId();
    }

    @Override
    public void registerModels() {
        TheatricalItems.registerGelRenderers();
    }
}