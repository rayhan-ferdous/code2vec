import fr.univartois.cril.xtext.alloyplugin.api.Util;

import fr.univartois.cril.xtext.alloyplugin.preferences.AlloyPreferencePage;



/**

 * An example showing how to create a multi-page editor. This example has 3

 * pages:

 * <ul>

 * <li>page 0 contains a nested text editor.

 * <li>page 1 allows you to change the font used in page 2

 * <li>page 2 shows the words in page 0 in sorted order

 * </ul>

 */

public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener {
