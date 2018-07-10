            } catch (HsqlException e) {

                printError("Database [index=" + i + ", db=" + dbType[i] + dbPath[i] + ", alias=" + dbAlias[i] + "] did not open: " + e.toString());

                setServerError(e);

                dbAlias[i] = null;

                dbPath[i] = null;

                dbType[i] = null;

                dbProps[i] = null;
