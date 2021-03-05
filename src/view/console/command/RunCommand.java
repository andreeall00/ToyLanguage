package view.console.command;

import controller.Controller;
import exception.MyException;

public class RunCommand extends Command {
    private Controller ctr;

    public RunCommand(String k, String d, Controller c){
        super(k, d);
        ctr = c;
    }

    @Override
    public void execute() {
        try {
            ctr.allStep();
        }
        catch (MyException e){
            System.out.println(e.getMessage());
        }
    }
}
