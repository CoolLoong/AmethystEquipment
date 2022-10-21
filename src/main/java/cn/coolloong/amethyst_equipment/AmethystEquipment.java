package cn.coolloong.amethyst_equipment;

import cn.coolloong.amethyst_equipment.Item.*;
import cn.coolloong.amethyst_equipment.Item.armor.AmethystBoots;
import cn.coolloong.amethyst_equipment.Item.armor.AmethystChestplate;
import cn.coolloong.amethyst_equipment.Item.armor.AmethystHelmet;
import cn.coolloong.amethyst_equipment.Item.armor.AmethystLeggings;
import cn.coolloong.amethyst_equipment.utils.RecipeManager112;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AmethystEquipment extends PluginBase implements Listener {
    public static PluginLogger log;
    public static AmethystEquipment instance;

    @Override
    public void onLoad() {
        instance = this;
        log = this.getLogger();
        try {
            Item.registerCustomItem(List.of(
                    AmethystAxe.class, AmethystHoe.class, AmethystSword.class, AmethystShovel.class, AmethystPickaxe.class,
                    AmethystBoots.class, AmethystChestplate.class, AmethystHelmet.class, AmethystLeggings.class, AmethystSpear.class));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.info("AmethystEquipment start error!");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        log.info("AmethystEquipment running success!");
        RecipeManager112.registerRecipeToServer(this, "recipes/");
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        if (player.containTag("noUseStab")) {
            player.removeTag("noUseStab");
        }
    }
}
