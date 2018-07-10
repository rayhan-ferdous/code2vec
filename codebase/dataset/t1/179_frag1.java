            if (keysSet.contains("server.port")) {

                parameters.put(JeeObserverServerContext.SERVER_PORT_PARAMETER, resourceBundle.getString("server.port"));

            }

            if (keysSet.contains("server.database.handler")) {

                parameters.put(JeeObserverServerContext.DATABASE_HANDLER_PARAMETER, resourceBundle.getString("server.database.handler"));

            }

            if (keysSet.contains("server.database.driver")) {

                parameters.put(JeeObserverServerContext.DATABASE_DRIVER_PARAMETER, resourceBundle.getString("server.database.driver"));

            }

            if (keysSet.contains("server.database.url")) {

                parameters.put(JeeObserverServerContext.DATABASE_URL_PARAMETER, resourceBundle.getString("server.database.url"));

            }

            if (keysSet.contains("server.database.user")) {

                parameters.put(JeeObserverServerContext.DATABASE_USER_PARAMETER, resourceBundle.getString("server.database.user"));

            }

            if (keysSet.contains("server.database.password")) {

                parameters.put(JeeObserverServerContext.DATABASE_PASSWORD_PARAMETER, resourceBundle.getString("server.database.password"));

            }

            if (keysSet.contains("server.database.schema")) {

                parameters.put(JeeObserverServerContext.DATABASE_SCHEMA_PARAMETER, resourceBundle.getString("server.database.schema"));

            }

            if (keysSet.contains("server.database.connectionPoolSize")) {

                parameters.put(JeeObserverServerContext.DATABASE_CONNECTION_POOL_SIZE_PARAMETER, resourceBundle.getString("server.database.connectionPoolSize"));

            }

            if (keysSet.contains("server.logger.level")) {
