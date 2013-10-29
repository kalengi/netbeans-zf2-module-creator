/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ke.nerdonia.ndzf2modules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "co.ke.nerdonia.ndzf2modules.ZF2ModuleActionListener")
@ActionRegistration(
        iconBase = "co/ke/nerdonia/ndzf2modules/ZF2_icon.png",
        displayName = "#CTL_ZF2ModuleActionListener")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 375, separatorAfter = 387),
    @ActionReference(path = "Loaders/folder/any/Actions", position = 225, separatorAfter = 237)
})
@Messages("CTL_ZF2ModuleActionListener=New ZF2 Module")
public final class ZF2ModuleActionListener implements ActionListener {

    private final Project context;

    public ZF2ModuleActionListener(Project context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }
}
