package dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;

public class MovimentacaoDAO {

    private static final String CAMINHO =
        //tem que alterar o nome do user
        "C:\\Users\\Davi\\Documents\\NetBeansProjects\\gynlog\\Movimentacao.txt";

    // Gera próximo ID lendo o arquivo
    private int gerarProximoId() {
        int maiorId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("ID:")) {
                    int id = Integer.parseInt(linha.split(":")[1].trim());
                    if (id > maiorId) maiorId = id;
                }
            }
        } catch (Exception e) {
            // arquivo não existe ainda
        }

        return maiorId + 1;
    }
    
    public void salvarMovimentacaoCompleta(String blocoVeiculo, Movimentacao mov) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Movimentacoes.txt", true))) {

            bw.write(blocoVeiculo.trim());
            bw.newLine();

            bw.write("Despesa:");
            bw.newLine();
            bw.write("Tipo: " + mov.getIdTipoDespesa());
            bw.newLine();
            bw.write("Descrição: " + mov.getDescricao());
            bw.newLine();
            bw.write("Data: " + mov.getData());
            bw.newLine();
            bw.write("Valor: " + mov.getValor());
            bw.newLine();
            bw.newLine();  

            bw.flush();

        } catch (Exception e) {
            System.out.println("Erro ao salvar movimentação: " + e.getMessage());
        }
    }

    // Salva uma movimentação
    public void save(Movimentacao mov) {

        if (mov.getId() == 0) {
            mov.setId(gerarProximoId());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO, true))) {

            bw.write("ID: " + mov.getId()); bw.newLine();
            bw.write("ID Veículo: " + mov.getIdVeiculo()); bw.newLine();
            bw.write("ID Tipo Despesa: " + mov.getIdTipoDespesa()); bw.newLine();
            bw.write("Descrição: " + mov.getDescricao()); bw.newLine();
            bw.write("Data: " + mov.getData()); bw.newLine();
            bw.write("Valor: " + mov.getValor()); bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Soma despesas por mês/ano
    public double somatorioDespesasPorMes(int mes, int ano) {
        double total = 0.0;
        String caminho = "Movimentacoes.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {

            String linha;
            String data = null;
            Double valor = null;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.startsWith("Data:")) {
                    data = linha.split(":", 2)[1].trim();
                    continue;
                }

                if (linha.startsWith("Valor:")) {
                    try {
                        valor = Double.parseDouble(linha.split(":", 2)[1].trim());
                    } catch (NumberFormatException e) { valor = null; }
                    continue;
                }

                // fim do bloco
                if (linha.startsWith("===") || linha.isEmpty()) {
                    if (data != null && valor != null) {
                        String[] partes = data.split("/");
                        if (partes.length >= 3) {
                            try {
                                int mesArquivo = Integer.parseInt(partes[1]);
                                int anoArquivo = Integer.parseInt(partes[2]);
                                if (mesArquivo == mes && anoArquivo == ano) {
                                    total += valor;
                                }
                            } catch (NumberFormatException ex) {}
                        }
                    }
                    data = null;
                    valor = null;
                }
            }

            // último bloco
            if (data != null && valor != null) {
                String[] partes = data.split("/");
                try {
                    int mesArquivo = Integer.parseInt(partes[1]);
                    int anoArquivo = Integer.parseInt(partes[2]);
                    if (mesArquivo == mes && anoArquivo == ano) total += valor;
                } catch (Exception e) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    // Soma total de IPVA por ano
    public double somatorioIPVAporAno(int ano) {

        double total = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

            String linha;
            boolean lendo = false;

            Integer tipo = null;
            String data = null;
            Double valor = null;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    lendo = true;
                    tipo = null;
                    data = null;
                    valor = null;
                    continue;
                }

                if (lendo) {

                    if (linha.startsWith("Tipo:")) {
                        try {
                            tipo = Integer.parseInt(linha.split(":")[1].trim());
                        } catch (Exception e) {}
                    }

                    if (linha.startsWith("Data:")) {
                        data = linha.split(":")[1].trim();
                    }

                    if (linha.startsWith("Valor:")) {
                        try {
                            valor = Double.parseDouble(linha.split(":")[1].trim());
                        } catch (Exception e) {}

                        if (tipo != null && tipo == 3) { 
                            if (data != null && data.contains("/")) {
                                String[] partes = data.split("/");
                                int anoTxt = Integer.parseInt(partes[2]);
                                if (anoTxt == ano) total += valor;
                            }
                        }

                        lendo = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    // Lista multas por mês/ano
    public String listarMultasPorMes(int mes, int ano) {

        StringBuilder sb = new StringBuilder();
        sb.append("===== MULTAS PAGAS NO MÊS =====\n\n");

        try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

            String linha;
            StringBuilder bloco = new StringBuilder();
            boolean lendo = false;

            Integer tipo = null;
            String data = null;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    bloco = new StringBuilder();
                    bloco.append(linha).append("\n");
                    lendo = true;
                    tipo = null;
                    data = null;
                    continue;
                }

                if (lendo) {
                    bloco.append(linha).append("\n");

                    if (linha.startsWith("Tipo:")) {
                        try {
                            tipo = Integer.parseInt(linha.split(":")[1].trim());
                        } catch (Exception e) {}
                    }

                    if (linha.startsWith("Data:")) {
                        data = linha.split(":")[1].trim();
                    }

                    if (linha.startsWith("Valor:")) {

                        if (tipo != null && tipo == 4) {
                            if (data != null && data.contains("/")) {
                                String[] partes = data.split("/");
                                int mesTxt = Integer.parseInt(partes[1]);
                                int anoTxt = Integer.parseInt(partes[2]);
                                if (mesTxt == mes && anoTxt == ano) {
                                    sb.append(bloco).append("\n");
                                }
                            }
                        }

                        lendo = false;
                    }
                }
            }

        } catch (Exception e) {
            return "Erro ao ler Movimentacoes.txt\n";
        }

        if (sb.toString().equals("===== MULTAS PAGAS NO MÊS =====\n\n")) {
            return "Nenhuma multa encontrada nesse mês.\n";
        }

        return sb.toString();
    }
    
    // Lista despesas de combustível por mês
    public String listarDespesasCombustivelPorMes(int mes, int ano) {

        StringBuilder sb = new StringBuilder();
        sb.append("===== DESPESAS DE COMBUSTÍVEL =====\n\n");

        try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

            String linha;
            StringBuilder bloco = new StringBuilder();
            boolean lendo = false;

            Integer tipo = null;
            String data = null;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    bloco = new StringBuilder();
                    bloco.append(linha).append("\n");
                    lendo = true;
                    tipo = null;
                    data = null;
                    continue;
                }

                if (lendo) {
                    bloco.append(linha).append("\n");

                    if (linha.startsWith("Tipo:")) {
                        try {
                            tipo = Integer.parseInt(linha.split(":")[1].trim());
                        } catch (Exception e) {}
                    }

                    if (linha.startsWith("Data:")) {
                        data = linha.split(":")[1].trim();
                    }

                    if (linha.startsWith("Valor:")) {

                        if (tipo != null && tipo == 1) {
                            if (data != null && data.contains("/")) {
                                String[] partes = data.split("/");
                                int mesTxt = Integer.parseInt(partes[1]);
                                int anoTxt = Integer.parseInt(partes[2]);
                                if (mesTxt == mes && anoTxt == ano) {
                                    sb.append(bloco).append("\n");
                                }
                            }
                        }

                        lendo = false;
                    }
                }
            }

        } catch (Exception e) {
            return "Erro ao ler Movimentacoes.txt\n";
        }

        if (sb.toString().equals("===== DESPESAS DE COMBUSTÍVEL =====\n\n")) {
            return "Nenhuma despesa de combustível encontrada.\n";
        }

        return sb.toString();
    }
    
    // Lista despesas por ID do veículo
    public String listarDespesasPorVeiculo(int idVeiculo) {

        StringBuilder sb = new StringBuilder();

        sb.append("===== DADOS DO VEÍCULO =====\n");

        // bloco do veículo
        try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {
            String linha;
            boolean blocoEncontrado = false;

            while ((linha = br.readLine()) != null) {

                if (linha.equals("ID: " + idVeiculo)) {
                    blocoEncontrado = true;
                    sb.append(linha).append("\n");
                    continue;
                }

                if (blocoEncontrado) {
                    if (linha.startsWith("========================================")) break;
                    sb.append(linha).append("\n");
                }
            }

            if (!blocoEncontrado) return "Veículo NÃO encontrado.\n";

        } catch (Exception e) {
            return "Erro ao ler BancodeDadosVeiculos.txt\n";
        }

        sb.append("\n===== DESPESAS DO VEÍCULO =====\n");

        // despesas do veículo
        try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {
            String linha;
            boolean blocoMov = false;
            StringBuilder blocoTemp = new StringBuilder();
            int idAtual = -1;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    idAtual = Integer.parseInt(linha.split(":")[1].trim());
                    blocoTemp = new StringBuilder();
                    blocoTemp.append(linha).append("\n");
                    continue;
                }

                if (idAtual != -1) {
                    blocoTemp.append(linha).append("\n");

                    if (linha.startsWith("Valor:")) {
                        if (idAtual == idVeiculo) sb.append(blocoTemp).append("\n");
                        idAtual = -1;
                    }
                }
            }

        } catch (Exception e) {
            return "Erro ao ler Movimentacoes.txt\n";
        }

        return sb.toString();
    }

    // Lista todas as movimentações
    public List<Movimentacao> listarTodos() {

        List<Movimentacao> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {

            String linha;
            Movimentacao mov = null;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    mov = new Movimentacao();
                    mov.setId(Integer.parseInt(linha.split(":")[1].trim()));
                }

                else if (linha.startsWith("ID Veículo:")) {
                    mov.setIdVeiculo(Integer.parseInt(linha.split(":")[1].trim()));
                }

                else if (linha.startsWith("ID Tipo Despesa:")) {
                    mov.setIdTipoDespesa(Integer.parseInt(linha.split(":")[1].trim()));
                }

                else if (linha.startsWith("Descrição:")) {
                    mov.setDescricao(linha.split(":")[1].trim());
                }

                else if (linha.startsWith("Data:")) {
                    mov.setData(linha.split(":")[1].trim());
                }

                else if (linha.startsWith("Valor:")) {
                    mov.setValor(Double.parseDouble(linha.split(":")[1].trim()));
                }

                else if (linha.startsWith("========================================") && mov != null) {
                    lista.add(mov);
                }
            }

        } catch (Exception e) {}

        return lista;
    }
}
