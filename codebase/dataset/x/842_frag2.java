import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.ByteArrayOutputStream;

import java.io.OutputStream;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;

import java.util.TreeMap;

import java.util.Map;



/**

 * Overlay on document with another one.<br>

 * e.g. Overlay an invoice with your company layout<br>

 * <br>

 * How it (should) work:<br>

 * If the document has 10 pages, and the layout 2 the following is the result:<br>

 * <pre>

 * Document: 1234567890

 * Layout  : 1212121212

 * </pre>

 * <br>

 *

 * @author Mario Ivankovits (mario@ops.co.at)

 * @author <a href="ben@benlitchfield.com">Ben Litchfield</a>

 *

 * @version $Revision: 1.6 $

 */

public class Overlay {
