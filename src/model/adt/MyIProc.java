package model.adt;

import exception.ProcException;
import javafx.util.Pair;
import model.stmt.IStmt;

import java.util.List;
import java.util.Map;

public interface MyIProc {
    void put(String id, Pair<List<String>, IStmt> val) throws ProcException;
    void remove(String id) throws ProcException;
    boolean isDefined(String id);
    Pair<List<String>, IStmt> lookup(String id) throws ProcException;
    void update(String id, Pair<List<String>, IStmt> val) throws ProcException;
    Map<String, Pair<List<String>, IStmt>> getContent();
}
