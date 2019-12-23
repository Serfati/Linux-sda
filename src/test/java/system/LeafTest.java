package system;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class LeafTest {
    static int numberOfTest = 0;
    Node newLeaf;
    Node anotherLeaf;
    FileSystem fs;

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Total of  tests: "+numberOfTest);
    }

    @Before
    public void setUp() throws OutOfSpaceException {
        fs = new FileSystem(5);
        FileSystem sda = new FileSystem(1000);
        newLeaf = new Leaf("0", 2);
        anotherLeaf = new Leaf("1", 3);
    }

    @After
    public void tearDown() {
        numberOfTest++;
    }

    @Test
    public void LeafInitWorks() {

        assertEquals(newLeaf.getClass(), anotherLeaf.getClass());
    }

    @Test
    public void LeftLeafIsSet() {
        assertNull(newLeaf.parent);
    }


    @Test
    public void testIsLeaf() throws OutOfSpaceException {

        Leaf instance = new Leaf("a", 10);
        assertEquals(instance.getClass(), Leaf.class);
    }

    @Test
    public void testDepthValue() throws OutOfSpaceException {
        Leaf instance = new Leaf("a", 0);
        assertEquals(0, instance.depth);
    }

    @Test
    public void sameName() throws OutOfSpaceException {
        Leaf testLeafOne = new Leaf("a", 0);
        Leaf testLeafTwo = new Leaf("a", 0);
        assertEquals(testLeafOne.name, testLeafTwo.name);
    }

    @Test
    public void testAllocate() {
        try {
            assertEquals(FileSystem.fileStorage.countFreeSpace(), 995);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void createWrongLeaf() {
        try {
            Leaf leaf = new Leaf("root", -1);
            fail();
        } catch(NegativeArraySizeException ignored) {

        } catch(Exception e) {
            fail();
        }
        //2) try to create with null name
        try {
            Leaf leaf = new Leaf(null, 0);
            assertNull(leaf.name);
        } catch(Exception e) {
            fail();
        }
    }
}
