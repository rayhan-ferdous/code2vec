import org.kalypso.nofdpidss.timeseries.i18n.Messages;

import org.kalypso.nofdpidss.timeseries.wizard.repository.timeseries.worker.CsvSheetImportDataModel.TSM_KEY;



/**

 * @author Dirk Kuch

 */

public class TsmZmlImportWorker implements ICoreRunnableWithProgress {



    private final CsvSheetImportDataModel m_model;



    public TsmZmlImportWorker(final CsvSheetImportDataModel model) {

        m_model = model;

    }



    /**

   * @see org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress#execute(org.eclipse.core.runtime.IProgressMonitor)

   */

    public IStatus execute(final IProgressMonitor monitor) {

        final File src = (File) m_model.getValue(TSM_KEY.eZmlFile);
