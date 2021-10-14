package listener.administrador.usuarios;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import view.administrador.usuarios.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RetirarUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer idUsuario;
    private final JComboBox<String> gruposInclusos;

    public RetirarUsuarioListener(AdicionarConsultar janela, Integer idUsuario) {
        this.janela = janela;
        this.idUsuario = idUsuario;
        this.gruposInclusos = janela.getGruposIncluidos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(gruposInclusos.getSelectedItem().equals(OpcaoComboEnum.GRUPO.getDescricao())){
                JOptionPane.showMessageDialog(null, "Necessário escolher um grupo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                List<Map<String, Object>> grupo = daoUtil.select(String.format("SELECT id, lider FROM GRUPOS WHERE nome = '%s'", gruposInclusos. getSelectedItem()), Arrays.asList("id", "lider"));
                Integer idGrupo = Integer.parseInt(grupo.get(0).get("id").toString());
                String lider = String.valueOf(grupo.get(0).get("lider"));
                daoUtil.delete(String.format("DELETE FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", idUsuario, idGrupo));

                if (lider.equals(String.valueOf(idUsuario))) {
                    daoUtil.update(String.format("UPDATE GRUPOS SET lider = null WHERE id = %d", idGrupo));
                }

                janela.carregarInfosUsuario(idUsuario);
                JOptionPane.showMessageDialog(null, "Usuário retirado do grupo", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
