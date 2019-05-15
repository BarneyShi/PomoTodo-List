package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProject {
    private Project p1;
    private Project p2;
    private Task t1;
    private Task t2;
    private Task t3;

    @BeforeEach
    public void testbefore() {
        Task t1 = new Task("Task one");
        this.t1 = t1;
        Task t2 = new Task("Task two");
        this.t2 = t2;
        Task t3 = new Task("Task three");
        this.t3 = t3;

        Project p1 = new Project("This is Project 1");
        this.p1 = p1;
        Project p2 = new Project("This is Project 2");
        this.p2 = p2;
    }

    @Test
    public void testconsr() {
        assertEquals(p1.getDescription(), "This is Project 1");
        assertEquals(p2.getDescription(), "This is Project 2");

    }

    @Test
    public void testadd() {
        p1.add(t1);
        assertFalse(p1.isCompleted());
        assertTrue(p1.contains(t1));
        p1.add(t1);
        //get list of tasks
        //List<Task> test1 = new ArrayList<>();
        //test1.addAll(p1.getTasks());

        //assertEquals("??", test1.toString());
        assertEquals(1, p1.getNumberOfTasks());
        try {
            p1.add(null);
            fail();
        } catch (NullArgumentException e) {
            //
        }
    }

    @Test
    public void testRemove() {
        p1.add(t1);
        p1.remove(t2);
    }


    @Test
    public void testnum() {
        p1.add(t1);
        p1.add(t2);
        p1.remove(t1);
        p1.add(t3);
        assertEquals(2, p1.getNumberOfTasks());
    }


    @Test
    public void testcontain() {
        p1.add(t1);
        assertTrue(p1.contains(t1));
        p1.remove(t1);
        p1.add(t2);
        assertFalse(p1.contains(t1));
        assertFalse(p1.contains(t3));
    }

    @Test
    public void testConsExcep() {
        try {
            Project p = new Project("");
            fail();
        } catch (EmptyStringException e) {

        }

        try {
            Project p = new Project(null);
            fail();
        } catch (EmptyStringException e) {

        }
    }

    @Test
    public void testaddExcep() {
        try {
            p1.add(null);
            fail();
        } catch (NullArgumentException e) {

        }
        p1.add(p1);

    }

    @Test
    public void testRemoveExcep() {
        try {
            p1.remove(null);
            fail();
        } catch (NullArgumentException e) {

        }
    }

    @Test
    public void testConExcep() {
        try {
            p1.add(null);
            fail();
        } catch (NullArgumentException e) {

        }
    }

    @Test
    public void testContainsNull() {
        try {
            p1.contains(null);
            fail();
        } catch (NullArgumentException e) {
            // do nothing
        }
    }

    @Test
    void testequal() {
        assertTrue(p1.equals(p1));
        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(t1));
        assertFalse(p1.hashCode() == p2.hashCode());
    }


    @Test
    void testCompleted() {
        try {
            assertFalse(p1.isCompleted());
            t1.setProgress(100);
            t2.setProgress(100);
            p1.add(t1);
            p1.add(t2);
            p2.add(t3);
            p2.add(p1);
            assertFalse(p2.isCompleted());
            t3.setProgress(100);
            assertTrue(p2.isCompleted());
        } catch (InvalidProgressException e) {
            fail();
        }

    }

    @Test
    void testGetNumofTasks() {
        p1.add(t1);
        p1.add(t2);
        p1.add(p2);
        p2.add(p1);
        p2.add(t2);
        assertEquals(3, p1.getNumberOfTasks());
    }

    @Test
    void testEst() {
        try {
            t1.setEstimatedTimeToComplete(2);
            t2.setEstimatedTimeToComplete(3);
            p1.add(t1);
            p1.add(t2);
            p2.add(t2);
            p1.add(p2);
            p2.add(t1);
            assertEquals(10, p1.getEstimatedTimeToComplete());
            p1.remove(t1);
            assertEquals(8, p1.getEstimatedTimeToComplete());
        } catch (NegativeInputException e) {
            fail();
        }
    }

    @Test
    void testGetProgress() {
        try {
            assertEquals(p1.getProgress(), 0);
            p1.add(t1);
            p1.add(t2);
            p1.add(t3);
            assertEquals(0, p1.getProgress());
            t1.setProgress(100);
            assertEquals(33, p1.getProgress());
            t2.setProgress(50);
            t3.setProgress(25);
            assertEquals(58, p1.getProgress());
            Todo t4 = new Task("ss");
            p2.add(t4);
            assertEquals(0, p2.getProgress());
            p2.add(p1);
            assertEquals(0, t4.progress);
            assertEquals(29, p2.getProgress());
        } catch (InvalidProgressException e) {
            fail();
        }
    }

    @Test
    void testGetEst1() {
        try {
            p1.add(t1);
            p1.add(t2);
            p1.add(t3);
            assertEquals(0, p1.getEstimatedTimeToComplete());
            t1.setEstimatedTimeToComplete(8);
            assertEquals(8, p1.getEstimatedTimeToComplete());
            t2.setEstimatedTimeToComplete(2);
            t3.setEstimatedTimeToComplete(10);
            assertEquals(20, p1.getEstimatedTimeToComplete());
            Task t4 = new Task("ss");
            p2.add(t4);
            t4.setEstimatedTimeToComplete(4);
            p2.add(p1);
            assertEquals(24, p2.getEstimatedTimeToComplete());
        } catch (NegativeInputException e) {
            fail();
        }
    }


    @Test
    void testget() {
        try {
            p1.getTasks();
            fail();
        } catch (UnsupportedOperationException e) {
            //
        }
    }
    //
    @Test
    void testImpUurgIterator() {

        t1.setPriority(new Priority(1));
        p2.setPriority(new Priority(3));
        t3.setPriority(new Priority(4));
        t2.setPriority(new Priority(2));
        p1.add(t1);
        p1.add(p2);
        p1.add(t2);
        p1.add(t3);
        Iterator it = p1.iterator();
        assertTrue(it.hasNext());
        assertEquals(t1,it.next());
        assertEquals(t2,it.next());
        assertEquals(p2,it.next());
        assertEquals(t3,it.next());
        //it.next();
        //assertFalse(it.hasNext());

//        try {
//            it.next();
//            fail();
//        } catch (NoSuchElementException e) {
//            //
//        }
    }

    @Test
    void testExcep() {
        Iterator i = p1.iterator();
        try {
            assertFalse(i.hasNext());
            i.next();
            fail();
        } catch (NoSuchElementException e) {
            //
        }
    }

    @Test
    void testMultiImpUrg() {
        t1.setPriority(new Priority(1));
        t2.setPriority(new Priority(2));
        t3.setPriority(new Priority(3));
        p2.setPriority(new Priority(1));
        p1.add(t1);
        p1.add(t3);
        p1.add(t2);
        p1.add(p2);

        for (Todo t: p1) {
            System.out.println(t.description);
        }
    }
}
