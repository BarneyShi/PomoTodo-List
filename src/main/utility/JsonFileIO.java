package utility;

import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import parsers.TaskParser;
import persistence.Jsonifier;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static persistence.Jsonifier.taskToJson;


// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");

    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        TaskParser tp = new TaskParser();
        List<Task> list = new ArrayList<>();
        try {
            Path p = Paths.get(jsonDataFile.getAbsolutePath());
            String content = new String(Files.readAllBytes(p));
            list = tp.parse(content);
        } catch (IOException e) {
            //
        }
        return list;
    }

    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {

        try {
            Path p = Paths.get(jsonDataFile.getAbsolutePath());
            String s = new String(Files.readAllBytes(p));
            JSONArray jsonArray = new JSONArray(s);
            for (Task t : tasks) {
                jsonArray.put(taskToJson(t));
            }
            //Path pp = Paths.get(jsonDataFile.getAbsolutePath());
            FileWriter fr = new FileWriter(jsonDataFile.getPath(), false);
            fr.write(jsonArray.toString(2));
            fr.flush();
            fr.close();
        } catch (IOException e) {
            //
        }
    }

    public static void overWrite(List<Task> list) {

        try {
//            Path p = Paths.get(jsonDataFile.getAbsolutePath());
//            String s = new String(Files.readAllBytes(p));
//            JSONArray jsonArray = new JSONArray(s);
            JSONArray newArray = new JSONArray();
            for (Task t : list) {
                JSONObject j = taskToJson(t);
                newArray.put(j);
            }
            FileWriter fr = new FileWriter(jsonDataFile.getPath(), false);
            fr.write(newArray.toString(2));
            fr.flush();
            fr.close();
        } catch (IOException e) {
            //
        }

    }


}
