package geoassist.misc;

import junit.framework.Assert;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(Utils.isEmpty(null));
        Assert.assertTrue(Utils.isEmpty(""));
        Assert.assertFalse(Utils.isEmpty("a"));

        Assert.assertFalse(Utils.isNotEmpty(null));
        Assert.assertFalse(Utils.isNotEmpty(""));
        Assert.assertTrue(Utils.isNotEmpty("a"));
    }
    
    @Test
    public void testeArredondamento() {
        Assert.assertEquals(2.12, Utils.round(2.123456789, 2));
        Assert.assertEquals(20.00, Utils.round(20.00000000, 2));
        Assert.assertEquals(0.93, Utils.round(1 - 0.01 * 7, 2));
        Assert.assertEquals(0.12, Utils.round(0.123456789, 2));
        Assert.assertEquals(10.01, Utils.round(10.009999999, 2));

        Assert.assertEquals(0d, Utils.round(0.000000001, 8));
        Assert.assertEquals(20d, Utils.round(20.00000000d, 8));
        Assert.assertEquals(2.12345679, Utils.round(2.123456789, 8));
        Assert.assertEquals(0.93, Utils.round(1 - 0.01 * 7, 8));
    }

    @Test
    public void testNumeralArredondadoParaString() {
        Assert.assertEquals("0,93000000", Utils.decToStr(1 - 0.01 * 7, 8));
        Assert.assertEquals("1", Utils.decToStr(1 - 0.01 * 7, 0));
        Assert.assertEquals("5,95", Utils.decToStr(5.949, 2));
    }

    @Test
    public void testExtraiParteDecimalParaString() {
        Assert.assertEquals("00000000", Utils.strDec(1, 8));
        Assert.assertEquals("00000000", Utils.strDec(0, 8));
        Assert.assertEquals("93000000", Utils.strDec(1 - 0.01 * 7, 8));
        Assert.assertEquals("95", Utils.strDec(5.949, 2));
        Assert.assertEquals("", Utils.strDec(1, 0));
    }
    
}
