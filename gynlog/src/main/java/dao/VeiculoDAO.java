package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Veiculo;

public class VeiculoDAO {

    private static final String CAMINHO_TXT =
        //tem que alterar o nome do user
        "C:\\Users\\Davi\\Documents\\NetBeansProjects\\gynlog\\BancodeDadosVeiculos.txt";

    private final List<Veiculo> veiculos = new ArrayList<>();

    public VeiculoDAO() {
        // inicializa lista
    }

    // Adiciona e grava no txt
    public void adicionar(Veiculo v) {
        veiculos.add(v);
        salvarEmTXT();
    }

    // Lista veículos inativos
    public String listarVeiculosInativos() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {

            String linha;
            StringBuilder bloco = new StringBuilder();
            boolean isInativo = false;

            while ((linha = br.readLine()) != null) {

                bloco.append(linha).append("\n");

                if (linha.startsWith("Status:")) {
                    String status = linha.split(":")[1].trim();
                    if (status.equalsIgnoreCase("Inativo")) {
                        isInativo = true;
                    }
                }

                if (linha.trim().equals("========================================")) {

                    if (isInativo) sb.append(bloco).append("\n");

                    bloco.setLength(0);
                    isInativo = false;
                }
            }

        } catch (Exception e) {
            return "Erro ao ler o arquivo de veículos.";
        }

        if (sb.length() == 0) {
            return "Nenhum veículo inativo encontrado.";
        }

        return sb.toString();
    }

    // Salva o último veículo no txt
    private void salvarEmTXT() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_TXT, true))) {

            Veiculo ultimo = veiculos.get(veiculos.size() - 1);

            writer.write("ID: " + ultimo.getId()); writer.newLine();
            writer.write("Tipo: " + ultimo.getTipo()); writer.newLine();
            writer.write("Modelo: " + ultimo.getModelo()); writer.newLine();
            writer.write("Placa: " + ultimo.getPlaca()); writer.newLine();
            writer.write("Ano: " + ultimo.getAno()); writer.newLine();
            writer.write("Status: " + ultimo.getStatus()); writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}