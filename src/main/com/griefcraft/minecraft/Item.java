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

public class Item {

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
		return String.format("%d\t[slot: %d]\t[amount: %d]\t[damage: %d]", id, slot, amount, damage);
	}

}
