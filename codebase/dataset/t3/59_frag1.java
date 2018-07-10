        return resource;

    }



    /**

	 * Runs the wizard in a dialog.

	 * 

	 * @generated

	 */

    public static void runWizard(Shell shell, Wizard wizard, String settingsKey) {

        IDialogSettings pluginDialogSettings = CescsmodelDiagramEditorPlugin.getInstance().getDialogSettings();

        IDialogSettings wizardDialogSettings = pluginDialogSettings.getSection(settingsKey);

        if (wizardDialogSettings == null) {

            wizardDialogSettings = pluginDialogSettings.addNewSection(settingsKey);

        }

        wizard.setDialogSettings(wizardDialogSettings);

        WizardDialog dialog = new WizardDialog(shell, wizard);

        dialog.create();

        dialog.getShell().setSize(Math.max(500, dialog.getShell().getSize().x), 500);

        dialog.open();

    }



    /**

	 * @generated

	 */

    public static Resource createDiagram(URI diagramURI, URI modelURI, IProgressMonitor progressMonitor) {
