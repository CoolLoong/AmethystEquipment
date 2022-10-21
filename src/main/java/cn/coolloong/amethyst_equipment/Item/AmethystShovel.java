package cn.coolloong.amethyst_equipment.Item;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

import java.util.List;

public class AmethystShovel extends ItemCustomTool {
    public AmethystShovel() {
        super("yes:amethyst_shovel", null, "amethyst_shovel");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addRepairItems(List.of(Item.fromString("minecraft:amethyst_shard")), 100)
                .addRepairItems(List.of(Item.fromString("yes:amethyst_shovel")), 400)
                .speed(12)
                .creativeGroup("itemGroup.name.shovel")
                .allowOffHand(true)
                .handEquipped(true)
                .build();
    }

    @Override
    public int getMaxDurability() {
        return 500;
    }

    @Override
    public int getTier() {
        return ItemSwordDiamond.TIER_DIAMOND;
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }

    @Override
    public int getEnchantAbility() {
        return 22;
    }

    @Override
    public boolean isShovel() {
        return true;
    }
}
