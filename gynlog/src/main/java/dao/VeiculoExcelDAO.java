package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import model.Veiculo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VeiculoExcelDAO {

    private static final String CAMINHO = 
            "C:\\Users\\Davi\\Documents\\NetBeansProjects\\gynlog\\bancoVeiculos.xlsx";

    public void salvarEmExcel(Veiculo v) {

        try {
            Workbook workbook;
            Sheet sheet;

            File arquivo = new File(CAMINHO);

            // Se o arquivo existe → abre
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            }
            // Se não existe → cria
            else {
                workbook = new XSSFWorkbook();
            }

            // Procura planilha
            sheet = workbook.getSheet("Veiculos");

            // Se não existir → cria cabeçalho
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

            // Descobre a próxima linha
            int rowNum = sheet.getLastRowNum() + 1;

            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(v.getId());
            row.createCell(1).setCellValue(v.getPlaca());
            row.createCell(2).setCellValue(v.getModelo());
            row.createCell(3).setCellValue(v.getTipo());
            row.createCell(4).setCellValue(v.getAno());
            row.createCell(5).setCellValue(v.getStatus());

            // Salva arquivo
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