public class FastFourierTransform {
  public static Complex[] computeFft(Complex[] cps) {
    return computeFft(cps, new Complex[cps.length], cps.length, 0, 1);
  }

  public static Complex[] computeFft(Complex[] cps, Complex[] tmpArr, int arrSize, int startPos, int stepSize) {
    if (arrSize == 1) {
      return cps;
    }

    int splitArrSize = arrSize / 2;
    computeFft(cps, tmpArr, splitArrSize, startPos, 2 * stepSize);
    computeFft(cps, tmpArr, splitArrSize, startPos + stepSize, 2 * stepSize);

    int evenArrPos = startPos;
    int oddArrPos = startPos + stepSize;
    int kthPos = startPos;
    int kn2thPos = startPos + splitArrSize * stepSize;
    for (int k = 0; k < splitArrSize; k++) {
      double radians = -2 * k * Math.PI / arrSize;
      Complex wk = new Complex(Math.cos(radians), Math.sin(radians));
      Complex result1 = Complex.add(cps[evenArrPos], Complex.multiply(wk, cps[oddArrPos]));
      Complex result2 = Complex.subtract(cps[evenArrPos], Complex.multiply(wk, cps[oddArrPos]));

      tmpArr[kthPos] = result1;
      tmpArr[kn2thPos] = result2;

      evenArrPos += 2 * stepSize;
      oddArrPos += 2 * stepSize;
      kthPos += stepSize;
      kn2thPos += stepSize;
    }

    for (int i = 0; i < cps.length; i++) {
      if (tmpArr[i] != null) {
        cps[i] = tmpArr[i];
      }

      tmpArr[i] = null;
    }

    return cps;
  }
}
