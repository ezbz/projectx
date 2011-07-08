package org.projectx.zookeeper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EphemeralNodeNameUtilsTest {
  private static final String ROOT_NODE = "/projectx/election";

  @Test
  public void test_constructNodeName_Zeo() {
    assertEquals("incorrect node name", ROOT_NODE + "/n_0000000000",
        EphemeralNodeNameUtils.constructNodeName(ROOT_NODE, 0));
  }

  @Test
  public void test_constructNodeName_Ten() {
    assertEquals("incorrect node name", ROOT_NODE + "/n_0000000010",
        EphemeralNodeNameUtils.constructNodeName(ROOT_NODE, 10));
  }

  @Test
  public void test_constructNodeName_Hundred() {
    assertEquals("incorrect node name", ROOT_NODE + "/n_0000000100",
        EphemeralNodeNameUtils.constructNodeName(ROOT_NODE, 100));
  }

  @Test
  public void test_parseSequenceFromName_Zero() {
    assertEquals("incorrect sequence number", 0,
        EphemeralNodeNameUtils.parseSequenceFromName(ROOT_NODE + "/n_0000000000"));
  }

  @Test
  public void test_parseSequenceFromName_Ten() {
    assertEquals("incorrect sequence number", 10,
        EphemeralNodeNameUtils.parseSequenceFromName(ROOT_NODE + "/n_0000000010"));
  }

  @Test
  public void test_parseSequenceFromName_Hundred() {
    assertEquals("incorrect sequence number", 100,
        EphemeralNodeNameUtils.parseSequenceFromName(ROOT_NODE + "/n_0000000100"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_parseSequenceFromName_EmptyName() {
    EphemeralNodeNameUtils.parseSequenceFromName("");
  }

  @Test(expected = NumberFormatException.class)
  public void test_parseSequenceFromName_NAN() {
    EphemeralNodeNameUtils.parseSequenceFromName("/xx/n_xxxx");
  }

}
