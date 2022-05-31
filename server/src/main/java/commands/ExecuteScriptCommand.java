package commands;



/**
 * class for command 'execute_script' to execute script
 */
public class ExecuteScriptCommand extends AbstractCommand{

    public ExecuteScriptCommand() {
        super("execute_script file_name",
                "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит");
    }


    @Override
    public String execute() {

        return ("Executing script " + getArgs().getArguments());

    }
}
