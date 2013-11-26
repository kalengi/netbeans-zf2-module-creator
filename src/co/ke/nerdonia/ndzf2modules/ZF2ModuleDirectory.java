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
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//import org.openide.DialogDisplayer;
////import org.openide.NotifyDescriptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;
//import org.w3c.dom.Document;
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

    public ZF2ModuleDirectory(ZF2Module parentModule, String modulePath, Element moduleDefinitionSection, String templateDirectory) throws /*XPathExpressionException,*/ IOException {
        this.module = parentModule;
        this.moduleDefinitionSection = moduleDefinitionSection;
        setParentDirectory(modulePath);
        setTemplateDirectory(templateDirectory);
        //XPath xPath =  XPathFactory.newInstance().newXPath();
        //String query = "/module";
        //Node moduleNode = (Node) xPath.compile(query).evaluate(moduleDefinition, XPathConstants.NODE);
        switch (moduleDefinitionSection.getNodeName()) {
            case "module":
            case "directory":
                String elementName = moduleDefinitionSection.getAttribute("name");
                setDirectoryName(elementName);
                break;  
            default:
                String message = "Unrecognized module definition element: " + moduleDefinitionSection.getNodeName();
                throw new IllegalStateException(message);
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
                                    Logger logger = Logger.getAnonymousLogger();
                                    logger.log(Level.WARNING, message);
                                    //throw new IllegalStateException(message);

                                }
                                else {
                                    fileTemplate.add("ModuleName", module.getModuleName());
                                    completeCode = fileTemplate.render();
                                }
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
                            default:
                                Logger logger = Logger.getAnonymousLogger();
                                logger.log(Level.WARNING, "Unrecognized file type: {0}", fileType);
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
        
        //directories
        NodeList directoryNodes = moduleDefinitionSection.getElementsByTagName("directory");
        if (directoryNodes.item(0) != null) {
            for(int i = 0; i < directoryNodes.getLength(); i++){
                if(((Node)moduleDefinitionSection).equals(directoryNodes.item(i).getParentNode())){
                    if (directoryNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element directoryElement = (Element) directoryNodes.item(i);
                        
                        addChild(directoryElement);
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
     * @param child Element containing the name of the child directory to be added
     * 
     * @return newly created ZF2ModuleDirectory object
     */
    public final ZF2ModuleDirectory addChild(Element child) throws IOException {
        String childName = child.getAttribute("name");
        String parentPath = directory.getPath();
        /* TODO: Add validation to check that the required data has been initialized...*/
        ZF2ModuleDirectory childDirectory = new ZF2ModuleDirectory(this.module, parentPath, child, this.templateDirectory);
        
        if(children == null){
            children = new HashMap<String, ZF2ModuleDirectory>();
        }
        
        children.put(childName, childDirectory);
        return children.get(childName);
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
