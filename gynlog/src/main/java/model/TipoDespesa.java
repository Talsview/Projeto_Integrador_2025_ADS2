package model;

public class TipoDespesa {

    private int tipo;
    private String descricao;

    public TipoDespesa() {
    }

    public TipoDespesa(int tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public int getId() {
        return tipo;
    }

    public void setId(int tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}