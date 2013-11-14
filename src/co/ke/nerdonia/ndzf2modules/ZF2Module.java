/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.io.IOException;
import java.io.Serializable;
import org.stringtemplate.v4.*;

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
        
        // Directory structure...
        //main, config
        moduleDirectoryStructure = new ZF2ModuleDirectory(modulePath, moduleName);
        ZF2ModuleDirectory configDirectory = moduleDirectoryStructure.addChild(configFolderName);
        
        //src
        ZF2ModuleDirectory srcDirectory = moduleDirectoryStructure.addChild(srcFolderName);
        ZF2ModuleDirectory srcModuleDirectory = srcDirectory.addChild(moduleName);
        srcModuleDirectory.addChild(controllerFolderName);
        srcModuleDirectory.addChild(formFolderName);
        srcModuleDirectory.addChild(modelFolderName);
        
        //view
        ZF2ModuleDirectory viewDirectory = moduleDirectoryStructure.addChild(viewFolderName);
        ZF2ModuleDirectory viewModuleDirectory = viewDirectory.addChild(moduleName.toLowerCase());
        viewModuleDirectory.addChild(moduleName.toLowerCase());
        
        // Config files...
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String templateFolder = cl.getResource("resources/CodeTemplates/Module.st").getPath();// getClass().getResource("/resources/CodeTemplates").getPath();
        //templateFolder = File.g
        STGroup templates = new STGroupDir(templateFolder);
        ST moduleTemplate = templates.getInstanceOf("Module");
        moduleTemplate.add("ModuleName", moduleName);
        String moduleCode = moduleTemplate.render();
        int letsSee = 200;
        
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
