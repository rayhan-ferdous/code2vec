        public void perform(Argument args[], Context context) throws ExtensionException, LogoException {

            try {

                String rname = args[0].getString();

                Object input = args[1].get();

                LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();

                hm.put("put", null);

                hm.put(rname, input);

                rConn.storeObject(hm);

            } catch (Exception ex) {

                throw new ExtensionException("Error in perform. \n" + ex);

            }

        }
