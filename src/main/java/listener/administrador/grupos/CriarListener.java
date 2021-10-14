package listener.administrador.grupos;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.validacoes.GrupoUtil;
import exception.DaoException;
import exception.validacoes.GrupoException;
import main.Main;
import view.administrador.grupos.AdicionarConsultar;
import view.administrador.grupos.Grupos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CriarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final GrupoUtil grupoUtil = new GrupoUtil();

    private final AdicionarConsultar janela;
    private final JTextField nome;
    private final JComboBox<String> lider;

    public CriarListener(AdicionarConsultar janela) {
        this.janela = janela;
        this.nome = janela.getNome();
        this.lider = janela.getUsuariosLider();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            Integer liderId = null;
            grupoUtil.validarGrupo(null, nome);
            if(lider.getSelectedItem() != null && !lider.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.SEM_LIDER.getDescricao())) {
                List<Map<String, Object>> liderIdList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", lider.getSelectedItem()), Collections.singletonList("id"));
                liderId = Integer.parseInt(liderIdList.get(0).get("id").toString());
            }
            daoUtil.insert(String.format("INSERT INTO GRUPOS (nome, lider) VALUES ('%s', %d)", nome.getText(), liderId));

            if(liderId != null) {
                List<Map<String, Object>> grupoIdList = daoUtil.select("SELECT MAX(id) AS idgrupo FROM GRUPOS", Collections.singletonList("idgrupo"));
                Integer grupoId = Integer.parseInt(grupoIdList.get(0).get("idgrupo").toString());

                daoUtil.insert(String.format("INSERT INTO USUARIOSGRUPOS (idusuario, idgrupo) VALUES (%d, %d)", liderId, grupoId));
            }

            janela.dispose();
            Main.getJanelas().add(new Grupos());
            JOptionPane.showMessageDialog(null, "Grupo adicionado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (GrupoException | DaoException exception){
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
