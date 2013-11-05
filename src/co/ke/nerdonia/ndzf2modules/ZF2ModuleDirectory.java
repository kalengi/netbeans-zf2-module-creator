/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alex
 */
public class ZF2ModuleDirectory {
    
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', ' ' };
    private String directoryName;
    private String parentDirectory;
    private File directory;

    private Map<String, ZF2ModuleDirectory> children;

    
    public ZF2ModuleDirectory(String parentDirectory, String directoryName) {
        setParentDirectory(parentDirectory);
        setDirectoryName(directoryName);
        createDirectory();
        children = new HashMap<String, ZF2ModuleDirectory>();
    }

    /**
     * Create directory
     *
     * 
     */
    private void createDirectory() {
        directory = new File(parentDirectory, directoryName);
        directory.mkdir();
    }
    
    
    /**
     * Check the validity of directoryName
     *
     * @param directoryName directoryName to be checked
     * 
     * @return true if valid, false if invalid
     */
    private boolean isFileNameValid(String directoryName) {
        for(int i = 0; i < ILLEGAL_CHARACTERS.length; i++){
            if(directoryName.contains(Character.toString(ILLEGAL_CHARACTERS[i]))){
                return false;
            }
        }
        
        return true;
    }
    
    
    /**
     * Add a child directory
     *
     * @param child child directory to be added
     * 
     * @return newly created ZF2ModuleDirectory object
     */
    public ZF2ModuleDirectory addChild(String child) {
        String parentPath = directory.getPath();
        ZF2ModuleDirectory childDirectory = new ZF2ModuleDirectory(parentPath, child);
        
        children.put(child, childDirectory);
        return children.get(child);
    }
    
    /**
     * Get the value of directory
     *
     * @return the value of directory
     */
    public File getDirectory() {
        return directory;
    }
    
    
    /**
     * Get the value of children
     *
     * @return the value of children
     */
    public Map<String, ZF2ModuleDirectory> getChildren() {
        return children;
    }

    /**
     * Get the value of parentDirectory
     *
     * @return the value of parentDirectory
     */
    public String getParentDirectory() {
        return parentDirectory;
    }

    /**
     * Set the value of parentDirectory
     *
     * @param parentDirectory new value of parentDirectory
     */
    public final void setParentDirectory(String parentDirectory) throws IllegalArgumentException {
        File parentDirectoryChecker = new File(parentDirectory);
        
        if(!parentDirectoryChecker.isDirectory()){
            throw new IllegalArgumentException("The supplied path is not valid");
        }
        
        this.parentDirectory = parentDirectory;
    }

    /**
     * Get the value of directoryName
     *
     * @return the value of directoryName
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Set the value of directoryName
     *
     * @param directoryName new value of directoryName
     */
    public final void setDirectoryName(String directoryName) throws IllegalArgumentException {
        if(!isFileNameValid(directoryName)){
            throw new IllegalArgumentException("Invalid directory name"); 
        }
        
        this.directoryName = directoryName;
    }

}
