package cn.coolloong.amethyst_equipment.Item.armor;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomArmor;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

import java.util.List;

public class AmethystBoots extends ItemCustomArmor {
    public AmethystBoots() {
        super("yes:amethyst_boots", null, "amethyst_boots");
    }

    @Override
    public CustomItemDefinition getDefinition() {//todo 加上自定义击退抵抗 加上物品修复
        return CustomItemDefinition
                .armorBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addRepairItems(List.of(Item.fromString("minecraft:amethyst_shard")), 100)
                .addRepairItems(List.of(Item.fromString("yes:amethyst_boots")), 200)
                .creativeGroup("itemGroup.name.boots")
                .build();
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_DIAMOND;
    }

    @Override
    public int getMaxDurability() {
        return 240;
    }

    @Override
    public int getEnchantAbility() {
        return 22;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }
}
