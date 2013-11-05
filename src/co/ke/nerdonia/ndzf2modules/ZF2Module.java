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
        
        //String moduleDirectoryPath = 
        File moduleDirectory = new File(modulePath, moduleName);
       
        moduleDirectory.mkdir();
        
        
        int stopHere = 44;
        
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
}
