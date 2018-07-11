            if (code == null) throw new IOException();

        } catch (IOException e) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: " + getName() + ". Error deciphering code: " + e);

            return null;

        }

        try {

            InputStream certificateIS = getClass().getClassLoader().getResourceAsStream(JAR_AGENT_CERT);

            if ((certificateIS) == null) throw new IOException();

            publicKey = readFully(certificateIS);

        } catch (IOException e) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: " + getName() + ". Error reading agent public key: " + e);

            return null;

        }

        code = verifyCode(code, publicKey);
