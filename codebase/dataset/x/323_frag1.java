    protected static final String STATEMENT_CHECK_SPECIFIC_LOCK = "SELECT lockat, sessionkey FROM TopicLock WHERE topic = ? AND virtualwiki = ? AND sessionkey = ?";



    protected static final String STATEMENT_REMOVE_LOCK = "DELETE FROM TopicLock WHERE topic = ? AND virtualwiki = ?";



    protected static final String STATEMENT_REMOVE_ANY_LOCK = "DELETE FROM TopicLock WHERE topic = ? AND virtualwiki = ?";



    protected static final String STATEMENT_READONLY_INSERT = "INSERT INTO TopicReadOnly( topic, virtualwiki ) VALUES ( ?, ? )";



    protected static final String STATEMENT_READONLY_DELETE = "DELETE FROM TopicReadOnly WHERE topic = ? AND virtualwiki = ?";



    protected static final String STATEMENT_READONLY_ALL = "SELECT topic FROM TopicReadOnly";



    protected static final String STATEMENT_READONLY_FIND = "SELECT COUNT(*) FROM TopicReadOnly WHERE topic = ? AND virtualwiki = ?";



    protected static final String STATEMENT_GET_ALL_VIRTUAL_WIKIS = "SELECT name FROM VirtualWiki";



    protected static final String STATEMENT_GET_TEMPLATE_NAMES = "SELECT name FROM WikiTemplate WHERE virtualwiki = ?";



    protected static final String STATEMENT_GET_TEMPLATE = "SELECT contents FROM WikiTemplate WHERE virtualwiki = ? AND name = ?";



    protected static final String STATEMENT_ADD_VIRTUAL_WIKI = "INSERT INTO VirtualWiki VALUES(?)";



    protected static final String STATEMENT_PURGE_DELETES = "DELETE FROM Topic WHERE virtualwiki = ? AND (contents = 'delete\n' or contents = '\n' or contents = '')";



    protected static final String STATEMENT_PURGE_TOPIC = "DELETE FROM Topic WHERE virtualwiki = ? AND name = ?";



    protected static final String STATEMENT_TOPICS_TO_PURGE = "SELECT name FROM Topic WHERE virtualwiki = ? AND (contents = 'delete\n' or contents = '\n' or contents = '')";



    protected static final String STATEMENT_ALL_TOPICS = "SELECT name, contents FROM Topic WHERE virtualwiki = ?";



    protected static final String STATEMENT_ALL_OLDER_TOPICS = "SELECT name, contents FROM Topic WHERE virtualwiki = ? AND versionat < ?";



    protected static final String STATEMENT_PURGE_VERSIONS = "DELETE FROM TopicVersion WHERE versionat < ? AND virtualwiki = ?";



    protected static final String STATEMENT_ADD_TEMPLATE = "INSERT INTO WikiTemplate( virtualwiki, name, contents ) VALUES( ?, ?, ? )";



    protected static final String STATEMENT_ADD_TEMPLATE_ORACLE = "INSERT INTO WikiTemplate( virtualwiki, name, contents ) VALUES( ?, ?, EMPTY_CLOB() )";



    protected static final String STATEMENT_TEMPLATE_EXISTS = "SELECT COUNT(*) FROM WikiTemplate WHERE virtualwiki = ? AND name = ?";



    protected static final String STATEMENT_UPDATE_TEMPLATE = "UPDATE WikiTemplate SET contents = ? WHERE virtualwiki = ? AND name = ?";



    protected static final String STATEMENT_UPDATE_TEMPLATE_ORACLE1 = "UPDATE WikiTemplate SET contents = EMPTY_CLOB() WHERE virtualwiki = ? AND name = ?";



    protected static final String STATEMENT_UPDATE_TEMPLATE_ORACLE2 = "SELECT contents FROM WikiTemplate WHERE virtualwiki = ? AND name = ? FOR UPDATE";



    protected static final String STATEMENT_GET_LOCK_LIST = "SELECT * FROM TopicLock WHERE virtualwiki = ?";
