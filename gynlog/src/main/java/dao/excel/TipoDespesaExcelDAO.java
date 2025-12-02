package dao.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import model.TipoDespesa;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TipoDespesaExcelDAO {

    private static final String CAMINHO =
        System.getProperty("user.home") + File.separator +
        "Documents" + File.separator +
        "bancoVeiculos.xlsx";

    public void salvarTipoExcel(TipoDespesa t) {

        try {
            Workbook workbook;
            Sheet sheet;

            File arquivo = new File(CAMINHO);

            // Abre ou cria o arquivo
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            // Procurar aba
            sheet = workbook.getSheet("TiposDespesa");

            // Criar se não existir
            if (sheet == null) {
                sheet = workbook.createSheet("TiposDespesa");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Descrição");
            }

            // ID automático → número da última linha + 1
            int rowNum = sheet.getLastRowNum() + 1;
            int novoId = rowNum; // simples e funcional

            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(novoId);
            row.createCell(1).setCellValue(t.getDescricao());

            // salva
            FileOutputStream fos = new FileOutputStream(CAMINHO);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("Tipo de despesa salvo no Excel com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}