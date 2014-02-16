package geoassist.enumeration;

public enum MetodoLevantamento {

    LT1(101, "LT1", "Levantamento por poligonal coincidente ao limite"), //
    LT2(102, "LT2", "Levantamento de limite por irradiação"), //
    LG1(103, "LG1", "Posicionamento relativo estático"), //
    LG2(104, "LG2", "Posicionamento relativo estático rápido"), //
    LG3(105, "LG3", "Posicionamento relativo semicinemático (stop and go)"), //
    LG4(106, "LG4", "Posicionamento relativo cinemático"), //
    LG5(107, "LG5", "Posicionamento RTK"), //
    LG6(108, "LG6", "Posicionamento por DGPS ou WADGPS"), //
    LG7(109, "LG7", "Posicionamento diferencial por meio do código C/A"), //
    LV(110, "LV", "Levantamento por método indireto");

    private final int codigo;
    private final String mnemonico;
    private final String descricao;

    private MetodoLevantamento(int codigo, String mnemonico, String descricao) {
        this.codigo = codigo;
        this.mnemonico = mnemonico;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMnemonico() {
        return mnemonico;
    }

    public String getDescricao() {
        return descricao;
    }

}
