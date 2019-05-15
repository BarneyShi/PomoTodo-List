package model;

import com.sun.org.apache.xpath.internal.operations.Neg;
import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.TaskParser;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TestTask {
    // TODO: design tests for new behaviour added to Task class
    private Task t1;
    private Task t2;
    private Task tn;
    private DueDate d;

    @BeforeEach
    public void testBefore() {
        t1 = new Task("t1");
        t2 = new Task("t2");
        Calendar c = Calendar.getInstance();
        c.set(2019,03,03,16,00);
        Date dd = c.getTime();
        d = new DueDate(dd);
    }

    @Test
    public void testConstr() {
        try {
            tn = new Task("");
            fail();
        } catch (EmptyStringException e) {
            //
        }
        try {
            tn = new Task(null);
            fail();
        } catch (EmptyStringException e) {
            //
        }
        assertEquals(t1.getDescription(),"t1");
        assertEquals(t1.getDueDate(),null);
        assertEquals(t1.getPriority().toString(),"DEFAULT");
    }
    @Test
    public void testaddTag(){
        t1.addTag("a");
        assertTrue(t1.containsTag("a"));
    }

    @Test
    public void testaddTagt(){
        try {
            Tag t = null;
            t1.addTag(t);
            fail();
        } catch (NullArgumentException e) {
            //
        }

        Tag t = new Tag("b");
        t1.addTag(t);
        assertTrue(t1.containsTag(t));
        t1.addTag(t);
        assertEquals(t1.getTags().size(),1);
    }
    @Test
    public void testremove(){
        t1.addTag("a");
        assertTrue(t1.containsTag("a"));
        t1.removeTag("a");
        assertFalse(t1.containsTag("a"));
        Tag t = new Tag("b");
        t1.addTag(t);
        assertTrue(t1.containsTag(t));
        t1.removeTag(t);
        assertFalse(t1.containsTag(t));
        t1.removeTag(t);
        Tag tn = null;
        try {
            t1.removeTag(tn);
        } catch (NullArgumentException e) {
            //
        }

    }
    @Test
    public void testpri() {
        try {
            t1.setPriority(null);
            fail();
        } catch (NullArgumentException e) {
            //
        }
        Priority p = new Priority(1);
        t1.setPriority(p);
        assertEquals(t1.getPriority().toString(),"IMPORTANT & URGENT");
    }
    @Test
    public void teststatus() {
        try {
            t1.setStatus(null);
            fail();
        } catch (NullArgumentException e) {
            //
        }
        Status s = Status.IN_PROGRESS;
        t1.setStatus(s);
        assertEquals(t1.getStatus().toString(),"IN PROGRESS");
    }
    @Test
    public void testdes() {
        try {
            t1.setDescription(null);
            fail();
        } catch (EmptyStringException e) {
            //
        }
        try {
            t1.setDescription("");
            fail();
        } catch (EmptyStringException e) {
            //
        }
        t1.setDescription("dd");
        assertEquals(t1.getDescription(),"dd" );
    }
    @Test
    public void testdate(){
        t1.setDueDate(d);
        assertEquals(t1.getDueDate().toString(),"Wed Apr 03 2019 04:00 PM");
    }
    @Test
    public void testcontains() {
        try {
            String s = null;
            t1.containsTag(s);
            fail();
        } catch (EmptyStringException e) {
            //
        }
        try {
            t1.containsTag("");
            fail();
        } catch (EmptyStringException e) {
            //
        }
        Tag t = new Tag("tt");
        t1.addTag(t);
        assertTrue(t1.containsTag("tt"));
        try {
            Tag ttt = null;
            t1.containsTag(ttt);
            fail();
        } catch (NullArgumentException e) {
            //
        }
    }

    @Test
    public void testparse() {
        t1.setDescription(" new description ## today; tags1");
    }

    @Test
    public void testtostring() {
        t1.toString();
        t1.setDescription("new ## today;tags1");
        t1.toString();
        t1.setDescription("new ## today;tag1;tag2;tag3");
        t1.toString();
    }

    @Test
    public void testequals() {
        Task tnull1 = null;
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(t2));
        assertFalse(t1.equals(d));
        t1.setDescription("des ## today;tag1;important;todo");
        t2.setDescription("des ## today;tag1;urgent;todo");
        assertFalse(t1.equals(t2));
        t1.setDescription("des ## today;tag1;important;todo");
        t2.setDescription("des ## tomorrow;tag1;urgent;todo");
        assertFalse(t1.equals(t2));
        t1.setDescription("des1 ## important;todo");
        t2.setDescription("des1 ## urgent;todo");
        assertFalse(t1.equals(t2));
    }

    @Test
    public void teststatusequals() {
        Task tt1 = new Task("des1");
        Task tt2 = new Task("des2");
        tt1.setDescription("test ## tomorrow;tags1;up next;important");
        tt2.setDescription("test ## tomorrow;tags1;in progress;important");
        //assertEquals("",tt2.toString());
        assertFalse(tt1.equals(tt2));
        tt1.setDescription("test ## tomorrow;tags1;up next;urgent");
        tt2.setDescription("test ## tomorrow;tags1;up next;urgent");
        //assertEquals("",tt2.toString());
        assertTrue(tt1.equals(tt2));
    }

    @Test
    void testAfterUMLConstructor() {
        Task tnew = new Task("test");
        assertEquals(tnew.getEstimatedTimeToComplete(),0);
        assertEquals(tnew.getProgress(),0);
        try {
            Task t = new Task(null);
            fail();
        } catch (EmptyStringException e) {
            //
        }
        try {
            Task t = new Task("");
            fail();
        } catch (EmptyStringException e) {
            //
        }
    }

    @Test

    void testStatusandProgress() {
        t1.setStatus(Status.DONE);
        assertEquals(100,t1.getProgress());
        t2.setProgress(100);
        assertEquals(Status.DONE,t2.getStatus());
    }


    @Test
    void testSetProgress() {
        try {
            t1.setProgress(10);
            assertEquals(10,t1.getProgress());
        } catch (InvalidProgressException e) {
            fail();
        }

        try {
            t1.setProgress(101);
            fail();
        } catch (InvalidProgressException e) {
            //
        }

        try {
            t1.setProgress(-1);
            fail();
        } catch (InvalidProgressException e) {
            //
        }

    }
    @Test
    void testSetEst() {
        try {
            t1.setEstimatedTimeToComplete(100);
            assertEquals(100,t1.getEstimatedTimeToComplete());
        } catch (NegativeInputException e) {
            fail();
        }

        try {
            t1.setEstimatedTimeToComplete(-1);
            fail();
        } catch (NegativeInputException e) {
            //
        }

    }
}
