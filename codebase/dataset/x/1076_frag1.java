    public boolean initialize(String dataSourceName) {

        String repositoryQueriesFilename = DATABASE_RESOURCES_PATH + Strings.BAR45 + TYPE + SiteFiles.Suffix.QUERIES;

        InputStream repositoryQueriesInputStream = null;

        javax.naming.Context context;

        javax.naming.Context envContext;

        this.queryRepository = new Properties();

        try {

            context = new InitialContext();

            repositoryQueriesInputStream = Resources.getAsStream(repositoryQueriesFilename);

            this.queryRepository.load(repositoryQueriesInputStream);

            this.queryFieldPrefix = this.queryRepository.getProperty(Database.QueryFields.PREFIX);

            this.queryFieldBindPrefix = this.queryRepository.getProperty(Database.QueryFields.PREFIX_BIND);

            this.queryFieldSuffix = this.queryRepository.getProperty(Database.QueryFields.SUFFIX);

            this.queryFieldBindSuffix = this.queryRepository.getProperty(Database.QueryFields.SUFFIX_BIND);

            this.idRoot = this.queryRepository.getProperty(Database.QueryFields.DATA_ID_ROOT);

            envContext = (javax.naming.Context) context.lookup("java:comp/env");

            this.dataSource = (DataSource) envContext.lookup(dataSourceName);

        } catch (NamingException e) {

            throw new FilesystemException(ErrorCode.FILESYSTEM_READ_FILE, repositoryQueriesFilename, e);

        } catch (IOException e) {

            throw new FilesystemException(ErrorCode.FILESYSTEM_READ_FILE, repositoryQueriesFilename, e);

        } finally {

            StreamHelper.close(repositoryQueriesInputStream);

        }

        return true;

    }
