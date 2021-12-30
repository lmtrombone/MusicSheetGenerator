import org.testng.Assert;
import org.testng.annotations.Test;

public class FastFourierTransformTest {
  @Test
  public void testFft() {
    Complex[] input = new Complex[4];
    input[0] = new Complex(1.0, 0.0);
    input[1] = new Complex(2.0, -1.0);
    input[2] = new Complex(0.0, -1.0);
    input[3] = new Complex(-1.0, 2.0);

    Complex[] expectedResult = new Complex[4];
    expectedResult[0] = new Complex(2.0, 0.0);
    expectedResult[1] = new Complex(-2.0, -2.0);
    expectedResult[2] = new Complex(0.0, -2.0);
    expectedResult[3] = new Complex(4.0, 4.0);

    Complex[] output = FastFourierTransform.computeFft(input);

    Assert.assertEquals(output[0], expectedResult[0]);
    Assert.assertEquals(output[1], expectedResult[1]);
    Assert.assertEquals(output[2], expectedResult[2]);
    Assert.assertEquals(output[3], expectedResult[3]);
  }
}