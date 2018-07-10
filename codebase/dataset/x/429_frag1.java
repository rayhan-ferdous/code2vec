public class DefaultModelReader extends AbstractModelReader {



    /** A model containing classes and the corresponding class descriptions. */

    private DescriptionModel model;



    /** The class description under construction. */

    private ClassDescription currentClassDescription;



    /** Information about the class being processed. */

    private BeanInfo currentBeanInfo;



    /** The base URL. */

    private URL baseURL;



    /** The source. */

    private String source;



    /** The multiplex mapping info. */

    private MultiplexMappingInfo multiplexInfo;



    /** The multiplex type info.*/

    private ArrayList multiplexTypeInfos;



    /** Storage for the properties of the current class. */

    private ArrayList propertyList;



    /** Storage for the constructors of the current class. */

    private ArrayList constructorList;



    /**

     * Creates a new model reader.

     */

    public DefaultModelReader() {

        super();

    }



    /**

     * Loads a description model.

     * 

     * @param file  the file name.

     * 

     * @return A description model.

     * 

     * @throws IOException  if there is an I/O problem.

     * @throws ObjectDescriptionException  if there is a problem reading the object descriptions.

     */

    public synchronized DescriptionModel load(final String file) throws IOException, ObjectDescriptionException {

        this.model = new DescriptionModel();

        this.baseURL = new File(file).toURL();
