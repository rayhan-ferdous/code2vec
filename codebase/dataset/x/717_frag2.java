import de.fraunhofer.isst.axbench.rca.advisors.AXBenchActionBarAdvisor;

import de.fraunhofer.isst.axbench.rca.editors.AXLElementEditor;

import de.fraunhofer.isst.axbench.rca.editors.AXLElementEditorInput;



/**

 * @brief Display aXLang model as tree.

 * 

 * This is the simple tree, which is built as follows:

 * - model

 *   - feature model 

 *     - features 

 *   - application model 

 *     - components

 *     - interfaces 

 *   - configuration model

 *     - component configurations

 *     

 * @author Ekkart Kleinod

 * @version 0.3.0

 * @since 0.1.0

 */

public class TreeView extends ViewPart implements ISaveablePart {



    /** Unique id for the perspective. */

    public static final String ID = RCAConstants.ID_VIEW_TREE;
