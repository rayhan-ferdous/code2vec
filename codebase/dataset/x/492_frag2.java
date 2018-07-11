import org.jfree.xml.writer.AttributeList;

import org.jfree.xml.writer.XMLWriter;



/**

 *

 * @author scsandra

 */

public class GroupIdentificationTask extends AbstractTask {



    private Dataset dataset;



    private double progress = 0.0;



    public GroupIdentificationTask(Dataset dataset) {

        this.dataset = dataset;

    }



    public String getTaskDescription() {

        return "Filtering files with Group Identifiacion Filter... ";
