package geoassist.dominio;

import geoassist.enumeration.MetodoLevantamento;
import geoassist.enumeration.TipoLimite;
import geoassist.geometria.Ponto3D;

public class MarcoImovel {

    public Ponto3D posicao;
    public Ponto3D desvio;
    public String nome;
    public String confrontante;
    public TipoLimite tipoLimite;
    public MetodoLevantamento metodoLevantamento;

}
