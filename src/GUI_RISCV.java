

import compiler.Binary;
import compiler.Decompiler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUI_RISCV extends JFrame
{
    public static final String LAF_JSON_KEY = "Look and Feel";
    public static final String DEFAULT_DIRECTORY_PATH_JSON_KEY = "Default Directory Path";
    public static final String FILE_TYPE_JSON_KEY = "File Type";
    public static final String FONT_SIZE_JSON_KEY = "Console Font Size";
    public static final HashMap<String,String> LOOK_AND_FEEL = get_all_looks_and_feels();
    public static final HashMap<String,Integer> FONT_SIZES = get_font_sizes();
    public static final String[] Console_elements = new String[]{"TextArea"};
    public static final String[] UI_elements = new String[]{"Label","RadioButton","Button",""};
    private static HashMap<String,Integer> get_font_sizes()
    {
        HashMap<String,Integer> fs = new HashMap<String,Integer>();
        fs.put("Small",12);
        fs.put("Medium",16);
        fs.put("Large",20);
        fs.put("Huge",24);
        return fs;
    }
    private static HashMap<String,String> get_all_looks_and_feels()
    {
        HashMap<String,String> laf = new HashMap<String, String>();
        laf.put("Acryl","com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        laf.put("Aero","com.jtattoo.plaf.aero.AeroLookAndFeel");
        laf.put("Aluminum","com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        laf.put("Bernstein","com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
        laf.put("Fast","com.jtattoo.plaf.fast.FastLookAndFeel");
        laf.put("Graphite","com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        laf.put("HiFi","com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        laf.put("Luna","com.jtattoo.plaf.luna.LunaLookAndFeel");
        laf.put("McWin","com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        laf.put("Metal","javax.swing.plaf.metal.MetalLookAndFeel");
        laf.put("Michaelsoft Binbows","com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        laf.put("Mint","com.jtattoo.plaf.mint.MintLookAndFeel");
        laf.put("Motif","com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        laf.put("Nimbus","javax.swing.plaf.nimbus.NimbusLookAndFeel");
        laf.put("Noire","com.jtattoo.plaf.noire.NoireLookAndFeel");
        laf.put("Smart","com.jtattoo.plaf.smart.SmartLookAndFeel");
        laf.put("Texture","com.jtattoo.plaf.texture.TextureLookAndFeel");
        laf.put("Default","javax.swing.plaf.nimbus.NimbusLookAndFeel");
        return laf;
    }
    public static String get_look_and_feel_location(String look_and_feel) throws Exception
    {
        String location = LOOK_AND_FEEL.get(look_and_feel);
        if(location==null)throw new Exception("Look and Feel not found");
        return location;
    }
    public void set_look_and_feel()
    {
        try
        {
            UIManager.setLookAndFeel(get_look_and_feel_location(look_and_feel));
            System.out.println(ConsoleFont.getSize());
            //TODO solve this issue
            //UIManager.put("TextArea.font", GUI_RISCV.ConsoleFont);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(main,ex.getStackTrace(),"Look and Feel not found",JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.updateComponentTreeUI(main);
        main.pack();
    }
    public void initialize()
    {
        filetypeTextField.setText(file_type);
        this.directoryTextField.setText(default_directory.getAbsolutePath());
        this.LFComboBox.setSelectedItem(look_and_feel);
        //TODO write
        //this.consoleFontComboBox.setSelectedItem(FONT_SIZES.);
        setRegisters();
        this.memorySizeComboBox.setSelectedItem("word");
        setMemory();
        try{paintMemory(1);}catch(Exception e){}
    }
    public static void load_preferences()
    {
        /*
        try to open config.json
        if not found, ask if one can be created.
        else
        String file type
        String default path (check if it exists, if not use system default)
        String Look and Feel (only name, not address, check if the combobox contains it)
         */
        JSONParser jp = new JSONParser();
        try
        {
            JSONObject obj = (JSONObject) jp.parse(new FileReader("program files/config.json"));
            try
            {
                file_type = (String)obj.get(FILE_TYPE_JSON_KEY); // this does not require verification
                Path default_directory_path = Paths.get((String)obj.get(DEFAULT_DIRECTORY_PATH_JSON_KEY));
                if(!(Files.isDirectory(default_directory_path) && Files.isWritable(default_directory_path)))throw new Exception("Invalid path for default directory");
                default_directory = default_directory_path.toFile();
                String laf = (String)obj.get(LAF_JSON_KEY);
                if(LOOK_AND_FEEL.containsKey(laf))look_and_feel=laf;
                else
                {
                    look_and_feel = "Default";
                    throw new Exception("look and feel not rcognized, using default look and feel");

                }
                int FontSize = (int)((long)obj.get(FONT_SIZE_JSON_KEY));
                ConsoleFont = ConsoleFont.deriveFont((float)(FontSize));
            }
            catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(),e.getMessage()+"\nconfig.json has been corrupted, default settings will be used","Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(new JFrame(),"config.json not found, using default settings","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }
    public void set_execution_code()
    {
        int PC_current = processor.Processor.has_instructions()?processor.Processor.PC():0;
        StringBuilder b = new StringBuilder();
        b.append("\tPC\t"+decompiled_binary.get(0)+"\n");
        for(int i=1;i<decompiled_binary.size();i++)
        {
            String pointer = (4*(i-1)==PC_current)?"=>":"";
            b.append(pointer+"\t"+(4*(i-1))+"\t"+decompiled_binary.get(i)+"\n");
        }
        codeTextArea.setText(b.toString());
    }
    public void save_and_exit()
    {
        file_type = filetypeTextField.getText();
        main.dispose();
        JSONObject obj = new JSONObject();
        obj.put(DEFAULT_DIRECTORY_PATH_JSON_KEY,default_directory.getAbsolutePath());
        obj.put(FILE_TYPE_JSON_KEY,file_type);
        obj.put(LAF_JSON_KEY,look_and_feel);
        obj.put(FONT_SIZE_JSON_KEY,ConsoleFont.getSize());
        try
        {
            Files.writeString(Paths.get("program files/config.json"), obj.toString());
        }
        catch(Exception ex){
            ex.printStackTrace();}//no error message here, it will be jarring to see one after closing
        System.exit(0);
    }
    private void setRegisters() {
        StringBuilder b = new StringBuilder();
        for(int i=0;i<32;i++)
        {
            int value = processor.Processor.register(i);
            String option = (String) this.registerFormatComboBox.getSelectedItem();
            String s;
            if(option.equals("ASCII"))
            {
                if(value>=0 && value<=Character.MAX_VALUE && compiler.Syntax.is_printable_ASCII((char)value))s=""+(char)(value);
                else s = ""+value;
            }
            else
            {
                int base = get_base(option);
                if(base==-1)this.registersTextArea.setText("Unrecognized base");
                s = compiler.Binary.convert(value,registersSignedRadioButton.isSelected(),base,32); //register size is always 32 bits
            }
            b.append("R"+i+"\t"+compiler.Syntax.name_of_register(i)+"\t"+s+"\n");
        }
        this.registersTextArea.setText(b.toString());
    }
    List<String> memory;
    private void setMemory() {
        memory = new ArrayList<>();
        int size = get_size(memorySizeComboBox.getSelectedItem().toString());
        for(int i=0;i<processor.Processor.get_memory_size()/size;i++)
        {
            long value=0;
            StringBuilder bytes = new StringBuilder();
            for(int j=0;j<size;j++)
            {
                byte b=processor.Processor.memory(size*i+j);
                try{bytes.append(Binary.to_binary_signed(b,8));}catch (Exception e){}
            }
            value = Binary.from_binary_signed(bytes.toString());
            String option = (String) this.memoryFormatComboBox.getSelectedItem();
            String s;
            if(option.equals("ASCII"))
            {
                if(value>=0 && value<=Character.MAX_VALUE && compiler.Syntax.is_printable_ASCII((char)value))s=""+(char)(value);
                else s = ""+value;
            }
            else
            {
                int base = get_base(option);
                if(base==-1)this.memoryTextArea.setText("Unrecognized base");
                s = compiler.Binary.convert(value,memorySignedRadioButton.isSelected(),base,8*size);
            }
            String index = "["+(size*i+size-1)+":"+(size*i)+"]";
            memory.add(index+(index.length()>=8?"":"\t")+"\t"+s+"\n");
        }
    }
    public static int get_base(String option)
    {
        if(option.equals("binary"))return 2;
        else if(option.equals("octal"))return 8;
        else if(option.equals("decimal"))return 10;
        else if(option.equals("hexadecimal"))return 16;
        return -1;
    }
    public static final int MEMORY_TEXT_AREA_LENGTH = 32;
    public void paintMemory(int num) throws Exception
    {
        int size = MEMORY_TEXT_AREA_LENGTH;
        int start = (num-1)*size;
        int end = (num)*size;
        StringBuilder b = new StringBuilder("");
        for(int i=start;i<end;i++)b.append(memory.get(i));
        memoryTextArea.setText(b.toString());
    }
    public int get_size(String s)
    {
        if(s.equals("byte"))return 1;
        if(s.equals("half"))return 2;
        else if(s.equals("word"))return 4;
        return 0;
    }
    public GUI_RISCV()
    {
        super("Vrishchik");
        ImageIcon icon = new ImageIcon("program files/icon.png");
        this.setIconImage(icon.getImage());
        initialize();
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                save_and_exit();
            }
        });
        final JFileChooser fc = new JFileChooser();
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        class Control
        {
            public volatile boolean is_active = false;
            public volatile boolean to_execute = false;
            public volatile boolean to_execute_all = false;
            public volatile int memory_page = 1;
        }
        final Control control = new Control();
        class ExecutionThread extends Thread
        {
            @Override
            public void run()
            {
                while(control.is_active)
                {
                    if(control.to_execute)
                    {
                        try
                        {
                            executeStepButton.setEnabled(false);
                            if(processor.Processor.is_over()) //TODO look into case where null file is given
                            {
                                executeStepButton.setEnabled(false);   // these must happen only in the case processor is over, not just if thread dies
                                executeAllButton.setEnabled(false);
                                break;
                            }
                            processor.Processor.execute_step();
                            if(processor.Processor.is_frozen())executionTabbedPane.setSelectedIndex(0);//autoshift to console
                            while(processor.Processor.is_frozen())
                            {
                                Environment.process();
                                if(Environment.output!=null)consoleTextArea.append(Environment.output);
                                Environment.output = null;
                            }
                            setRegisters();
                            setMemory();
                            try{paintMemory(control.memory_page);}catch(Exception ex){}
                            set_execution_code();
                            control.to_execute=control.to_execute_all; // this decides whether the execution continues or not
                            if(processor.Processor.is_over())
                            {

                                executeStepButton.setEnabled(false);
                                executeAllButton.setEnabled(false);
                                break;
                            }
                            executeStepButton.setEnabled(true);
                        }
                        catch (Exception ingnored){}
                    }
                }
            }
        }
        directoryChangeButton.addActionListener(e -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rv = fc.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                default_directory = fc.getSelectedFile();
                directoryTextField.setText(default_directory.getAbsolutePath());
                fc.setCurrentDirectory(default_directory);
            }
        });
        compileLoadButton.addActionListener(e -> {
            JFileChooser filechooser = new JFileChooser();
            filechooser.setCurrentDirectory(default_directory);
            filechooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter ffff = new FileNameExtensionFilter(filetypeTextField.getText(),filetypeTextField.getText());
            filechooser.setFileFilter(ffff);
            int rv = filechooser.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                compilation_source_file = filechooser.getSelectedFile();
                compileFilenameTextField.setText(compilation_source_file.getAbsolutePath());
                compileButton.setEnabled(true);
                compileLoadButton.setEnabled(false);
            }
        });
        compileButton.addActionListener(e -> {
            String error = "";
            boolean is_correct = true;
            try
            {
                List<String> lines = Files.readAllLines(Paths.get(compilation_source_file.getAbsolutePath()));
                compiler.Compiler.compile(lines);
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
            else
            {
                JOptionPane.showMessageDialog(compileTab,compilation_source_file.getName()+" has been successfully compiled","Compilation Successful",JOptionPane.INFORMATION_MESSAGE);
                CreateBinaryButton.setEnabled(true);
            }

        });
        executeLoadButton.addActionListener(e -> {

            JFileChooser filechooser = new JFileChooser();
            filechooser.setCurrentDirectory(default_directory);
            filechooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter ffff = new FileNameExtensionFilter("binary","bin");
            filechooser.setFileFilter(ffff);
            int rv = filechooser.showOpenDialog(GUI_RISCV.this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                execution_source_file = filechooser.getSelectedFile();
                executeFilenameTextField.setText(execution_source_file.getName());
                try
                {
                    execution_binary = Files.readAllBytes(Paths.get(execution_source_file.getAbsolutePath()));
                    Decompiler.decompile(execution_binary,10);//TODO look into option to set base
                    decompiled_binary = Decompiler.get_source_lines();
                    set_execution_code();
                    executionTabbedPane.setSelectedIndex(1); // this is to set decompiled code when user loads code
                    processor.Processor.Read(execution_binary);
                    executionStatusLabel.setText("Running");
                    executeLoadButton.setEnabled(false);
                    dataForwardingRadioButton.setEnabled(false);
                    control.is_active = true;
                    executeStepButton.setEnabled(true);
                    executeAllButton.setEnabled(true);
                    enterButton.setEnabled(true);
                    inputTextArea.setEditable(true);
                    ExecutionThread et = new ExecutionThread();
                    et.start();
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(executionTab,ex.getMessage()+"\n"+ex.getStackTrace(),"Error",JOptionPane.ERROR_MESSAGE);
                    executeFilenameTextField.setText("");
                }
            }
        });
        executeStepButton.addActionListener(e -> {
            try
            {
                System.out.println(control.to_execute);
                control.to_execute = true;
                /*
                processor.Processor.execute_step();
                setRegisters();
                setMemory();
                paintMemory(memory_page[0]);
                set_execution_code();

                if(processor.Processor.is_over())executeStepButton.setEnabled(false);
                */
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(executionTab,ex.getStackTrace(),"Error",JOptionPane.ERROR_MESSAGE);
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

        LFComboBox.addActionListener(e -> {
            look_and_feel = LFComboBox.getSelectedItem().toString();
            set_look_and_feel();
        });
        resetButton.addActionListener(e -> {
            compilation_source_file = null;
            compileFilenameTextField.setText("");
            sourceTextArea.setText("");
            binaryTextArea.setText("");
            transcriptTextArea.setText("");
            labelsTextArea.setText("");
            compileButton.setEnabled(false);
            CreateBinaryButton.setEnabled(false);
            compileLoadButton.setEnabled(true);
            compiler.Compiler.reset();
        });
        CreateBinaryButton.addActionListener(e -> {
            if(!compiler.Compiler.is_ready())
            {
                JOptionPane.showMessageDialog(CreateBinaryButton,"Unable to generate binary, syntax errors detected","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            try
            {
                String name = compilation_source_file.getName();
                name = name.substring(0,name.indexOf("."+filetypeTextField.getText()));
                Path destination = Paths.get("" + compilation_source_file.getParent() + "/" + name + ".bin");
                if(destination.toFile().exists()) // allows user to confirm overwriting on existing file
                {
                    if(JOptionPane.showConfirmDialog(compileTab,name+".bin already exists.\nWould you like to overwrite it?")!=JOptionPane.YES_OPTION)return;
                }
                compiler.Compiler.write(destination);
                JOptionPane.showMessageDialog(compileTab,name+".bin has been generated and stored in \n"+compilation_source_file.getParent());
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(CreateBinaryButton,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        clearRegistersButton.addActionListener(e -> {
                    processor.Processor.reset_registers();
                    setRegisters();
                });
        clearMemoryButton.addActionListener(e -> {
                    processor.Processor.reset_memory();
                    setMemory();
                    try
                    {
                        paintMemory(control.memory_page);
                    }catch(Exception ex){}
                });
        memoryFormatComboBox.addActionListener(e -> {
            if(memoryFormatComboBox.getSelectedItem().equals("ASCII"))
            {
                memorySignedRadioButton.setEnabled(false);
                memorySizeComboBox.setSelectedItem("byte");
                memorySizeComboBox.setEnabled(false);
            }
            else
            {
                memorySizeComboBox.setEnabled(true);
                memorySignedRadioButton.setEnabled(true);
            }
            setMemory();
            try{paintMemory(control.memory_page);}catch(Exception ignored){}
        });
        processorResetButton.addActionListener(e -> {
            control.is_active = false; // this ensure that the current thread dies
            control.to_execute = false;
            control.to_execute_all = false;
            execution_source_file = null;
            execution_binary = null;
            decompiled_binary = null;
            Environment.reset();
            codeTextArea.setText("");
            executeFilenameTextField.setText("");
            consoleTextArea.setText("");
            executeStepButton.setEnabled(false);
            executeAllButton.setEnabled(false);
            enterButton.setEnabled(false);
            inputTextArea.setText("");
            inputTextArea.setEditable(false);
            executeLoadButton.setEnabled(true);
            dataForwardingRadioButton.setEnabled(true);
            processor.Processor.reset_instruction();
            executionStatusLabel.setText("Waiting");
        });
        memorySignedRadioButton.addActionListener(e -> {setMemory();try{paintMemory(control.memory_page);}catch(Exception ex){}});
        memorySizeComboBox.addActionListener(e -> {
            setMemory();
            control.memory_page =1;
            try
            {
                paintMemory(control.memory_page);
            }catch(Exception ex){}});

        memoryForwardButton.addActionListener(e -> {
            try
            {
                paintMemory(control.memory_page+1);
                control.memory_page++;
            }
            catch(Exception ex){}
        });
        memoryBackwardButton.addActionListener(e -> {
            try
            {
                paintMemory(control.memory_page-1);
                control.memory_page--;
            }
            catch(Exception ex){}
        });
        memoryAllBackwardButton.addActionListener(e -> {
            control.memory_page=1;try{paintMemory(control.memory_page);}catch(Exception ignore){}
        });
        memoryAllForwardButton.addActionListener(e -> {
            control.memory_page=memory.size()/MEMORY_TEXT_AREA_LENGTH;try{paintMemory(control.memory_page);}catch(Exception ignore){}
        });
        executeAllButton.addActionListener(e -> {
            executeStepButton.setEnabled(false);
            executeAllButton.setEnabled(false);
            control.to_execute_all = true;
            control.to_execute = true;
        });
        //
        threadButton.addActionListener(e -> textArea1.setText("number of active threads:"+Thread.activeCount()));
        PCButton.addActionListener(e -> textArea2.setText("PC:"+processor.Processor.PC()));
        enterButton.addActionListener(e -> {
            String input = inputTextArea.getText();
            inputTextArea.setText("");
            consoleTextArea.append(input); // it is essential that this is carried out first to prevent the execution thread from modifying the console at the same time
            Environment.input = input;
        });
        consoleFontComboBox.addActionListener(e -> {
            int size = FONT_SIZES.get(consoleFontComboBox.getSelectedItem());
            ConsoleFont = ConsoleFont.deriveFont((float) (size));
            set_look_and_feel();
        });
    }
    private JFrame main = this;
    private static String file_type = "s";
    private static File default_directory = new JFileChooser().getCurrentDirectory();
    private static String look_and_feel = "Nimbus";
    private static Font UIFont = new Font("Consolas",Font.PLAIN,12);
    private static Font ConsoleFont = new Font("Consolas",Font.PLAIN,16);
    public static Font get_UI_font()
    {
        return UIFont;
    }
    public static Font get_console_font()
    {
        return ConsoleFont;
    }
    public static String get_look_and_feel()
    {
        return look_and_feel;
    }
    private File compilation_source_file = null;
    private File execution_source_file = null;
    private byte[] execution_binary = null;
    private List<String> decompiled_binary = null;
    private String registersFormat;
    private String memoryFormat;
    private String memorySize;
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JPanel infoTab;
    private JPanel compileTab;
    private JPanel executionTab;
    private JPanel helpTab;
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
    private JTextField executeFilenameTextField;
    private JButton executeLoadButton;
    private JButton executeStepButton;
    private JTextArea registersTextArea;
    private JTextArea memoryTextArea;
    private JRadioButton registersSignedRadioButton;
    private JRadioButton memorySignedRadioButton;
    private JTextArea REDACTEDTextArea;
    private JTextArea comingSoonTextArea1;
    private JTextArea thereMightHaveBeenTextArea;
    private JRadioButton createBinaryRadioButton;
    private JComboBox LFComboBox;
    private JButton resetButton;
    private JButton CreateBinaryButton;
    private JButton processorResetButton;
    private JButton memoryBackwardButton;
    private JButton memoryForwardButton;
    private JButton memoryAllBackwardButton;
    private JButton memoryAllForwardButton;
    private JTextArea codeTextArea;
    private JLabel executionStatusLabel;
    private JButton executeAllButton;
    private JButton enterButton;
    private JButton threadButton;
    private JTabbedPane tabbedPane4;
    private JButton PCButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JPanel executeChooserPane;
    private JTabbedPane dataTabbedPane;
    private JTabbedPane executionTabbedPane;
    private JPanel consoleJPanel;
    private JPanel codeJPanel;
    private JPanel pipelineJPanel;
    private JTextArea pipelineTextArea;
    private JRadioButton dataForwardingRadioButton;
    private JTextArea inputTextArea;
    private JTextArea consoleTextArea;
    private JComboBox UIFontComboBox;
    private JComboBox consoleFontComboBox;
    private JTabbedPane tabbedPane2;
    private JButton ipsumButton;
    private JRadioButton dolorRadioButton;
    private JComboBox comboBox1;
    private JTextArea LIDSATextArea;
    private JButton filetypeChangeButton;

}



/*
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
 */