import org.apache.commons.logging.LogFactory;

import net.sf.appfw.common.util.XMLProperties;



/**

 * A kind of <code>AbstractMessages</code>, uses <code>XMLProperties</code>

 * to load property files of XML format.

 * <p>

 * <b>In Chinese:</b> ��HiveMind��<code>AbstractMessages</code>�����࣬ ������<code>XMLProperties</code>����XML�ļ���װ����Ϣ���塣

 * 

 * @see net.sf.appfw.common.util.XMLProperties

 * 

 * @author Zhengmao Hu

 */

public class XmlMessagesImpl extends AbstractMessages {



    private static final Log _log = LogFactory.getLog(XmlMessagesImpl.class);



    private Properties _properties;



    private Locale _locale;



    /**

	 * Construct a corresponding <code>Messages</code> of the specified class.

	 * "Msgs.xml" is appended to the class name when looking for the property

	 * file. For example, the base name of property file for class

	 * <code>com.test.Test</code> is <code>com/test/TestMsgs.xml</code>.

	 * <code>Locale.getDefault()</code> is referenced to decide the property

	 * file name.

	 * <p>

	 * <b>In Chinese:</b> ����һ����ָ������ͬ������Msgs.xmlΪ��׺����Դ���Ӧ��Messages��

	 * ����com.test.Test��Ӧ�ľ���com/test/TestMsgs.xml�� ʹ��Locale.getDefault()��

	 */

    public XmlMessagesImpl(Class claz) {
