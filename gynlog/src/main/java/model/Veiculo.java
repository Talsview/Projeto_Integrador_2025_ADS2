package model;

public class Veiculo {
    private int id;
    private String tipo;          
    private String modelo;
    private String tipoVeiculo; 
    private String placa;
    private int ano;
    private String status;

    //Construtor Tipo de Veiculo
    public Veiculo(int id, String tipo, String modelo, String tipoVeiculo, String placa, int ano, String status) {
        this.id = id;
        this.tipo = tipo;
        this.modelo = modelo;
        this.tipoVeiculo = tipoVeiculo;
        this.placa = placa;
        this.ano = ano;
        this.status = status;
    }

    public Veiculo() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipoVeiculo() { return tipoVeiculo; }
    public void setTipoVeiculo(String tipoVeiculo) { this.tipoVeiculo = tipoVeiculo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}