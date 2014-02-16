package geoassist.enumeration;

public enum TipoLimite {

    LA1(201, "LA1", "Limite por vértice do tipo M"), //
    LA2(202, "LA2", "Limite artificial por barragem"), //
    LA3(203, "LA3", "Limite artificial por canal"), //
    LA4(204, "LA4", "Limite artificial por estrada"), //
    LA5(205, "LA5", "Limite artificial não categorizada"), //
    LN1(206, "LN1", "Limite natural por água corrente"), //
    LN2(207, "LN2", "Limite natural por água dormente"), //
    LN3(208, "LN3", "Limite natural por terreno alagado ou alagável"), //
    LN4(209, "LN4", "Limite natural por encosta ou cânion"), //
    LN5(210, "LN5", "Limite natural não categorizada");

    private final int codigo;
    private final String mnemonico;
    private final String descricao;

    private TipoLimite(int codigo, String mnemonico, String descricao) {
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
