package controle;

import controle.enums.OpcaoComboEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PermissaoUtil implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private JComboBox<String> tipoPermissao;
    private JTextField permitidosFiltro;
    private JComboBox<String> permitidos;

    public PermissaoUtil() {
    }

    public PermissaoUtil(JComboBox<String> tipoPermissao, JTextField permitidosFiltro){
        this.tipoPermissao = tipoPermissao;
        this.permitidosFiltro = permitidosFiltro;
        this.permitidos = null;
    }

    public PermissaoUtil(JComboBox<String> tipoPermissao, JComboBox<String> permitidos) {
        this.tipoPermissao = tipoPermissao;
        this.permitidosFiltro = null;
        this.permitidos = permitidos;
    }

    public Map<Integer, JMenuItem> carregarItens(String tabela){
        Map<Integer, JMenuItem> itens = new HashMap<>();

        try {
            List<Map<String, Object>> cadastros = daoUtil.select(String.format("SELECT id, nome, tipopermissao, permitidos FROM %s WHERE tipopermissao != '%s'", tabela, TipoPermissaoEnum.PADRAO.getDescricao()), Arrays.asList("id", "nome", "tipopermissao", "permitidos"));

            for (Map<String, Object> cadastro : cadastros) {
                if(cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.USUARIO.getDescricao())){
                    if(Main.getUsuarioLogado().getId() == Integer.parseInt(cadastro.get("permitidos").toString())){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itens.put(Integer.parseInt(cadastro.get("id").toString()), itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())){
                    if(Main.getUsuarioLogado().getDepartamento() == Integer.parseInt(cadastro.get("permitidos").toString())){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itens.put(Integer.parseInt(cadastro.get("id").toString()), itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.GRUPO.getDescricao())){
                    List<Map<String, Object>> grupo = daoUtil.select(String.format("SELECT id FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", Main.getUsuarioLogado().getId(), Integer.parseInt(cadastro.get("permitidos").toString())), Collections.singletonList("id"));
                    if(!grupo.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itens.put(Integer.parseInt(cadastro.get("id").toString()), itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao())){
                    List<Map<String, Object>> gerente = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE departamento = %d AND gerente = true AND id = %d", Integer.parseInt(cadastro.get("permitidos").toString()), Main.getUsuarioLogado().getId()), Collections.singletonList("id"));
                    if(!gerente.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itens.put(Integer.parseInt(cadastro.get("id").toString()), itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())){
                    List<Map<String, Object>> lider = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE lider = %d AND id = %d", Main.getUsuarioLogado().getId(), Integer.parseInt(cadastro.get("permitidos").toString())), Collections.singletonList("id"));
                    if(!lider.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itens.put(Integer.parseInt(cadastro.get("id").toString()), itemCadastro);
                    }
                }
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return itens;
    }

    public String carregaPermitidosNome(String tipoPermissao, Integer permitidosId) throws DaoException{
        String permitidosNome = "Escolha os permitidos";

        if(tipoPermissao.equals(TipoPermissaoEnum.USUARIO.getDescricao())) {
            List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT login FROM USUARIOS WHERE id = %d", permitidosId), Collections.singletonList("login"));
            if (!usuarioList.isEmpty()) {
                permitidosNome = String.valueOf(usuarioList.get(0).get("login"));
            } else {
                permitidosNome = "Usuário não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            List<Map<String, Object>> departamentoList = daoUtil.select(String.format("SELECT nome FROM DEPARTAMENTOS WHERE id = %d", permitidosId), Collections.singletonList("nome"));
            if (!departamentoList.isEmpty()) {
                permitidosNome = String.valueOf(departamentoList.get(0).get("nome"));
            } else {
                permitidosNome = "Departamento não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.GRUPO.getDescricao())) {
            List<Map<String, Object>> grupoList = daoUtil.select(String.format("SELECT nome FROM GRUPOS WHERE id = %d", permitidosId), Collections.singletonList("nome"));
            if (!grupoList.isEmpty()) {
                permitidosNome = String.valueOf(grupoList.get(0).get("nome"));
            } else {
                permitidosNome = "Grupo não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.GERENTE.getDescricao())) {
            List<Map<String, Object>> gerenteList = daoUtil.select(String.format("SELECT d.nome AS nome, u.login AS login FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON d.id = u.departamento WHERE u.departamento = %d AND u.gerente = true", permitidosId), Arrays.asList("nome", "login"));
            if (!gerenteList.isEmpty()) {
                permitidosNome = gerenteList.get(0).get("nome") + " - " + gerenteList.get(0).get("login");
            } else {
                permitidosNome = "Gerente não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.LIDER.getDescricao())) {
            List<Map<String, Object>> liderList = daoUtil.select(String.format("SELECT g.nome AS nome, u.login AS login FROM GRUPOS g INNER JOIN USUARIOS u ON u.id = g.lider WHERE g.id = %d", permitidosId), Arrays.asList("nome", "login"));
            if(!liderList.isEmpty()){
                permitidosNome = liderList.get(0).get("nome") + " - " + liderList.get(0).get("login");
            }
            else {
                permitidosNome = "Líder não encontrado";
            }
        }

        return permitidosNome;
    }

    public int carregaPermitidosId(String tipoPermissao, String permitidos) throws DaoException{
        int permitidosId = -1;

        if(tipoPermissao.equals(TipoPermissaoEnum.USUARIO.getDescricao())) {
            List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", permitidos), Collections.singletonList("id"));
            if (!usuarioList.isEmpty()) {
                permitidosId = Integer.parseInt(usuarioList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            List<Map<String, Object>> departamentoList = daoUtil.select(String.format("SELECT id FROM DEPARTAMENTOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
            if (!departamentoList.isEmpty()) {
                permitidosId = Integer.parseInt(departamentoList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.GRUPO.getDescricao())) {
            List<Map<String, Object>> grupoList = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
            if (!grupoList.isEmpty()) {
                permitidosId = Integer.parseInt(grupoList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.GERENTE.getDescricao())) {
            if (!permitidos.equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
                permitidos = permitidos.substring(0, permitidos.indexOf(" - "));
                List<Map<String, Object>> gerenteList = daoUtil.select(String.format("SELECT d.id AS id FROM DEPARTAMENTOS d INNER JOIN USUARIOS u ON u.departamento = d.id WHERE d.nome = '%s' AND u.gerente = true", permitidos), Collections.singletonList("id"));
                if (!gerenteList.isEmpty()) {
                    permitidosId = Integer.parseInt(gerenteList.get(0).get("id").toString());
                }
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.LIDER.getDescricao())) {
            if (!permitidos.equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
                permitidos = permitidos.substring(0, permitidos.indexOf(" - "));
                List<Map<String, Object>> liderList = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
                if (!liderList.isEmpty()) {
                    permitidosId = Integer.parseInt(liderList.get(0).get("id").toString());
                }
            }
        }
        if(tipoPermissao.equals(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
            permitidosId = -1;
        }

        return permitidosId;
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
