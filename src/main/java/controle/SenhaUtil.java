package controle;

import java.util.Base64;

public class SenhaUtil {
    public String criptografar(String senhaDescriptografada){
        return Base64.getMimeEncoder().encodeToString(senhaDescriptografada.getBytes());
    }

    public String descriptografar(String senhaCriptografada){
        return new String(Base64.getMimeDecoder().decode(senhaCriptografada));
    }
}
