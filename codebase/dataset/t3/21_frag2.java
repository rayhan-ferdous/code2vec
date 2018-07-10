    public void test_getProvider() {

        for (int i = 0; i < digestAlgs.length; i++) {

            try {

                Provider p = MessageDigest.getInstance(digestAlgs[i], providerName).getProvider();

                assertNotNull("provider is null", p);

            } catch (NoSuchAlgorithmException e) {

                fail("getInstance did not find algorithm " + digestAlgs[i]);

            } catch (NoSuchProviderException e) {

                fail("getInstance did not find provider " + providerName);

            }

        }

    }
