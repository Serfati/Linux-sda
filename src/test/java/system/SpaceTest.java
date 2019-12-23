package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

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
    public void testAlloc1() {
        try {
            Leaf leaf1 = new Leaf("testLeaf", 5);
            assertEquals(5, FileSystem.fileStorage.countFreeSpace());
            assertEquals(leaf1.allocations.length, 5);
        } catch(Exception e) {
            fail();
        }

    }

    @Test
    public void testAlloc3() {
        try {
            Leaf leaf1 = new Leaf("testLeaf", 5);
            int[] allocs = leaf1.allocations;
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
    public void testAlloc4() {
        try {
            Leaf leaf1 = new Leaf("testLeaf", 5);
            Leaf[] blocks = FileSystem.fileStorage.getAlloc();
            assertEquals(blocks[0], leaf1);
            assertEquals(blocks[1], leaf1);
            assertEquals(blocks[2], leaf1);
            assertEquals(blocks[3], leaf1);
            assertEquals(blocks[4], leaf1);
        } catch(Exception e) {
            fail();
        }
    }

    //--------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testAlloc6() throws Exception {
        new Leaf("testLeaf", 11);
        fail();
    }

    @Test
    public void testDeAlloc1() {
        try {
            Tree parent = new Tree("root");
            Leaf leaf = new Leaf("testLeaf", 4);
            parent.children.put(parent.name, leaf);
            leaf.parent = parent;
            FileSystem.fileStorage.Dealloc(leaf);
            Leaf[] array = FileSystem.fileStorage.getAlloc();
            Arrays.stream(array, 0, 4).forEachOrdered(Assert::assertNull);
        } catch(Exception e) {
            fail();
        }
    }


    @Test
    public void testDeAlloc2() {
        try {
            Tree parent = new Tree("root");
            Leaf leaf = new Leaf("testLeaf", 4);
            parent.children.put(parent.name, leaf);
            leaf.parent = parent;
            FileSystem.fileStorage.Dealloc(leaf);
            assertEquals(10, FileSystem.fileStorage.countFreeSpace());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testDeAlloc3() {
        try {
            Tree parent = new Tree("root");
            Leaf leaf = new Leaf("testLeaf", 4);
            parent.children.put(parent.name, leaf);
            leaf.parent = parent;
            FileSystem.fileStorage.Dealloc(leaf);
            assertFalse(parent.children.containsKey(leaf.name));
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
