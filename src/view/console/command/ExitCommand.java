package view.console.command;

public class ExitCommand extends Command {
    public ExitCommand(String k, String d) {
        super(k, d);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
