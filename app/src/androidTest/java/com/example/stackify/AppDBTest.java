package com.example.stackify;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;

import junit.framework.TestCase;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AppDBTest {
    private SolutionDao dao;
    private AppDB db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDB.class).build();
        dao = db.solutionDao();
    }

    @After
    public void closeDb() throws IOException {
        if (db != null) {
            db.close();
        }
    }

    @Test
    public void writeAndReadSolution() throws Exception {
        Solution solution = new Solution(600, 800, 1600);
        solution.setSolutionName("Test Solution");
        Segment segment1 = new Segment(600, 800, 800);
        Segment segment2 = new Segment(600, 800, 800);
        Box seg1box1 = new Box(1, 600, 800, 750);
        Box seg1box2 = new Box(2, 600, 800, 400);
        Box seg2box1 = new Box(3, 600, 800, 800);

        segment1.addBox(seg1box1);
        segment1.addBox(seg1box2);
        segment2.addBox(seg2box1);

        solution.addSegment(segment1);
        solution.addSegment(segment2);

        dao.insert(solution);

        Solution readSolution = dao.get("Test Solution");

        assertEquals("Test Solution", readSolution.getSolutionName());
        assertEquals(2, solution.getSegmentList().size());
        Segment readSeg1 = solution.getSegmentList().get(0);
        Segment readSeg2 = solution.getSegmentList().get(1);
        assertEquals(2, readSeg1.getNumOfBoxes());
        assertEquals(1, readSeg2.getNumOfBoxes());
        Box readSeg1Box1 = readSeg1.getBoxList().get(0);
        assertEquals(750, readSeg1Box1.getLength());
    }
}