package model;

import exception.MyException;
import javafx.util.Pair;
import model.adt.*;
import model.stmt.IStmt;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIStack<MyIDictionary<String, IValue>> symTable;
    private MyIList<IValue> out;
    private IStmt originalProgram;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private IHeap<IValue> heap;
    private MyISemaphore semaphoreTable;
    private MyILatch latchTable;
    private MyILock lockTable;
    private MyIBarrier barrierTable;
    private MyIProc procTable;
    public static int id = 0;
    private final int personalId;

    public int getPersonalId() {
        return personalId;
    }

    public IHeap<IValue> getHeap() {
        return heap;
    }

    public void setHeap(IHeap<IValue> heap) {
        this.heap = heap;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public MyIStack<MyIDictionary<String, IValue>> getSymTable() {
        return symTable;
    }

    public void setSymTable(MyIStack<MyIDictionary<String, IValue>> symTable) {
        this.symTable = symTable;
    }

    public MyIList<IValue> getOut() {
        return out;
    }

    public void setOut(MyIList<IValue> out) {
        this.out = out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    public MyISemaphore getSemaphoreTable() {
        return semaphoreTable;
    }

    public void setSemaphoreTable(MyISemaphore semaphoreTable) {
        this.semaphoreTable = semaphoreTable;
    }

    public MyILatch getLatchTable() {
        return latchTable;
    }

    public void setLatchTable(MyILatch latchTable) {
        this.latchTable = latchTable;
    }

    public MyILock getLockTable() {
        return lockTable;
    }

    public void setLockTable(MyILock lockTable) {
        this.lockTable = lockTable;
    }

    public MyIBarrier getBarrierTable() {
        return barrierTable;
    }

    public void setBarrierTable(MyIBarrier barrierTable) {
        this.barrierTable = barrierTable;
    }

    public MyIProc getProcTable() {
        return procTable;
    }

    public void setProcTable(MyIProc procTable) {
        this.procTable = procTable;
    }

    public PrgState(MyIStack<IStmt> e, MyIStack<MyIDictionary<String, IValue>> s, MyIList<IValue> o, IStmt op,
                    MyIDictionary<StringValue, BufferedReader> ft, IHeap<IValue> h,
                    MyISemaphore sem, MyILatch l, MyILock lk, MyIBarrier b, MyIProc p) {
        exeStack = e;
        symTable = s;
        out = o;
        originalProgram = op;
        fileTable = ft;
        heap = h;
        semaphoreTable = sem;
        latchTable = l;
        lockTable = lk;
        barrierTable = b;
        procTable = p;
        e.push(op);
        this.personalId = getNextId();
    }

    public static synchronized int getNextId() {
        id = id + 1;
        return id;
    }

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new MyException("Program stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Id: " + personalId + "\nExeStack:\n" + exeStack.toString() + "Heap:\n" + heap.toString() + "SymTable:\n" + symTable.top().toString() +
                "Out:\n" + out.toString() + "FileTable:\n" + fileTable.toString() + "SemaphoreTable:\n" + semaphoreTable.toString() +
                "LatchTable:\n" + latchTable.toString() + "BarrierTable:\n" + barrierTable.toString() + "ProcTable:\n" + procTable.toString();
    }

}
