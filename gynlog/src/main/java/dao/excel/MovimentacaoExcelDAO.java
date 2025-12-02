package dao.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Movimentacao;

public class MovimentacaoExcelDAO {

    private static final String CAMINHO =
        //tem que alterar o nome do user
        "C:\\Users\\Davi\\Documents\\NetBeansProjects\\gynlog\\bancoVeiculos.xlsx";

    public void salvarMovExcel(Movimentacao mov) {

        try {
            Workbook workbook;
            Sheet sheet;

            File arquivo = new File(CAMINHO);

            // abre ou cria arquivo
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            // procura aba de despesas
            sheet = workbook.getSheet("Despesas");

            // se não existir → cria e adiciona cabeçalho
            if (sheet == null) {
                sheet = workbook.createSheet("Despesas");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("ID Veículo");
                header.createCell(2).setCellValue("Tipo");
                header.createCell(3).setCellValue("Descrição");
                header.createCell(4).setCellValue("Data");
                header.createCell(5).setCellValue("Valor");
            }

            // próximo ID = última linha + 1
            int rowNum = sheet.getLastRowNum() + 1;
            int idDespesa = rowNum; // simples e funcional

            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(idDespesa);
            row.createCell(1).setCellValue(mov.getIdVeiculo());
            row.createCell(2).setCellValue(mov.getIdTipoDespesa());
            row.createCell(3).setCellValue(mov.getDescricao());
            row.createCell(4).setCellValue(mov.getData());
            row.createCell(5).setCellValue(mov.getValor());

            // salva tudo
            FileOutputStream fos = new FileOutputStream(CAMINHO);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("Despesa salva na aba 'Despesas' com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
