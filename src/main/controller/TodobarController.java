package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.*;
import utility.JsonFileIO;
import utility.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static utility.JsonFileIO.jsonDataFile;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private File todoOptionPopUpFxmlFile = new File(todoOptionsPopUpFXML);
    private File todoActionPopUpFxmlFile = new File(todoActionsPopUpFXML);

    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;

    private Task task;
    private JFXPopup todoActionsPopup;
    private JFXPopup todoOptionsPopup;


    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTodoOptionsPopUp();
        loadTodoOptionsPopUpListener();
        loadActionsPopUP();
        loadTodoActionsPopUpListener();
    }

    // EFFECTS: edit task
    private void loadTodoOptionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoOptionPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new TodoOptionsController());
            todoOptionsPopup = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadActionsPopUP() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoActionPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new TodoActionsController());
            todoActionsPopup = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTodoOptionsPopUpListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                todoOptionsPopup.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15
                );
            }
        });
    }

    private void loadTodoActionsPopUpListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                todoActionsPopup.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15
                );
            }
        });
    }

    private void deleteTask(Task task) {
        try {
            Path p = Paths.get(jsonDataFile.getAbsolutePath());
            String s = new String(Files.readAllBytes(p));
            JSONArray jsonArray = new JSONArray(s);
            JSONArray newArray = new JSONArray();
            for (Object oj : jsonArray) {
                JSONObject j = (JSONObject) oj;
                while (!j.getString("description").equals(task.getDescription())) {
                    newArray.put(j);
                }
            }
            FileWriter fr = new FileWriter(jsonDataFile.getPath(), false);
            fr.write(newArray.toString(2));
            fr.flush();
            fr.close();
        } catch (IOException e) {
            //
        }

    }


    class TodoOptionsController {

        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TodobarOptionsController", "Edit Task");
                    PomoTodoApp.setScene(new EditTask(task));
                    break;
                case 1:
                    Logger.log("TodobarOptionsController", "Delete Task");
                    //Try
                    //deleteTask(task);
                    //JsonFileIO.deleteTask(task);
                    PomoTodoApp.getTasks().remove(task);
                    JsonFileIO.overWrite(PomoTodoApp.getTasks());
                    PomoTodoApp.setScene(new ListView(PomoTodoApp.getTasks()));
                    break;
                default:
                    Logger.log("TodobarOptionsController", "Not defined now");
            }
            todoOptionsPopup.hide();
        }
    }

    class TodoActionsController {

        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0: Logger.log("TodobarActionsController", "Set Priority to TODO");
                    break;
                case 1: Logger.log("TodobarActionsController", "Set Priority to UP NEXT");
                    break;
                case 2: Logger.log("TodobarActionsController", "Set Priority to In Progress");
                    break;
                case 3: Logger.log("TodobarActionsController", "Set Priority to DONE");
                    break;
                case 4: Logger.log("TodobarActionsController", "Promodo Selected");
                    break;
                default: Logger.log("TodobarActionsController", "Actions not defined now");
            }
            todoActionsPopup.hide();
        }
    }
}
