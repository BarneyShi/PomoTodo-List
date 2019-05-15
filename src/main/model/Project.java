package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.io.PrintWriter;
import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {

    //private List<Task> tasks;
    private List<Todo> todos;
    private int flag;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
//        if (description == null || description.length() == 0) {
//            throw new EmptyStringException("Cannot construct a project with no description");
//        }
//        this.description = description;
        todos = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }
        if (!task.equals(this)) {
            if (!todos.contains(task)) {
                todos.add(task);
                //etcHours = etcHours + task.etcHours;
                //float p = (float) ((progress + task.progress) / (getNumberOfTasks()));
                //progress = (int) Math.floor(p);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }
        if (todos.contains(task)) {
            todos.remove(task);
            //etcHours = etcHours - task.etcHours;
            ////remove -> progress
//            int p = 0;
//            for (Todo t : todos) {
//                p = t.progress + p;
//            }
//            float f = (float) (p / (getNumberOfTasks()));
//            progress = (int) Math.floor(f);
        }
    }

    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }


    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
//     the percentage of completion (rounded down to the nearest integer).
//     the value returned is the average of the percentage of completion of
//     all the tasks and sub-projects in this project.
    public int getProgress() {
        if (getNumberOfTasks() == 0) {
            return 0;
        } else {
            int p = 0;
            for (Todo t : todos) {
                if (t instanceof Task) {
                    p = t.progress + p;
                } else {
                    //This place can be really tricky!!
                    p = p + t.getProgress();
                }
            }
            float d = (float) (p / (getNumberOfTasks()));
            progress = (int) Math.floor(d);
            return progress;
        }
    }


    // EFFECTS: returns the number of tasks (and sub-projects) in this project

    public int getNumberOfTasks() {
        return todos.size();
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
//     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }

    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return todos.contains(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public int getEstimatedTimeToComplete() {
        int time = 0;
        for (Todo t : todos) {
            if (t instanceof Task) {
                time = time + t.etcHours;
            } else {
                time = time + t.getEstimatedTimeToComplete();
            }
        }
        return time;
    }

    @Override
    public Iterator<Todo> iterator() {
        return new ImpUrgIterator();
    }


    private class ImpUrgIterator implements Iterator<Todo> {
        private Iterator<Todo> it = todos.iterator();
        private int numPriority;
        private int index;
        private int num;


        ImpUrgIterator() {
            numPriority = 1;
            index = 0;
            num = todos.size();

        }

        @Override
        public boolean hasNext() {
            return num > 0;
        }
        //num: how many todo have  I printed.
        //index: the position now.

        @Override
        public Todo next() {
            while (hasNext()) {
                Priority p = new Priority(numPriority);
                while (index < todos.size()) {
                    Todo t = todos.get(index);
                    index++;
                    if (t.priority.equals(p)) {
                        num--;
                        return t;
                    }
                }
                index = 0;
                numPriority++;
            }
            throw new NoSuchElementException();
        }
    }


}
