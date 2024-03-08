package com.signals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.IIOException;

import static javafx.scene.input.KeyCode.*;

public class Controller {
    public List<Character> codeArr = new ArrayList<>();
    public List<String> decodeArr = new ArrayList<>();

    FileChooser fileChooser = new FileChooser();
    public String startPath;
    public int porogTemp;
    @FXML
    private TextArea IsxTA;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuOpenDec;

    @FXML
    private Menu menuOpenFile;

    @FXML
    private MenuItem menuSaveResult;

    @FXML
    private AnchorPane myPane;

    @FXML
    private ScrollPane parentPane;

    @FXML
    private SplitPane superParentPane;

    @FXML
    private VBox vbMenu;
    @FXML
    private MenuBar mainMenu;
    public TxtOpen txt;

    private String DecodeMethod;
    private String UpDownInterleaver;
    private String UpDownDeInterleaver;
    private String LeftRightInterleaver;
    private String LeftRightDeInterleaver;
    private int textAreaColCount=64;
    String filePath = null;
    @FXML
    void initialize() throws IIOException {
        mainMenu.setVisible(false);
        IsxTA.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != IsxTA
                        && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        IsxTA.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    for (File file : db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        Task<Void> task = new Task<Void>() {
                            @Override
                            public Void call() {
                                TxtOpen txt = new TxtOpen(filePath);
                                try {
                                    if (filePath.endsWith(".txt")){
                                        codeArr = txt.ReadTxt();
                                        SignalProcessing.decode8PSK_3000(codeArr,"txt");
                                    } else {
                                        codeArr = txt.bytesToBits(txt.ReadAll());
                                        SignalProcessing.decode8PSK_3000(codeArr, "raw");
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < codeArr.size() + 1; i++) {
                                    sb.append(Character.toString(codeArr.get(i - 1)));
                                    if (i % textAreaColCount == 0) {
                                        sb.append("\n");
                                    }
                                }
                                Platform.runLater(() -> {
                                    IsxTA.clear();
                                    IsxTA.appendText(sb.toString());
                                });

                                return null;
                            }
                        }; // Запускаем задачу в новом потоке
                        new Thread(task).start();
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

}


