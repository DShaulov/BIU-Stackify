package com.example.stackify;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;

import com.google.gson.Gson;

import junit.framework.TestCase;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
        List<Box> boxList = new ArrayList<Box>();
        Solution solution = new Solution(boxList, 600, 800, 1600, 3);
        solution.setSolutionName("Test Solution");
        Segment segment1 = new Segment(600, 800, 800);
        Segment segment2 = new Segment(600, 800, 800);
        Box seg1box1 = new Box(1, 600, 800, 750);
        Box seg1box2 = new Box(2, 600, 800, 400);
        Box seg2box1 = new Box(3, 600, 800, 800);

        boxList.add(seg1box1);
        boxList.add(seg1box2);
        boxList.add(seg2box1);

        segment1.addBox(seg1box1);
        segment1.addBox(seg1box2);
        segment2.addBox(seg2box1);

        solution.addSegment(segment1);
        solution.addSegment(segment2);

        solution.setBoxList(boxList);

        dao.insert(solution);

        Solution readSolution = dao.get("Test Solution");

        assertEquals("Test Solution", readSolution.getSolutionName());
        assertEquals(2, solution.getSegmentList().size());

        LocalDate readDate = readSolution.getDate();
        assertEquals(16, readDate.getDayOfMonth());

        List<Box> readBoxList = readSolution.getBoxList();
        assertEquals(400, readBoxList.get(1).getLength());
        assertEquals(800, readBoxList.get(2).getLength());

        Segment readSeg1 = solution.getSegmentList().get(0);
        Segment readSeg2 = solution.getSegmentList().get(1);
        assertEquals(2, readSeg1.getNumOfBoxes());
        assertEquals(1, readSeg2.getNumOfBoxes());
        Box readSeg1Box1 = readSeg1.getBoxList().get(0);
        assertEquals(750, readSeg1Box1.getLength());
    }

    @Test
    public void gsonTest() {
        List<Box> boxList = new ArrayList<Box>();
        Solution solution = new Solution(boxList, 600, 800, 1600, 3);
        solution.setSolutionName("Test Solution");
        Segment segment1 = new Segment(600, 800, 800);
        Segment segment2 = new Segment(600, 800, 800);
        Box seg1box1 = new Box(1, 600, 800, 750);
        seg1box1.setBottomLeft(new Coordinate(0, 0));
        Box seg1box2 = new Box(2, 600, 800, 400);
        seg1box2.setBottomLeft(new Coordinate(400, 400));
        Box seg2box1 = new Box(3, 600, 800, 800);
        seg2box1.setBottomLeft(new Coordinate(0, 0));

        boxList.add(seg1box1);
        boxList.add(seg1box2);
        boxList.add(seg2box1);

        segment1.addBox(seg1box1);
        segment1.addBox(seg1box2);
        segment2.addBox(seg2box1);

        solution.addSegment(segment1);
        solution.addSegment(segment2);

        solution.setBoxList(boxList);

        Gson gson = new Gson();
        String json = gson.toJson(solution);
        System.out.println(json);
    }
}