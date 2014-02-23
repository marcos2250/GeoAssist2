package geoassist.dominio;

import geoassist.enumeration.MetodoLevantamento;
import geoassist.enumeration.TipoLimite;
import geoassist.geometria.Ponto3D;

public class UtilEntidades {

    public static Imovel criaImovel() {
        Imovel imovel = new Imovel();

        imovel.addMarco(criaMarco("P-01", "Fulano", 0, 0, 0));
        imovel.addMarco(criaMarco("P-02", "", 1, 0, 0));
        imovel.addMarco(criaMarco("P-03", "Sicrano", 1, 1, 0));
        imovel.addMarco(criaMarco("P-04", "Beltrano", 0, 1, 0));

        return imovel;
    }

    public static Marco criaMarco(String nome, String confrontante, double x, double y, double z) {
        Marco marco = new Marco();

        marco.posicao = new Ponto3D(x, y, z);
        marco.nome = nome;
        marco.confrontante = confrontante;
        marco.desvio = new Ponto3D();
        marco.tipoLimite = TipoLimite.LA1;
        marco.metodoLevantamento = MetodoLevantamento.LG1;

        return marco;
    }
}
