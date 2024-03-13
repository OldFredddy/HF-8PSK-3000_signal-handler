package com.signals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    void initialize() throws IOException, URISyntaxException {
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
        TxtOpen txt = new TxtOpen("/home/root1/develop/Java_less/HF-8PSK-3000_signal-handler/1.1/1_результат демодуляции структура_симв.txt");
        codeArr = txt.ReadTxt();
        filePath = "/home/root1/develop/Java_less/HF-8PSK-3000_signal-handler/1.1/1_результат демодуляции структура_симв.txt";
        List<String> decodedData = SignalProcessing.decode8PSK_3000(codeArr, filePath.endsWith(".txt") ? "txt" : "raw");
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        String newFileName = fileName.replaceFirst("(\\.txt)?$", "_decoded.txt"); // Добавляем '_decoded' перед расширением
        Path outputPath = path.getParent().resolve(newFileName);

// Объединяем элементы списка в одну строку без добавления новой строки между элементами
        String contentToWrite = String.join("", decodedData);

// Записываем результат в файл
        Files.write(outputPath, contentToWrite.getBytes(StandardCharsets.UTF_8));

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
                            public Void call() throws URISyntaxException {
                                TxtOpen txt = new TxtOpen(filePath);
                                try {
                                    List<Character> codeArr;
                                    if (filePath.endsWith(".txt")) {
                                        codeArr = txt.ReadTxt();
                                    } else {
                                        codeArr = txt.bytesToBits(txt.ReadAll());
                                    }
                                    List<String> decodedData = SignalProcessing.decode8PSK_3000(codeArr, filePath.endsWith(".txt") ? "txt" : "raw");
                                    Path path = Paths.get(filePath);
                                    String fileName = path.getFileName().toString();
                                    String newFileName = fileName.replaceFirst("(\\.txt)?$", "_decoded.txt"); // Добавляем '_decoded' перед расширением
                                    Path outputPath = path.getParent().resolve(newFileName);
                                    Files.write(outputPath, decodedData);
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


