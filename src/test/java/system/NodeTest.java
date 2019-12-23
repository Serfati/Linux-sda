package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class NodeTest {
    Tree tree;
    Tree child1;
    Node child2;

    @Before
    public void setUp() {
        tree = new Tree("root");
        child1 = new Tree("left");
        child2 = new Tree("right");
        child1.parent = tree;
        child1.depth = 1;

        child2.parent = child1;
        child2.depth = 2;
    }

    @Test
    public void testGetPathRoot() {
        Assert.assertEquals(tree.getPath().length, 0);
        Assert.assertNull(tree.parent);
        Assert.assertEquals(tree.depth, 0);
    }

    @Test
    public void testGetPath() {
        assertArrayEquals(new String[]{"left"}, child1.getPath());
        assertArrayEquals(new String[]{"left", "right"}, child2.getPath());
    }
}
