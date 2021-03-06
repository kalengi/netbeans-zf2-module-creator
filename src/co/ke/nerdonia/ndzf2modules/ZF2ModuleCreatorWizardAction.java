/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbPreferences;
import org.xml.sax.SAXException;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
 //@ActionID(category="ZF2 Module", id="co.ke.nerdonia.ndzf2modules.ZF2ModuleCreatorWizardAction")
 //@ActionRegistration(displayName="New ZF2 Module...")
 //@ActionReference(path="Menu/Tools", position=8)

@ActionID(category = "ZF2 Module", id = "co.ke.nerdonia.ndzf2modules.ZF2ModuleCreatorWizardAction")
@ActionRegistration(iconBase = "co/ke/nerdonia/ndzf2modules/ZF2_icon.png", displayName = "New ZF2 Module...")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 375, separatorAfter = 387),
    @ActionReference(path = "Loaders/folder/any/Actions", position = 225, separatorAfter = 237)
})

public final class ZF2ModuleCreatorWizardAction implements ActionListener {

    private final FileObject context;

    public ZF2ModuleCreatorWizardAction(FileObject context) {
        this.context = context;
        /*if(!context.isFolder()){
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Only works for folders..."));
            System.exit(0);
        }*/
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new ZF2ModuleCreatorWizardPanel1());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("ZF2 Module name");
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            String moduleName = ((ZF2ModuleCreatorWizardPanel1)panels.get(0)).getModuleNameFromVisualPanel();
            
            try{
                createZF2Module(moduleName);
                
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Module " + moduleName + " created in folder " + context.getNameExt()));
            }
            catch(Exception ex){
                String message = "Module creation failed with the error: " + ex.getMessage();
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(message));
            }
            
        }
    }

    private void createZF2Module(String moduleName) throws IOException, IllegalStateException, ParserConfigurationException, SAXException, XPathExpressionException {
        ZF2Module zf2Module = new ZF2Module(moduleName);
        
        zf2Module.setModulePath(context.getPath());
        
        // module definition...
        Preferences userPreferences = NbPreferences.forModule(NdZF2ModulePanel.class);
        String moduleDefinitionPath = userPreferences.get("ModuleDefinitionPreference", "");
        
        if(moduleDefinitionPath.isEmpty()){
            throw new IllegalStateException("Please specify the Module Definition file in the options");
        }
        
        zf2Module.setModuleDefinitionPath(moduleDefinitionPath);
        zf2Module.create();
    }
    
    
}
