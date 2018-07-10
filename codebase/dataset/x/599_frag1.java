    private boolean doLdapLogin(String username, String password) {

        Hashtable<String, String> authEnv = new Hashtable<String, String>(11);

        String base = "ou=People, o=tsisolutions";

        String dn = "uid=" + username + "," + base;

        String ldapURL = "ldap://fds.int.traserv.com:389";

        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        authEnv.put(Context.PROVIDER_URL, ldapURL);

        authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

        authEnv.put(Context.SECURITY_PRINCIPAL, dn);

        authEnv.put(Context.SECURITY_CREDENTIALS, password);

        try {

            DirContext authContext = new InitialDirContext(authEnv);

            authContext.close();

            System.out.println("Authentication Success!");

        } catch (AuthenticationException authEx) {

            System.out.println("Authentication failed!");

        } catch (NamingException namEx) {

            System.out.println("Something went wrong!");

            namEx.printStackTrace();

        }

        return true;

    }
