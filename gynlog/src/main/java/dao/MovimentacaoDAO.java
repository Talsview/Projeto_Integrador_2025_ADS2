package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;

public class MovimentacaoDAO {

    private static final String CAMINHO =
            System.getProperty("user.home") + File.separator +
            "Documents" + File.separator +
            "NetBeansProjects" + File.separator +
            "gynlog" + File.separator +
            "Movimentacoes.txt";

    public MovimentacaoDAO() {
        File file = new File(CAMINHO);
        File pasta = file.getParentFile();

        if (!pasta.exists()) pasta.mkdirs();

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int gerarProximoId() {
        int maiorId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("ID:")) {
                    int id = Integer.parseInt(linha.split(":", 2)[1].trim());
                    if (id > maiorId) maiorId = id;
                }
            }
        } catch (Exception ignored) {}
        return maiorId + 1;
    }

    public void salvarMovimentacaoCompleta(String blocoVeiculo, Movimentacao mov) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO, true))) {
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
        } catch (IOException e) {
            System.out.println("Erro ao salvar movimentação: " + e.getMessage());
        }
    }

    public void save(Movimentacao mov) {
        if (mov.getId() == 0) mov.setId(gerarProximoId());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO, true))) {
            bw.write("ID: " + mov.getId());
            bw.newLine();
            bw.write("ID Veículo: " + mov.getIdVeiculo());
            bw.newLine();
            bw.write("ID Tipo Despesa: " + mov.getIdTipoDespesa());
            bw.newLine();
            bw.write("Descrição: " + mov.getDescricao());
            bw.newLine();
            bw.write("Data: " + mov.getData());
            bw.newLine();
            bw.write("Valor: " + mov.getValor());
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double somatorioDespesasPorMes(int mes, int ano) {
        if (mes < 1 || mes > 12) throw new IllegalArgumentException("Mês inválido!");
        if (String.valueOf(ano).length() != 4) throw new IllegalArgumentException("Ano inválido!");

        double total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            String data = null;
            Double valor = null;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.startsWith("Data:")) data = linha.split(":", 2)[1].trim();
                if (linha.startsWith("Valor:")) {
                    try { valor = Double.parseDouble(linha.split(":", 2)[1].trim()); } 
                    catch (Exception ignored) { valor = null; }
                }

                if (linha.isEmpty() || linha.startsWith("ID:")) {
                    if (data != null && valor != null) {
                        try {
                            String[] partes = data.split("/");
                            int mesTxt = Integer.parseInt(partes[1]);
                            int anoTxt = Integer.parseInt(partes[2]);
                            if (mesTxt == mes && anoTxt == ano) total += valor;
                        } catch (Exception ignored) {}
                    }
                    data = null;
                    valor = null;
                }
            }
        } catch (Exception ignored) {}
        return total;
    }

    public double somatorioIPVAporAno(int ano) {
        if (ano < 2000) throw new IllegalArgumentException("Ano mínimo é 2000.");

        double total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            boolean lendo = false;
            Integer tipo = null;
            String data = null;
            Double valor = null;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.startsWith("ID:")) {
                    lendo = true;
                    tipo = null;
                    data = null;
                    valor = null;
                    continue;
                }
                if (!lendo) continue;

                if (linha.startsWith("Tipo:")) {
                    try { tipo = Integer.parseInt(linha.split(":", 2)[1].trim()); } catch (Exception ignored) { tipo = null; }
                }
                if (linha.startsWith("Data:")) data = linha.split(":", 2)[1].trim();
                if (linha.startsWith("Valor:")) {
                    try { valor = Double.parseDouble(linha.split(":", 2)[1].trim()); } catch (Exception ignored) { valor = null; }

                    if (tipo != null && tipo == 3 && data != null && valor != null) {
                        try {
                            String[] partes = data.split("/");
                            int anoTxt = Integer.parseInt(partes[2]);
                            if (anoTxt == ano) total += valor;
                        } catch (Exception ignored) {}
                    }
                    lendo = false;
                }
            }
        } catch (Exception ignored) {}
        return total;
    }

    private List<String> lerBlocosPorTipo(int tipoFiltro, int mes, int ano) {
        List<String> blocos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            StringBuilder bloco = new StringBuilder();
            Integer tipo = null;
            String data = null;
            boolean lendo = false;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.startsWith("ID:")) {
                    bloco = new StringBuilder();
                    bloco.append(linha).append("\n");
                    lendo = true;
                    tipo = null;
                    data = null;
                    continue;
                }
                if (!lendo) continue;
                bloco.append(linha).append("\n");

                if (linha.startsWith("Tipo:")) {
                    try { tipo = Integer.parseInt(linha.split(":", 2)[1].trim()); } catch (Exception ignored) { tipo = null; }
                }
                if (linha.startsWith("Data:")) data = linha.split(":", 2)[1].trim();

                if (linha.startsWith("Valor:") && tipo != null && data != null && tipo == tipoFiltro) {
                    try {
                        String[] partes = data.split("/");
                        int mesTxt = Integer.parseInt(partes[1]);
                        int anoTxt = Integer.parseInt(partes[2]);
                        if (mesTxt == mes && anoTxt == ano) blocos.add(bloco.toString());
                    } catch (Exception ignored) {}
                    lendo = false;
                }
            }
        } catch (Exception ignored) {}
        return blocos;
    }

    public String listarMultasPorMes(int mes, int ano) {
        if (mes < 1 || mes > 12) return "Mês inválido.\n";
        if (String.valueOf(ano).length() != 4) return "Ano inválido.\n";

        StringBuilder sb = new StringBuilder("===== MULTAS PAGAS NO MÊS =====\n\n");
        for (String bloco : lerBlocosPorTipo(4, mes, ano)) sb.append(bloco).append("\n");
        return sb.toString();
    }

    public String listarDespesasCombustivelPorMes(int mes, int ano) {
        StringBuilder sb = new StringBuilder("===== DESPESAS DE COMBUSTÍVEL =====\n\n");
        for (String bloco : lerBlocosPorTipo(1, mes, ano)) sb.append(bloco).append("\n");
        return sb.toString();
    }

    public String listarDespesasPorVeiculo(int idVeiculo) {
        StringBuilder sb = new StringBuilder("===== DADOS DO VEÍCULO =====\n");

        try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {
            String linha;
            boolean encontrado = false;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.equals("ID: " + idVeiculo)) {
                    encontrado = true;
                    sb.append(linha).append("\n");
                    continue;
                }
                if (encontrado) {
                    if (linha.startsWith("========================================")) break;
                    sb.append(linha).append("\n");
                }
            }
            if (!encontrado) return "Veículo NÃO encontrado.\n";
        } catch (Exception e) {
            return "Erro ao ler BancodeDadosVeiculos.txt\n";
        }

        sb.append("\n===== DESPESAS DO VEÍCULO =====\n");

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            boolean lendo = false;
            int idAtual = -1;
            StringBuilder bloco = new StringBuilder();

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.startsWith("ID:")) {
                    try { idAtual = Integer.parseInt(linha.split(":", 2)[1].trim()); } catch (Exception ignored) { idAtual = -1; }
                    bloco = new StringBuilder();
                    lendo = true;
                    continue;
                }
                if (!lendo) continue;

                if (linha.startsWith("Despesa:") || linha.startsWith("Descrição:") || linha.startsWith("Data:") || linha.startsWith("Valor:"))
                    bloco.append(linha).append("\n");

                if (linha.startsWith("Valor:") && idAtual == idVeiculo) sb.append(bloco).append("\n");

                if (linha.startsWith("Valor:")) lendo = false;
            }
        } catch (Exception e) {
            return "Erro ao ler Movimentacoes.txt\n";
        }

        return sb.toString();
    }

    public List<Movimentacao> listarTodos() {
        List<Movimentacao> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {
            String linha;
            Movimentacao mov = null;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("ID:")) {
                    if (mov != null) lista.add(mov);
                    mov = new Movimentacao();
                    mov.setId(Integer.parseInt(linha.split(":", 2)[1].trim()));
                } else if (mov != null) {
                    if (linha.startsWith("ID Veículo:")) mov.setIdVeiculo(Integer.parseInt(linha.split(":", 2)[1].trim()));
                    else if (linha.startsWith("ID Tipo Despesa:")) mov.setIdTipoDespesa(Integer.parseInt(linha.split(":", 2)[1].trim()));
                    else if (linha.startsWith("Descrição:")) mov.setDescricao(linha.split(":", 2)[1].trim());
                    else if (linha.startsWith("Data:")) mov.setData(linha.split(":", 2)[1].trim());
                    else if (linha.startsWith("Valor:")) mov.setValor(Double.parseDouble(linha.split(":", 2)[1].trim()));
                    else if (linha.startsWith("========================================")) {
                        lista.add(mov);
                        mov = null;
                    }
                }
            }
            if (mov != null) lista.add(mov);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao ler arquivo Movimentacoes: " + e.getMessage());
        }
        return lista;
    }
}