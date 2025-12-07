package dao.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Movimentacao;

public class MovimentacaoExcelDAO {

    private static final String CAMINHO =
            System.getProperty("user.home") + File.separator +
            "Documents" + File.separator +
            "NetBeansProjects" + File.separator +
            "gynlog" + File.separator +
            "bancoVeiculos.xlsx";

    public void salvarMovExcel(Movimentacao mov) {

        File arquivo = new File(CAMINHO);

        try (Workbook workbook = arquivo.exists()
                ? new XSSFWorkbook(new FileInputStream(arquivo))
                : new XSSFWorkbook()) {

            Sheet sheet = workbook.getSheet("Despesas");

            // Cria aba "Despesas" com cabeçalho se não existir
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

            // Número de linhas reais, ignorando possíveis buracos
            int rowNum = sheet.getPhysicalNumberOfRows();

            // ID da despesa baseado na linha (linha 0 é o header)
            int idDespesa = rowNum;

            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(idDespesa);
            row.createCell(1).setCellValue(mov.getIdVeiculo());
            row.createCell(2).setCellValue(mov.getIdTipoDespesa());
            row.createCell(3).setCellValue(mov.getDescricao());
            row.createCell(4).setCellValue(mov.getData());
            row.createCell(5).setCellValue(mov.getValor());

            // Salvar arquivo
            try (FileOutputStream fos = new FileOutputStream(CAMINHO)) {
                workbook.write(fos);
            }

            System.out.println("Despesa salva na aba 'Despesas' com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}