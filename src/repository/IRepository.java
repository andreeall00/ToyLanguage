package repository;

import exception.MyException;
import model.PrgState;
import model.adt.MyList;

import java.util.List;

public interface IRepository {
    void add(PrgState ps);
    void logPrgStateExec(PrgState ps) throws MyException;
    MyList<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgStates);
}
