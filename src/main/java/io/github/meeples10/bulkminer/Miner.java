package io.github.meeples10.bulkminer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class Miner {
    private static int overflowDetector = 0;

    public static void mine(Block block, Player miner, int xp) {
        overflowDetector = Main.maxDistance * 5000;
        List<Block> list = new ArrayList<>();
        findNeighbors(block, Main.maxDistance, list);
        mineAll(list, block, miner, xp);
    }

    private static void findNeighbors(Block block, int distance, List<Block> list) {
        if(distance == 0) return;
        overflowDetector--;
        if(overflowDetector <= 0) {
            Bukkit.getPluginManager().getPlugin(Main.NAME).getLogger()
                    .warning("Stack overflow protection: Cancelling flood fill");
        }
        list.add(block);
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    if(x == 0 && y == 0 && z == 0) continue;
                    Block b = block.getRelative(x, y, z);
                    if(b.getType() == block.getType() && !list.contains(b)) findNeighbors(b, distance - 1, list);
                }
            }
        }
    }

    private static void mineAll(List<Block> blocks, Block initial, Player miner, int xp) {
        ItemStack tool = miner.getInventory().getItemInMainHand();
        Damageable d = (Damageable) tool.getItemMeta();
        short maxDurability = tool.getType().getMaxDurability();
        List<ItemStack> drops = new ArrayList<>();
        int xpDrop = xp;
        for(Block b : blocks) {
            drops.addAll(b.getDrops(miner.getInventory().getItemInMainHand(), miner));
            xpDrop += (int) (xp * Math.random());
            b.setType(Material.AIR);
            if(!d.isUnbreakable() && miner.getGameMode() != GameMode.CREATIVE) damageItem(tool, d);
            if(d.hasDamage() && !d.isUnbreakable() && d.getDamage() > maxDurability) {
                miner.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                miner.playSound(miner.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                break;
            }
        }
        tool.setItemMeta(d);
        if(Main.dropXp && xpDrop > 0) {
            ExperienceOrb orb = miner.getWorld().spawn(initial.getLocation(), ExperienceOrb.class);
            orb.setExperience(xpDrop);
        }
        consolidateStacks(drops);
        for(ItemStack item : drops) {
            miner.getWorld().dropItemNaturally(initial.getLocation(), item);
        }
    }

    private static void damageItem(ItemStack tool, Damageable d) {
        if(Math.random() < 1.0 / (double) (tool.getEnchantmentLevel(Enchantment.DURABILITY) + 1))
            d.setDamage(d.getDamage() + 1);
    }

    private static void consolidateStacks(List<ItemStack> list) {
        List<ItemStack> temp = new ArrayList<>();
        for(ItemStack stack : list) {
            for(ItemStack i : temp) {
                if(i.isSimilar(stack)) {
                    int tempCount = i.getAmount();
                    if(tempCount < 64) {
                        int transfer = Math.min(64 - tempCount, stack.getAmount());
                        i.setAmount(i.getAmount() + transfer);
                        stack.setAmount(stack.getAmount() - transfer);
                    }
                }
            }
            if(stack.getAmount() > 0) {
                temp.add(stack);
            }
        }
        list.clear();
        list.addAll(temp);
    }
}
