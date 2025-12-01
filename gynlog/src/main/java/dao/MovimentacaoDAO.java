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
        "C:\\Users\\Davi\\Documents\\NetBeansProjects\\gynlog\\Movimentacao.txt";

    // --------------------------------------------------
    // Gera o próximo ID automaticamente lendo o arquivo
    // --------------------------------------------------
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
            // arquivo ainda não existe → ID inicia em 1
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
        bw.newLine();   // separa registros

        bw.flush();

    } catch (Exception e) {
        System.out.println("Erro ao salvar movimentação: " + e.getMessage());
    }
}
    // --------------------------------------------------
    // SALVAR UMA NOVA MOVIMENTAÇÃO NO TXT
    // --------------------------------------------------
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
    
    public double somatorioDespesasPorMes(int mes, int ano) {
    double total = 0.0;
    String caminho = "Movimentacoes.txt"; // ajuste se você usa caminho absoluto

    try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {

        String linha;
        String data = null;      // última data lida do bloco atual
        Double valor = null;     // último valor lido do bloco atual

        while ((linha = br.readLine()) != null) {
            linha = linha.trim();

            if (linha.startsWith("Data:")) {
                // espera formato dd/MM/yyyy (ex: 22/05/2025)
                data = linha.split(":", 2)[1].trim();
                continue;
            }

            if (linha.startsWith("Valor:")) {
                String s = linha.split(":", 2)[1].trim();
                try {
                    valor = Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    // ignora valores inválidos
                    valor = null;
                }
                continue;
            }

            // detecta fim do bloco: linha separadora ou linha vazia
            if (linha.startsWith("===") || linha.isEmpty()) {
                if (data != null && valor != null) {
                    // processa bloco
                    String[] partes = data.split("/");
                    if (partes.length >= 3) {
                        try {
                            int mesArquivo = Integer.parseInt(partes[1]);
                            int anoArquivo = Integer.parseInt(partes[2]);
                            if (mesArquivo == mes && anoArquivo == ano) {
                                total += valor;
                            }
                        } catch (NumberFormatException ex) {
                            // ignora data mal formatada
                        }
                    }
                }
                // reset para próximo bloco
                data = null;
                valor = null;
            }
        }

        // Se último bloco não terminou com separador, processa também
        if (data != null && valor != null) {
            String[] partes = data.split("/");
            if (partes.length >= 3) {
                try {
                    int mesArquivo = Integer.parseInt(partes[1]);
                    int anoArquivo = Integer.parseInt(partes[2]);
                    if (mesArquivo == mes && anoArquivo == ano) {
                        total += valor;
                    }
                } catch (NumberFormatException ex) {
                    // ignora
                }
            }
        }

    } catch (Exception e) {
        // trate o erro como preferir (log/JOptionPane). Aqui apenas print:
        e.printStackTrace();
    }

    return total;
}
    
    public double somarIPVA(int ano) {
    double total = 0.0;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;
        boolean dentroDespesa = false;
        int tipoDespesa = -1;
        String data = "";
        double valor = 0.0;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Detecta início do bloco de despesa
            if (linha.equals("Despesa:")) {
                dentroDespesa = true;
                tipoDespesa = -1;
                data = "";
                valor = 0.0;
                continue;
            }

            // Detecta separador
            if (linha.startsWith("========================================")) {

                // Se o bloco anterior era IPVA daquele ano, soma
                if (dentroDespesa && tipoDespesa == 3 && data.endsWith(String.valueOf(ano))) {
                    total += valor;
                }

                dentroDespesa = false;
                continue;
            }

            // Processa os campos dentro da despesa
            if (dentroDespesa) {

                if (linha.startsWith("Tipo:")) {
                    tipoDespesa = Integer.parseInt(linha.split(":")[1].trim());
                }

                if (linha.startsWith("Data:")) {
                    data = linha.split(":")[1].trim();
                }

                if (linha.startsWith("Valor:")) {
                    valor = Double.parseDouble(linha.split(":")[1].trim());
                }
            }
        }

        // Último bloco do arquivo
        if (dentroDespesa && tipoDespesa == 3 && data.endsWith(String.valueOf(ano))) {
            total += valor;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
    
    public double somatorioMultasPorVeiculoEAno(int idVeiculo, int anoProcurado) {
    double total = 0.0;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;
        boolean bloco = false;   // indica que estamos dentro de um bloco de movimentação
        int idV = 0;
        int ano = 0;
        int idTipo = 0;
        double valor = 0.0;

        while ((linha = br.readLine()) != null) {

            if (linha.startsWith("ID Movimentacao:")) {
                bloco = true;
                idV = 0;
                ano = 0;
                idTipo = 0;
                valor = 0.0;
            }

            if (bloco) {

                if (linha.startsWith("ID Veiculo:")) {
                    idV = Integer.parseInt(linha.split(":")[1].trim());
                }

                if (linha.startsWith("ID Tipo:")) {
                    idTipo = Integer.parseInt(linha.split(":")[1].trim());
                }

                if (linha.startsWith("Data:")) {
                    String data = linha.split(":")[1].trim();
                    ano = Integer.parseInt(data.substring(6));
                }

                if (linha.startsWith("Valor:")) {
                    valor = Double.parseDouble(linha.split(":")[1].trim());
                }

                // fim do bloco
                if (linha.trim().equals("-----")) {
                    if (idV == idVeiculo && ano == anoProcurado && idTipo == 4) { 
                        // Tipo 4 = MULTA
                        total += valor;
                    }
                    bloco = false;
                }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
    public double totalCombustivelMes(int mesProcurado, int anoProcurado) {

    File arquivo = new File("Movimentacoes.txt");
    if (!arquivo.exists()) {
        return 0;
    }

    double total = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {

        String linha;
        String tipoDespesa = "";
        String data = "";
        double valor = 0;

        while ((linha = br.readLine()) != null) {

            if (linha.startsWith("TipoDespesa:")) {
                tipoDespesa = linha.split(":")[1].trim();
            }

            if (linha.startsWith("Data:")) {
                data = linha.split(":")[1].trim();  // formato: 21/01/2025
            }

            if (linha.startsWith("Valor:")) {
                valor = Double.parseDouble(linha.split(":")[1].trim().replace(",", "."));
            }

            // Fim do bloco
            if (linha.trim().equals("========================================")) {

                try {
                    int mes = Integer.parseInt(data.substring(3, 5));
                    int ano = Integer.parseInt(data.substring(6));

                    if (tipoDespesa.equalsIgnoreCase("Combustível") &&
                        mes == mesProcurado &&
                        ano == anoProcurado) 
                    {
                        total += valor;
                    }

                } catch (Exception e) {
                    // pular bloco inválido
                }

                tipoDespesa = "";
                data = "";
                valor = 0;
            }
        }

    } catch (Exception e) {
        return 0;
    }

    return total;
}
    
    public String listarDespesasPorVeiculo(int idVeiculo) {

    StringBuilder sb = new StringBuilder();

    sb.append("===== DADOS DO VEÍCULO =====\n");

    // 1️⃣ LER O BLOCO DO VEÍCULO
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

                if (linha.startsWith("========================================")) {
                    break; // fim do bloco
                }

                sb.append(linha).append("\n");
            }
        }

        if (!blocoEncontrado) {
            return "Veículo NÃO encontrado.\n";
        }

    } catch (Exception e) {
        return "Erro ao ler BancodeDadosVeiculos.txt\n";
    }

    sb.append("\n===== DESPESAS DO VEÍCULO =====\n");

    // 2️⃣ LER AS DESPESAS DO MOVIMENTACOES.TXT
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

                    if (idAtual == idVeiculo) {
                        sb.append(blocoTemp).append("\n");
                    }

                    idAtual = -1;
                }
            }
        }

    } catch (Exception e) {
        return "Erro ao ler Movimentacoes.txt\n";
    }

    return sb.toString();
}


    // --------------------------------------------------
    // LER TODAS AS MOVIMENTAÇÕES DO TXT
    // --------------------------------------------------
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