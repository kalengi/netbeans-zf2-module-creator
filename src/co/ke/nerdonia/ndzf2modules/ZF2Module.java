/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
//import org.openide.filesystems.FileObject;
//import org.openide.util.Exceptions;

/**
 *
 * @author alex githatu
 */
public class ZF2Module implements Serializable{
    
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', ' ' };
    private String moduleName; 
    private String modulePath;
    private static final String configFolderName = "config";
    private static final String srcFolderName = "src";
    private static final String viewFolderName = "view";
    private static final String controllerFolderName = "Controller";
    private static final String formFolderName = "Form";
    private static final String modelFolderName = "Model";
   

    


    public ZF2Module(String moduleName){
        setModuleName(moduleName);
    }
    
    public ZF2Module(){
        
    }

    /**
     * Create ZF2 module
     *
     * 
     */
    public void create() throws IOException, IllegalStateException {
        
        if(moduleName.isEmpty()){
            throw new IllegalStateException("The module name is not specified"); 
        }
        
        if(modulePath.isEmpty()){
            throw new IllegalStateException("The module path is not specified"); 
        }
        
        //main module directory
        File moduleDirectory = new File(modulePath, moduleName);
        moduleDirectory.mkdir();
        
        //config directory
        String moduleFolderPath = moduleDirectory.getPath();
        File configDirectory = new File(moduleFolderPath, configFolderName);
        configDirectory.mkdir();
        
        //src directory
        File srcDirectory = new File(moduleFolderPath, srcFolderName);
        srcDirectory.mkdir();
        
        File subDirectory = new File(srcDirectory.getPath(), moduleName);
        subDirectory.mkdir();
        
        String subDirectoryPath = subDirectory.getPath();
        subDirectory = new File(subDirectoryPath, controllerFolderName);
        subDirectory.mkdir();
        
        subDirectory = new File(subDirectoryPath, formFolderName);
        subDirectory.mkdir();
        
        subDirectory = new File(subDirectoryPath, modelFolderName);
        subDirectory.mkdir();
        
        
        //view directory
        File viewDirectory = new File(moduleFolderPath, viewFolderName);
        viewDirectory.mkdir();
        
        subDirectory = new File(viewDirectory.getPath(), moduleName.toLowerCase());
        subDirectory.mkdir();
        
        subDirectoryPath = subDirectory.getPath();
        subDirectory = new File(subDirectoryPath, moduleName.toLowerCase());
        subDirectory.mkdir();
        
    }

    /**
     * Check the validity of moduleName
     *
     * @param moduleName moduleName to be checked
     * 
     * @return true if valid, false if invalid
     */
    private boolean isFileNameValid(String moduleName) {
        for(int i = 0; i < ILLEGAL_CHARACTERS.length; i++){
            if(moduleName.contains(Character.toString(ILLEGAL_CHARACTERS[i]))){
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get the value of moduleName
     *
     * @return the value of moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Set the value of moduleName
     *
     * @param moduleName new value of moduleName
     */
    public final void setModuleName(String moduleName) throws IllegalArgumentException {
        if(!isFileNameValid(moduleName)){
            throw new IllegalArgumentException("Invalid module name"); 
        }
        
        this.moduleName = moduleName;
    }
    
    /**
     * Get the value of modulePath
     *
     * @return the value of modulePath
     */
    public String getModulePath() {
        return modulePath;
    }

    /**
     * Set the value of modulePath
     *
     * @param modulePath new value of modulePath
     */
    public void setModulePath(String modulePath) throws IllegalArgumentException {
        File moduleFolder = new File(modulePath);
        
        if(!moduleFolder.isDirectory()){
            throw new IllegalArgumentException("The supplied path is not valid");
        }
        
        this.modulePath = modulePath;
    }
    
    /**
     * Get the value of modelFolderName
     *
     * @return the value of modelFolderName
     */
    public static String getModelFolderName() {
        return modelFolderName;
    }

    /**
     * Get the value of formFolderName
     *
     * @return the value of formFolderName
     */
    public static String getFormFolderName() {
        return formFolderName;
    }

    /**
     * Get the value of controllerFolderName
     *
     * @return the value of controllerFolderName
     */
    public static String getControllerFolderName() {
        return controllerFolderName;
    }

    /**
     * Get the value of viewFolderName
     *
     * @return the value of viewFolderName
     */
    public static String getViewFolderName() {
        return viewFolderName;
    }

    /**
     * Get the value of srcFolderName
     *
     * @return the value of srcFolderName
     */
    public static String getSrcFolderName() {
        return srcFolderName;
    }

    /**
     * Get the value of configFolderName
     *
     * @return the value of configFolderName
     */
    public static String getConfigFolderName() {
        return configFolderName;
    }
}
