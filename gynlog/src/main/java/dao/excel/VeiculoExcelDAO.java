package dao.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    // Salva novo veículo ou atualiza existente
    public void salvarEmExcel(Veiculo v) {

        File arquivo = new File(CAMINHO);
        File pasta = arquivo.getParentFile();

        // Cria diretórios se não existirem
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        try (Workbook workbook = arquivo.exists()
                ? new XSSFWorkbook(new FileInputStream(arquivo))
                : new XSSFWorkbook()) {

            Sheet sheet = workbook.getSheet("Veiculos");

            // Cria aba e cabeçalho se não existir
            if (sheet == null) {
                sheet = workbook.createSheet("Veiculos");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Placa");
                header.createCell(2).setCellValue("Modelo");
                header.createCell(3).setCellValue("Fabricante");
                header.createCell(4).setCellValue("TipoVeiculo");
                header.createCell(5).setCellValue("Ano");
                header.createCell(6).setCellValue("Status");
            }

            boolean atualizado = false;

            // Procura se o veículo já existe
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // pula header
                Cell cellId = row.getCell(0);
                if (cellId != null && (int) cellId.getNumericCellValue() == v.getId()) {
                    // Atualiza dados do veículo
                    row.getCell(1).setCellValue(v.getPlaca());
                    row.getCell(2).setCellValue(v.getModelo());
                    row.getCell(3).setCellValue(v.getTipo());
                    row.getCell(4).setCellValue(v.getTipoVeiculo());
                    row.getCell(5).setCellValue(v.getAno());
                    row.getCell(6).setCellValue(v.getStatus());
                    atualizado = true;
                    break;
                }
            }

            // Se não encontrou, adiciona novo veículo
            if (!atualizado) {
                int rowNum = sheet.getPhysicalNumberOfRows();
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(v.getId());
                row.createCell(1).setCellValue(v.getPlaca());
                row.createCell(2).setCellValue(v.getModelo());
                row.createCell(3).setCellValue(v.getTipo());
                row.createCell(4).setCellValue(v.getTipoVeiculo());
                row.createCell(5).setCellValue(v.getAno());
                row.createCell(6).setCellValue(v.getStatus());
            }

            // Salva arquivo
            try (FileOutputStream fos = new FileOutputStream(CAMINHO)) {
                workbook.write(fos);
            }

            System.out.println(atualizado ? "Veículo atualizado no Excel!" : "Novo veículo salvo no Excel!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Atualiza somente o status de um veículo existente
    public void atualizarStatusExcel(int idVeiculo, String novoStatus) {
    File arquivo = new File(CAMINHO);

    if (!arquivo.exists()) {
        System.out.println("Arquivo Excel não encontrado: " + CAMINHO);
        return;
    }

    try (Workbook workbook = new XSSFWorkbook(new FileInputStream(arquivo))) {

        Sheet sheet = workbook.getSheet("Veiculos");
        if (sheet == null) {
            System.out.println("Aba 'Veiculos' não encontrada.");
            return;
        }

        boolean encontrado = false;

        // Percorre todas as linhas da planilha, ignorando o cabeçalho (linha 0)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell cellId = row.getCell(0); // Coluna do ID
            if (cellId == null) continue;

            // Verifica se o ID bate
            if ((int) cellId.getNumericCellValue() == idVeiculo) {
                Cell cellStatus = row.getCell(6); // Coluna Status
                if (cellStatus == null) {
                    cellStatus = row.createCell(6);
                }
                cellStatus.setCellValue(novoStatus);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            try (FileOutputStream fos = new FileOutputStream(CAMINHO)) {
                workbook.write(fos);
            }
            System.out.println("Status atualizado no Excel com sucesso!");
        } else {
            System.out.println("ID do veículo não encontrado na planilha.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Erro ao atualizar status no Excel!");
    }
}


}