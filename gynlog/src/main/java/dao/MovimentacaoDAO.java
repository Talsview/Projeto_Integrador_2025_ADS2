package dao;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;

public class MovimentacaoDAO {
    
    public MovimentacaoDAO() {

    File file = new File(CAMINHO);
    File pasta = file.getParentFile();

    // Cria a pasta caso não exista
    if (!pasta.exists()) {
        pasta.mkdirs();
    }

    // Cria o arquivo caso não exista
    try {
        if (!file.exists()) {
            file.createNewFile();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    // Caminho do arquivo de despesas
    private static final String CAMINHO =
        System.getProperty("user.home") + File.separator +
        "Documents" + File.separator +
        "NetBeansProjects" + File.separator +
        "gynlog" + File.separator +
        "Movimentacoes.txt";

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
        } catch (Exception ignored) {}

        return maiorId + 1;
    }

    // Salva bloco de veículo + informações da despesa
    public void salvarMovimentacaoCompleta(String blocoVeiculo, Movimentacao mov) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Movimentacoes.txt", true))) {

        // Bloco do veículo
        bw.write(blocoVeiculo.trim());
        bw.newLine();

        // Bloco da despesa
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

    } catch (Exception e) {
        System.out.println("Erro ao salvar movimentação: " + e.getMessage());
    }
}

    // Salva somente uma movimentação simples
    public void save(Movimentacao mov) {

    // Gera ID se ainda não existir
    if (mov.getId() == 0) {
        mov.setId(gerarProximoId());
    }

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


    // Soma despesas por mês e ano
    public double somatorioDespesasPorMes(int mes, int ano) {

    if (mes < 1 || mes > 12)
        throw new IllegalArgumentException("Mês inválido!");
    if (String.valueOf(ano).length() != 4)
        throw new IllegalArgumentException("Ano inválido!");

    double total = 0;
    String data = null;
    Double valor = null;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Captura data
            if (linha.startsWith("Data:")) {
                data = linha.split(":", 2)[1].trim();
            }

            // Captura valor
            if (linha.startsWith("Valor:")) {
                try {
                    valor = Double.parseDouble(linha.split(":", 2)[1].trim());
                } catch (Exception e) {
                    valor = null; // protege contra valor formatado errado
                }
            }

            // Fim do bloco → calcula
            if (linha.isEmpty() || linha.startsWith("ID:")) {

                if (data != null && valor != null) {
                    try {
                        String[] partes = data.split("/");

                        if (partes.length == 3) {
                            int mesTxt = Integer.parseInt(partes[1]);
                            int anoTxt = Integer.parseInt(partes[2]);

                            if (mesTxt == mes && anoTxt == ano) {
                                total += valor;
                            }
                        }
                    } catch (Exception ignored) {
                        // ignora data mal formatada
                    }
                }

                // reset para próximo bloco
                data = null;
                valor = null;
            }
        }

    } catch (Exception ignored) {}

    return total;
}

    // Soma total de IPVA por ano
    public double somatorioIPVAporAno(int ano) {

    if (ano < 2000)
        throw new IllegalArgumentException("Ano mínimo é 2000.");

    double total = 0;
    Integer tipo = null;
    String data = null;
    Double valor = null;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;
        boolean lendo = false;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Início de um novo bloco
            if (linha.startsWith("ID:")) {
                lendo = true;
                tipo = null;
                data = null;
                valor = null;
                continue;
            }

            if (!lendo) continue;

            // Tipo da despesa
            if (linha.startsWith("Tipo:")) {
                try {
                    tipo = Integer.parseInt(linha.split(":", 2)[1].trim());
                } catch (Exception ignored) {
                    tipo = null;
                }
            }

            // Data
            if (linha.startsWith("Data:")) {
                data = linha.split(":", 2)[1].trim();
            }

            // Valor e fechamento do bloco
            if (linha.startsWith("Valor:")) {

                try {
                    valor = Double.parseDouble(linha.split(":", 2)[1].trim());
                } catch (Exception ignored) {
                    valor = null;
                }

                // Tipo 3 = IPVA
                if (tipo != null && tipo == 3 && data != null && valor != null) {

                    String[] partes = data.split("/");
                    if (partes.length == 3) {
                        try {
                            int anoTxt = Integer.parseInt(partes[2]);
                            if (anoTxt == ano) {
                                total += valor;
                            }
                        } catch (Exception ignored) {}
                    }
                }

                lendo = false; // fim do bloco
            }
        }

    } catch (Exception ignored) {}

    return total;
}

    // Lista todas as multas por mês/ano
    public String listarMultasPorMes(int mes, int ano) {

    // Validações simples
    if (mes < 1 || mes > 12) return "Mês inválido.\n";
    if (String.valueOf(ano).length() != 4) return "Ano inválido.\n";

    StringBuilder sb = new StringBuilder("===== MULTAS PAGAS NO MÊS =====\n\n");
    StringBuilder bloco = new StringBuilder();

    Integer tipo = null;
    String data = null;
    boolean lendo = false;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Início de um novo bloco
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

            // Captura o tipo
            if (linha.startsWith("Tipo:")) {
                try {
                    tipo = Integer.parseInt(linha.split(":", 2)[1].trim());
                } catch (NumberFormatException ignored) {
                    tipo = null;
                }
            }

            // Captura a data
            if (linha.startsWith("Data:")) {
                data = linha.split(":", 2)[1].trim();
            }

            // Final do bloco (linha com Valor)
            if (linha.startsWith("Valor:")) {

                if (tipo != null && tipo == 4 && data != null) { // tipo 4 = multa
                    try {
                        String[] partes = data.split("/");
                        int mesTxt = Integer.parseInt(partes[1]);
                        int anoTxt = Integer.parseInt(partes[2]);

                        if (mesTxt == mes && anoTxt == ano) {
                            sb.append(bloco).append("\n");
                        }

                    } catch (Exception ignored) {
                        // Evita erro se a data estiver mal formatada
                    }
                }

                lendo = false; // fim do bloco
            }
        }

    } catch (Exception e) {
        return "Erro ao ler Movimentacoes.txt\n";
    }

    return sb.toString();
}
    // Lista despesas de combustível
    public String listarDespesasCombustivelPorMes(int mes, int ano) {

    StringBuilder sb = new StringBuilder("===== DESPESAS DE COMBUSTÍVEL =====\n\n");
    StringBuilder bloco = new StringBuilder();

    Integer tipo = null;
    String data = null;
    boolean lendo = false;

    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Início de um novo bloco
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

            // Tipo da despesa
            if (linha.startsWith("Tipo:")) {
                try {
                    tipo = Integer.parseInt(linha.split(":", 2)[1].trim());
                } catch (Exception ignored) {
                    tipo = null;
                }
            }

            // Data
            if (linha.startsWith("Data:")) {
                data = linha.split(":", 2)[1].trim();
            }

            // Final do bloco (linha com Valor)
            if (linha.startsWith("Valor:")) {

                // Tipo 1 = combustível
                if (tipo != null && tipo == 1 && data != null) {
                    try {
                        String[] partes = data.split("/");
                        int mesTxt = Integer.parseInt(partes[1]);
                        int anoTxt = Integer.parseInt(partes[2]);

                        if (mesTxt == mes && anoTxt == ano) {
                            sb.append(bloco).append("\n");
                        }

                    } catch (Exception ignored) {
                        // evita erro de data mal formatada
                    }
                }

                lendo = false; // fim do bloco
            }
        }

    } catch (Exception e) {
        return "Erro ao ler Movimentacoes.txt\n";
    }

    return sb.toString();
}

    // Lista despesas por ID do veículo
    public String listarDespesasPorVeiculo(int idVeiculo) {

    StringBuilder sb = new StringBuilder("===== DADOS DO VEÍCULO =====\n");

    // --- BUSCA BLOCO DO VEÍCULO ---
    try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {

        String linha;
        boolean blocoEncontrado = false;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            if (linha.equals("ID: " + idVeiculo)) {
                blocoEncontrado = true;
                sb.append(linha).append("\n");
                continue;
            }

            if (blocoEncontrado) {
                if (linha.startsWith("========================================"))
                    break;
                sb.append(linha).append("\n");
            }
        }

        if (!blocoEncontrado)
            return "Veículo NÃO encontrado.\n";

    } catch (Exception e) {
        return "Erro ao ler BancodeDadosVeiculos.txt\n";
    }

    sb.append("\n===== DESPESAS DO VEÍCULO =====\n");

    // --- BUSCA DESPESAS DO VEÍCULO ---
    try (BufferedReader br = new BufferedReader(new FileReader("Movimentacoes.txt"))) {

        String linha;
        boolean lendo = false;
        int idAtual = -1;
        StringBuilder bloco = new StringBuilder();

        while ((linha = br.readLine()) != null) {
            linha = linha.trim();

            // Detecta início do bloco
            if (linha.startsWith("ID:")) {
                try {
                    idAtual = Integer.parseInt(linha.split(":", 2)[1].trim());
                } catch (Exception ignored) {
                    idAtual = -1;
                }
                bloco = new StringBuilder();
                lendo = true;
                continue;
            }

            if (!lendo) continue;

            // Adiciona apenas linhas da despesa: do "Despesa:" até "Valor:"
            if (linha.startsWith("Despesa:") ||
                linha.startsWith("Descrição:") || linha.startsWith("Data:") ||
                linha.startsWith("Valor:")) {
                bloco.append(linha).append("\n");
            }

            if (linha.startsWith("Valor:")) {
                if (idAtual == idVeiculo) {
                    sb.append(bloco).append("\n");
                }
                lendo = false; // encerra bloco
            }
        }

    } catch (Exception e) {
        return "Erro ao ler Movimentacoes.txt\n";
    }

    return sb.toString();
}
    // Retorna todas as movimentações como objetos
    public List<Movimentacao> listarTodos() {

    List<Movimentacao> lista = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO))) {

        String linha;
        Movimentacao mov = null;

        while ((linha = br.readLine()) != null) {

            if (linha.startsWith("ID:")) {
                // Se já tinha uma mov completa antes, adiciona
                if (mov != null) lista.add(mov);

                mov = new Movimentacao();
                mov.setId(Integer.parseInt(linha.split(":", 2)[1].trim()));

            } else if (mov != null) {

                if (linha.startsWith("ID Veículo:")) {
                    mov.setIdVeiculo(Integer.parseInt(linha.split(":", 2)[1].trim()));

                } else if (linha.startsWith("ID Tipo Despesa:")) {
                    mov.setIdTipoDespesa(Integer.parseInt(linha.split(":", 2)[1].trim()));

                } else if (linha.startsWith("Descrição:")) {
                    mov.setDescricao(linha.split(":", 2)[1].trim());

                } else if (linha.startsWith("Data:")) {
                    mov.setData(linha.split(":", 2)[1].trim());

                } else if (linha.startsWith("Valor:")) {
                    mov.setValor(Double.parseDouble(linha.split(":", 2)[1].trim()));

                } else if (linha.startsWith("========================================")) {
                    // Fim de bloco → adiciona a movimentação
                    lista.add(mov);
                    mov = null; // Reinicia
                }
            }
        }

        // Se o arquivo não terminar com ===, adiciona a última movimentação
        if (mov != null) lista.add(mov);

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Erro ao ler arquivo Movimentacoes: " + e.getMessage());
    }

    return lista;
    }
}