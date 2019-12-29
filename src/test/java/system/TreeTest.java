package system;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreeTest {
    Tree tree = new Tree("tree");
    Tree sub = tree.GetChildByName("sub");

    @Test
    public void depth() {
        assertEquals(tree.depth+1, sub.depth);
    }

    @Test
    public void grow() {
        assertEquals(tree.depth+1, sub.depth);
    }

    @Test
    public void child() {
        Assert.assertNotNull(tree.GetChildByName("sub"));
    }

    @Test
    public void rootParent() {
        Assert.assertNull(tree.parent);
    }

    @Test
    public void rootDepth() {
        assertEquals(0, tree.depth);
    }
}
