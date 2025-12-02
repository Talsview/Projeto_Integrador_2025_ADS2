package dao.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import model.Veiculo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VeiculoExcelDAO {

    private static final String CAMINHO =
        System.getProperty("user.home") + File.separator +
        "Documents" + File.separator +
        "NetBeansProjects" + File.separator +
        "gynlog" + File.separator +
        "bancoVeiculos.xlsx";

    public void salvarEmExcel(Veiculo v) {

        try {
            Workbook workbook;
            Sheet sheet;

            File arquivo = new File(CAMINHO);

            // abre arquivo ou cria novo
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            // usa planilha ou cria com cabeçalho
            sheet = workbook.getSheet("Veiculos");
            if (sheet == null) {
                sheet = workbook.createSheet("Veiculos");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Placa");
                header.createCell(2).setCellValue("Modelo");
                header.createCell(3).setCellValue("Tipo");
                header.createCell(4).setCellValue("Ano");
                header.createCell(5).setCellValue("Status");
            }

            // próxima linha
            int rowNum = sheet.getLastRowNum() + 1;

            // escreve registro
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(v.getId());
            row.createCell(1).setCellValue(v.getPlaca());
            row.createCell(2).setCellValue(v.getModelo());
            row.createCell(3).setCellValue(v.getTipo());
            row.createCell(4).setCellValue(v.getAno());
            row.createCell(5).setCellValue(v.getStatus());

            // salva
            FileOutputStream fos = new FileOutputStream(CAMINHO);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("Excel salvo com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}