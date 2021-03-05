package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectController {
    @FXML
    private ListView<String> programs;
    private Map<String, Pair<IStmt, MyIProc>> descriptions;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        this.setPrograms();
        ObservableList<String> programs = FXCollections.observableArrayList();
        AtomicInteger currentIndex = new AtomicInteger(1);
        this.descriptions.forEach((key, value) -> programs.add((currentIndex.getAndIncrement()) + ". " + key));
        this.programs.setItems(programs);
        this.programs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setPrograms() {
        descriptions = new HashMap<>();

        IStmt ex1 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                new SemaphoreStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                new AcquireStmt("i"), new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntValue(2))),
                new ReleaseStmt("i")))), new CompStmt(new AcquireStmt("i"), new CompStmt(new PrintStmt(new ReadHeapExp(
                new VarExp("a"))), new ReleaseStmt("i"))))))));
        descriptions.put(ex1.toString(), new Pair<>(ex1, new MyProc()));


        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                new LatchStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                new WriteHeapStmt("a", new ValueExp(new IntValue(2))), new CountDownStmt("i"))),
                new CompStmt(new AwaitLStmt("i"), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        descriptions.put(ex2.toString(), new Pair<>(ex2, new MyProc()));

        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(1))), new CompStmt(
                new LockStmt("i"), new CompStmt(new ForkStmt(new CompStmt(new LockLockStmt("i"), new CompStmt(
                new WriteHeapStmt("a", new ValueExp(new IntValue(2))), new UnlockLockStmt("i")))),
                new CompStmt(new LockLockStmt("i"), new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("a"))),
                        new UnlockLockStmt("i"))))))));
        descriptions.put(ex3.toString(), new Pair<>(ex3, new MyProc()));

        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(new VarDeclStmt("i",
                new IntType()), new CompStmt(new NewStmt("a", new ValueExp(new IntValue(2))), new CompStmt(
                new BarrierStmt("i", new ReadHeapExp(new VarExp("a"))), new CompStmt(new ForkStmt(new CompStmt(
                new AwaitBStmt("i"), new WriteHeapStmt("a", new ValueExp(new IntValue(2))))), new CompStmt(
                new AwaitBStmt("i"), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        descriptions.put(ex4.toString(), new Pair<>(ex4, new MyProc()));

//        MyIProc procTable = new MyProc();
        IStmt procSum = new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(new AssignStmt("x",
                new ArithExp("+", new VarExp("a"), new VarExp("b"))), new PrintStmt(new VarExp("x"))));
//        procTable.put("sum", new Pair<>(new ArrayList<String>(Arrays.asList("a", "b")), procSum));
        IStmt ex5 = new CompStmt(new ProcStmt("sum", Arrays.asList("a", "b"), procSum), new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()), new CompStmt(new AssignStmt("a", new ValueExp(
                        new IntValue(10))), new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))),
                        new CompStmt(new ForkStmt(new CallStmt("sum", new MyList<IExp>(Arrays.asList(new VarExp("a"),
                                new VarExp("b"))))), new CompStmt(new PrintStmt(new VarExp("x")), new CompStmt(
                                new PrintStmt(new VarExp("a")), new PrintStmt(new VarExp("b")))))))))));
        descriptions.put(ex5.toString(), new Pair<>(ex5, new MyProc()));

        IStmt ex6 = new CompStmt(new VarDeclStmt("v1", new IntType()), new CompStmt(new VarDeclStmt("v2", new IntType()),
                new CompStmt(new AssignStmt("v1", new ValueExp(new IntValue(2))), new CompStmt(new AssignStmt("v2",
                        new ValueExp(new IntValue(3))), new IfStmt(new RelationalExp("!=", new VarExp("v1"),
                        new ValueExp(new IntValue(0))), new PrintStmt(new MulExp(new VarExp("v1"), new VarExp("v2"))),
                        new PrintStmt(new VarExp("v1")))))));
        descriptions.put(ex6.toString(), new Pair<>(ex6, new MyProc()));
    }

    public void handleSelect(MouseEvent event) {
        try {
            PrgState.id = 0;
            String key = programs.getSelectionModel().getSelectedItem();
            MyIStack<MyIDictionary<String, IValue>> symTable = new MyStack<>();
            symTable.push(new MyDictionary<>());
            PrgState prg = new PrgState(new MyStack<>(), symTable, new MyList<>(),
                    descriptions.get(key.substring(key.indexOf(' ') + 1)).getKey(), new MyDictionary<>(),
                    new Heap<>(), new MySemaphore(), new MyLatch(), new MyLock(), new MyBarrier(),
                    descriptions.get(key.substring(key.indexOf(' ') + 1)).getValue());
            prg.getOriginalProgram().typeCheck(new MyDictionary<>());
            IRepository repo = new Repository(prg, "src/log/log" + (programs.getSelectionModel().getSelectedIndices().get(0) + 1) + ".txt");
            Controller ctr = new Controller(repo);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/gui/fxml/programRun.fxml"));
            Parent root = loader.load();
            RunController ctrRun = loader.getController();
            ctrRun.setStage(this.stage);
            ctrRun.setScene(this.stage.getScene());
            ctrRun.setController(ctr);
            ctr.addObserver(ctrRun);
            ctr.notifyObservers();
            stage.setScene(new Scene(root, 1300, 700));
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
