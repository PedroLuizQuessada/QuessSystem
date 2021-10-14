package listener.administrador.grupos;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import view.administrador.grupos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdicionarUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer idGrupo;
    private final JComboBox<String> usuariosExclusos;

    public AdicionarUsuarioListener(AdicionarConsultar janela, Integer idGrupo) {
        this.janela = janela;
        this.idGrupo = idGrupo;
        this.usuariosExclusos = janela.getUsuariosExclusos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = (String) usuariosExclusos.getSelectedItem();
            if (login != null && login.equals(OpcaoComboEnum.USUARIO.getDescricao())) {
                JOptionPane.showMessageDialog(null, "Necessário escolher um usuário", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                List<Map<String, Object>> idUsuarioList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", login), Collections.singletonList("id"));
                Integer idUsuario = Integer.parseInt(idUsuarioList.get(0).get("id").toString());
                daoUtil.insert(String.format("INSERT INTO USUARIOSGRUPOS (idusuario, idgrupo) VALUES(%d, %d)", idUsuario, idGrupo));

                janela.carregarInfosGrupo(idGrupo);
                JOptionPane.showMessageDialog(null, "Usuário adicionado ao grupo", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
