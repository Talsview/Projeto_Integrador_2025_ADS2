package dao;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Veiculo;

public class VeiculoDAO {
    
    public static final String CAMINHO =
            System.getProperty("user.home") + File.separator +
            "Documents" + File.separator +
            "NetBeansProjects" + File.separator +
            "gynlog" + File.separator +
            "BancodeDadosVeiculos.txt";

    private final List<Veiculo> veiculos = new ArrayList<>();

    public VeiculoDAO() {
        File file = new File(CAMINHO);
        File pasta = file.getParentFile();
        if (!pasta.exists()) pasta.mkdirs();

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionar(Veiculo v) {
        veiculos.add(v);
        salvarEmTXT();
    }

    private void salvarEmTXT() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO, true))) {
            Veiculo ultimo = veiculos.get(veiculos.size() - 1);

            writer.write("ID: " + ultimo.getId()); writer.newLine();
            writer.write("Tipo: " + ultimo.getTipo()); writer.newLine();
            writer.write("Modelo: " + ultimo.getModelo()); writer.newLine();
            writer.write("TipoVeiculo: " + (ultimo.getTipoVeiculo() == null ? "" : ultimo.getTipoVeiculo())); writer.newLine();
            writer.write("Placa: " + ultimo.getPlaca()); writer.newLine();
            writer.write("Ano: " + ultimo.getAno()); writer.newLine();
            writer.write("Status: " + ultimo.getStatus()); writer.newLine();
            writer.write("========================================"); writer.newLine();
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String listarVeiculosInativos() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            StringBuilder bloco = new StringBuilder();
            boolean isInativo = false;

            while ((linha = br.readLine()) != null) {
                bloco.append(linha).append("\n");

                if (linha.startsWith("Status:") &&
                    linha.split(":", 2)[1].trim().equalsIgnoreCase("Inativo")) {
                    isInativo = true;
                }

                if (linha.startsWith("========================================")) {
                    if (isInativo) sb.append(bloco).append("\n");
                    bloco.setLength(0);
                    isInativo = false;
                }
            }
        } catch (IOException e) {
            return "Erro ao ler o arquivo de veículos.";
        }
        return sb.length() == 0 ? "Nenhum veículo inativo encontrado." : sb.toString();
    }

    public String buscarStatusPorId(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            boolean lendoBloco = false;
            boolean blocoDoId = false;

            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("ID:")) {
                    lendoBloco = true;
                    blocoDoId = linha.equals("ID: " + id);
                    continue;
                }

                if (lendoBloco && blocoDoId && linha.startsWith("Status:")) {
                    return linha.split(":", 2)[1].trim();
                }

                if (linha.startsWith("========================================")) {
                    lendoBloco = false;
                    blocoDoId = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean alterarStatusVeiculo(int id, String novoStatus) {
        File arquivo = new File(CAMINHO);

        try {
            List<String> linhas = Files.readAllLines(arquivo.toPath());
            List<String> novoConteudo = new ArrayList<>();
            boolean dentroBloco = false;
            boolean blocoDoId = false;
            boolean encontrou = false;

            for (String linha : linhas) {
                if (linha.startsWith("ID:")) {
                    dentroBloco = true;
                    blocoDoId = linha.equals("ID: " + id);
                    if (blocoDoId) encontrou = true;
                }

                if (dentroBloco && blocoDoId && linha.startsWith("Status:")) {
                    novoConteudo.add("Status: " + novoStatus);
                } else {
                    novoConteudo.add(linha);
                }

                if (linha.startsWith("========================================")) {
                    dentroBloco = false;
                    blocoDoId = false;
                }
            }

            if (!encontrou) return false;

            Files.write(arquivo.toPath(), novoConteudo);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}