package controller;

import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyDictionary;
import model.adt.MyIDictionary;
import model.adt.MyList;
import model.observer.Observable;
import model.value.IValue;
import model.value.RefValue;
import repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller extends Observable {
    private IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository r) {
        repo = r;
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgList) {
        prgList.forEach(prg -> repo.logPrgStateExec(prg));
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());
        List<PrgState> newPrgList = null;
        try {
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new MyException(e.getMessage());
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new MyException(e.getMessage());
        }
        prgList.addAll(newPrgList);
        prgList.forEach(prg -> repo.logPrgStateExec(prg));
        repo.setPrgList(prgList);
    }

    public void allStep() throws MyException {
        (repo.getPrgList().getContent()).forEach(p -> p.getOriginalProgram().typeCheck(new MyDictionary<>()));
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList().getContent());
        while (prgList.size() > 0) {
            List<Integer> symTableAddr = new ArrayList<>();
            prgList.forEach(prg -> {
                List<IValue> vals = new ArrayList<>();
                Iterator<MyIDictionary<String, IValue>> it = prg.getSymTable().stream().iterator();
                while (it.hasNext()) {
                    vals.addAll(it.next().getContent().values());
                }
                getAddrFromSymTable(vals, prg.getHeap().getContent().values())
                    .forEach(addr -> {
                        if (!symTableAddr.contains(addr)) symTableAddr.add(addr);
                    });});
            prgList.forEach(prg -> prg.getHeap().setContent(garbageCollector(symTableAddr, prg.getHeap())));
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList().getContent());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void add(PrgState p) {
        repo.add(p);
    }

    public Map<Integer, IValue> garbageCollector(List<Integer> symTableAddr, IHeap<IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues, Collection<IValue> heapT) {
        return Stream.concat(
                heapT.stream()
                        .filter(v -> v instanceof RefValue)
                        .map(v -> {
                            RefValue v1 = (RefValue) v;
                            return v1.getAddress();
                        }),
                symTableValues.stream()
                        .filter(v -> v instanceof RefValue)
                        .map(v -> {
                            RefValue v1 = (RefValue) v;
                            return v1.getAddress();
                        })
        )
                .collect(Collectors.toList());
    }

    public MyList<PrgState> getProgramList() {
        return repo.getPrgList();
    }

    public void setPrgList(List<PrgState> prgList) {
        repo.setPrgList(prgList);
    }

    public Optional<PrgState> getProgram(Integer id) {
        return this.repo.getPrgList().stream()
                .filter(p -> Objects.equals(p.getPersonalId(), id))
                .findFirst();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}

