package cn.coolloong.amethyst_equipment.Item;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

import java.util.List;

public class AmethystHoe extends ItemCustomTool {
    public AmethystHoe() {
        super("yes:amethyst_hoe", null, "amethyst_hoe");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addRepairItems(List.of(Item.fromString("minecraft:amethyst_shard")), 100)
                .addRepairItems(List.of(Item.fromString("yes:amethyst_hoe")), 400)
                .speed(12)
                .creativeGroup("itemGroup.name.hoe")
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
        return 5;
    }

    @Override
    public int getEnchantAbility() {
        return 22;
    }

    @Override
    public boolean isHoe() {
        return true;
    }
}
