/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(
        location = "org-netbeans-modules-php-project-ui-options-PHPOptionsCategory",
        displayName = "NdZF2",
        keywords = "ndzf2module, zf2, zend framework",
        keywordsCategory = "org-netbeans-modules-php-project-ui-options-PHPOptionsCategory/NdZF2Module")
//@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_NdZF2Module=NdZF2", "AdvancedOption_Keywords_NdZF2Module=ndzf2module, zf2, zend framework"})
public final class NdZF2ModuleOptionsPanelController extends OptionsPanelController {

    private NdZF2ModulePanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    NdZF2ModuleOptionsPanelController(){
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.INFO, "Creating panel option...");
    }
    
    public void update() {
        getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private NdZF2ModulePanel getPanel() {
        if (panel == null) {
            panel = new NdZF2ModulePanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
