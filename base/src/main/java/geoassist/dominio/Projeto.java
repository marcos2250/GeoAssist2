package geoassist.dominio;

import geoassist.geometria.Ponto3D;

public class Projeto {

    private Datum datum;
    private Imovel imovel;

    private Ponto3D centro;
    private Ponto3D minFolha;
    private Ponto3D maxFolha;

    public Datum getDatum() {
        return datum;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public Ponto3D getCentro() {
        return centro;
    }

    public Ponto3D getMinFolha() {
        return minFolha;
    }

    public Ponto3D getMaxFolha() {
        return maxFolha;
    }

    public void setCentro(Ponto3D centro) {
        this.centro = centro;
    }

    public void setMinFolha(Ponto3D minFolha) {
        this.minFolha = minFolha;
    }

    public void setMaxFolha(Ponto3D maxFolha) {
        this.maxFolha = maxFolha;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

}
