package repository;

import exception.MyException;
import model.PrgState;
import model.adt.MyList;
import model.value.BoolValue;

import java.io.*;
import java.util.List;

public class Repository implements IRepository {
    private MyList<PrgState> myPrgStates;
    private String logFilePath;
    private BoolValue first = new BoolValue(true);

    public Repository() {
        myPrgStates = new MyList<>();
    }

    public Repository(String filePath) {
        logFilePath = filePath;
        myPrgStates = new MyList<>();
    }

    public Repository(PrgState ps, String filePath) {
        logFilePath = filePath;
        myPrgStates = new MyList<>();
        myPrgStates.append(ps);
    }

    @Override
    public void add(PrgState ps) {
        myPrgStates.append(ps);
    }

    @Override
    public void logPrgStateExec(PrgState ps) throws MyException {
        File file = new File(logFilePath);
        try {
            boolean created = file.createNewFile();
            if (!created && first.getVal()) {
                PrintWriter logFile = new PrintWriter(file);
                logFile.close();
            }
            first = new BoolValue(false);
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            logFile.println(ps.toString());
            logFile.close();
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public MyList<PrgState> getPrgList() {
        return myPrgStates;
    }

    @Override
    public void setPrgList(List<PrgState> prgStates) {
        myPrgStates = new MyList<>();
        prgStates.forEach(prg -> myPrgStates.append(prg));
    }
}
