package listener.modelagem.cadastros;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TipoPermissaoListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final JComboBox<String> tipoPermissao;
    private final JTextField permitidosFiltro;
    private final JComboBox<String> permitidos;

    public TipoPermissaoListener(JComboBox<String> tipoPermissao, JTextField permitidosFiltro){
        this.tipoPermissao = tipoPermissao;
        this.permitidosFiltro = permitidosFiltro;
        this.permitidos = null;
    }

    public TipoPermissaoListener(JComboBox<String> tipoPermissao, JComboBox<String> permitidos) {
        this.tipoPermissao = tipoPermissao;
        this.permitidosFiltro = null;
        this.permitidos = permitidos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (permitidos == null) {
                if (tipoPermissao.getSelectedItem().equals(TipoPermissaoEnum.PADRAO.getDescricao())) {
                    permitidosFiltro.setEnabled(false);
                    permitidosFiltro.setText("");
                } else {
                    permitidosFiltro.setEnabled(true);
                }
            } else if (permitidosFiltro == null) {
                carregarPermitidos();
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPermitidos() throws DaoException {
        List<Map<String, Object>> permitidosList = new ArrayList<>();
        permitidos.removeAllItems();
        permitidos.addItem(OpcaoComboEnum.PERMITIDOS.getDescricao());
        permitidos.setSelectedItem(OpcaoComboEnum.PERMITIDOS.getDescricao());

        if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.USUARIO.getDescricao())) {
            permitidosList = daoUtil.select("SELECT login AS permitido FROM USUARIOS ORDER BY login", Collections.singletonList("permitido"));
        }

        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            permitidosList = daoUtil.select("SELECT nome AS permitido FROM DEPARTAMENTOS ORDER BY nome", Collections.singletonList("permitido"));
        }

        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GRUPO.getDescricao())) {
            permitidosList = daoUtil.select("SELECT nome AS permitido FROM GRUPOS ORDER BY nome", Collections.singletonList("permitido"));
        }

        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao())) {
            permitidosList = daoUtil.select("SELECT d.nome AS permitido, u.login AS complemento FROM DEPARTAMENTOS d LEFT JOIN USUARIOS u ON u.departamento = d.id WHERE u.gerente = true ORDER BY d.nome", Arrays.asList("permitido", "complemento"));
        }

        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())) {
                permitidosList = daoUtil.select("SELECT g.nome AS permitido, u.login AS complemento FROM grupos g LEFT JOIN USUARIOS u ON u.id = g.lider ORDER BY g.nome", Arrays.asList("permitido", "complemento"));
        }

        if(!permitidosList.isEmpty()){
            String complemento = "";
            for (Map<String, Object> permitidosMap: permitidosList){
                if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao()) || tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())){
                    complemento = " - ";
                    try {
                        complemento = complemento + (permitidosMap.get("complemento").toString());
                    }
                    catch (NullPointerException ignored){

                    }
                }
                permitidos.addItem(permitidosMap.get("permitido").toString() + complemento);
            }
        }
    }
}
