package controle;

import javax.swing.*;
import java.awt.*;

public class AreaTextoUtil {
    public void configurarTextArea(JTextArea campo){
        campo.setPreferredSize(new Dimension(300, 65));
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
    }
}
