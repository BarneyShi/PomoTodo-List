package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagJson = new JSONObject();
        tagJson.put("name", tag.getName());
        return tagJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject priJson = new JSONObject();
        priJson.put("important", priority.isImportant());
        priJson.put("urgent", priority.isUrgent());
        return priJson;
    }

    // EFFECTS: returns JSON respresentation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        if (dueDate != null) {
            JSONObject duedateJson = new JSONObject();
            Date d = dueDate.getDate();
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            duedateJson.put("year", c.get(Calendar.YEAR));
            duedateJson.put("month", c.get(Calendar.MONTH));
            duedateJson.put("day", c.get(Calendar.DAY_OF_MONTH));
            duedateJson.put("hour", c.get(Calendar.HOUR_OF_DAY));
            duedateJson.put("minute", c.get(Calendar.MINUTE));
            return duedateJson;
        } else {
            return null;
        }
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskJson = new JSONObject();
        taskJson.put("description", task.getDescription());
        //Tag
        //List<JSONObject> tagJsonList = new ArrayList<>();
        JSONArray tagJsonList = new JSONArray();
        for (Tag t : task.getTags()) {
            tagJsonList.put(tagToJson(t));
        }
        taskJson.put("tags", tagJsonList);
        //due-date
        if (task.getDueDate() != null) {
            taskJson.put("due-date", dueDateToJson(task.getDueDate()));
        } else {
            taskJson.put("due-date", JSONObject.NULL);
        }

        //priority
        taskJson.put("priority", priorityToJson(task.getPriority()));
        //status
        taskJson.put("status", task.getStatus().toString().replace(" ", "_"));
        return taskJson;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray taskListArray = new JSONArray();
        for (Task t : tasks) {
            taskListArray.put(taskToJson(t));
        }
        return taskListArray;
    }
}

