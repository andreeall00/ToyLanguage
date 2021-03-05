package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.PrgState;
import model.adt.*;
import model.observer.Observer;
import model.stmt.IStmt;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class RunController extends Observer {
    @FXML
    private Label nrPrgStates;
    @FXML
    private ListView<Integer> prgStatesElement;
    @FXML
    private ListView<IStmt> execStackElement;
    @FXML
    private TableView<Pair<String, IValue>> symbolTableElement;
    @FXML
    TableColumn<Pair<Integer, IValue>, Integer> symbolsTableNameColumn;
    @FXML
    TableColumn<Pair<Integer, IValue>, Integer> symbolsTableValueColumn;
    @FXML
    private ListView<String> outElement;
    @FXML
    private TableView<Pair<Integer, IValue>> heapTableElement;
    @FXML
    TableColumn<Pair<Integer, IValue>, Integer> heapTableAddressColumn;
    @FXML
    TableColumn<Pair<Integer, IValue>, Integer> heapTableValueColumn;
    @FXML
    private ListView<StringValue> fileTableElement;
    @FXML
    private TableView<MyITriplet<IntValue, IntValue, List<Integer>>> semaphoreTableElement;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, IntValue> semaphoreTableIndexColumn;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, IntValue> semaphoreTableValueColumn;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, List<Integer>> semaphoreTableValueListColumn;
    @FXML
    private TableView<Pair<IntValue, IntValue>> latchTableElement;
    @FXML
    TableColumn<Pair<IntValue, IntValue>, IntValue> latchTableLocationColumn;
    @FXML
    TableColumn<Pair<IntValue, IntValue>, IntValue> latchTableValueColumn;
    @FXML
    private TableView<Pair<IntValue, Integer>> lockTableElement;
    @FXML
    TableColumn<Pair<IntValue, Integer>, IntValue> lockTableLocationColumn;
    @FXML
    TableColumn<Pair<IntValue, Integer>, Integer> lockTableValueColumn;
    @FXML
    private TableView<MyITriplet<IntValue, IntValue, List<Integer>>> barrierTableElement;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, IntValue> barrierTableIndexColumn;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, IntValue> barrierTableValueColumn;
    @FXML
    TableColumn<MyITriplet<IntValue, IntValue, List<Integer>>, List<Integer>> barrierTableValueListColumn;
    @FXML
    private TableView<Pair<String, String>> procTableElement;
    @FXML
    TableColumn<Pair<String, String>, String> procTableSignatureColumn;
    @FXML
    TableColumn<Pair<String, String>, String> procTableBodyColumn;

    private Stage stage;
    private Scene scene;
    private Controller controller;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.controller.setExecutor(Executors.newFixedThreadPool(2));
    }

    @FXML
    void initialize() {
        prgStatesElement.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.heapTableAddressColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.heapTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.symbolsTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.symbolsTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.semaphoreTableIndexColumn.setCellValueFactory(new PropertyValueFactory<>("value1"));
        this.semaphoreTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value2"));
        this.semaphoreTableValueListColumn.setCellValueFactory(new PropertyValueFactory<>("value3"));
        this.latchTableLocationColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.latchTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.lockTableLocationColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.lockTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.barrierTableIndexColumn.setCellValueFactory(new PropertyValueFactory<>("value1"));
        this.barrierTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value2"));
        this.barrierTableValueListColumn.setCellValueFactory(new PropertyValueFactory<>("value3"));
        this.procTableSignatureColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.procTableBodyColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    public void handleBack(MouseEvent event) {
        this.closeProgram();
        this.stage.setScene(this.scene);
    }

    @FXML
    public void handleRunOneStep(MouseEvent event) {
        try {
            List<PrgState> prgList = controller.removeCompletedPrg(controller.getProgramList().getContent());

            if (prgList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The program is done");
                alert.showAndWait();
                return;
            }

            List<Integer> symTableAddr = new ArrayList<>();
            prgList.forEach(prg -> {
                List<IValue> vals = new ArrayList<>();
                Iterator<MyIDictionary<String, IValue>> it = prg.getSymTable().stream().iterator();
                while (it.hasNext()) {
                    vals.addAll(it.next().getContent().values());
                }
                controller.getAddrFromSymTable(vals, prg.getHeap().getContent().values())
                        .forEach(addr -> {
                            if (!symTableAddr.contains(addr)) symTableAddr.add(addr);
                        });});
            prgList.forEach(prg -> prg.getHeap().setContent(controller.garbageCollector(symTableAddr, prg.getHeap())));
            controller.oneStepForAllPrg(prgList);
            controller.removeCompletedPrg(controller.getProgramList().getContent());

            controller.notifyObservers();
        } catch (Exception e) {
            this.closeProgram();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleSelectState() {
        this.update();
    }

    @Override
    public void update() {
        try {
            this.setNrPrgStates();
            this.setPrgStatesElement();
            this.setExecStackElement();
            this.setSymbolTableElement();
            this.setOutElement();
            this.setHeapTableElement();
            this.setFileTableElement();
            this.setSemaphoreTableElement();
            this.setLatchTableElement();
            this.setLockTableElement();
            this.setBarrierTableElement();
            this.setProcTableElement();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setProcTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        MyIProc procTable = program.getProcTable();
        ArrayList<Pair<String, String>> entries = new ArrayList<>();
        for (Map.Entry<String, Pair<List<String>, IStmt>> entry : procTable.getContent().entrySet()) {
            entries.add(new Pair<>(entry.getKey() + entry.getValue().getKey(), entry.getValue().getValue().toString()));
        }
        this.procTableElement.setItems(FXCollections.observableArrayList(entries));
    }

    private void setBarrierTableElement() throws Exception {
        List<MyITriplet<IntValue, IntValue, List<Integer>>> triplets = new ArrayList<>();
        PrgState program = this.getSelectedProgram();
        MyIBarrier barrierTable = program.getBarrierTable();
        for (IntValue key : barrierTable.getContent().keySet())
            triplets.add(new MyTriplet<>(key, barrierTable.lookup(key).getKey(), FXCollections.observableArrayList(barrierTable.lookup(key).getValue())));
        this.barrierTableElement.setItems(FXCollections.observableArrayList(triplets));
    }

    private void setLockTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        MyILock lockTable = program.getLockTable();
        ArrayList<Pair<IntValue, Integer>> entries = new ArrayList<>();
        for (Map.Entry<IntValue, Integer> entry : lockTable.getContent().entrySet()) {
            entries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.lockTableElement.setItems(FXCollections.observableArrayList(entries));
    }

    private void setLatchTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        MyILatch latchTable = program.getLatchTable();
        ArrayList<Pair<IntValue, IntValue>> entries = new ArrayList<>();
        for (Map.Entry<IntValue, IntValue> entry : latchTable.getContent().entrySet()) {
            entries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.latchTableElement.setItems(FXCollections.observableArrayList(entries));
    }

    private void setSemaphoreTableElement() throws Exception {
        List<MyITriplet<IntValue, IntValue, List<Integer>>> triplets = new ArrayList<>();
        PrgState program = this.getSelectedProgram();
        MyISemaphore semTable = program.getSemaphoreTable();
        for (IntValue key : semTable.getContent().keySet())
            triplets.add(new MyTriplet<>(key, semTable.lookup(key).getKey(), FXCollections.observableArrayList(semTable.lookup(key).getValue())));
        this.semaphoreTableElement.setItems(FXCollections.observableArrayList(triplets));
    }

    private void setNrPrgStates() {
        this.nrPrgStates.setText("Number of Program States " + this.controller.getProgramList().size());
    }

    private void setPrgStatesElement() {
        ObservableList<Integer> states = FXCollections.observableArrayList();
        states.addAll(
                this.controller
                        .getProgramList()
                        .stream()
                        .map(PrgState::getPersonalId)
                        .collect(Collectors.toList())
        );
        prgStatesElement.setItems(states);
    }

    private void setExecStackElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        ObservableList<IStmt> programs = FXCollections.observableArrayList();
        programs.addAll(
                program.getExeStack()
                        .stream()
                        .collect(Collectors.toList())
        );
        FXCollections.reverse(programs);
        this.execStackElement.setItems(programs);
    }

    private void setSymbolTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        MyIDictionary<String, IValue> symTable = program.getSymTable().top();
        ArrayList<Pair<String, IValue>> entries = new ArrayList<>();
        for (Map.Entry<String, IValue> entry : symTable.getContent().entrySet()) {
            entries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.symbolTableElement.setItems(FXCollections.observableArrayList(entries));
    }

    private void setOutElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        ObservableList<String> out = FXCollections.observableArrayList();
        out.addAll(
                program.getOut().stream()
                        .map(IValue::toString)
                        .collect(Collectors.toList())
        );
        this.outElement.setItems(out);
    }

    private void setHeapTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        IHeap<IValue> heap = program.getHeap();
        ArrayList<Pair<Integer, IValue>> entries = new ArrayList<>();
        for (Map.Entry<Integer, IValue> entry : heap.getContent().entrySet()) {
            entries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.heapTableElement.setItems(FXCollections.observableArrayList(entries));
    }

    private void setFileTableElement() throws Exception {
        PrgState program = this.getSelectedProgram();
        MyIDictionary<StringValue, BufferedReader> fileTable = program.getFileTable();
        List<StringValue> list = new ArrayList<>(fileTable.getContent().keySet());
        this.fileTableElement.setItems(FXCollections.observableArrayList(list));
    }

    PrgState getSelectedProgram() throws Exception {
        Integer id = this.prgStatesElement.getSelectionModel().getSelectedItem();

        if (!Objects.nonNull(id)) {
            this.prgStatesElement.getSelectionModel().selectIndices(0);
            id = this.prgStatesElement.getItems().get(0);

        }
        Optional<PrgState> program = this.controller.getProgram(id);
        if (program.isEmpty())
            throw new Exception("Program is done");
        return program.get();
    }

    private void closeProgram() {
        controller.getExecutor().shutdownNow();
        controller.setPrgList(new ArrayList<>());
        prgStatesElement.getItems().clear();
        setNrPrgStates();
    }
}
