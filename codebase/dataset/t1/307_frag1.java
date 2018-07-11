        Engine.migrate(this.config, 0);

        assertTrue(Execute.tableExists(getConnection(), versionTable));

        assertTrue(3 == _getNumberOfRows(this.config, versionTable));

        assertTrue(Execute.columnExists(getConnection(), ConfigStore.PROJECT_FIELD_NAME, versionTable));

        assertTrue(Execute.columnExists(getConnection(), ConfigStore.VERSION_FIELD_NAME, versionTable));

        assertTrue(2 <= VersionQuery.getVersion(this.config, "com.eroi.migrate.version"));

        assertTrue(0 == VersionQuery.getVersion(this.config, ProjectX.PROJEXT_ID));

        assertTrue(4 == VersionQuery.getVersion(this.config, ConfigStore.PROJECT_ID_UNSPECIFIED));
