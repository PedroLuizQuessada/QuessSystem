package controle.validacoes;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.validacoes.UsuarioException;
import exception.DaoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UsuarioUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarUsuario(Integer id, JTextField login, JPasswordField senha, JTextField email, JComboBox<String> departamento) throws UsuarioException, DaoException {
        if(login.getText().length() < 5){
            throw new UsuarioException("O login deve ter pelo menos 5 caracteres");
        }

        if(login.getText().equalsIgnoreCase(OpcaoComboEnum.USUARIO.getDescricao()) || login.getText().equalsIgnoreCase(OpcaoComboEnum.SEM_GERENTE.getDescricao()) || login.getText().equalsIgnoreCase(OpcaoComboEnum.SEM_LIDER.getDescricao()) || login.getText().equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao()) || login.getText().equalsIgnoreCase("null")){
            throw new UsuarioException("Nome reservado para o sistema");
        }

        if(String.valueOf(senha.getPassword()).length() < 5){
            throw new UsuarioException("A senha deve ter pelo menos 5 caracteres");
        }

        if(!email.getText().contains("@") || !email.getText().contains(".com")){
            throw new UsuarioException("E-mail inválido");
        }

        List<Map<String, Object>> usuarios = daoUtil.select(String.format("SELECT COUNT(id) as total FROM USUARIOS WHERE (login = '%s' OR email = '%s') AND id <> %d", login.getText(), email.getText(), id), Collections.singletonList("total"));
        if(Integer.parseInt(usuarios.get(0).get("total").toString()) > 0){
            throw new UsuarioException("O login e o e-mail devem ser únicos");
        }

        if(Objects.equals(departamento.getSelectedItem(), OpcaoComboEnum.DEPARTAMENTO.getDescricao())){
            throw new UsuarioException("Necessário escolher um departamento");
        }
    }
}
