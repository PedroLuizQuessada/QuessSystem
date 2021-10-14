package listener.administrador.usuarios;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import view.administrador.usuarios.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IncluirUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer idUsuario;
    private final JComboBox<String> gruposExclusos;

    public IncluirUsuarioListener(AdicionarConsultar janela, Integer idUsuario) {
        this.janela = janela;
        this.idUsuario = idUsuario;
        this.gruposExclusos = janela.getGruposExcluidos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(gruposExclusos.getSelectedItem().equals(OpcaoComboEnum.GRUPO.getDescricao())){
                JOptionPane.showMessageDialog(null, "Necessário escolher um grupo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                List<Map<String, Object>> idGrupoList = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE nome = '%s'", gruposExclusos.getSelectedItem()), Collections.singletonList("id"));
                Integer idGrupo = Integer.parseInt(idGrupoList.get(0).get("id").toString());

                daoUtil.insert(String.format("INSERT INTO USUARIOSGRUPOS (idusuario, idgrupo) VALUES (%d, %d)", idUsuario, idGrupo));

                janela.carregarInfosUsuario(idUsuario);
                JOptionPane.showMessageDialog(null, "Usuário incluído no grupo", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
