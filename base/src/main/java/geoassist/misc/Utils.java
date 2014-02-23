package geoassist.misc;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String strDec(double n, int comprimento) {
        // Retorna a parte decimal de um numero em forma de string, com
        // comprimento fixo
        String sn;

        double num = abs(n); // tira o sinal
        num = num - floor(num); // tira a parte inteira
        num = round(num, comprimento);

        if (num != 0) {
            String numStr = String.valueOf(num);
            sn = numStr.substring(2, numStr.length());
        } else {
            sn = "";
        }

        return sn + "000000000000000".substring(0, comprimento - sn.length());
    }

    public static String decToStr(double valor, int casasDecimais) {
        BigDecimal bd = new BigDecimal(valor);
        bd = bd.setScale(casasDecimais, RoundingMode.HALF_UP);
        return bd.toString().replace(".", ",");
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

}
