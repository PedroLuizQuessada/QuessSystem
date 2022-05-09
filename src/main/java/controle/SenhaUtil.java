package controle;

import java.util.Base64;
import java.util.Random;

public class SenhaUtil {
    private final String LETRAS = "abcdefghijklmnopqrstuvwxyz";
    private final char[] CARACTERES = (LETRAS + LETRAS.toUpperCase() + "0123456789").toCharArray();

    public String criptografar(String senhaDescriptografada){
        return Base64.getMimeEncoder().encodeToString(senhaDescriptografada.getBytes());
    }

    public String descriptografar(String senhaCriptografada){
        return new String(Base64.getMimeDecoder().decode(senhaCriptografada));
    }

    public String geraSenha(){
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i <  8; i++){
            senha.append(CARACTERES[new Random().nextInt(CARACTERES.length)]);
        }

        return senha.toString();
    }
}
