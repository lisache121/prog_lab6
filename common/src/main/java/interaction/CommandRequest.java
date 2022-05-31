package interaction;

import data.Dragon;
import data.DragonHead;

import java.io.Serializable;

public class CommandRequest implements Request{
    private String commandName;
    private String commandStringArgument;
    private Dragon commandObjectArgument;

    public CommandRequest(String commandNm, String commandSA, Dragon commandOA) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        commandObjectArgument = commandOA;
    }
    @Override
    public String getArguments() {
        return commandStringArgument;
    }

    @Override
    public Dragon getDragon() {
        return (Dragon) commandObjectArgument;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    public void setCommandObjectArgument(Dragon commandObjectArgument) {
        this.commandObjectArgument = commandObjectArgument;
    }
}
