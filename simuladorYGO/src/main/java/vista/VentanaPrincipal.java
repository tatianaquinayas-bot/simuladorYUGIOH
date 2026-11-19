/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

/**
 *
 * @author 57323
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    /**
     * Creates new form VentanaPrincipal
     */
    

    private final controlador.ControladorDuelo controlador =
            new controlador.ControladorDuelo();

    private final javax.swing.DefaultListModel<String> modeloMonstruosJugador1 =
            new javax.swing.DefaultListModel<>();

    private final javax.swing.DefaultListModel<String> modeloMonstruosJugador2 =
            new javax.swing.DefaultListModel<>();

    private boolean dueloIniciado = false;
    private int jugadorActual = 1;

    public VentanaPrincipal() {
        initComponents();

        setTitle("Yu-Gi-Oh! Battle Simulator");
        setLocationRelativeTo(null);
        setResizable(false);

        lstMonstruosJugador1.setModel(modeloMonstruosJugador1);
        lstMonstruosJugador2.setModel(modeloMonstruosJugador2);

        cmbJugador.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Jugador 1", "Jugador 2"}));

        cmbPosicion.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Ataque", "Defensa"}));

        cmbAtacante.setModel(new javax.swing.DefaultComboBoxModel<>());
        cmbDefensor.setModel(new javax.swing.DefaultComboBoxModel<>());

        txtHistorial.setEditable(false);

        configurarEventos();

        btnRegistrar.setEnabled(false);
        btnBuscarAPI.setEnabled(false);
        btnAtacar.setEnabled(false);
        btnAtaqueDirecto.setEnabled(false);
    }

    private void configurarEventos() {
        btnRegistrarJugador1.addActionListener(e -> {
            String nombre = txtJugador1.getText().trim();
            if (nombre.isEmpty() || nombre.equalsIgnoreCase("Escribe tu nombre:")) {
                mostrarMensaje("Escribe el nombre del Jugador 1.");
            } else {
                lblTituloJugador1.setText(nombre);
                mostrarMensaje("Jugador 1 registrado: " + nombre);
            }
        });

        btnRegistrarJugador2.addActionListener(e -> {
            String nombre = txtJugador2.getText().trim();
            if (nombre.isEmpty() || nombre.equalsIgnoreCase("Escribe tu nombre:")) {
                mostrarMensaje("Escribe el nombre del Jugador 2.");
            } else {
                lblTituloJugador2.setText(nombre);
                mostrarMensaje("Jugador 2 registrado: " + nombre);
            }
        });

        btnIniciar.addActionListener(e -> iniciarDuelo());
        btnRegistrar.addActionListener(e -> registrarMonstruo());
        btnBuscarAPI.addActionListener(e -> buscarCarta());
        btnAtacar.addActionListener(e -> realizarCombate(false));
        btnAtaqueDirecto.addActionListener(e -> realizarCombate(true));
        btnActualizarHistorial.addActionListener(e -> actualizarHistorial());
        btnReporteLP.addActionListener(e -> mostrarReporteLP());
        btnReporteMonstruos.addActionListener(e -> mostrarReporteMonstruos());
    }

    private void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje);
    }

    private void iniciarDuelo() {
        String nombre1 = txtJugador1.getText().trim();
        String nombre2 = txtJugador2.getText().trim();

        if (nombre1.isEmpty() || nombre2.isEmpty()
                || nombre1.equalsIgnoreCase("Escribe tu nombre:")
                || nombre2.equalsIgnoreCase("Escribe tu nombre:")) {
            mostrarMensaje("Debes escribir el nombre de los dos jugadores.");
            return;
        }

        try {
            controlador.iniciarDuelo(nombre1, nombre2);
            lblTituloJugador1.setText(nombre1);
            lblTituloJugador2.setText(nombre2);
            lblLP1.setText("8000 LP");
            lblLP2.setText("8000 LP");

            dueloIniciado = true;
            jugadorActual = 1;

            btnRegistrar.setEnabled(true);
            btnBuscarAPI.setEnabled(true);
            btnAtacar.setEnabled(true);
            btnAtaqueDirecto.setEnabled(true);

            actualizarListas();
            actualizarHistorial();
            mostrarMensaje("¡Duelo iniciado! Comienza " + nombre1 + ".");
        } catch (Exception ex) {
            mostrarMensaje("No se pudo iniciar el duelo: " + ex.getMessage());
        }
    }

    private void registrarMonstruo() {
        if (!dueloIniciado) {
            mostrarMensaje("Primero debes iniciar el duelo.");
            return;
        }

        try {
            String nombre = txtNombreCarta.getText().trim();
            int ataque = Integer.parseInt(txtAtaque.getText().trim());
            int defensa = Integer.parseInt(txtDefensa.getText().trim());
            int jugador = cmbJugador.getSelectedIndex() + 1;
            String posicion = cmbPosicion.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                mostrarMensaje("Escribe el nombre del monstruo.");
                return;
            }
            if (ataque < 0 || defensa < 0) {
                mostrarMensaje("ATQ y DEF no pueden ser negativos.");
                return;
            }

            controlador.registrarMonstruo(nombre, ataque, defensa, posicion, jugador);
            actualizarListas();

            txtNombreCarta.setText("");
            txtAtaque.setText("");
            txtDefensa.setText("");

            mostrarMensaje("Monstruo registrado correctamente para el Jugador "
                    + jugador + ".");
        } catch (NumberFormatException ex) {
            mostrarMensaje("ATQ y DEF deben ser números enteros.");
        } catch (Exception ex) {
            mostrarMensaje("No se pudo registrar el monstruo: " + ex.getMessage());
        }
    }

    private void buscarCarta() {
        String nombre = txtNombreCarta.getText().trim();

        if (nombre.isEmpty()) {
            mostrarMensaje("Escribe el nombre de una carta para buscarla.");
            return;
        }

        try {
            modelo.CartaMonstruo carta = controlador.buscarCartaAPI(nombre);
            if (carta == null) {
                mostrarMensaje("No se encontró la carta.");
                return;
            }

            txtNombreCarta.setText(carta.consultarNombre());
            txtAtaque.setText(String.valueOf(carta.consultarAtaque()));
            txtDefensa.setText(String.valueOf(carta.consultarDefensa()));
            cmbPosicion.setSelectedItem(carta.consultarPosicion());

            mostrarMensaje("Carta encontrada. Revisa los datos y pulsa REGISTRAR.");
        } catch (Exception ex) {
            mostrarMensaje("No fue posible consultar la API: " + ex.getMessage());
        }
    }

    private void actualizarListas() {
        modeloMonstruosJugador1.clear();
        modeloMonstruosJugador2.clear();

        modelo.Duelo duelo = controlador.consultarDuelo();

        for (modelo.CartaMonstruo monstruo : duelo.consultarMonstruosJugador1()) {
            modeloMonstruosJugador1.addElement(
                    monstruo.consultarNombre() + " | ATQ: "
                    + monstruo.consultarAtaque() + " | DEF: "
                    + monstruo.consultarDefensa() + " | "
                    + monstruo.consultarPosicion());
        }

        for (modelo.CartaMonstruo monstruo : duelo.consultarMonstruosJugador2()) {
            modeloMonstruosJugador2.addElement(
                    monstruo.consultarNombre() + " | ATQ: "
                    + monstruo.consultarAtaque() + " | DEF: "
                    + monstruo.consultarDefensa() + " | "
                    + monstruo.consultarPosicion());
        }

        actualizarSelectores();
    }

    private void actualizarSelectores() {
        cmbAtacante.removeAllItems();
        cmbDefensor.removeAllItems();

        modelo.Duelo duelo = controlador.consultarDuelo();

        java.util.List<modelo.CartaMonstruo> atacantes =
                jugadorActual == 1
                ? duelo.consultarMonstruosJugador1()
                : duelo.consultarMonstruosJugador2();

        java.util.List<modelo.CartaMonstruo> defensores =
                jugadorActual == 1
                ? duelo.consultarMonstruosJugador2()
                : duelo.consultarMonstruosJugador1();

        for (modelo.CartaMonstruo monstruo : atacantes) {
            cmbAtacante.addItem(monstruo.consultarNombre()
                    + " | ATQ: " + monstruo.consultarAtaque());
        }

        for (modelo.CartaMonstruo monstruo : defensores) {
            cmbDefensor.addItem(monstruo.consultarNombre()
                    + " | DEF: " + monstruo.consultarDefensa());
        }

        lblAtacante.setText("Atacante (Jugador " + jugadorActual + ")");
        lblDefensor.setText("Defensor (Jugador " + (jugadorActual == 1 ? 2 : 1) + ")");
    }

    
    private void realizarCombate(boolean ataqueDirecto) {
        if (!dueloIniciado) {
            mostrarMensaje("Primero debes iniciar el duelo.");
            return;
        }

        int indiceAtacante = cmbAtacante.getSelectedIndex();
        if (indiceAtacante < 0) {
            mostrarMensaje("El jugador actual no tiene monstruos para atacar.");
            return;
        }

        int indiceDefensor = -1;
        if (!ataqueDirecto) {
            indiceDefensor = cmbDefensor.getSelectedIndex();
            if (indiceDefensor < 0) {
                mostrarMensaje("Selecciona un monstruo defensor.");
                return;
            }
        }

        try {
            modelo.RegistroCombate registro = controlador.realizarAtaque(
                    jugadorActual, indiceAtacante, indiceDefensor);

            actualizarListas();
            actualizarPuntosVida();
            actualizarHistorial();

            javax.swing.JOptionPane.showMessageDialog(
                    this, registro.generarReporte(), "Resultado del combate",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            modelo.Duelo duelo = controlador.consultarDuelo();
            if (duelo.hayGanador()) {
                mostrarMensaje("¡" + duelo.consultarGanador() + " ha ganado el duelo!");
                btnAtacar.setEnabled(false);
                btnAtaqueDirecto.setEnabled(false);
                btnRegistrar.setEnabled(false);
                btnBuscarAPI.setEnabled(false);
                return;
            }

            jugadorActual = (jugadorActual == 1) ? 2 : 1;
            actualizarSelectores();

        } catch (java.io.IOException | RuntimeException ex) {
            mostrarMensaje("No se pudo realizar el combate: " + ex.getMessage());
        }
    }

    private void actualizarPuntosVida() {
        modelo.Duelo duelo = controlador.consultarDuelo();
        lblLP1.setText(duelo.consultarJugador1().getPuntosVida() + " LP");
        lblLP2.setText(duelo.consultarJugador2().getPuntosVida() + " LP");
    }

    private void actualizarHistorial() {
        if (!dueloIniciado) {
            txtHistorial.setText("El historial aparecerá cuando inicie el duelo.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        java.util.List<modelo.RegistroCombate> historial =
                controlador.consultarDuelo().consultarHistorial();

        if (historial.isEmpty()) {
            texto.append("Aún no se han realizado combates.");
        } else {
            for (int i = 0; i < historial.size(); i++) {
                texto.append("COMBATE ").append(i + 1).append("\n")
                        .append(historial.get(i).generarReporte())
                        .append("\n-----------------------------\n");
            }
        }

        txtHistorial.setText(texto.toString());
        txtHistorial.setCaretPosition(txtHistorial.getDocument().getLength());
    }

    private void mostrarReporteLP() {
        if (!dueloIniciado) {
            mostrarMensaje("Primero debes iniciar el duelo.");
            return;
        }
        mostrarMensaje(controlador.generarReporteLP());
    }

    private void mostrarReporteMonstruos() {
        if (!dueloIniciado) {
            mostrarMensaje("Primero debes iniciar el duelo.");
            return;
        }
        mostrarMensaje(controlador.generarReporteMonstruos());
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        pnlJugadores = new javax.swing.JPanel();
        pnlJugador1 = new javax.swing.JPanel();
        lblNombreJugador1 = new javax.swing.JLabel();
        btnRegistrarJugador1 = new javax.swing.JButton();
        lblTituloJugador1 = new javax.swing.JLabel();
        txtJugador1 = new javax.swing.JTextField();
        lblTextoLP1 = new javax.swing.JLabel();
        lblLP1 = new javax.swing.JLabel();
        pnlJugador2 = new javax.swing.JPanel();
        lblNombreJugador2 = new javax.swing.JLabel();
        btnRegistrarJugador2 = new javax.swing.JButton();
        lblTextoLP2 = new javax.swing.JLabel();
        lblLP2 = new javax.swing.JLabel();
        lblTituloJugador2 = new javax.swing.JLabel();
        txtJugador2 = new javax.swing.JTextField();
        btnIniciar = new javax.swing.JButton();
        lblJugador = new javax.swing.JPanel();
        lblRegistroTitulo = new javax.swing.JLabel();
        lblNombreCarta = new javax.swing.JLabel();
        txtNombreCarta = new javax.swing.JTextField();
        lblAtaque = new javax.swing.JLabel();
        lblDefensa = new javax.swing.JLabel();
        txtDefensa = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblPosicion = new javax.swing.JLabel();
        txtAtaque = new javax.swing.JTextField();
        cmbJugador = new javax.swing.JComboBox<>();
        cmbPosicion = new javax.swing.JComboBox<>();
        btnBuscarAPI = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        pnlCampoBatalla = new javax.swing.JPanel();
        lblCampoTitulo = new javax.swing.JLabel();
        pnlCampoJugador1 = new javax.swing.JPanel();
        lblTituloMonstruos1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstMonstruosJugador1 = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        lblTituloMonstruos2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstMonstruosJugador2 = new javax.swing.JList<>();
        pnlCombate = new javax.swing.JPanel();
        lblAtacante = new javax.swing.JLabel();
        lblDefensor = new javax.swing.JLabel();
        cmbAtacante = new javax.swing.JComboBox<>();
        cmbDefensor = new javax.swing.JComboBox<>();
        btnAtacar = new javax.swing.JButton();
        btnAtaqueDirecto = new javax.swing.JButton();
        lblCombateTitulo1 = new javax.swing.JLabel();
        pnlEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlHistorial = new javax.swing.JPanel();
        lblHistorialTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtHistorial = new javax.swing.JTextArea();
        btnActualizarHistorial = new javax.swing.JButton();
        pnlReportes = new javax.swing.JPanel();
        lblReportesTitulo = new javax.swing.JLabel();
        btnReporteLP = new javax.swing.JButton();
        btnReporteMonstruos = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Yu-Gi-Oh");
        setPreferredSize(new java.awt.Dimension(1100, 750));
        setResizable(false);

        pnlJugadores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        pnlJugador1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        lblNombreJugador1.setText("Nombre Jugador:");

        btnRegistrarJugador1.setBackground(new java.awt.Color(204, 204, 255));
        btnRegistrarJugador1.setText("REGISTRAR JUGADOR 1");

        lblTituloJugador1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTituloJugador1.setForeground(new java.awt.Color(0, 0, 102));
        lblTituloJugador1.setText("JUGADOR 1");

        txtJugador1.setForeground(new java.awt.Color(204, 204, 204));
        txtJugador1.setText("Escribe tu nombre:");

        lblTextoLP1.setText("Puntos de Vida");

        lblLP1.setText("8000 LP");

        javax.swing.GroupLayout pnlJugador1Layout = new javax.swing.GroupLayout(pnlJugador1);
        pnlJugador1.setLayout(pnlJugador1Layout);
        pnlJugador1Layout.setHorizontalGroup(
            pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                        .addComponent(lblTextoLP1)
                        .addGap(103, 103, 103))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                        .addComponent(lblLP1)
                        .addGap(124, 124, 124))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblNombreJugador1)
                        .addGroup(pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                                .addComponent(btnRegistrarJugador1)
                                .addGap(65, 65, 65))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                                .addComponent(txtJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJugador1Layout.createSequentialGroup()
                        .addComponent(lblTituloJugador1)
                        .addGap(115, 115, 115))))
        );
        pnlJugador1Layout.setVerticalGroup(
            pnlJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJugador1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(lblTituloJugador1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombreJugador1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTextoLP1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLP1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrarJugador1)
                .addContainerGap())
        );

        pnlJugador2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        lblNombreJugador2.setText("Nombre Jugador:");

        btnRegistrarJugador2.setBackground(new java.awt.Color(204, 204, 255));
        btnRegistrarJugador2.setText("REGISTRAR JUGADOR 2");

        lblTextoLP2.setText("Puntos de Vida");

        lblLP2.setText("8000 LP");

        lblTituloJugador2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTituloJugador2.setForeground(new java.awt.Color(0, 0, 102));
        lblTituloJugador2.setText("JUGADOR 2");

        txtJugador2.setForeground(new java.awt.Color(204, 204, 204));
        txtJugador2.setText("Escribe tu nombre:");

        javax.swing.GroupLayout pnlJugador2Layout = new javax.swing.GroupLayout(pnlJugador2);
        pnlJugador2.setLayout(pnlJugador2Layout);
        pnlJugador2Layout.setHorizontalGroup(
            pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJugador2Layout.createSequentialGroup()
                .addGroup(pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlJugador2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(btnRegistrarJugador2))
                    .addGroup(pnlJugador2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreJugador2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlJugador2Layout.createSequentialGroup()
                .addGroup(pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlJugador2Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTituloJugador2)
                            .addComponent(lblTextoLP2)))
                    .addGroup(pnlJugador2Layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(lblLP2)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlJugador2Layout.setVerticalGroup(
            pnlJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJugador2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblTituloJugador2)
                .addGap(18, 18, 18)
                .addComponent(lblNombreJugador2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTextoLP2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLP2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrarJugador2)
                .addContainerGap())
        );

        btnIniciar.setBackground(new java.awt.Color(153, 153, 255));
        btnIniciar.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        btnIniciar.setText("INICIAR DUELO");

        lblRegistroTitulo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRegistroTitulo.setForeground(new java.awt.Color(0, 0, 102));
        lblRegistroTitulo.setText("REGISTRO DE MONSTRUOS");
        lblRegistroTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblNombreCarta.setText("Nombre de la Carta");

        txtNombreCarta.setForeground(new java.awt.Color(204, 204, 204));
        txtNombreCarta.setText("Escribe el Nombre del Mounstro...");

        lblAtaque.setText("ATAQUE");

        lblDefensa.setText("DEFENSA");

        jLabel4.setText("JUGADOR");

        lblPosicion.setText("POSICIÓN");

        cmbJugador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbPosicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscarAPI.setBackground(new java.awt.Color(153, 153, 255));
        btnBuscarAPI.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnBuscarAPI.setText("BUSCAR CARTA");

        btnRegistrar.setBackground(new java.awt.Color(153, 153, 255));
        btnRegistrar.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnRegistrar.setText("REGISTRAR MONSTRUO");

        pnlCampoBatalla.setForeground(new java.awt.Color(204, 204, 204));

        lblCampoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCampoTitulo.setForeground(new java.awt.Color(0, 0, 102));
        lblCampoTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCampoTitulo.setText("CAMPO DE BATALLA");

        pnlCampoJugador1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblTituloMonstruos1.setText("MONSTRUOS DEL JUGADOR 1");

        lstMonstruosJugador1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstMonstruosJugador1);

        javax.swing.GroupLayout pnlCampoJugador1Layout = new javax.swing.GroupLayout(pnlCampoJugador1);
        pnlCampoJugador1.setLayout(pnlCampoJugador1Layout);
        pnlCampoJugador1Layout.setHorizontalGroup(
            pnlCampoJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCampoJugador1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTituloMonstruos1)
                .addGap(56, 56, 56))
            .addGroup(pnlCampoJugador1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlCampoJugador1Layout.setVerticalGroup(
            pnlCampoJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCampoJugador1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloMonstruos1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblTituloMonstruos2.setText("MONSTRUOS DEL JUGADOR 2");

        lstMonstruosJugador2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstMonstruosJugador2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTituloMonstruos2)
                .addGap(42, 42, 42))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloMonstruos2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCampoBatallaLayout = new javax.swing.GroupLayout(pnlCampoBatalla);
        pnlCampoBatalla.setLayout(pnlCampoBatallaLayout);
        pnlCampoBatallaLayout.setHorizontalGroup(
            pnlCampoBatallaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCampoBatallaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCampoJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCampoBatallaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCampoTitulo)
                .addGap(233, 233, 233))
        );
        pnlCampoBatallaLayout.setVerticalGroup(
            pnlCampoBatallaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCampoBatallaLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(lblCampoTitulo)
                .addGap(18, 18, 18)
                .addGroup(pnlCampoBatallaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCampoJugador1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout lblJugadorLayout = new javax.swing.GroupLayout(lblJugador);
        lblJugador.setLayout(lblJugadorLayout);
        lblJugadorLayout.setHorizontalGroup(
            lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblJugadorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblRegistroTitulo)
                .addGap(198, 198, 198))
            .addGroup(lblJugadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnBuscarAPI, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblNombreCarta, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtNombreCarta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAtaque, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAtaque, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbJugador, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDefensa)
                    .addComponent(lblPosicion)
                    .addComponent(txtDefensa)
                    .addComponent(cmbPosicion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(pnlCampoBatalla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        lblJugadorLayout.setVerticalGroup(
            lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblJugadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRegistroTitulo)
                .addGap(19, 19, 19)
                .addComponent(lblNombreCarta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreCarta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAtaque)
                    .addComponent(lblDefensa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDefensa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAtaque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblPosicion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(lblJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnBuscarAPI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlCampoBatalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblAtacante.setText("MOUNSTRO ATACANTE");

        lblDefensor.setText("MOUNSTRO DEFENSOR");

        cmbAtacante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbDefensor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAtacar.setBackground(new java.awt.Color(153, 153, 255));
        btnAtacar.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnAtacar.setText("ATACAR");

        btnAtaqueDirecto.setBackground(new java.awt.Color(153, 153, 255));
        btnAtaqueDirecto.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnAtaqueDirecto.setText("ATAQUE DIRECTO");

        lblCombateTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCombateTitulo1.setForeground(new java.awt.Color(0, 0, 102));
        lblCombateTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCombateTitulo1.setText("ZONA DE COMBATE");

        javax.swing.GroupLayout pnlCombateLayout = new javax.swing.GroupLayout(pnlCombate);
        pnlCombate.setLayout(pnlCombateLayout);
        pnlCombateLayout.setHorizontalGroup(
            pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCombateLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAtacar, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(lblAtacante)
                    .addComponent(cmbAtacante, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDefensor)
                    .addComponent(btnAtaqueDirecto, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                    .addComponent(cmbDefensor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54))
            .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCombateLayout.createSequentialGroup()
                    .addContainerGap(258, Short.MAX_VALUE)
                    .addComponent(lblCombateTitulo1)
                    .addGap(218, 218, 218)))
        );
        pnlCombateLayout.setVerticalGroup(
            pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCombateLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAtacante)
                    .addComponent(lblDefensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbAtacante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDefensor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAtacar, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(btnAtaqueDirecto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlCombateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlCombateLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(lblCombateTitulo1)
                    .addContainerGap(157, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlJugadoresLayout = new javax.swing.GroupLayout(pnlJugadores);
        pnlJugadores.setLayout(pnlJugadoresLayout);
        pnlJugadoresLayout.setHorizontalGroup(
            pnlJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJugadoresLayout.createSequentialGroup()
                .addGroup(pnlJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlJugadoresLayout.createSequentialGroup()
                        .addComponent(pnlJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlJugador2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblJugador, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlJugadoresLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlCombate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlJugadoresLayout.setVerticalGroup(
            pnlJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJugadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlJugador1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlJugador2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlCombate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlEncabezado.setBackground(new java.awt.Color(153, 153, 255));

        lblTitulo.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        lblTitulo.setText("YU-GI-OH SIMULADOR");

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        lblHistorialTitulo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblHistorialTitulo.setForeground(new java.awt.Color(0, 0, 102));
        lblHistorialTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHistorialTitulo.setText("HISTORIAL DEL DUELO");

        jLabel1.setText("Aqui apareceran los acontecimientos del programa:");

        txtHistorial.setColumns(20);
        txtHistorial.setRows(5);
        jScrollPane3.setViewportView(txtHistorial);

        btnActualizarHistorial.setBackground(new java.awt.Color(153, 153, 255));
        btnActualizarHistorial.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnActualizarHistorial.setText("ACTUALIZAR HISTORIAL");

        javax.swing.GroupLayout pnlHistorialLayout = new javax.swing.GroupLayout(pnlHistorial);
        pnlHistorial.setLayout(pnlHistorialLayout);
        pnlHistorialLayout.setHorizontalGroup(
            pnlHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHistorialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblHistorialTitulo)
                .addGap(238, 238, 238))
            .addGroup(pnlHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(btnActualizarHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHistorialLayout.setVerticalGroup(
            pnlHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHistorialTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizarHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
        );

        lblReportesTitulo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblReportesTitulo.setForeground(new java.awt.Color(0, 0, 102));
        lblReportesTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblReportesTitulo.setText("REPORTES");

        btnReporteLP.setBackground(new java.awt.Color(153, 153, 255));
        btnReporteLP.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnReporteLP.setText("REPORTE DE LP");

        btnReporteMonstruos.setBackground(new java.awt.Color(153, 153, 255));
        btnReporteMonstruos.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        btnReporteMonstruos.setText("REPORTE DE MONSTRUOS");

        javax.swing.GroupLayout pnlReportesLayout = new javax.swing.GroupLayout(pnlReportes);
        pnlReportes.setLayout(pnlReportesLayout);
        pnlReportesLayout.setHorizontalGroup(
            pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlReportesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblReportesTitulo)
                .addGap(256, 256, 256))
            .addGroup(pnlReportesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnReporteLP, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReporteMonstruos, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlReportesLayout.setVerticalGroup(
            pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblReportesTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReporteMonstruos, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(btnReporteLP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlHistorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlJugadores, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane8.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarHistorial;
    private javax.swing.JButton btnAtacar;
    private javax.swing.JButton btnAtaqueDirecto;
    private javax.swing.JButton btnBuscarAPI;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrarJugador1;
    private javax.swing.JButton btnRegistrarJugador2;
    private javax.swing.JButton btnReporteLP;
    private javax.swing.JButton btnReporteMonstruos;
    private javax.swing.JComboBox<String> cmbAtacante;
    private javax.swing.JComboBox<String> cmbDefensor;
    private javax.swing.JComboBox<String> cmbJugador;
    private javax.swing.JComboBox<String> cmbPosicion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblAtacante;
    private javax.swing.JLabel lblAtaque;
    private javax.swing.JLabel lblCampoTitulo;
    private javax.swing.JLabel lblCombateTitulo1;
    private javax.swing.JLabel lblDefensa;
    private javax.swing.JLabel lblDefensor;
    private javax.swing.JLabel lblHistorialTitulo;
    private javax.swing.JPanel lblJugador;
    private javax.swing.JLabel lblLP1;
    private javax.swing.JLabel lblLP2;
    private javax.swing.JLabel lblNombreCarta;
    private javax.swing.JLabel lblNombreJugador1;
    private javax.swing.JLabel lblNombreJugador2;
    private javax.swing.JLabel lblPosicion;
    private javax.swing.JLabel lblRegistroTitulo;
    private javax.swing.JLabel lblReportesTitulo;
    private javax.swing.JLabel lblTextoLP1;
    private javax.swing.JLabel lblTextoLP2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloJugador1;
    private javax.swing.JLabel lblTituloJugador2;
    private javax.swing.JLabel lblTituloMonstruos1;
    private javax.swing.JLabel lblTituloMonstruos2;
    private javax.swing.JList<String> lstMonstruosJugador1;
    private javax.swing.JList<String> lstMonstruosJugador2;
    private javax.swing.JPanel pnlCampoBatalla;
    private javax.swing.JPanel pnlCampoJugador1;
    private javax.swing.JPanel pnlCombate;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlHistorial;
    private javax.swing.JPanel pnlJugador1;
    private javax.swing.JPanel pnlJugador2;
    private javax.swing.JPanel pnlJugadores;
    private javax.swing.JPanel pnlReportes;
    private javax.swing.JTextField txtAtaque;
    private javax.swing.JTextField txtDefensa;
    private javax.swing.JTextArea txtHistorial;
    private javax.swing.JTextField txtJugador1;
    private javax.swing.JTextField txtJugador2;
    private javax.swing.JTextField txtNombreCarta;
    // End of variables declaration//GEN-END:variables
}
