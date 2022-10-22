package cn.coolloong.amethyst_equipment.Item;

import cn.coolloong.amethyst_equipment.AmethystEquipment;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.item.customitem.data.Offset;
import cn.nukkit.item.customitem.data.RenderOffsets;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AnimateEntityPacket;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.potion.Effect;

import java.util.ArrayList;
import java.util.List;

public class AmethystSpear extends ItemCustomTool {
    public AmethystSpear() {
        super("yes:amethyst_spear", "Amethyst Spear\n\n§6Special Attack:\n§e[§9+20 atk\uE11B§e]\n§e[§90.64 secs\uE109§e]\n§e[§93.0 secs\uE108§e]", "amethyst_spear");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addRepairItems(List.of(Item.fromString("minecraft:amethyst_shard")), 100)
                .addRepairItems(List.of(Item.fromString("yes:amethyst_spear")), 400)
                .renderOffsets(new RenderOffsets(
                                Offset.builder()
                                        .position(0.48f, -0.128f, -0.946f)
                                        .rotation(11.696f, -64.536f, 79.413f)
                                        .scale(0.038f, 0.037f, 0.038f),
                                Offset.builder()
                                        .position(0.258f, 0.979f, -0.541f)
                                        .rotation(-63.268f, -43.969f, 144.041f)
                                        .scale(0.094f, 0.094f, 0.094f),
                                Offset.builder()
                                        .position(-1.053f, 0.136f, -0.803f)
                                        .rotation(27.273f, 67.731f, -64.494f)
                                        .scale(0.063f, 0.063f, 0.063f),
                                Offset.builder()
                                        .position(0.258f, 0.979f, -0.541f)
                                        .rotation(-63.268f, -43.969f, 144.041f)
                                        .scale(0.094f, 0.094f, 0.094f)
                        )
                )
                .creativeGroup("itemGroup.name.sword")
                .allowOffHand(false)
                .handEquipped(true)
                .customBuild(nbt -> {
                    nbt.getCompound("components")
                            .putCompound("minecraft:cooldown", new CompoundTag()
                                    .putString("category", "amethyst_spear")
                                    .putFloat("duration", 3f))
                            .getCompound("item_properties").putBoolean("animates_in_toolbar", true)
                            .getCompound("item_properties").putInt("use_duration", 640);
                });
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
    public boolean isSword() {
        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        if (!player.containTag("noUseStab")) {
            player.setItemCoolDown(60, "amethyst_spear");//cd 3s 60tick
            player.addTag("noUseStab");
            var pk = new AnimateEntityPacket();
            pk.setAnimation("animation.player.stab2");
            pk.setController("__runtime_controller");
            pk.setStopExpression("query.any_animation_finished");
            pk.setStopExpressionVersion(16777216);
            pk.setNextState("default");
            pk.setBlendOutTime(0f);
            pk.setEntityRuntimeIds(List.of(player.getId()));
            for (var pl : AmethystEquipment.instance.getServer().getOnlinePlayers().values()) {
                pl.dataPacket(pk);
            }
            AmethystEquipment.instance.getServer().getScheduler().scheduleDelayedTask(AmethystEquipment.instance, () -> {
                var pk2 = new AnimateEntityPacket();
                pk2.setAnimation("animation.player.move.arms.stationary");
                pk2.setController("__runtime_controller");
                pk2.setStopExpression("query.any_animation_finished");
                pk2.setStopExpressionVersion(16777216);
                pk2.setNextState("default");
                pk2.setBlendOutTime(0f);
                pk2.setEntityRuntimeIds(List.of(player.getId()));
                for (var pl : AmethystEquipment.instance.getServer().getOnlinePlayers().values()) {
                    pl.dataPacket(pk2);
                }
            }, 36);

            AmethystEquipment.instance.getServer().getScheduler().scheduleDelayedTask(AmethystEquipment.instance, () -> {
                var level = player.getLevel();
                if (!(directionVector.y >= 0) || !(directionVector.y <= 0.5)) {
                    directionVector.setY(0);
                }
                Vector3 pos = player.add(directionVector.clone().multiply(3));
                var targetEntities = this.beHitEntities(level, pos, player, directionVector.clone(), 6);
                var r20Player = this.r20Player(pos, player, directionVector.clone());

                level.addParticleEffect(pos.asVector3f(), "yes:spear_particle", -1, level.getDimension(), Player.EMPTY_ARRAY);
                player.addEffect(Effect.getEffectByName("weakness").setDuration(40).setAmplifier(255));
                for (var pl : r20Player) {
                    PlaySoundPacket pk1 = new PlaySoundPacket();
                    pk1.volume = 1f;
                    pk1.x = pos.getFloorX();
                    pk1.y = pos.getFloorY();
                    pk1.z = pos.getFloorZ();
                    pk1.name = "weapon.spear_weewee";
                    pk1.pitch = 1;//这个pitch是音调的意思不是player.pitch
                    pl.dataPacket(pk1);
                }

                if (targetEntities.size() != 0) {
                    for (var entity : targetEntities) {
                        entity.attack(20);
                    }
                    if (!player.isCreative()) {
                        this.meta += 20;
                        player.getInventory().setItemInHand(this);
                    }
                    for (var pl : r20Player) {
                        var target = targetEntities.get(0);
                        PlaySoundPacket pk2 = new PlaySoundPacket();
                        pk2.volume = 1f;
                        pk2.x = target.getFloorX();
                        pk2.y = target.getFloorY();
                        pk2.z = target.getFloorZ();
                        pk2.name = "weapon.spear_hit_weewee";
                        pk2.pitch = 1;
                        pl.dataPacket(pk2);
                    }
                }
            }, 17);
            AmethystEquipment.instance.getServer().getScheduler().scheduleDelayedTask(AmethystEquipment.instance, () -> {
                player.removeTag("noUseStab");
            }, 60);
            return true;
        } else return false;
    }

    private List<Entity> beHitEntities(Level level, Vector3 particle, Player player, Vector3 direction, int distance) {
        var chunk = level.getChunk(particle.getFloorX() >> 4, particle.getFloorZ() >> 4);
        var chunks = new ArrayList<>(chunk.getEntities().values());
        if (chunk.getIndex() != level.getChunk((particle.getFloorX() + 1) >> 4, particle.getFloorZ() >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() + 1) >> 4, particle.getFloorZ() >> 4).values());
        } else if (chunk.getIndex() != level.getChunk((particle.getFloorX() - 1) >> 4, particle.getFloorZ() >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() - 1) >> 4, particle.getFloorZ() >> 4).values());
        } else if (chunk.getIndex() != level.getChunk(particle.getFloorX() >> 4, (particle.getFloorZ() + 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities(particle.getFloorX() >> 4, (particle.getFloorZ() + 1) >> 4).values());
        } else if (chunk.getIndex() != level.getChunk(particle.getFloorX() >> 4, (particle.getFloorZ() - 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities(particle.getFloorX() >> 4, (particle.getFloorZ() - 1) >> 4).values());
        } else if (chunk.getIndex() != level.getChunk((particle.getFloorX() - 1) >> 4, (particle.getFloorZ() - 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() - 1) >> 4, (particle.getFloorZ() - 1) >> 4).values());
        } else if (chunk.getIndex() != level.getChunk((particle.getFloorX() + 1) >> 4, (particle.getFloorZ() + 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() + 1) >> 4, (particle.getFloorZ() + 1) >> 4).values());
        } else if (chunk.getIndex() != level.getChunk((particle.getFloorX() + 1) >> 4, (particle.getFloorZ() - 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() + 1) >> 4, (particle.getFloorZ() - 1) >> 4).values());
        } else if (chunk.getIndex() != level.getChunk((particle.getFloorX() - 1) >> 4, (particle.getFloorZ() + 1) >> 4).getIndex()) {
            chunks.addAll(level.getChunkEntities((particle.getFloorX() - 1) >> 4, (particle.getFloorZ() + 1) >> 4).values());
        }
        return chunks.stream()
                .filter(e -> {
                    if (!e.equals(player)) {
                        var aabb = e.getBoundingBox();
                        var target = player.add(direction.clone().multiply(distance));
                        int tX = target.getFloorX();
                        int tY = target.getFloorY();
                        int tZ = target.getFloorZ();
                        int plX = player.getFloorX();
                        int plY = player.getFloorY();
                        int plZ = player.getFloorZ();
                        double x1 = Math.floor(aabb.getMinX());
                        double x2 = Math.ceil(aabb.getMaxX());
                        double z1 = Math.floor(aabb.getMinZ());
                        double z2 = Math.ceil(aabb.getMaxZ());

                        for (int x = Math.min(tX, plX); x <= Math.max(tX, plX); ++x) {
                            for (int y = Math.min(tY, plY); y <= Math.max(tY, plY); ++y) {
                                for (int z = Math.min(tZ, plZ); z <= Math.max(tZ, plZ); ++z) {
                                    if (x >= x1 && x <= x2 && y >= Math.floor(aabb.getMinY()) && y <= Math.ceil(aabb.getMaxY()) && z >= z1 && z <= z2) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                })
                .toList();
    }

    private List<Player> r20Player(Vector3 particle, Player player, Vector3 direction) {
        var pos = player.add(direction.clone().multiply(3));
        return player.getServer().getOnlinePlayers().values()
                .stream()
                .filter(pl -> Math.abs(pl.x - particle.x) < 21 && Math.abs(pl.y - particle.y) < 21 && Math.abs(pl.z - particle.z) < 21)
                .toList();
    }
}
