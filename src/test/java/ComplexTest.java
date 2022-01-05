import org.testng.Assert;
import org.testng.annotations.Test;

public class ComplexTest {
  @Test
  public void testAdd() {
    Complex cp1 = new Complex(1.0, 5.0);
    Complex cp2 = new Complex( 3.0, 2.0);

    Complex actualResult = Complex.add(cp1, cp2);
    Complex expectedResult = new Complex(4.0, 7.0);

    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void testSubtract() {
    Complex cp1 = new Complex(7.0, 9.0);
    Complex cp2 = new Complex( 1.0, 6.0);

    Complex actualResult = Complex.subtract(cp1, cp2);
    Complex expectedResult = new Complex(6.0, 3.0);

    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void testMultiply() {
    Complex cp1 = new Complex(3.0, 4.0);
    Complex cp2 = new Complex( 1.0, 2.0);

    Complex actualResult = Complex.multiply(cp1, cp2);
    Complex expectedResult = new Complex(-5.0, 10.0);

    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void testMagnitude() {
    Complex cp = new Complex(6.0, 8.0);

    double actualMagnitude = cp.getMagnitude();
    double expectedMagnitude = 10.0;

    Assert.assertEquals(actualMagnitude, expectedMagnitude);
  }

}