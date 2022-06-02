import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

/*
TODO look into improper rendering of nimbus, metal and others
TODO check to ensure overwriting does not occur for file
TODO look into the exact function of "load"
 */

public class GUI_RISCV extends JFrame
{
    public static final String LAF_JSON_KEY = "Look and Feel";
    public static final String DEFAULT_DIRECTORY_PATH_JSON_KEY = "Default Directory Path";
    public static final String FILE_TYPE_JSON_KEY = "File Type";
    public static final HashMap<String,String> LOOK_AND_FEEL = get_all_lookas_and_feels();
    private static HashMap<String,String> get_all_lookas_and_feels()
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
        setRegisters();
        this.memorySizeComboBox.setSelectedItem("word");
        setMemory();
        try{paintMemory(1);}catch(Exception e){}
    }
    public static void load_preferences()
    {
        /*
        try to open config.json
        if not found, abort
        else
        String file type
        String default path (check if it exists, if not use system default)
        String Look and Feel (only name, not address, check if the combobox contains it)
         */
        JSONParser jp = new JSONParser();
        try
        {
            JSONObject obj = (JSONObject) jp.parse(new FileReader("config.json"));
            try
            {
                file_type = (String)obj.get(FILE_TYPE_JSON_KEY); // this does not require verification
                String laf = (String)obj.get(LAF_JSON_KEY);
                Path default_directory_path = Paths.get((String)obj.get(DEFAULT_DIRECTORY_PATH_JSON_KEY));
                if(!(Files.isDirectory(default_directory_path) && Files.isWritable(default_directory_path)))throw new Exception("Invalid path for default directory");
                if(LOOK_AND_FEEL.containsKey(laf))look_and_feel=laf;
                else
                {
                    look_and_feel = "Default";
                    throw new Exception("look and feel not rcognized, using default look and feel");

                }
                default_directory = default_directory_path.toFile();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(new JFrame(),e.getMessage()+"\nconfig.json has been corrupted, default settings will be used","Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(new JFrame(),"config.json not found, using default settings","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }
    public void save_and_exit()
    {
        file_type = filetypeTextField.getText();
        main.dispose();
        JSONObject obj = new JSONObject();
        obj.put(DEFAULT_DIRECTORY_PATH_JSON_KEY,default_directory.getAbsolutePath());
        obj.put(FILE_TYPE_JSON_KEY,file_type);
        obj.put(LAF_JSON_KEY,look_and_feel);
        try
        {
            Files.writeString(Paths.get("config.json"), obj.toString());
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
            b.append("Register "+i+"\t"+compiler.Syntax.name_of_register(i)+"\t"+s+"\n");
        }
        this.registersTextArea.setText(b.toString());
    }
    List<String> memory;
    private void setMemory()
    {
        memory = new ArrayList<>();
        int size = get_size(memorySizeComboBox.getSelectedItem().toString());
        for(int i=0;i<processor.Processor.get_memory_size()/size;i++)
        {
            int value=0;
            for(int j=0;j<size;j++)
            {
                value=value<<4;
                value+=processor.Processor.memory(size*i+j);
            }
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
    public GUI_RISCV(String title)
    {
        super(title);
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
                executeButton.setEnabled(true);
            }
        });
        final int[] memory_page = {1}; // an array is used here because it is accessed from inner class
        executeButton.addActionListener(e -> {
            try
            {
                byte[] binary = Files.readAllBytes(Paths.get(execution_source_file.getAbsolutePath()));
                processor.Processor.Read(binary);
                processor.Processor.execute_all();
                setRegisters();
                setMemory();
                paintMemory(memory_page[0]);
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
                        paintMemory(memory_page[0]);
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
            try{paintMemory(memory_page[0]);}catch(Exception ignored){}
        });
        processorResetButton.addActionListener(e -> {
            execution_source_file = null;
            executeFilenameTextField.setText("");
            executeButton.setEnabled(false);
            processor.Processor.reset_instruction();

        });
        memorySignedRadioButton.addActionListener(e -> {setMemory();try{paintMemory(memory_page[0]);}catch(Exception ex){}});
        memorySizeComboBox.addActionListener(e -> {
            setMemory();
            memory_page[0] =1;
            try
            {
                paintMemory(memory_page[0]);
            }catch(Exception ex){}});

        memoryForwardButton.addActionListener(e -> {
            try
            {
                paintMemory(memory_page[0]+1);
                memory_page[0]++;
            }
            catch(Exception ex){}
        });
        memoryBackwardButton.addActionListener(e -> {
            try
            {
                paintMemory(memory_page[0]-1);
                memory_page[0]--;
            }
            catch(Exception ex){}
        });
        memoryAllBackwardButton.addActionListener(e -> {
            memory_page[0]=1;try{paintMemory(memory_page[0]);}catch(Exception ignore){}
        });
        memoryAllForwardButton.addActionListener(e -> {
            memory_page[0]=memory.size()/MEMORY_TEXT_AREA_LENGTH;try{paintMemory(memory_page[0]);}catch(Exception ignore){}
        });
    }
    private JFrame main = this;
    private static String file_type = "s";
    private static File default_directory = new JFileChooser().getCurrentDirectory();
    private static String look_and_feel = "Nimbus";
    public static String get_look_and_feel()
    {
        return look_and_feel;
    }
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