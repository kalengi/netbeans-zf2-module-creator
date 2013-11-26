/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alex Githatu
 */
public class ZF2ModuleDirectory {
    
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', ' ' };
    private String directoryName;
    private String parentDirectory;
    private File directory;
    private Element moduleDefinitionSection;
    private String templateDirectory;
    private Map<String, ZF2ModuleDirectory> children;
    private ZF2Module module;

    
    public ZF2ModuleDirectory(String parentDirectory, String directoryName) {
        setParentDirectory(parentDirectory);
        setDirectoryName(directoryName);
        createDirectory();
        children = new HashMap<String, ZF2ModuleDirectory>();
    }

    public ZF2ModuleDirectory(ZF2Module parentModule, String modulePath, Document moduleDefinition, String templateDirectory) throws XPathExpressionException, IOException {
        this.module = parentModule;
        setParentDirectory(modulePath);
        setTemplateDirectory(templateDirectory);
        
        XPath xPath =  XPathFactory.newInstance().newXPath();
        
        String query = "/module";
        Node moduleNode = (Node) xPath.compile(query).evaluate(moduleDefinition, XPathConstants.NODE);
        if(moduleNode != null){
            //setup module
            moduleDefinitionSection = (Element) moduleNode;
            /*Element moduleNodeElement = (Element) moduleNode;*/
            String elementName = moduleDefinitionSection.getAttribute("name");
            setDirectoryName(elementName);
        }
        else {
            //setup module directories
            
        }
        
        createDirectory();
        
        //files
        NodeList fileNodes = moduleDefinitionSection.getElementsByTagName("file");
        if (fileNodes.item(0) != null) {
            for(int i = 0; i < fileNodes.getLength(); i++){
                if(((Node)moduleDefinitionSection).equals(fileNodes.item(i).getParentNode())){
                    if (fileNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element fileElement = (Element) fileNodes.item(i);

                        String fileName = fileElement.getAttribute("name");
                        String fileType =  fileElement.getAttribute("type");
                        String completeCode = "No code found";
                        switch (fileType.toLowerCase()) {
                            case "template":
                                STGroup templates = new STRawGroupDir(templateDirectory, '$', '$');
                                //STGroup.verbose = true;
                                //String templateName = "Module";
                                ST fileTemplate = templates.getInstanceOf(fileName);
                                if(fileTemplate == null){
                                    String message = "The template " + fileName + ".st could not be loaded";
                                    throw new IllegalStateException(message);

                                }
                                fileTemplate.add("ModuleName", module.getModuleName());
                                completeCode = fileTemplate.render();
                                break;
                            case "source":
                                String filePath = templateDirectory + File.separator + fileName + ".php";
                                File sourceFile = new File(filePath);
                                try (BufferedReader fileReader = new BufferedReader(new FileReader(sourceFile))) {
                                    completeCode = "";
                                    String line;
                                    while((line = fileReader.readLine()) != null){
                                        completeCode += line + System.lineSeparator(); // System.getProperty("line.separator");
                                    }
                                }
                                break;

                        }


                        String filePath = parentDirectory + File.separator + directoryName + File.separator + fileName + ".php";
                        File targetFile = new File(filePath);
                        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(targetFile))) {
                            fileWriter.write(completeCode);
                        }
                    }
                }
            }
        }
        
    }
    
    /**
     * Set the value of templateDirectory
     *
     * @param templateDirectory new value of templateDirectory
     */
    public final void setTemplateDirectory(String templateDirectory) throws IllegalArgumentException {
        File templateDirectoryChecker = new File(templateDirectory);
        
        if(!templateDirectoryChecker.isDirectory()){
            throw new IllegalArgumentException("The supplied template directory is not valid");
        }
        
        this.templateDirectory = templateDirectory;
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
