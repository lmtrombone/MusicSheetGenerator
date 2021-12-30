import java.util.Objects;

public class Complex {
  private double re;
  private double im;

  public Complex() {}

  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  public static Complex add(Complex cp1, Complex cp2) {
    double re = cp1.re + cp2.re;
    double im = cp1.im + cp2.im;
    return new Complex(re, im);
  }

  public static Complex subtract(Complex cp1, Complex cp2) {
    double re = cp1.re - cp2.re;
    double im = cp1.im - cp2.im;
    return new Complex(re, im);
  }

  public static Complex multiply(Complex cp1, Complex cp2) {
    double re = (cp1.re * cp2.re) - (cp1.im * cp2.im);
    double im = (cp1.re * cp2.im) + (cp1.im * cp2.re);
    return new Complex(re, im);
  }

  public double getRe() {
    return re;
  }

  public void setRe(double re) {
    this.re = re;
  }

  public double getIm() {
    return im;
  }

  public void setIm(double im) {
    this.im = im;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Complex complex = (Complex) o;
    return Double.compare(complex.re, re) == 0 && Double.compare(complex.im, im) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(re, im);
  }

  @Override
  public String toString() {
    if (im == 0) {
      return String.valueOf(re);
    } else if (re == 0) {
      return String.format("%si", im);
    } else if (im < 0) {
      return String.format("%s - %si", re, -im);
    }

    return String.format("%s + %si", re, im);
  }
}