package com.mycompany.teste_excel;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CriarExcel {

    private static final String CAMINHO = "dados.xlsx";

    public static void adicionarLinha(int id, String nome, int idade) {
        Workbook workbook;
        Sheet sheet;

        File arquivo = new File(CAMINHO);

        try {
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Dados");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Nome");
                header.createCell(2).setCellValue("Idade");
            }

            int ultimaLinha = sheet.getLastRowNum() + 1;

            Row novaLinha = sheet.createRow(ultimaLinha);
            novaLinha.createCell(0).setCellValue(id);
            novaLinha.createCell(1).setCellValue(nome);
            novaLinha.createCell(2).setCellValue(idade);

            FileOutputStream fos = new FileOutputStream(CAMINHO);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
