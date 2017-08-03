package ide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class InterfaceController implements Initializable {
    
    @FXML public TextArea input;
    @FXML public TextArea output;
    @FXML public ProgressBar prog;
    String filePath = "";
    
    @FXML
    public void debugFile(){
        if(!input.getText().equals("")){
            if(filePath.equals("")){
                saveAs();
            }else{
                save();
            }
            Process p;
            try{
                String cmd = "python " + filePath;
                p = Runtime.getRuntime().exec(cmd);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String b = br.readLine();
                output.clear();
                output.setText(b);
                p.waitFor();
                p.destroy();
            } catch (InterruptedException | IOException ex) {
                output.setText(output.getText() + "\nError: " + ex.getMessage());
            }
        }else{
            output.setText(output.getText() + "\nError: " + "An empty file cannot debug");
        }
    }
    
    public void newFile(){
        
    }
    
    public void saveFile(){
        if(filePath.equals("")){
            saveAs();
        }else{
            save();
        }
    }
    
    public void openFile(){
        BufferedReader br = null;
        try {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(null);
            filePath = file.getAbsolutePath();
            br = new BufferedReader(new FileReader(filePath));
            String st;
            while((st=br.readLine()) != null){
                input.appendText(st);
            }
        } catch (FileNotFoundException ex) {
            output.setText(output.getText() + "\nError: " + ex.getMessage());
        } catch (IOException ex) {
            output.setText(output.getText() + "\nError: " + ex.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                output.setText(output.getText() + "\nError: " + ex.getMessage());
            }
        }
        
    }
    
    public void closeAll(){
        System.exit(0);
    }
    
    public void saveAs(){
        try{
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Python", "*.py");
            fc.getExtensionFilters().add(ef);
            File file = fc.showSaveDialog(null);
            filePath = file.getAbsolutePath();
            save();
        }catch(Exception ex){
            output.setText(output.getText() + "\nError: " + ex.getMessage());
        }
    }
    
    public void save(){
        try{
            File file = new File(filePath);
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(input.getText());
                writer.flush();
                writer.close();
            }
        }catch(IOException ex){
            output.setText(output.getText() + "\nError: " + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
