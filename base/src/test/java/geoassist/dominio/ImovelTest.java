package geoassist.dominio;

import geoassist.geometria.Ponto3D;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ImovelTest {

    private Imovel imovel;

    @Before
    public void setUp() {
        imovel = new Imovel();
        imovel.addMarco(UtilEntidades.criaMarco("P-01", "Alguem", 1000, 1000, 0));
        imovel.addMarco(UtilEntidades.criaMarco("P-02", "", 1000, 2000, 0));
        imovel.addMarco(UtilEntidades.criaMarco("P-03", "Outro", 2000, 2000, 0));
        imovel.addMarco(UtilEntidades.criaMarco("P-04", "", 2000, 1000, 0));
    }

    @Test
    public void testPontoCentral() {
        Ponto3D centro = imovel.getCentro();
        Assert.assertEquals(1500d, centro.x);
        Assert.assertEquals(1500d, centro.y);
    }

    @Test
    public void testNumeroDePontos() {
        Assert.assertEquals(4, imovel.getNumeroDePontos());
    }

}
