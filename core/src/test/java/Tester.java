import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Tester {

  @Test
  public void testingCrunchifyAddition() {
    assertEquals("Here is test for Addition Result: ", 30, addition(27, 3));
  }

  @Test
  public void testingHelloWorld() {
    assertEquals("Here is test for Hello World String: ", "Hello + World", helloWorld());
  }

  public int addition(int x, int y) {
    return x + y;
  }

  public String helloWorld() {
    String helloWorld = "Hello +" + " World";
    return helloWorld;
  }
}
