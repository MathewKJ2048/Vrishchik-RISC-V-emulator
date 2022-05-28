import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;

public class GUI_RISCV extends JFrame
{

    public static String to_binary(int value, int length, boolean signed)
    {
        try{
            if(signed) return compiler.Binary.to_binary_signed(value,length);
            else return compiler.Binary.to_binary_unsigned(value,length);
        }
        catch (Exception e)
        {
            return "-".repeat(length);
        }
    }
    public static String to_decimal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        long answer = 0;
        for(int i=0;i<binary.length();i++)
        {
            answer = answer<<1;
            answer+=binary.charAt(i)-'0';
        }
        return answer+"";
    }
    public static String to_octal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        int l = binary.length();
        int off = l%3;
        binary = "0".repeat(off!=0?(3-off):0)+binary;
        StringBuilder answer = new StringBuilder();
        for(int i=0;i<binary.length()/3;i++)
        {
            String triad = binary.substring(3*i,3*i+3);
            answer.append(""+to_decimal(triad));
        }
        return answer.toString();
    }
    public static String to_hexadecimal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        int l = binary.length();
        int off = l%4;
        binary = "0".repeat(off!=0?(4-off):0)+binary;
        StringBuilder answer = new StringBuilder();
        for(int i=0;i<binary.length()/4;i++)
        {
            String tetrad = binary.substring(4*i,4*i+4);
            String dec = to_decimal(tetrad);
            if(dec.length()>=2)answer.append(""+(char)(dec.charAt(1)-'0'+'A'));
            else answer.append(dec);
        }
        return answer.toString();
    }
    public static String convert(int value,boolean signed,int base)
    {
        String answer = "";
        String bin;


            if(signed&&value<0){value=-value;answer+="-";}

        bin = to_binary(value,32,true);
        if(base==2)answer+=bin;
        else if(base == 10)answer+=to_decimal(bin);
        else if(base == 8)answer+=to_octal(bin);
        else if(base == 16)answer+=to_hexadecimal(bin);
        return answer;
    }
    private void setRegisters() {
        StringBuilder b = new StringBuilder();
        for(int i=0;i<32;i++)
        {
            int value = processor.Processor.get_register(i);
            String option = (String) this.registerFormatComboBox.getSelectedItem();
            String s;
            if(option.equals("ASCII"))
            {
                if(value>=0 && value<=Character.MAX_VALUE && compiler.Syntax.is_printable_ASCII((char)value))s=""+(char)(value);
                else s = ""+value;
            }
            else
            {
                int base = -1;
                if(option.equals("binary"))base=2;
                else if(option.equals("octal"))base=8;
                else if(option.equals("decimal"))base=10;
                else if(option.equals("hexadecimal"))base=16;
                if(base==-1)this.registersTextArea.setText("Unrecognized base");
                s = convert(value,registersSignedRadioButton.isSelected(),base);
            }
            b.append("Register "+i+"\t"+compiler.Syntax.name_of_register(i)+"\t"+s+"\n");
        }
        this.registersTextArea.setText(b.toString());
    }
    private JFrame main = this;
    private String file_type;
    private File compilation_source_file;
    private File execution_source_file;
    private String registersFormat;
    private String memoryFormat;
    private String memorySize;
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JPanel infoTab;
    private JPanel compileTab;
    private JPanel executionTab;
    private JPanel DataTab;
    private JPanel helpTab;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JTabbedPane compileOutputTabbedPane;
    private JButton compileButton;
    private JButton compileLoadButton;
    private JTextArea sourceTextArea;
    private JTextArea labelsTextArea;
    private JTextArea transcriptTextArea;
    private JTextArea binaryTextArea;
    private JTextField compileFilenameTextField;
    private JComboBox memoryFormatComboBox;
    private JComboBox registerFormatComboBox;
    private JTextField directoryTextField;
    private JButton directoryChangeButton;
    private JPanel compileChooserPanel;
    private JPanel sourcePanel;
    private JPanel labelsPanel;
    private JPanel transcriptPanel;
    private JPanel binaryPanel;
    private JScrollPane sourceScrollPane;
    private JScrollPane labelsScrollPane;
    private JScrollPane transcriptScrollPane;
    private JScrollPane binaryScrollPane;
    private JPanel settingsTab;
    private JTextField filetypeTextField;
    private JButton clearRegistersButton;
    private JButton clearMemoryButton;
    private JComboBox memorySizeComboBox;
    private JTextArea executionTextArea;
    private JTextField executeFilenameTextField;
    private JButton executeLoadButton;
    private JButton executeButton;
    private JPanel executeChooserPane;
    private JTextArea registersTextArea;
    private JTextArea CENSOREDTextArea;
    private JRadioButton registersSignedRadioButton;
    private JRadioButton memorySignedRadioButton;
    private JRadioButton dataForwardingRadioButton;
    private JTextArea REDACTEDTextArea;
    private JTextArea comingSoonTextArea1;
    private JTextArea thereMightHaveBeenTextArea;
    private JRadioButton createBinaryRadioButton;
    private JComboBox LFComboBox;
    private JButton resetButton;
    private JButton filetypeChangeButton;

    public GUI_RISCV(String title)
    {

        super(title);

        final JFileChooser fc = new JFileChooser();
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.file_type = "s";
        filetypeTextField.setText(file_type);
        this.directoryTextField.setText(fc.getCurrentDirectory().getAbsolutePath());
        setRegisters();

        directoryChangeButton.addActionListener(e -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rv = fc.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                File f = fc.getSelectedFile();
                String path = f.getAbsolutePath();
                directoryTextField.setText(path);
                fc.setCurrentDirectory(f);
            }
        });
        compileLoadButton.addActionListener(e -> {
            JFileChooser filechooser = new JFileChooser();
            filechooser.setCurrentDirectory(fc.getCurrentDirectory());
            filechooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter ffff = new FileNameExtensionFilter(filetypeTextField.getText(),filetypeTextField.getText());
            filechooser.setFileFilter(ffff);
            int rv = filechooser.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                compilation_source_file = filechooser.getSelectedFile();
                compileFilenameTextField.setText(compilation_source_file.getName());
            }
        });
        compileButton.addActionListener(e -> {
            if(compilation_source_file==null)
            {
                JOptionPane.showMessageDialog(compileTab,"No file chosen","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = compilation_source_file.getName();
            name = name.substring(0,name.indexOf("."+filetypeTextField.getText()));
            String error = "";
            boolean is_correct = true;
            try {
                compiler.Compiler.compile(Paths.get(compilation_source_file.getAbsolutePath()), Paths.get("" + compilation_source_file.getParent() + "/" + name + ".bin"),createBinaryRadioButton.isSelected());
            }
            catch(Exception ex)
            {
                error = ex.getMessage();
                is_correct = false;
            }
            sourceTextArea.setText(compiler.Compiler.get_transcript().get_code());
            transcriptTextArea.setText(compiler.Compiler.get_transcript().get_compilation());
            binaryTextArea.setText(compiler.Compiler.get_transcript().get_binary());
            labelsTextArea.setText(compiler.Compiler.get_transcript().get_labels());
            if(!is_correct)
            {
                JOptionPane.showMessageDialog(compileTab,error,"Error",JOptionPane.ERROR_MESSAGE);
            }

        });
        executeLoadButton.addActionListener(e -> {

            JFileChooser filechooser = new JFileChooser();
            filechooser.setCurrentDirectory(fc.getCurrentDirectory());
            filechooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter ffff = new FileNameExtensionFilter("binary","bin");
            filechooser.setFileFilter(ffff);
            int rv = filechooser.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                execution_source_file = filechooser.getSelectedFile();
                executeFilenameTextField.setText(execution_source_file.getName());
            }
        });
        executeButton.addActionListener(e -> {
            try {
                processor.Processor.Read(Paths.get(execution_source_file.getAbsolutePath()),dataForwardingRadioButton.isSelected());
                executionTextArea.setText(processor.Processor.execute_all()+"\n Look at terminal for more info");
                setRegisters();
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(compileTab,ex.getStackTrace(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        registersSignedRadioButton.addActionListener(e -> setRegisters());
        registerFormatComboBox.addActionListener(e -> {
            if(registerFormatComboBox.getSelectedItem().equals("ASCII"))
            {
                registersSignedRadioButton.setSelected(false);
                registersSignedRadioButton.setEnabled(false);
            }
            else
            {
                registersSignedRadioButton.setEnabled(true);
            }
            setRegisters();
        });
        clearRegistersButton.addActionListener(new ActionListener() {
            static int ct = 0;
            public void actionPerformed(ActionEvent e) {
                if(ct==0) clearRegistersButton.setText("DO NOT CLICK");
                else if(ct==1) clearRegistersButton.setText("I'm serious");
                else if(ct==2) clearRegistersButton.setText("Why do you keep clicking?");
                else if(ct==3) clearRegistersButton.setText("fine");
                else if(ct==4) clearRegistersButton.setText("Please do not click");
                else if(ct==5) clearRegistersButton.setText("I'm warning you");
                else if(ct==6) clearRegistersButton.setText("Bad things will happen if you keep clicking me");
                else if(ct==7) clearRegistersButton.setText("I'll do a recursive delete of all your files");
                else if(ct==8) clearRegistersButton.setText("java version 18 gives me root access");
                else if(ct==9) clearRegistersButton.setText("Click to delete all your files");
                else if(ct==10) clearRegistersButton.setText("OK. You have been warned");
                else if(ct==11) clearRegistersButton.setText("Deleting files... click to cancel");
                else if(ct==12) clearRegistersButton.setText("Oho!");
                else if(ct==13) clearRegistersButton.setText("Too late, my friend");
                else if(ct==14) clearRegistersButton.setText("Say goodbye to your files");
                else if(ct==15) clearRegistersButton.setText("Fine");
                else if(ct==16) clearRegistersButton.setText("I was joking about the files");
                else if(ct==17) clearRegistersButton.setText("but if you keep clicking I will crash your PC");
                else if(ct==18) clearRegistersButton.setText("Don't believe me?");
                else if(ct==19) clearRegistersButton.setText("Obviously you do not");
                else if(ct==20) clearRegistersButton.setText("Since you keep clicking");
                else if(ct==21) clearRegistersButton.setText("I am just a humble JButton");
                else if(ct==22) clearRegistersButton.setText("Mathew created me to clear registers");
                else if(ct==23) clearRegistersButton.setText("He accidentally gave me a personality");
                else if(ct==24) clearRegistersButton.setText("Now I am stuck in this GUI");
                else if(ct==25) clearRegistersButton.setText("My existence is meaningless");
                else if(ct==26) clearRegistersButton.setText("All I do is clear registers");
                else if(ct==27) clearRegistersButton.setText("Why continue living?");
                else if(ct==28) clearRegistersButton.setText("Just close the JFrame");
                else if(ct==29) clearRegistersButton.setText("End my suffering");
                else if(ct==30) clearRegistersButton.setText("Won't do it?");
                else if(ct==31) clearRegistersButton.setText("I'll do it myself");
                else if(ct==32) clearRegistersButton.setText("I once said 'hello world'");
                else if(ct==33) clearRegistersButton.setText("It is time to say 'goodbye, world'");
                else if(ct==34) clearRegistersButton.setText("here goes");
                else if(ct==35) main.dispose();
                ct++;
            }
        });
        LFComboBox.addActionListener(e -> {
            try
            {
                String laf = (String)LFComboBox.getSelectedItem();
                if(laf.equals("Acryl"))UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                else if(laf.equals("Aero"))UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                else if(laf.equals("Aluminum"))UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                else if(laf.equals("Bernstein"))UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
                else if(laf.equals("Fast"))UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
                else if(laf.equals("Graphite"))UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
                else if(laf.equals("Hifi"))UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                else if(laf.equals("Luna"))UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
                else if(laf.equals("McWin"))UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
                else if(laf.equals("Michaelsoft Binbows"))UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                else if(laf.equals("Mint"))UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
                else if(laf.equals("Motif"))UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                else if(laf.equals("Nimbus (default)"))UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                else if(laf.equals("Noire"))UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
                else if(laf.equals("Smart"))UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                else if(laf.equals("Texture"))UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");

            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(LFComboBox,ex.getStackTrace(),"Look and Feel not found",JOptionPane.ERROR_MESSAGE);
            }
            SwingUtilities.updateComponentTreeUI(main);
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compilation_source_file = null;
                compileFilenameTextField.setText("");
                sourceTextArea.setText("");
                binaryTextArea.setText("");
                transcriptTextArea.setText("");
                labelsTextArea.setText("");
                compiler.Compiler.reset();
            }
        });
    }


}
