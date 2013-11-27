/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.prefs.Preferences;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.NbPreferences;
import org.stringtemplate.v4.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Alex Githatu
 */
public class ZF2Module implements Serializable{
    
    private String moduleName; 
    private String modulePath;
    private static final String configFolderName = "config";
    private static final String srcFolderName = "src";
    private static final String viewFolderName = "view";
    private static final String controllerFolderName = "Controller";
    private static final String formFolderName = "Form";
    private static final String modelFolderName = "Model";
    private ZF2ModuleDirectory moduleDirectoryStructure;
    private String moduleDefinitionPath;
    private Document moduleDefinition;
    private String templateDirectory;
    


    


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
    public void create() throws IOException, IllegalStateException, ParserConfigurationException, SAXException, XPathExpressionException {
        
        if(moduleName.isEmpty()){
            throw new IllegalStateException("The module name is not specified"); 
        }
        
        if(modulePath.isEmpty()){
            throw new IllegalStateException("The module path is not specified"); 
        }
        
        if(moduleDefinitionPath.isEmpty()){
            throw new IllegalStateException("The Module Definition path is not specified"); 
        }
        
        Preferences userPreferences = NbPreferences.forModule(NdZF2ModulePanel.class);
        templateDirectory = userPreferences.get("TemplateDirectoryPreference", "");
        
        if(templateDirectory.isEmpty()){
            throw new IllegalStateException("Please specify the Template Directory in the options ");
        }
        
        loadModuleDefinition();
        
        Element moduleElement = moduleDefinition.getDocumentElement();
        moduleDirectoryStructure = new ZF2ModuleDirectory(this, modulePath, moduleElement, templateDirectory);
        
    }

    
    private void loadModuleDefinition() throws ParserConfigurationException, SAXException, IOException {
        File moduleDefinitionFile = new File(moduleDefinitionPath);
        
        //STGroup.verbose = true;
        STGroup templates = new STGroupFile(moduleDefinitionPath);
        String moduleDefinitionTemplateName = FilenameUtils.getBaseName(moduleDefinitionFile.getPath());
        
        ST moduleDefinitionTemplate = templates.getInstanceOf(moduleDefinitionTemplateName);
        moduleDefinitionTemplate.add("ModuleName", moduleName);
        moduleDefinitionTemplate.add("ModuleNameLower", moduleName.toLowerCase());
        String moduleDefinitionXML = moduleDefinitionTemplate.render();
        
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        moduleDefinition = builder.parse(new ByteArrayInputStream(moduleDefinitionXML.getBytes()));
        moduleDefinition.getDocumentElement().normalize();
        
    }
    
    /**
     * Set the value of moduleDefinitionPath
     *
     * @param moduleDefinitionPath new value of moduleDefinitionPath
     */
    public void setModuleDefinitionPath(String moduleDefinitionPath) {
        
        if(moduleDefinitionPath.isEmpty()){
            throw new IllegalArgumentException("The Module Definition path cannot be blank");
        }
        
        File moduleDefinitionChecker = new File(moduleDefinitionPath);

        if(!moduleDefinitionChecker.isFile() || moduleDefinitionChecker.isDirectory()){
            throw new IllegalArgumentException("The Module Definition file is not valid");
        }
        
        
        this.moduleDefinitionPath = moduleDefinitionPath;
    }

    /**
     * Get the value of moduleDefinitionPath
     *
     * @return the value of moduleDefinitionPath
     */
    public String getModuleDefinitionPath() {
        return moduleDefinitionPath;
    }

    /**
     * Get the value of moduleDirectoryStructure
     *
     * @return the value of moduleDirectoryStructure
     */
    public ZF2ModuleDirectory getModuleDirectoryStructure() {
        return moduleDirectoryStructure;
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
    public final void setModuleName(String moduleName)  {
                
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
    public void setModulePath(String modulePath) {
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
