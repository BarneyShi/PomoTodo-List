package parsers;

import model.DueDate;
import model.Priority;
import model.Status;
import model.Task;
import model.exceptions.EmptyStringException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Represents Task parser
public class TaskParser {

    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        List<Task> tasksList = new ArrayList<>();
        JSONArray tasksArray = new JSONArray(input);
        for (Object object : tasksArray) {
            try {
                JSONObject jsonObject = (JSONObject) object;
                Task t = new Task("for-now");
                //get description
                parseDes(t, jsonObject);
                //get tags
                parseTags(t, jsonObject);
                //get duedate
                parseDuedate(t, jsonObject);
                //set priority
                parsePriority(t, jsonObject);
                //set status
                parseStatus(t, jsonObject);
                tasksList.add(t);
            } catch (EmptyStringException e) {
                continue;
            }
        }
        return tasksList;
    }

    public void parseDuedate(Task t, JSONObject j) {
        if (j.has("due-date")) {
            if (j.optJSONObject("due-date") != null) {
                if (duedateHasallfiled(j)) {
                    assignDuedate(t, j);
                } else {
                    throw new EmptyStringException();
                }
            } else {
                DueDate dn = null;
                t.setDueDate(dn);
            }
        } else {
            throw new EmptyStringException();
        }
    }

    public void assignDuedate(Task t, JSONObject j) {
        Calendar c = Calendar.getInstance();
        JSONObject jd = j.optJSONObject("due-date");
        c.set(Calendar.YEAR, jd.getInt("year"));
        c.set(Calendar.MONTH, jd.getInt("month"));
        c.set(Calendar.DAY_OF_MONTH, jd.getInt("day"));
        c.set(Calendar.HOUR_OF_DAY, jd.getInt("hour"));
        c.set(Calendar.MINUTE, jd.getInt("minute"));
        Date d = c.getTime();
        DueDate duedate = new DueDate(d);
        t.setDueDate(duedate);
    }

    public boolean duedateHasallfiled(JSONObject j) {
        if (hashRightyear(j) && hashRightMonth(j) && hashRightday(j) && hashRighthour(j) && hashRightminute(j)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hashRightyear(JSONObject j) {
        boolean hasRightfield = true;
        if (!j.getJSONObject("due-date").has("year")) {
            hasRightfield = false;
        } else {
            if (!(j.getJSONObject("due-date").get("year") instanceof Integer)) {
                hasRightfield = false;
            }
        }
        return hasRightfield;
    }

    public boolean hashRightMonth(JSONObject j) {
        boolean hasRightfield = true;
        if (!j.getJSONObject("due-date").has("month")) {
            hasRightfield = false;
        } else {
            if (!(j.getJSONObject("due-date").get("month") instanceof Integer)) {
                hasRightfield = false;
            }
        }
        return hasRightfield;
    }

    public boolean hashRightday(JSONObject j) {
        boolean hasRightfield = true;
        if (!j.getJSONObject("due-date").has("day")) {
            hasRightfield = false;
        } else {
            if (!(j.getJSONObject("due-date").get("day") instanceof Integer)) {
                hasRightfield = false;
            }
        }
        return hasRightfield;
    }

    public boolean hashRighthour(JSONObject j) {
        boolean hasRightfield = true;
        if (!j.getJSONObject("due-date").has("hour")) {
            hasRightfield = false;
        } else {
            if (!(j.getJSONObject("due-date").get("hour") instanceof Integer)) {
                hasRightfield = false;
            }
        }
        return hasRightfield;
    }

    public boolean hashRightminute(JSONObject j) {
        boolean hasRightfield = true;
        if (!j.getJSONObject("due-date").has("minute")) {
            hasRightfield = false;
        } else {
            if (!(j.getJSONObject("due-date").get("minute") instanceof Integer)) {
                hasRightfield = false;
            }
        }
        return hasRightfield;
    }


    public void parsePriority(Task t, JSONObject j) {
        if (j.has("priority")) {
            if (hasBooleanofPriority(j)) {
                setprioritybyBoolean(t, j);
            } else {
                throw new EmptyStringException();
            }
        } else {
            throw new EmptyStringException();
        }
    }

    public void setprioritybyBoolean(Task t, JSONObject j) {
        if (j.getJSONObject("priority").getBoolean("important")) {
            if (j.getJSONObject("priority").getBoolean("urgent")) {
                t.setPriority(new Priority(1));
            } else {
                t.setPriority(new Priority(2));
            }
        } else {
            if (j.getJSONObject("priority").getBoolean("urgent")) {
                t.setPriority(new Priority(3));
            } else {
                t.setPriority(new Priority(4));
            }
        }
    }

    public boolean hasBooleanofPriority(JSONObject j) {
        if (j.getJSONObject("priority").has("urgent") && j.getJSONObject("priority").has("important")) {
            if ((j.getJSONObject("priority").get("urgent") instanceof Boolean)
                    && (j.getJSONObject("priority").get("important") instanceof Boolean)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void parseStatus(Task t, JSONObject j) {
        if (j.has("status")) {
            if (!j.getString("status").isEmpty()) {
                setstatusofTask(t, j);
            } else {
                throw new EmptyStringException();
            }
        } else {
            throw new EmptyStringException();
        }
    }

    public void setstatusofTask(Task t, JSONObject j) {
        String s = j.getString("status");
        if (s.equalsIgnoreCase("TODO")) {
            t.setStatus(Status.TODO);
        } else if (s.equalsIgnoreCase("UP_NEXT")) {
            t.setStatus(Status.UP_NEXT);
        } else if (s.equalsIgnoreCase("IN_PROGRESS")) {
            t.setStatus(Status.IN_PROGRESS);
        } else if (s.equalsIgnoreCase("DONE")) {
            t.setStatus(Status.DONE);
        } else {
            throw new EmptyStringException();
        }
    }

    public void parseTags(Task t, JSONObject j) {
        if (j.has("tags")) {
            if (j.get("tags") instanceof JSONArray) {
                tagsNotEmpty(t, j);
            } else {
                throw new EmptyStringException();
            }
        } else {
            throw new EmptyStringException();
        }
    }

    public void tagsNotEmpty(Task t, JSONObject j) {
        if (!j.getJSONArray("tags").isEmpty()) {
            if (correctKeyforallTags(j)) {
                JSONArray arrayTags = j.getJSONArray("tags");
                for (Object o : arrayTags) {
                    JSONObject jt = (JSONObject) o;
                    t.addTag(jt.getString("name"));
                }
            } else {
                throw new EmptyStringException();
            }
        } else {
            throw new EmptyStringException();
        }
    }

    public boolean correctKeyforallTags(JSONObject j) {
        Boolean keyrightfor = true;
        for (Object jk : j.getJSONArray("tags")) {
            JSONObject jo = (JSONObject) jk;
            if (jo.has("name")) {
                //
            } else {
                keyrightfor = false;
            }
        }
        return keyrightfor;
    }

    public void parseDes(Task t, JSONObject j) {
        if (j.has("description")) {
            if (!j.getString("description").isEmpty()) {
                t.setDescription(j.getString("description"));
            } else {
                throw new EmptyStringException();
            }
        } else {
            throw new EmptyStringException();
        }
    }
}
