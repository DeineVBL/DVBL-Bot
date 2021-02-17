/*
 * Copyright notice
 * Copyright (c) swausb || Nils Körting-Eberhardt 2021
 * Created: 06.01.2021 @ 21:53:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by swausb and Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * CommandManager.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.manager;


import real.entwickler.dvblbot.utils.ICommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private List<ICommand> registeredCommands;

    public CommandManager() {
        this.registeredCommands = new ArrayList<>();
    }

    public void registerCommand (ICommand iCommand) {
        this.registeredCommands.add(iCommand);
    }

    public List<ICommand> getRegisteredCommands() {
        return registeredCommands;
    }
}
