package model;

public class TipoDespesa {

    private int id;
    private String descricao;

    public TipoDespesa() {
    }

    // Construtor para facilitar criação direta no ComboBox
    public TipoDespesa(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // ESSENCIAL para exibir a descrição no JComboBox
    @Override
    public String toString() {
        return descricao;
    }
}