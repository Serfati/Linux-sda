package system;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TreeTest {
    Tree tree = new Tree("tree");
    Tree subTree = tree.GetChildByName("subTree");

    @Test
    public void ChildName2() {
        Assert.assertNotNull(subTree);
    }

    @Test
    public void ChildName() {
        Assert.assertNotNull(tree.GetChildByName("subTree"));
    }

    @Test
    public void rootParent() {
        Assert.assertNull(tree.parent);
    }

    @Test
    public void rootDepthTest() {
        assertEquals(0, tree.depth);
    }

    @Test
    public void depthTest() {
        assertEquals(tree.depth+1, subTree.depth);
    }

    @Test
    public void checkChildren() {
        try {

            Tree tree = new Tree("root");
            Tree son1 = new Tree("son1");
            tree.children.put("son1", son1);
            Tree result = tree.GetChildByName("son1");
            assertEquals(0, result.depth);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void depthGrow() {
        assertEquals(tree.depth+1, subTree.depth);
    }
}
