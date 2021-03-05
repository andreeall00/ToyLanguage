package model.adt;

import exception.ProcException;
import javafx.util.Pair;
import model.stmt.IStmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProc implements MyIProc{
    private Map<String, Pair<List<String>, IStmt>> procedures;

    public MyProc() {
        procedures = new HashMap<>();
    }

    @Override
    public void put(String id, Pair<List<String>, IStmt> val) throws ProcException {
        if (isDefined(id))
            throw new ProcException("Key already exists");
        procedures.put(id, val);
    }

    @Override
    public void remove(String id) throws ProcException {
        if (!isDefined(id))
            throw new ProcException("Key doesn't exist");
        procedures.remove(id);
    }

    @Override
    public boolean isDefined(String id) {
        return procedures.containsKey(id);
    }

    @Override
    public Pair<List<String>, IStmt> lookup(String id) throws ProcException {
        if (!isDefined(id))
            throw new ProcException("Key doesn't exist");
        return procedures.get(id);
    }

    @Override
    public void update(String id, Pair<List<String>, IStmt> val) throws ProcException {
        if (!isDefined(id))
            throw new ProcException("Key doesn't exist");
        procedures.replace(id, val);
    }

    @Override
    public Map<String, Pair<List<String>, IStmt>> getContent() {
        return procedures;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String key: procedures.keySet()) {
            s.append("procedure ").append(key).append("(").append(procedures.get(key).getKey()).append(") ").append(procedures.get(key).getValue()).append("\n");
        }
        return s.toString();
    }
}
