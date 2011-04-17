#INVisible

Instructions for command-line use
---------------------------------

The comand-line functionality is very simple and straightforward. You just simply add commands, you would have entered in interactive mode, as a command-line arguments, separating them with commas. You don't need to bother about outputting the filter result, because after the last argument, the output of all filtered inventories (no paging ofcourse) will happen automatically.

Examples
--------

INVisible.jar world my_world ; load player 

This will output inventories of all players

INVisible.jar world my_world ; load player ; name airguru

This will output the inventory of player airguru

INVisible.jar world my_world ; load world 

This will output contents of all chests

INVisible.jar world my_world ; load player ; items 1 2 3

This will output inventories of all players, who have items 1 or 2 or 3 in their inventories.

Remember, it's just like interactive mode, so your first command should always be "world" :)
