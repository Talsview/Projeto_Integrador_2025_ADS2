package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Veiculo;

public class VeiculoDAO {

    private static final String CAMINHO =
        System.getProperty("user.home") + File.separator +
        "Documents" + File.separator +
        "NetBeansProjects" + File.separator +
        "gynlog" + File.separator +
        "BancodeDadosVeiculos.txt";


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
    
    public String buscarStatusPorId(int id) {
    File arquivo = new File("BancodeDadosVeiculos.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
        String linha;
        boolean dentroBloco = false;
        boolean blocoDoId = false;

        while ((linha = br.readLine()) != null) {

            if (linha.startsWith("ID:")) {
                dentroBloco = true;
                blocoDoId = linha.equals("ID: " + id);
                continue;
            }

            if (dentroBloco && blocoDoId && linha.startsWith("Status:")) {
                return linha.split(":")[1].trim();
            }

            if (linha.startsWith("========================================")) {
                dentroBloco = false;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null; // Não achou
}
    
    public boolean alterarStatusVeiculo(int id, String novoStatus) {
    File arquivo = new File("BancodeDadosVeiculos.txt");

    try {
        List<String> linhas = Files.readAllLines(arquivo.toPath());
        List<String> novoConteudo = new ArrayList<>();

        boolean dentroBloco = false;
        boolean blocoDoId = false;

        for (String linha : linhas) {

            // Detectou o começo do bloco
            if (linha.startsWith("ID:")) {
                dentroBloco = true;
                blocoDoId = linha.equals("ID: " + id);
            }

            // Se este bloco é do ID procurado → alterar STATUS
            if (dentroBloco && blocoDoId && linha.startsWith("Status:")) {
                novoConteudo.add("Status: " + novoStatus);
            } else {
                novoConteudo.add(linha);
            }

            // DEAD-LINE do bloco
            if (linha.startsWith("========================================")) {
                dentroBloco = false;
            }
        }

        // Salvar arquivo inteiro reescrito
        Files.write(arquivo.toPath(), novoConteudo);

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // Salva o último veículo no txt
    private void salvarEmTXT() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO, true))) {

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