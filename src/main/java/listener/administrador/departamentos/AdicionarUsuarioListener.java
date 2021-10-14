package listener.administrador.departamentos;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import view.administrador.departamentos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer idDepartamento;
    private final JComboBox<String> usuariosExclusos;

    public AdicionarUsuarioListener(AdicionarConsultar janela, Integer idDepartamento) {
        this.janela = janela;
        this.idDepartamento = idDepartamento;
        this.usuariosExclusos = janela.getUsuariosExclusos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = (String) usuariosExclusos.getSelectedItem();
            if(login != null && login.equals(OpcaoComboEnum.USUARIO.getDescricao())){
                JOptionPane.showMessageDialog(null, "Necessário escolher um usuário", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                daoUtil.update(String.format("UPDATE USUARIOS SET DEPARTAMENTO = %d WHERE login = '%s'", idDepartamento, login));

                janela.carregarInfosDepartamento(idDepartamento);
                JOptionPane.showMessageDialog(null, "Usuário adicionado ao departamento", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
