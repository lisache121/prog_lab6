package interaction;

import data.Dragon;

import java.io.Serializable;

public interface Request extends Serializable {
    public String getArguments();
    public Dragon getDragon();
    public String getCommandName();
}
