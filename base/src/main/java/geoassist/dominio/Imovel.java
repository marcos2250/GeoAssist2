package geoassist.dominio;

import geoassist.geometria.Ponto3D;

import java.util.ArrayList;
import java.util.List;

public class Imovel {

    private List<Marco> marcos = new ArrayList<Marco>();

    public Ponto3D getCentro() {
        Ponto3D centro = new Ponto3D();

        for (Marco marco : marcos) {
            centro.x += marco.posicao.x;
            centro.y += marco.posicao.y;
            centro.z += marco.posicao.z;
        }

        centro.x /= getNumeroDePontos();
        centro.y /= getNumeroDePontos();
        centro.z /= getNumeroDePontos();

        return centro;
    }

    public Marco getMarco(int i) {
        return marcos.get(i);
    }

    public Ponto3D getCoordenada(int i) {
        return marcos.get(i).posicao;
    }

    public int getNumeroDePontos() {
        return marcos.size();
    }

    public void addMarco(Marco novoMarco) {
        if (marcos.contains(novoMarco)) {
            return;
        }
        marcos.add(novoMarco);
    }

    public void removeMarco(Marco marco) {
        marcos.remove(marco);
    }

}
