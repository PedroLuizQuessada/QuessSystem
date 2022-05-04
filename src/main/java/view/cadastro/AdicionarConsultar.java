package view.cadastro;

import controle.*;
import controle.enums.TipoCampoEnum;
import controle.enums.TiposRegrasRegrasCondicionaisEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.cadastro.RecarregarFormularioListener;
import listener.cadastro.SalvarAlterarListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdicionarConsultar extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final RadioUtil radioUtil = new RadioUtil();
    private final RegrasCondicionaisUtil regrasCondicionaisUtil;

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final JButton voltar = new JButton("Voltar");
    private final JButton jButton = new JButton("Salvar");
    private final Map<String, Object> campos = new HashMap<>();
    private final Map<String, Object> valoresCampos;
    private int camposAdicionados = 0;
    private int posX = 0;
    private int posY = 0;

    public AdicionarConsultar(Integer idCadastro, String nomeCadastro, Integer idRegistro, Map<String, Object>... valoresCampos){
        this.valoresCampos = valoresCampos.length > 0 ? valoresCampos[0] : null;
        regrasCondicionaisUtil = new RegrasCondicionaisUtil(this.valoresCampos, this.campos);
        List<Map<String, Object>> info = new ArrayList<>();
        String titulo = "Consultar Registro";
        if (idRegistro == null) {
            titulo = "Adicionar Registro";
        }
        jPanel.setLayout(new GridBagLayout());
        jButton.addActionListener(new SalvarAlterarListener(this, idCadastro, idRegistro, campos, regrasCondicionaisUtil));
        voltar.addActionListener(new VoltarListener(this, idCadastro));
        c.gridy = 0;

        try {
            List<Map<String, Object>> campos = daoUtil.select(String.format("SELECT id, label, coluna, tipo, bloqueado, obrigatorio, quebralinha, nativo FROM CAMPOSCADASTROS WHERE vinculado = true AND idcadastro = %d ORDER BY ordem", idCadastro), Arrays.asList("id", "label", "coluna", "tipo", "bloqueado", "obrigatorio", "quebralinha", "nativo"));
            for (int i = 0; i < campos.size(); i++) {
                boolean visivelCondicionalmente = regrasCondicionaisUtil.verificarRegraCondicional(nomeCadastro, Integer.parseInt(campos.get(i).get("id").toString()), idRegistro, TiposRegrasRegrasCondicionaisEnum.VISIBILIDADE);
                if (idRegistro != null) {
                    jButton.setText("Alterar");
                    info = daoUtil.select(String.format("SELECT %s FROM %s WHERE id = %d", campos.get(i).get("coluna"), nomeCadastro, idRegistro), Collections.singletonList(String.format("%s", campos.get(i).get("coluna"))));
                }

                if (visivelCondicionalmente) {
                    camposAdicionados = camposAdicionados + 1;
                    if (camposAdicionados == 0 || (campos.get(i).get("quebralinha") != null && campos.get(i).get("quebralinha").toString().equalsIgnoreCase("true"))) {
                        c.insets = new Insets(0, 40, 0, 0);
                        c.gridx = 0;

                        if (i != 0) {
                            c.gridy++;
                        }
                    } else {
                        c.gridx++;
                        c.insets = new Insets(0, 40, 0, 0);
                    }

                    jPanel.add(new JLabel(campos.get(i).get("label").toString()), c);
                    posY = c.gridy;

                    JComponent campo = new JTextField();
                    c.insets.top = 45;

                    if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
                        JTextField campoTexto = new JTextField();
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                try {
                                    campoTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoTexto.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoTexto.setText(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                try {
                                    campoTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoTexto.setText("");
                                }
                            }
                            else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        campoTexto.setColumns(7);
                        campo = campoTexto;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
                        JTextField campoNumerico = new JTextField();
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                try {
                                    campoNumerico.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoNumerico.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoNumerico.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoNumerico.setText(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                try {
                                    campoNumerico.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoNumerico.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoNumerico.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        campoNumerico.setColumns(7);
                        campoNumerico.addKeyListener(new NumericoUtil(campoNumerico));
                        campo = campoNumerico;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
                        JTextField campoDataHora = new JTextField();
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                DataHoraUtil dataHoraUtil = new DataHoraUtil(campoDataHora);
                                try {
                                    campoDataHora.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                    campoDataHora.setText(dataHoraUtil.converterDataHoraString());
                                } catch (NullPointerException exception) {
                                    campoDataHora.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoDataHora.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoDataHora.setText(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                DataHoraUtil dataHoraUtil = new DataHoraUtil(campoDataHora);
                                try {
                                    campoDataHora.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                    campoDataHora.setText(dataHoraUtil.converterDataHoraString());
                                } catch (NullPointerException exception) {
                                    campoDataHora.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoDataHora.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        campoDataHora.setColumns(7);
                        campoDataHora.addKeyListener(new DataHoraUtil(campoDataHora));
                        campo = campoDataHora;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
                        JTextField campoData = new JTextField();
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                DataUtil dataUtil = new DataUtil(campoData);
                                try {
                                    campoData.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                    campoData.setText(dataUtil.converterDataString());
                                } catch (NullPointerException exception) {
                                    campoData.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoData.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoData.setText(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                DataUtil dataUtil = new DataUtil(campoData);
                                try {
                                    campoData.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                    campoData.setText(dataUtil.converterDataString());
                                } catch (NullPointerException exception) {
                                    campoData.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoData.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                            }
                        }
                        campoData.setColumns(7);
                        campoData.addKeyListener(new DataUtil(campoData));
                        campo = campoData;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
                        JCheckBox campoCheckbox = new JCheckBox();
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                try {
                                    campoCheckbox.setSelected(Boolean.parseBoolean(info.get(0).get(campos.get(i).get("coluna").toString()).toString()));
                                } catch (NullPointerException ignored) {
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("estadopadrao"));
                                campoCheckbox.setSelected(Boolean.parseBoolean(configsCampo.get(0).get("estadopadrao").toString()));
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoCheckbox.setSelected(Boolean.parseBoolean(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna"))))));
                            }
                            else if (idRegistro != null) {
                                try {
                                    campoCheckbox.setSelected(Boolean.parseBoolean(info.get(0).get(campos.get(i).get("coluna").toString()).toString()));
                                } catch (NullPointerException ignored) {
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("estadopadrao"));
                                campoCheckbox.setSelected(Boolean.parseBoolean(configsCampo.get(0).get("estadopadrao").toString()));
                            }
                        }
                        campo = campoCheckbox;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
                        JTextArea campoAreaTexto = new JTextArea();
                        c.insets.top = 90;
                        AreaTextoUtil areaTextoUtil = new AreaTextoUtil(campoAreaTexto);
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                try {
                                    campoAreaTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoAreaTexto.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao, limitecaracteres FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Arrays.asList("valorpadrao", "limitecaracteres"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoAreaTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                                if (configsCampo.get(0).get("limitecaracteres") != null) {
                                    areaTextoUtil = new AreaTextoUtil(campoAreaTexto, Integer.parseInt(configsCampo.get(0).get("limitecaracteres").toString()));
                                    campoAreaTexto.addKeyListener(areaTextoUtil);
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoAreaTexto.setText(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                try {
                                    campoAreaTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException exception) {
                                    campoAreaTexto.setText("");
                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao, limitecaracteres FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Arrays.asList("valorpadrao", "limitecaracteres"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoAreaTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                                }
                                if (configsCampo.get(0).get("limitecaracteres") != null) {
                                    areaTextoUtil = new AreaTextoUtil(campoAreaTexto, Integer.parseInt(configsCampo.get(0).get("limitecaracteres").toString()));
                                    campoAreaTexto.addKeyListener(areaTextoUtil);
                                }
                            }
                        }
                        areaTextoUtil.configurarTextArea();
                        campo = campoAreaTexto;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
                        JComboBox<String> campoCombobox = new JComboBox<>();
                        comboboxUtil.carregarOpcoesCombobox(Integer.parseInt(campos.get(i).get("id").toString()), campoCombobox, false);
                        if (this.valoresCampos == null) {
                            if (idRegistro != null) {
                                try {
                                    campoCombobox.setSelectedItem(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException ignored) {

                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT opcaopadrao FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("opcaopadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoCombobox.setSelectedItem(configsCampo.get(0).get("opcaopadrao").toString());
                                }
                            }
                        }
                        else {
                            if (this.valoresCampos.containsKey(String.valueOf(campos.get(i).get("coluna")))) {
                                campoCombobox.setSelectedItem(String.valueOf(this.valoresCampos.get(String.valueOf(campos.get(i).get("coluna")))));
                            }
                            else if (idRegistro != null) {
                                try {
                                    campoCombobox.setSelectedItem(info.get(0).get(campos.get(i).get("coluna")).toString());
                                } catch (NullPointerException ignored) {

                                }
                            } else {
                                List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT opcaopadrao FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("opcaopadrao"));
                                if (configsCampo.get(0).get("valorpadrao") != null) {
                                    campoCombobox.setSelectedItem(configsCampo.get(0).get("opcaopadrao").toString());
                                }
                            }
                        }
                        campo = campoCombobox;
                    } else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
                        ButtonGroup campoRadio = new ButtonGroup();
                        List<JRadioButton> botoes = radioUtil.carregarOpcoesRadio(Integer.parseInt(campos.get(i).get("id").toString()), campoRadio);
                        for (JRadioButton botao : botoes) {
                            botao.setActionCommand(botao.getText());
                            jPanel.add(botao, c);
                            c.insets.top = c.insets.top + 45;

                            if (this.valoresCampos == null) {
                                if (idRegistro != null && info.get(0).get(campos.get(i).get("coluna")) != null && info.get(0).get(campos.get(i).get("coluna")).toString().equals(botao.getText())) {
                                    botao.setSelected(true);
                                }
                                else if (idRegistro == null) {
                                    List<Map<String, Object>> estadoPadraoList = daoUtil.select("SELECT estadopadrao FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d AND cadastro = true", Collections.singletonList("estadopadrao"));
                                    if (estadoPadraoList.get(0).get("estadopadrao") != null && estadoPadraoList.get(0).get("estadopadrao").equals(botao.getText())) {
                                        botao.setSelected(true);
                                    }
                                }
                            }
                            else {
                                String valorPrevio = String.valueOf(this.valoresCampos.getOrDefault(String.valueOf(campos.get(i).get("coluna")), ""));
                                if (valorPrevio.equals(botao.getText())) {
                                    botao.setSelected(true);
                                }
                                else if (idRegistro != null && info.get(0).get(campos.get(i).get("coluna")) != null && info.get(0).get(campos.get(i).get("coluna")).toString().equals(botao.getText())) {
                                    botao.setSelected(true);
                                }
                                else if (idRegistro == null) {
                                    List<Map<String, Object>> estadoPadraoList = daoUtil.select("SELECT estadopadrao FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d AND cadastro = true", Collections.singletonList("estadopadrao"));
                                    if (estadoPadraoList.get(0).get("estadopadrao") != null && estadoPadraoList.get(0).get("estadopadrao").equals(botao.getText())) {
                                        botao.setSelected(true);
                                    }
                                }
                            }

                            if (campos.get(i).get("bloqueado").toString().equalsIgnoreCase("true") || (campos.get(i).get("nativo") != null && campos.get(i).get("nativo").toString().equalsIgnoreCase("true")) || regrasCondicionaisUtil.verificarRegraCondicional(nomeCadastro, Integer.parseInt(campos.get(i).get("id").toString()), idRegistro, TiposRegrasRegrasCondicionaisEnum.BLOQUEIO)) {
                                botao.setEnabled(false);
                            }
                        }

                        this.campos.put(campos.get(i).get("coluna").toString(), campoRadio);
                    }

                    if (campos.get(i).get("bloqueado").toString().equalsIgnoreCase("true") || (campos.get(i).get("nativo") != null && campos.get(i).get("nativo").toString().equalsIgnoreCase("true")) || regrasCondicionaisUtil.verificarRegraCondicional(nomeCadastro, Integer.parseInt(campos.get(i).get("id").toString()), idRegistro, TiposRegrasRegrasCondicionaisEnum.BLOQUEIO)) {
                        campo.setEnabled(false);
                    }

                    if (!campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
                        jPanel.add(campo, c);

                        if (campos.get(i).get("nativo") == null) {
                            this.campos.put(campos.get(i).get("coluna").toString(), campo);
                        }
                    }

                    try {
                        if (c.gridy == 0 && campos.get(i + 1).get("quebralinha") != null && campos.get(i + 1).get("quebralinha").toString().equalsIgnoreCase("true")) {
                            c.gridx++;
                            c.insets.top = 45;
                            jPanel.add(voltar, c);
                        }
                        else if (c.gridy == 0 && campos.get(i).get("quebralinha") != null && campos.get(i).get("quebralinha").toString().equalsIgnoreCase("true")) {
                            int posYAtual = c.gridy;
                            c.gridx--;
                            c.gridy = posY + 1;
                            c.insets.top = 45;
                            jPanel.add(voltar, c);
                            c.gridx++;
                            c.gridy = posYAtual;
                        }
                    } catch (IndexOutOfBoundsException exception) {
                        c.gridx++;
                        jPanel.add(voltar, c);
                    }

                    if (c.gridx > posX) {
                        posX = c.gridx;
                    }
                }
            }

            List<Map<String, Object>> camposQueAfetamRegrasCondicionais = daoUtil.select(String.format("SELECT DISTINCT cc.coluna AS coluna, cc.tipo AS tipo FROM regrascondicionais rc INNER JOIN camposcadastros cc ON cc.id = rc.idcampoinfo WHERE cadastro = true AND (tiporegra = '%s' OR tiporegra = '%s')", TiposRegrasRegrasCondicionaisEnum.VISIBILIDADE.getDescricao(), TiposRegrasRegrasCondicionaisEnum.BLOQUEIO.getDescricao()), Arrays.asList("coluna", "tipo"));
            for (Map<String, Object> campoQueAfetaRegraCondicional: camposQueAfetamRegrasCondicionais) {
                if (String.valueOf(campoQueAfetaRegraCondicional.get("tipo")).equals(TipoCampoEnum.COMBOBOX.getDescricao())) {
                    ((JComboBox) this.campos.get(String.valueOf(campoQueAfetaRegraCondicional.get("coluna")))).addActionListener(new RecarregarFormularioListener(this, idCadastro, nomeCadastro, idRegistro, this.campos));
                }
                if (String.valueOf(campoQueAfetaRegraCondicional.get("tipo")).equals(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    ((JCheckBox) this.campos.get(String.valueOf(campoQueAfetaRegraCondicional.get("coluna")))).addActionListener(new RecarregarFormularioListener(this, idCadastro, nomeCadastro, idRegistro, this.campos));
                }
                if (String.valueOf(campoQueAfetaRegraCondicional.get("tipo")).equals(TipoCampoEnum.RADIO.getDescricao())) {
                    ButtonGroup radio = (ButtonGroup) this.campos.get(String.valueOf(campoQueAfetaRegraCondicional.get("coluna")));
                    for (AbstractButton botao: Collections.list(radio.getElements())) {
                        botao.addActionListener(new RecarregarFormularioListener(this, idCadastro, nomeCadastro, idRegistro, this.campos));
                    }
                }
            }

            c.gridy++;
            c.gridx = posX / 2;
            c.insets.top = 40;

            if(posX % 2 == 1){
                c.insets.left = c.insets.left + 180;
            }

            jPanel.add(jButton, c);

            if(camposAdicionados == 0){
                c.insets.left = 40;
                c.gridx++;
                jPanel.add(voltar, c);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(jPanel);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1200, 700);
    }
}
