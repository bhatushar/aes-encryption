package gui;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import security.Encryptor;
import security.Decryptor;

/**
 * Class is used to create a GUI form for File Encryptor/Decryptor
 * @extends JFrame
 * @version 1.0
 */
public class Form extends JFrame {
    private JRadioButton encryptRadio, decryptRadio;
    private JLabel srcLabel, pwdLabel;
    private JTextField srcTxt;
    private JPasswordField pwdTxt;
    private JButton browseBtn, runBtn;
    private JTextArea log;
    private JScrollPane logScroll;
    private JFileChooser fileChooser;

    /**
     * Form Constructor
     * Method sets various form parameters for the main window
     * <p>
     * Set container properties, initialize components, build panel layout, attach listeners and set Form visibility
     */
    public Form() {
        // Container properties
        this.setTitle("File Encryptor/Decryptor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 300);
        // Construction split into methods
        initializeComponents();
        buildLayout();
        createListeners();
        // Show form
        this.setVisible(true);
    }

    /**
     * Initializes all {@link Form} components with default values.
     *  <li>{@link Form#encryptRadio} - Encryption selector</li>
     *  <li>{@link Form#decryptRadio} - Decryption selector</li>
     *  <li>en_de - Local button group for radios</li>
     *  <li>{@link Form#srcTxt} - Text field for source file path</li>
     *  <li>{@link Form#pwdTxt} - Password field</li>
     *  <li>{@link Form#srcLabel} - Label for srcTxt</li>
     *  <li>{@link Form#pwdLabel} - Label for pwdTxt</li>
     *  <li>{@link Form#browseBtn} - Button to launch file chooser</li>
     *  <li>{@link Form#fileChooser} - Default JFileChooser</li>
     *  <li>{@link Form#runBtn} - Button to run encryption/decryption process</li>
     *  <li>{@link Form#log} - Text area to show program output</li>
     *  <li>{@link Form#logScroll} - Make log scrollable</li>
     */
    private void initializeComponents() {
        // Create encryption/decryption radio button
        encryptRadio = new JRadioButton("Encrypt");
        decryptRadio = new JRadioButton("Decrypt");

        // Add radios to group - enable selecting only one at a time
        ButtonGroup en_de = new ButtonGroup();
        en_de.add(encryptRadio);
        en_de.add(decryptRadio);
        encryptRadio.setSelected(true);

        // Create path input field
        srcTxt = new JTextField(30);
        // Create password input field
        pwdTxt = new JPasswordField(30);

        // Create all labels
        srcLabel = new JLabel("Path to file");
        srcLabel.setLabelFor(srcTxt);
        pwdLabel = new JLabel("Password");
        pwdLabel.setLabelFor(pwdTxt);

        // Create file browse button and file chooser
        browseBtn = new JButton("...");
        fileChooser = new JFileChooser();

        // Encrypt/Decrypt button
        runBtn = new JButton("Encrypt");

        // Set logging area
        log = new JTextArea(9, 40);
        log.setEditable(false);
        log.setLineWrap(true);
        // Enable log scrolling
        logScroll = new JScrollPane(log);
    }

    /**
     * Arranges items in prescribed layout.
     * <p>
     *     The first row contains two radio buttons for selecting an operation - encryption or decryption.
     *     Second row contains a label, text input field and file browser button.
     *     Third row contains another label and a password input field.
     *     Fourth row consists of a button on the right edge of the container.
     *     Rest of the space will be covered by logging area.
     * </p>
     */
    private void buildLayout() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JPanel radioSection = new JPanel();
        radioSection.setLayout(new FlowLayout());
        radioSection.add(encryptRadio);
        radioSection.add(decryptRadio);

        JPanel mainSection = new JPanel();
        mainSection.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Label for source path input
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainSection.add(srcLabel, gbc);
        // Text field for source path
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainSection.add(srcTxt, gbc);
        // File browser button
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        mainSection.add(browseBtn, gbc);
        // Label for password input
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainSection.add(pwdLabel, gbc);
        // Password Field
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainSection.add(pwdTxt, gbc);
        // Encryption/Decryption button
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainSection.add(runBtn, gbc);

        this.add(radioSection);
        this.add(mainSection);
        this.add(logScroll);
    }

    /**
     * Creates all required action listeners.
     * Following actions are defined:
     * <li>Change text of runBtn on clicking encryptRadio</li>
     * <li>Change text of runBtn on clicking decryptRadio</li>
     * <li>Launch file browser dialog on clicking browseBtn</li>
     * <li>Encrypt/Decrypt on clicking runBtn</li>
     */
    private void createListeners() {
        encryptRadio.addActionListener(e -> runBtn.setText("Encrypt"));
        decryptRadio.addActionListener(e -> runBtn.setText("Decrypt"));
        browseBtn.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                srcTxt.setText(path);
            } else {
                System.out.println("Can't open file");
            }
        });
        runBtn.addActionListener(e -> {
            String srcPath = srcTxt.getText();
            // getPassword() returns a char[]
            String password = new String(pwdTxt.getPassword());
            if (srcPath.length() == 0)
                System.out.println("No source file selected");
            else if (password.length() == 0)
                System.out.println("Provide a password");
            else {
                if (encryptRadio.isSelected()) {
                    Encryptor en = new Encryptor(srcPath, password);
                    en.encrypt();
                }
                else {
                    Decryptor de = new Decryptor(srcPath, password);
                    de.decrypt();
                }
            }
        });
    }

    /**
     * @return {@link Form#log}
     */
    public JTextArea getLog() {
        return log;
    }
}
