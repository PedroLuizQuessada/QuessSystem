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

public class SalvarLiderListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer id;
    private final JComboBox<String> usuariosLider;

    public SalvarLiderListener(AdicionarConsultar janela, Integer id, JComboBox<String> usuariosLider) {
        this.janela = janela;
        this.id = id;
        this.usuariosLider = usuariosLider;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Integer usuarioId = null;

            if(!usuariosLider.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.SEM_LIDER.getDescricao())) {
                List<Map<String, Object>> usuarioIdList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", usuariosLider.getSelectedItem()), Collections.singletonList("id"));
                usuarioId = Integer.parseInt(usuarioIdList.get(0).get("id").toString());
            }

            daoUtil.update(String.format("UPDATE GRUPOS SET lider = %d WHERE id = %d", usuarioId, id));

            if(usuarioId != null) {
                List<Map<String, Object>> usuarioParticipa = daoUtil.select(String.format("SELECT COUNT(1) AS participa FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", usuarioId, id), Collections.singletonList("participa"));
                if(Integer.parseInt(usuarioParticipa.get(0).get("participa").toString()) == 0){
                    daoUtil.insert(String.format("INSERT INTO USUARIOSGRUPOS (idusuario, idgrupo) VALUES (%d, %d)", usuarioId, id));
                }
            }

            janela.carregarInfosGrupo(id);
            JOptionPane.showMessageDialog(null, "Líder atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
