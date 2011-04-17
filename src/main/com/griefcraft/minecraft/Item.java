/**
 * This file is part of INVisible (https://github.com/Hidendra/INVisible)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.griefcraft.minecraft;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Item {

	/**
	 * Item names for improved reading
	 */
        public static final Map<Integer, String> items;
        static {
            Map<Integer,String> itemMap = new HashMap<Integer,String>();

            String[] block_names = {"air", "stone", "grass", "dirt", "cobblestone", "wood", "sapling", "bedrock", "water", "spring", "lava", "lava-spring", "sand", "gravel", "gold-ore", "iron-ore", "coal-ore", "log", "leaves", "sponge", "glass", "lapis-lazuli-ore", "lapis-lazuli", "dispenser", "sandstone", "note-block", "agua-cloth", "cyan-cloth", "blue-cloth", "purple-cloth", "indigo-cloth", "violet-cloth", "magenta-cloth", "pink-cloth", "black-cloth", "cloth", "wool", "flower", "rose", "brown-mushroom", "red-mushroom", "gold", "iron", "double-step", "step", "brick", "tnt", "bookshelf", "mossy-cobblestone", "obsidian", "torch", "fire", "mob-spawner", "wooden-stairs", "chest", "redstone-wire", "diamond-ore", "diamond", "workbench", "crops", "soil", "furnace", "burning-furnace", "signpost", "wooden-door", "ladder", "tracks", "stone-stairs", "wall-sign", "lever", "stone-plate", "iron-door", "wooden-plate", "redstone-ore", "glowing-redstone-ore", "redstone-torch-off", "redstone-torch", "stone-button", "snow", "ice", "snow-block", "cactus", "clay", "sugar-cane", "jukebox", "fence", "pumpkin", "brimstone", "slow-sand", "lightstone", "portal", "jack-o-lantern", "cake"};
            String[] item_names = {"iron-shovel", "iron-pickaxe", "iron-axe", "flint-and-steel", "apple", "bow", "arrow", "coal", "diamond", "iron-ingot", "gold-ingot", "iron-sword", "wooden-sword", "wooden-shovel", "wooden-pickaxe", "wooden-axe", "stone-sword", "stone-shovel", "stone-pickaxe", "stone-axe", "diamond-sword", "diamond-shovel", "diamond-pickaxe", "diamond-axe", "stick", "bowl", "mushroom-soup", "gold-sword", "gold-shovel", "gold-pickaxe", "gold-axe", "string", "feather", "sulphur", "wooden-hoe", "stone-hoe", "iron-hoe", "diamond-hoe", "gold-hoe", "seeds", "wheat", "bread", "leather-helmet", "leather-chestplate", "leather-leggings", "leather-boots", "chainmail-helmet", "chainmail-chestplate", "chainmail-leggings", "chainmail-boots", "iron-helmet", "iron-chestplate", "iron-leggings", "iron-boots", "diamond-helmet", "diamond-chestplate", "diamond-leggings", "diamond-boots", "gold-helmet", "gold-chestplate", "gold-leggings", "gold-boots", "flint", "raw-porkchop", "cooked-porkchop", "paintings", "golden-apple", "sign", "wooden-door", "bucket", "water-bucket", "lava-bucket", "mine-cart", "saddle", "iron-door", "redstone", "snowball", "boat", "leather", "milk", "clay-brick", "clay-balls", "sugar-cane", "paper", "book", "slimeball", "storage-minecart", "powered-minecart", "egg", "compass", "fishing-rod", "clock", "glowstone-dust", "raw-fish", "cooked-fish", "dye", "bone", "sugar", "cake", "bed", "redstone-repeater", "cookie"};
            String[] special_item_names = {"gold-music-disc", "green-music-disc"};

            for (int i = 0;i<block_names.length;i++) {
                itemMap.put(i, block_names[i]);
            }

            for (int i = 0;i<item_names.length;i++) {
                itemMap.put(i+0x100, item_names[i]);
            }

            for (int i = 0;i<special_item_names.length;i++) {
                itemMap.put(i+0x8d0, special_item_names[i]);
            }

            items = Collections.unmodifiableMap(itemMap);
        }
    
	/**
	 * Item slot
	 */
	private int slot;

	/**
	 * Item id
	 */
	private int id;

	/**
	 * Item amount
	 */
	private int amount;

	/**
	 * Item damage
	 */
	private int damage;

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getSlot() {
		return slot;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public int getDamage() {
		return damage;
	}

	@Override
	public String toString() {
            //what if, in the future, new item gets added? Thou shall not return null!
            if (items.get(id)!=null) {
		return String.format("%s\t[slot: %d]\t[amount: %d]\t[damage: %d]", items.get(id), slot, amount, damage);
            } else {
                return String.format("%d\t[slot: %d]\t[amount: %d]\t[damage: %d]", id, slot, amount, damage);
            }
	}

}
