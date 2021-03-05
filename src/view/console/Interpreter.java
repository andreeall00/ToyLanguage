package view.console;

import controller.Controller;
import javafx.util.Pair;
import model.PrgState;
import model.adt.*;
import model.exp.*;
import model.stmt.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.RefType;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;
import repository.IRepository;
import repository.Repository;
import view.console.command.ExitCommand;
import view.console.command.RunCommand;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Interpreter {
    public void run() {
        IStmt ex1 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                        new SemaphoreStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                                new AcquireStmt("i"), new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntValue(2))),
                new ReleaseStmt("i")))), new CompStmt(new AcquireStmt("i"), new CompStmt(new PrintStmt(new ReadHeapExp(
                        new VarExp("a"))), new ReleaseStmt("i"))))))));
        PrgState.id = 0;
        MyIStack<MyIDictionary<String, IValue>> symTable1 = new MyStack<>();
        symTable1.push(new MyDictionary<>());
        PrgState prg1 = new PrgState(new MyStack<>(), symTable1, new MyList<>(), ex1,
                new MyDictionary<>(), new Heap<>(), new MySemaphore(), new MyLatch(),
                new MyLock(), new MyBarrier(), new MyProc());
        IRepository repo1 = new Repository(prg1, "log1.txt");
        Controller ctr1 = new Controller(repo1);

        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                new LatchStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                        new WriteHeapStmt("a", new ValueExp(new IntValue(2))), new CountDownStmt("i"))),
                new CompStmt(new AwaitLStmt("i"), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        PrgState.id = 0;
        MyIStack<MyIDictionary<String, IValue>> symTable2 = new MyStack<>();
        symTable2.push(new MyDictionary<>());
        PrgState prg2 = new PrgState(new MyStack<>(), symTable2, new MyList<>(), ex2,
                new MyDictionary<>(), new Heap<>(), new MySemaphore(), new MyLatch(),
                new MyLock(), new MyBarrier(), new MyProc());
        IRepository repo2 = new Repository(prg2, "log2.txt");
        Controller ctr2 = new Controller(repo2);

        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                new LockStmt("i"), new CompStmt(new ForkStmt(new CompStmt(new LockLockStmt("i"), new CompStmt(
                        new WriteHeapStmt("a", new ValueExp(new IntValue(2))), new UnlockLockStmt("i")))),
                new CompStmt(new LockLockStmt("i"), new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("a"))),
                        new UnlockLockStmt("i"))))))));
        PrgState.id = 0;
        MyIStack<MyIDictionary<String, IValue>> symTable3 = new MyStack<>();
        symTable3.push(new MyDictionary<>());
        PrgState prg3 = new PrgState(new MyStack<>(), symTable3, new MyList<>(), ex3,
                new MyDictionary<>(), new Heap<>(), new MySemaphore(), new MyLatch(),
                new MyLock(), new MyBarrier(), new MyProc());
        IRepository repo3 = new Repository(prg3, "log3.txt");
        Controller ctr3 = new Controller(repo3);

        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(2))), new CompStmt(
                new BarrierStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                        new AwaitBStmt("i"), new WriteHeapStmt("a", new ValueExp(new IntValue(2))))), new CompStmt(
                                new AwaitBStmt("i"), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        PrgState.id = 0;
        MyIStack<MyIDictionary<String, IValue>> symTable4 = new MyStack<>();
        symTable4.push(new MyDictionary<>());
        PrgState prg4 = new PrgState(new MyStack<>(), symTable4, new MyList<>(), ex4,
                new MyDictionary<>(), new Heap<>(), new MySemaphore(), new MyLatch(),
                new MyLock(), new MyBarrier(), new MyProc());
        IRepository repo4 = new Repository(prg4, "log4.txt");
        Controller ctr4 = new Controller(repo4);

//        MyIProc procTable = new MyProc();
        IStmt procSum = new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(new AssignStmt("x",
                new ArithExp("+", new VarExp("a"), new VarExp("b"))), new PrintStmt(new VarExp("x"))));
//        procTable.put("sum", new Pair<>(new ArrayList<String>(Arrays.asList("a", "b")), procSum));
        IStmt ex5 = new CompStmt( new ProcStmt("sum", Arrays.asList("a","b"), procSum), new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()), new CompStmt(new AssignStmt("a", new ValueExp(
                        new IntValue(10))), new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))),
                        new CompStmt(new ForkStmt(new CallStmt("sum", new MyList<IExp>(Arrays.asList(new VarExp("a"),
                                new VarExp("b"))))), new CompStmt(new PrintStmt(new VarExp("x")), new CompStmt(
                                new PrintStmt(new VarExp("a")), new PrintStmt(new VarExp("b")))))))))));
        PrgState.id = 0;
        MyIStack<MyIDictionary<String, IValue>> symTable5 = new MyStack<>();
        symTable5.push(new MyDictionary<>());
        PrgState prg5 = new PrgState(new MyStack<>(), symTable5, new MyList<>(), ex5,
                new MyDictionary<>(), new Heap<>(), new MySemaphore(), new MyLatch(),
                new MyLock(), new MyBarrier(), new MyProc());
        IRepository repo5 = new Repository(prg5, "log5.txt");
        Controller ctr5 = new Controller(repo5);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunCommand("1", ex1.toString(), ctr1));
        menu.addCommand(new RunCommand("2", ex2.toString(), ctr2));
        menu.addCommand(new RunCommand("3", ex3.toString(), ctr3));
        menu.addCommand(new RunCommand("4", ex4.toString(), ctr4));
        menu.addCommand(new RunCommand("5", procSum.toString() + ex5.toString(), ctr5));
        menu.show();
    }
}
