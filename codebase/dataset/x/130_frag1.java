import uk.ac.gla.terrier.utility.Files;

import uk.ac.gla.terrier.utility.ApplicationSetup;



/** 

 * Implements a collection that can read arbitrary files on disk. It will 

 * use the file list given to it in the constructor, or it will read the

 * file specified by the property <tt>collection.spec</tt>.

 * @author Craig Macdonald &amp; Vassilis Plachouras

 * @version $Revision: 1.39 $ 

 */

public class SimpleFileCollection implements Collection {
