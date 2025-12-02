package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.TipoDespesa;

public class TipoDespesaDAO {

    private static final String ARQUIVO = "TipoDespesaBD.txt";

    // Salvar novo tipo
    public void save(TipoDespesa t) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {

            bw.write("ID:" + t.getId());
            bw.newLine();
            bw.write("Descricao:" + t.getDescricao());
            bw.newLine();
            bw.write("----");
            bw.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gerar próximo ID
    public int gerarNovoId() {
        int maior = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    int id = Integer.parseInt(linha.split(":")[1].trim());
                    if (id > maior) maior = id;
                }
            }

        } catch (Exception e) {
            // arquivo ainda não existe
        }

        return maior + 1;
    }

    // Listar todos
    public List<TipoDespesa> listarTodos() {

        List<TipoDespesa> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String linha;
            TipoDespesa t = null;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("ID:")) {
                    t = new TipoDespesa();
                    t.setId(Integer.parseInt(linha.split(":")[1].trim()));
                }

                if (linha.startsWith("Descricao:") && t != null) {
                    t.setDescricao(linha.split(":")[1].trim());
                }

                if (linha.startsWith("----") && t != null) {
                    lista.add(t);
                    t = null;
                }
            }

        } catch (Exception e) {
            // arquivo não encontrado
        }

        return lista;
    }
}