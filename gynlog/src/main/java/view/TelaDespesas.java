package view;

import dao.TipoDespesaDAO;
import dao.MovimentacaoDAO;
import model.Movimentacao;
import model.TipoDespesa;
import model.Veiculo;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;

public class TelaDespesas extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(TelaDespesas.class.getName());

    public TelaDespesas() {
        initComponents();
        carregarTiposFixos(); // substitui o modelo gerado pela IDE
    }

    // Carrega tipos fixos no combo
    private void carregarTiposFixos() {

        // Criando um model do tipo correto (TipoDespesa)
        javax.swing.DefaultComboBoxModel<TipoDespesa> modelo =
                new javax.swing.DefaultComboBoxModel<>();

        modelo.addElement(new TipoDespesa(1, "Combustível"));
        modelo.addElement(new TipoDespesa(2, "Manutenção"));
        modelo.addElement(new TipoDespesa(3, "IPVA"));
        modelo.addElement(new TipoDespesa(4, "Multa"));

        comboTipo.setModel(modelo); // Substitui o model errado gerado pelo NetBeans
    }
    
    // Lê o bloco completo do veículo no TXT
    private String lerBlocoVeiculo(int idProcurado) {

    StringBuilder bloco = new StringBuilder();
    boolean lendo = false;

    try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {

        String linha;

        while ((linha = br.readLine()) != null) {

            // início de bloco
            if (linha.startsWith("ID:")) {
                int id = Integer.parseInt(linha.split(":", 2)[1].trim());
                if (id == idProcurado) {
                    lendo = true;     // começa a copiar
                }
            }

            if (lendo) {
                bloco.append(linha).append("\n");

                // linha vazia = fim do bloco
                if (linha.trim().isEmpty()) {
                    break;  
                }
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao ler veículo: " + e.getMessage());
    }

    return bloco.toString().trim().isEmpty() ? null : bloco.toString();
}

    // Busca um veículo específico no TXT
    private Veiculo buscarVeiculoPorId(int idProcurado) {

    Veiculo v = null;

    try (BufferedReader br = new BufferedReader(new FileReader("BancodeDadosVeiculos.txt"))) {

        String linha;

        while ((linha = br.readLine()) != null) {

            linha = linha.trim();

            // Início do bloco do veículo
            if (linha.startsWith("ID:")) {
                v = new Veiculo();
                v.setId(Integer.parseInt(linha.split(":", 2)[1].trim()));
                continue;
            }

            if (v != null) {

                if (linha.startsWith("Placa:")) {
                    v.setPlaca(linha.split(":", 2)[1].trim());
                    continue;
                }

                if (linha.startsWith("Modelo:")) {
                    v.setModelo(linha.split(":", 2)[1].trim());
                    continue;
                }

                if (linha.startsWith("Ano:")) {
                    v.setAno(Integer.parseInt(linha.split(":", 2)[1].trim()));
                    continue;
                }

                if (linha.startsWith("Status:")) {
                    v.setStatus(linha.split(":", 2)[1].trim());
                    continue;
                }
            }

            // Linha vazia = fim do bloco
            if (linha.isEmpty() && v != null) {

                if (v.getId() == idProcurado) {
                    return v;  // achou!
                }

                v = null; // se não for o ID, descarta e continua
            }
        }

        // Caso o último bloco não tenha linha vazia
        if (v != null && v.getId() == idProcurado) {
            return v;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao ler veículos: " + e.getMessage());
    }

    return null;
}
    // Valida data no formato dd/MM/yyyy
    private boolean dataValida(String data) {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        sdf.parse(data);
        return true;
    } catch (Exception e) {
        return false;
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        txtIdVeiculo = new javax.swing.JTextField();
        comboTipo = new javax.swing.JComboBox();
        txtDescricao = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID do veículo");

        jLabel2.setText("Tipo de Despesa");

        jLabel3.setText("Descrição");

        jLabel4.setText("Data");

        jLabel5.setText("Valor");

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Combustível", "Manutenção", "IPVA", "Multa" }));

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addGap(63, 63, 63)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtIdVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addContainerGap(370, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVoltar)
                        .addGap(110, 110, 110))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comboTipo, txtData, txtDescricao, txtIdVeiculo, txtValor});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnVoltar)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtIdVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(142, 142, 142)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
    // ID obrigatório
    String idTexto = txtIdVeiculo.getText().trim();
    if (idTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "O ID do veículo não pode estar vazio.");
        return;
    }

    // ID numérico
    int idVeic;
    try {
        idVeic = Integer.parseInt(idTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "O ID do veículo deve conter apenas números!");
        return;
    }

    // Buscar veículo no TXT
    String bloco = lerBlocoVeiculo(idVeic);
    if (bloco == null) {
        JOptionPane.showMessageDialog(this, "Veículo não encontrado no arquivo!");
        return;
    }

    TipoDespesa tipo = (TipoDespesa) comboTipo.getSelectedItem();

    // Validar data
    String dataTexto = txtData.getText().trim();
    if (dataTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "A data não pode estar vazia!");
        return;
    }
    if (!dataValida(dataTexto)) {
        JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy.");
        return;
    }

    // Validar valor
    String valorTexto = txtValor.getText().trim();
    if (valorTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "O valor não pode estar vazio!");
        return;
    }

    double valor;
    try {
        valor = Double.parseDouble(valorTexto.replace(",", "."));
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "O valor deve conter somente números!");
        return;
    }

    // Criar objeto Movimentacao
    Movimentacao mov = new Movimentacao();
    mov.setIdVeiculo(idVeic);
    mov.setIdTipoDespesa(tipo.getId());
    mov.setDescricao(txtDescricao.getText());
    mov.setData(dataTexto);
    mov.setValor(valor);

    // SALVAR NO TXT
    MovimentacaoDAO dao = new MovimentacaoDAO();
    dao.salvarMovimentacaoCompleta(bloco, mov);

    // SALVAR NO EXCEL (pacote dao.excel)
    dao.excel.MovimentacaoExcelDAO excelDAO = new dao.excel.MovimentacaoExcelDAO();
    excelDAO.salvarMovExcel(mov);

    JOptionPane.showMessageDialog(this, "Despesa salva com sucesso!");

} catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage());
}


    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
        this.dispose();
        
        
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new TelaDespesas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox comboTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtIdVeiculo;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
