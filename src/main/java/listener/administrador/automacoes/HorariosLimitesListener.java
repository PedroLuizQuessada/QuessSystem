package listener.administrador.automacoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HorariosLimitesListener implements ActionListener {
    private final JCheckBox horarioLimites;
    private final JLabel horarioLimiteInicioLabel;
    private final JTextField horarioLimiteInicio;
    private final JLabel horarioLimiteFimLabel;
    private final JTextField horarioLimiteFim;

    public HorariosLimitesListener(JCheckBox horarioLimites, JLabel horarioLimiteInicioLabel, JTextField horarioLimiteInicio, JLabel horarioLimiteFimLabel, JTextField horarioLimiteFim) {
        this.horarioLimites = horarioLimites;
        this.horarioLimiteInicioLabel = horarioLimiteInicioLabel;
        this.horarioLimiteInicio = horarioLimiteInicio;
        this.horarioLimiteFimLabel = horarioLimiteFimLabel;
        this.horarioLimiteFim = horarioLimiteFim;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (horarioLimites.isSelected()) {
            horarioLimiteInicioLabel.setVisible(true);
            horarioLimiteInicio.setVisible(true);
            horarioLimiteFimLabel.setVisible(true);
            horarioLimiteFim.setVisible(true);
        }
        else {
            horarioLimiteInicioLabel.setVisible(false);
            horarioLimiteInicio.setVisible(false);
            horarioLimiteFimLabel.setVisible(false);
            horarioLimiteFim.setVisible(false);
        }
    }
}
