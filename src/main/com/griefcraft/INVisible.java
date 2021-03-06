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

package com.griefcraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.griefcraft.minecraft.Inventory;
import com.griefcraft.minecraft.Inventory.Type;
import com.griefcraft.minecraft.Item;
import com.griefcraft.minecraft.Location;
import com.griefcraft.minecraft.World;
import com.griefcraft.util.FilterOptions;

public class INVisible {

	/**
	 * Version
	 */
	public final static double VERSION = 2.00;

	/**
	 * The reader that reads the console
	 */
	private BufferedReader consoleReader;

	/**
	 * The world we are dealing with
	 */
	private World world;

	/**
	 * Hook for Bukkit
	 */
	private CommandSender commandSender;

	public INVisible() {
		log("INVisible v" + VERSION);

		log("");
		printCommands();

		do {
			logn("\n> ");
			String line = readLine();

			processCommand(line);
		} while (true);
	}

	/**
	 * Used by plugins or external sources (prevent help by being outputted)
	 * 
	 * @param noarg
	 */
	public INVisible(Object noarg) {
	}

	/**
	 * Set the command sender (used by Bukkit)
	 * 
	 * @param commandSender
	 */
	public void setCommandSender(CommandSender commandSender) {
		this.commandSender = commandSender;
	}

	/**
	 * Display the current page
	 */
	public void displayPage() {
		if (FilterOptions.PAGE <= 0) {
			log("Page is not positive, defaulting to 1");
			FilterOptions.PAGE = 1;
		}

		displayPage(FilterOptions.PAGE);
	}

	/**
	 * Display a page
	 * 
	 * @param page
	 */
	public void displayPage(int page) {
		log("[page " + page + "]");

		List<Inventory> inventories = filter(world.getInventories());

		// get the offset and ensure we have enough pages
		int size = inventories.size() - 1;
		int offset = (FilterOptions.PAGE - 1) * FilterOptions.PER_PAGE;
		int ceil = offset + FilterOptions.PER_PAGE - 1;

		if (size == -1) {
			log("No loaded inventories.");
			return;
		}

		if (offset > size) {
			log("Page is too large - defaulting to 1");
			FilterOptions.PAGE = 1;
			displayPage();
			return;
		}

		if (ceil > size) {
			ceil = offset + (size - offset);
		}

		for (int index = offset; index <= ceil; index++) {
			Inventory inventory = inventories.get(index);
        
                        List<Item> items = inventory.getItems();

                        log((index + 1) + ": " + inventory.toString() + " {");

                        for (Item item : items) {
                                log("\t" + item.toString());
                        }

                        log("}");
                        log("");
                        
		}
	}

 	/**
	 * Displayes all inventories (for commandline output purposes)
	 */
        public void displayAll() {
            List<Inventory> inventories = filter(world.getInventories());
            log("");
            for (int index = 0; index < inventories.size(); index++) {
			Inventory inventory = inventories.get(index);
                        List<Item> items = inventory.getItems();

                        log((index + 1) + ": " + inventory.toString() + " {");

                        for (Item item : items) {
                                log("\t" + item.toString());
                        }

                        log("}");
                        log("");
		}
        }

	/**
	 * @return the amount of pages
	 */
	public int getPageCount() {
		List<Inventory> inventories = filter(world.getInventories());

		// calculate the delta, or divide the list size by the page size
		int size = inventories.size();
		int delta = size / FilterOptions.PER_PAGE;

		if (delta * FilterOptions.PER_PAGE < size) {
			delta++;
		}

		return delta;
	}

	/**
	 * Apply filtering to a list of inventories
	 * 
	 * @param inventories
	 * @return
	 */
	public List<Inventory> filter(List<Inventory> inventories) {
		// we need a clone
		List<Inventory> clone = new ArrayList<Inventory>();
		clone.addAll(inventories);

		// apply the individual filters now
		Iterator<Inventory> iterator = clone.iterator();

                // first, manually remove the null inventories
                while (iterator.hasNext()) {
                    if (iterator.next().isNull())
                    {
                        iterator.remove();
                    }
                }

                //and once again
                iterator = clone.iterator();

		while (iterator.hasNext()) {
			boolean remove = false;
			Inventory inventory = iterator.next();

			if (FilterOptions.ITEMS.size() > 0) {
				List<Integer> items = FilterOptions.ITEMS;

				boolean found = false;

				for (Item item : inventory.getItems()) {
					if (items.contains(item.getId())) {
						found = true;
					}
				}

				if (!found) {
					remove = true;
				}
			}

			if (FilterOptions.TYPE != null) {
				if (FilterOptions.TYPE != inventory.getType()) {
					remove = true;
				}
			}

			if (FilterOptions.NAME != null) {
				if (!FilterOptions.NAME.equalsIgnoreCase(inventory.getName())) {
					remove = true;
				}
			}

                        if ((FilterOptions.FIRST_CORNER != null) && (FilterOptions.SECOND_CORNER != null)) {
                                remove = remove || !isInRange(inventory.getLocation().getX(),FilterOptions.FIRST_CORNER.getX(),FilterOptions.SECOND_CORNER.getX());
                                remove = remove || !isInRange(inventory.getLocation().getY(),FilterOptions.FIRST_CORNER.getY(),FilterOptions.SECOND_CORNER.getY());
                                remove = remove || !isInRange(inventory.getLocation().getZ(),FilterOptions.FIRST_CORNER.getZ(),FilterOptions.SECOND_CORNER.getZ());
                        }

			if (remove) {
				iterator.remove();
			}
		}

		return clone;
	}

        /**
	 * Checks, whether given value, lies within specified range (unordered)
	 *
	 * @param value given value
         * @param firstEnd one endpoint, not neccesarily the lower one
         * @param secondEnd second endpoint
	 */
        public boolean isInRange(int value, int firstEnd, int secondEnd) {
            if (firstEnd < secondEnd) {
                return ((value>=firstEnd) && (value<=secondEnd));
            } else {
                return ((value>=secondEnd) && (value<=firstEnd));
            }
        }

	/**
	 * Process a command
	 * 
	 * @param line
	 */
	public void processCommand(String line) {
		String command = line.toLowerCase();
		String args = "";

		if (line.contains(" ")) {
			int index = line.indexOf(" ");
			command = line.substring(0, index);
			args = line.substring(index + 1);
		}

		if (command.equals("help")) {
			printCommands();
		}

		else if (command.equals("world")) {
			world = new World(args);

			if (!world.isValid()) {
				log("Invalid world: \"" + args + "\"");
				world = null;
			} else {
				log("World revision: " + world.getChunkType());
			}
		}

		// if world is still null, tell them they need to set one first
		else if (world == null) {
			log("Please select a world first!");
		}

		else if (command.equals("size")) {
			int filtered = world.sizeof() - filter(world.getInventories()).size();

			log("Total\t" + world.sizeof());
			log("\t" + filtered + " are being filtered");
			log("\t" + (world.sizeof() - filtered) + " inventories will be shown");
			log("Pages\t" + getPageCount());
			log("");
			for (Type type : Type.values()) {
				log(type + "\t" + world.sizeof(type));
			}
		}

		else if (command.equals("load")) {
			boolean found = false;

			for (Type type : Type.values()) {
				if (args.equalsIgnoreCase(type.toString())) {
					log("Loading inventories of type " + type);

					long start = System.currentTimeMillis();
					boolean result = world.load(type);
					long end = System.currentTimeMillis();
					long time = end - start;

					if (result) {
						log("Success (" + time + " ms)");
					} else {
						log("Failure (" + time + " ms)");
					}

					found = true;

					break;
				}
			}

			if (!found) {
				log("Invalid argument: " + args);
			}
		}

		else if (command.equals("loadall")) {
			log("Loading all inventories");
			long start = System.currentTimeMillis();
			boolean result = world.loadAll();
			long end = System.currentTimeMillis();
			long time = end - start;

			if (result) {
				log("Success (" + time + " ms)");
			} else {
				log("Failure (" + time + " ms)");
			}
		}

		else if (command.equals("clear")) {
			boolean found = false;

			for (Type type : Type.values()) {
				if (args.equalsIgnoreCase(type.toString())) {
					world.clear(type);
					log("Purged " + type + " inventories from memory");
					found = true;

					break;
				}
			}

			if (!found) {
				log("Invalid argument: " + args);
			}
		}

		else if (command.equals("clearall")) {
			for (Type type : Type.values()) {
				world.clear(type);
			}

			log("Purged all inventories from memory");
		}

		else if (command.equals("show")) {
			displayPage();
		}

		else if (command.equals("next")) {
			FilterOptions.PAGE++;
			log("Switched to page: " + FilterOptions.PAGE);
			displayPage();
		}

		else if (command.equals("type")) {
			for (Type type : Type.values()) {
				if (args.equalsIgnoreCase(type.toString())) {
					FilterOptions.TYPE = type;
					log("Filtering inventories by " + type);
				}
			}
		}

		else if (command.equals("name")) {
			if (args.isEmpty()) {
				log("Invalid argument");
			} else {
				FilterOptions.NAME = args;
				log("Filtering inventories by " + args);
			}
		}
                
                else if (command.equals("inside")) {
                        if (args.isEmpty()) {
                                log("Invalid argument");
                        } else {
                            int index = args.indexOf(" ");
                            if (index==-1) {
                                log("You must specify two coordinates separated by space");
                            } else {
                               String first = args.substring(0, index);
                               String second = args.substring(index + 1);
                               boolean success = true;
                               Location firstLoc = null;
                               Location secondLoc = null;

                               try {
                                    String[] coords = null;
                                    coords = first.split("\\,");
                                    firstLoc = new Location(Integer.parseInt(coords[0]),
                                                            Integer.parseInt(coords[1]),
                                                            Integer.parseInt(coords[2]));
                                } catch (Exception e) {
                                    log("Error in the first coordinate");
                                    success = false;
                                }

                                try {
                                    String[] coords = null;
                                    coords = second.split("\\,");
                                    secondLoc = new Location(Integer.parseInt(coords[0]),
                                                            Integer.parseInt(coords[1]),
                                                            Integer.parseInt(coords[2]));
                                } catch (Exception e) {
                                    log("Error in the second coordinate");
                                    success = false;
                                }

                                if (success) {
                                    FilterOptions.FIRST_CORNER = firstLoc;
                                    FilterOptions.SECOND_CORNER = secondLoc;
                                }
                            }
                        }
                }

		else if (command.equals("page")) {
			try {
				FilterOptions.PAGE = Integer.parseInt(args);
				log("Switched to page " + args);
			} catch (Exception e) {
			}
		}

		else if (command.equals("perpage")) {
			try {
				FilterOptions.PER_PAGE = Integer.parseInt(args);
				log("Showing " + args + " inventories per page");
			} catch (Exception e) {
			}
		}

		else if (command.equals("items")) {
			for (String argument : args.split(" ")) {
				try {
					int id = Integer.parseInt(argument);

					// check for removal
					if (id < 0) {
						int realId = id * -1;

						if (FilterOptions.ITEMS.contains(realId)) {
							FilterOptions.ITEMS.remove((Integer) realId);
							log("Removed item " + realId + " from the Items filter");
						}
					} else if (!FilterOptions.ITEMS.contains(id)) {
						FilterOptions.ITEMS.add(id);
						log("Added item " + id + " to the Items filter");
					}
				} catch (Exception e) {
					log("Error parsing " + argument);
				}
			}
			log("Item filter is now: " + getFilteredItemString());
		}

		else if (command.equals("resetfilter")) {
			FilterOptions.ITEMS.clear();
			FilterOptions.NAME = null;
			FilterOptions.TYPE = null;
			FilterOptions.PAGE = 1;
			FilterOptions.PER_PAGE = 5;
			log("Reset filter options to the defaults");
		}

		else {
			log("Unknown command: " + command);
		}
	}

	public String getFilteredItemString() {
		String tmp = "[ ";

		for (int id : FilterOptions.ITEMS) {
			tmp += id + " ";
		}

		return tmp + "]";
	}

	/**
	 * Print commands to the console
	 */
	public void printCommands() {
		log("Setup");
		log("\tworld <name>\tSet the path to the world you want to use");
		log("\thelp\t\tDisplay this message");
		log("");

		log("Loading inventories");
		log("\tloadall\t\tLoad all inventories (player inventories and world inventories)");
		log("\tload <type>\tLoad all of the specified inventory type (WORLD or PLAYER)");
		log("");

		log("Post-processing");
		log("\tshow\t\tView the current page of inventories");
		log("\tsize\t\tView the number of loaded inventories by type");
		log("\tnext\t\tSwitch to the next page and show it");
		log("");

		log("Filter options");
		log("\titems <list>\tOnly show results that contain one of the space-seperated items");
		log("\t\t\tUsing a negative id (ex. -45 for id 45) will remove it from the filter");
		log("\t\t\tCurrently: " + getFilteredItemString());
		log("\ttype <type>\tOnly show results for a specified inventory type (WORLD or PLAYER)");
		log("\t\t\tCurrently: " + (FilterOptions.TYPE == null ? "ALL" : FilterOptions.TYPE));
		log("\tname <name>\tOnly show results for this name. It can be a Player name (ex. Notch)");
		log("\t\t\tor a block name (ex. Furnace)");
		log("\t\t\tCurrently: " + (FilterOptions.NAME == null ? "ALL" : FilterOptions.NAME));
                log("\tinside <x1,y1,z1> <x2,y2,z2>");
                log("\t\t\t Only show results inside a cuboid given by its two corners");
                log("\t\t\t Example: inside 10,0,10 20,128,20");
                log("\t\t\t Current corners:" + 
                     (((FilterOptions.FIRST_CORNER == null) || (FilterOptions.SECOND_CORNER == null)) ? " ALL WORLD" :
                     " First: "+FilterOptions.FIRST_CORNER.toString()+
                     " Second: "+FilterOptions.SECOND_CORNER.toString()));
		log("\tpage <num>\tCurrently: " + FilterOptions.PAGE);
		log("\tperpage <num>\tCurrently: " + FilterOptions.PER_PAGE);
		log("\tresetfilter\tReset the filter options");
		log("");

		log("Removal");
		log("\tclear <type>\tRemove the specified inventory from memory (WORLD or PLAYER)");
		log("\tclearall\tRemove all inventories from memory");
		log("");
	}

	/**
	 * Read a line from the console
	 * 
	 * @return
	 */
	public String readLine() {
		if (consoleReader == null) {
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
		}

		try {
			return consoleReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Log a message to the console or CommandSender if it is not NULL
	 * 
	 * @param str
	 */
	public void log(String str) {
		if (commandSender != null) {
			// also, replace tabs with 3 spaces if it's a player
			if (commandSender instanceof Player) {
				str = str.replaceAll("\\t", "   ");
			}

			// split the new lines
			for (String line : str.split("\\n")) {
				commandSender.sendMessage(line);
			}
		} else {
			System.out.println(str);
		}
	}

	/**
	 * Log a message to the console without a new line character
	 * 
	 * @param str
	 */
	public void logn(String str) {
		System.out.print(str);
	}


        /**
	 * Parses commandline arguments and feeds them to the interactive-mode parser
         *
         * @param args commandline arguments
	 */
        public void processArguments(String[] args) {
            StringBuilder buffer = new StringBuilder();

            for (int i = 0; i<args.length;i++) {
                if (args[i].equals(";"))
                {
                    log("Command : "+buffer.toString());
                    processCommand(buffer.toString());
                    buffer = new StringBuilder();
                } else {
                    if (buffer.length()>0) { buffer.append(" "); }
                    buffer.append(args[i]);
                }
            }

            if (buffer.length()>0)
            {
                log("Command : "+buffer.toString());
                processCommand(buffer.toString());
            }
        }

	public static void main(String[] args) {
		//check, whether we use interactive-mode or command-line mode
		if (args.length > 0) {
                    INVisible i = new INVisible(null);
                    i.processArguments(args);
                    i.displayAll();
                } else {
			new INVisible();	
		}
		
	}

}
