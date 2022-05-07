package listener.administrador.automacoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiasExecucaoListener implements ActionListener {
    private final JCheckBox diasExecucao;
    private final JLabel segundaLabel;
    private final JLabel tercaLabel;
    private final JLabel quartaLabel;
    private final JLabel quintaLabel;
    private final JLabel sextaLabel;
    private final JLabel sabadoLabel;
    private final JLabel domingoLabel;
    private final JCheckBox segunda;
    private final JCheckBox terca;
    private final JCheckBox quarta;
    private final JCheckBox quinta;
    private final JCheckBox sexta;
    private final JCheckBox sabado;
    private final JCheckBox domingo;

    public DiasExecucaoListener(JLabel segundaLabel, JLabel tercaLabel, JLabel quartaLabel, JLabel quintaLabel, JLabel sextaLabel, JLabel sabadoLabel, JLabel domingoLabel, JCheckBox diasExecucao, JCheckBox segunda, JCheckBox terca, JCheckBox quarta, JCheckBox quinta, JCheckBox sexta, JCheckBox sabado, JCheckBox domingo) {
        this.diasExecucao = diasExecucao;
        this.segundaLabel = segundaLabel;
        this.tercaLabel = tercaLabel;
        this.quartaLabel = quartaLabel;
        this.quintaLabel = quintaLabel;
        this.sextaLabel = sextaLabel;
        this.sabadoLabel = sabadoLabel;
        this.domingoLabel = domingoLabel;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
        this.sabado = sabado;
        this.domingo = domingo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (diasExecucao.isSelected()) {
            segundaLabel.setVisible(true);
            tercaLabel.setVisible(true);
            quartaLabel.setVisible(true);
            quintaLabel.setVisible(true);
            sextaLabel.setVisible(true);
            sabadoLabel.setVisible(true);
            domingoLabel.setVisible(true);
            segunda.setVisible(true);
            terca.setVisible(true);
            quarta.setVisible(true);
            quinta.setVisible(true);
            sexta.setVisible(true);
            sabado.setVisible(true);
            domingo.setVisible(true);
        }
        else {
            segundaLabel.setVisible(false);
            tercaLabel.setVisible(false);
            quartaLabel.setVisible(false);
            quintaLabel.setVisible(false);
            sextaLabel.setVisible(false);
            sabadoLabel.setVisible(false);
            domingoLabel.setVisible(false);
            segunda.setVisible(false);
            terca.setVisible(false);
            quarta.setVisible(false);
            quinta.setVisible(false);
            sexta.setVisible(false);
            sabado.setVisible(false);
            domingo.setVisible(false);
        }
    }
}
