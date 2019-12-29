package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpaceTest {
    final int size = 10;
    Space newSpace;
    Space anotherSpace;
    FileSystem fs;

    @Before
    public void setUp() {
        newSpace = new Space(100);
        anotherSpace = new Space(200);
        fs = new FileSystem(size);
        assertEquals(FileSystem.fileStorage.countFreeSpace(), size);
    }

    @Test
    public void testAllocs() {
        try {
            Leaf newNode = new Leaf("testLeaf", 5);
            int[] allocs = newNode.allocations;
            try {
                assertEquals(5, FileSystem.fileStorage.countFreeSpace());
                assertEquals(newNode.allocations.length, 5);
            } catch(Exception e) {
                fail();
            }
            assertEquals(allocs[0], 0);
            assertEquals(allocs[1], 1);
            assertEquals(allocs[2], 2);
            assertEquals(allocs[3], 3);
            assertEquals(allocs[4], 4);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testBlocks() {
        try {
            Leaf newNode = new Leaf("testLeaf", 5);
            Leaf[] blocks = FileSystem.fileStorage.getAlloc();
            assertEquals(blocks[0], newNode);
            assertEquals(blocks[1], newNode);
            assertEquals(blocks[2], newNode);
            assertEquals(blocks[3], newNode);
            assertEquals(blocks[4], newNode);
        } catch(Exception e) {
            fail();
        }
    }

    //--------------------------------------------------//
    @Test
    public void testDeAllocs() {
        try {
            Tree parent = new Tree("root");
            Leaf leaf = new Leaf("testLeaf", 4);
            parent.children.put(parent.name, leaf);
            leaf.parent = parent;
            FileSystem.fileStorage.Dealloc(leaf);
            Arrays.stream(FileSystem.fileStorage.getAlloc(), 0, 4).forEachOrdered(Assert::assertNull);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void SpaceInitWorks() {
        assertEquals(newSpace.getClass(), anotherSpace.getClass());
    }

    @Test
    public void testIsSpace() {
        newSpace = new Space(10);
        assertEquals(newSpace.getClass(), Space.class);
    }

    @Test
    public void sameFreeSpace() {
        anotherSpace = new Space(100);
        assertEquals(newSpace.countFreeSpace(), anotherSpace.countFreeSpace());
    }
}
