package listener.cadastro;

import controle.ExcelUtil;
import exception.ConversaoException;
import exception.GerarPlanilhaException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GerarRelatorioListener implements ActionListener, Runnable {
    private final ExcelUtil excelUtil = new ExcelUtil();

    private final List<String> camposPesquisaveisLabel;
    private final List<List<String>> infos;
    private final JButton gerarRelatorio;
    private final List<JComponent> campos;

    public GerarRelatorioListener(List<String> camposPesquisaveisLabel, List<List<String>> infos, JButton gerarRelatorio, List<JComponent> campos) {
        this.camposPesquisaveisLabel = camposPesquisaveisLabel;
        this.infos = infos;
        this.gerarRelatorio = gerarRelatorio;
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gerarRelatorio.setEnabled(false);
        gerarRelatorio.setText("Gerando relatório...");

        for (JComponent campo: campos) {
            campo.setEnabled(false);
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    excelUtil.gerarPlanilha(camposPesquisaveisLabel, infos);
                }
                catch (ConversaoException | GerarPlanilhaException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }

                gerarRelatorio.setEnabled(true);
                gerarRelatorio.setText("Gerar relatório");

                for (JComponent campo: campos) {
                    campo.setEnabled(true);
                }

                JOptionPane.showMessageDialog(null, "Relatório gerado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
