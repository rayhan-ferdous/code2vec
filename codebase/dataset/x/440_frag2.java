                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

    }



    protected static Hashtable salvaConfigurazione(Hashtable hash) {

        String Sep = System.getProperty("file.separator");

        try {

            FileOutputStream fos = new FileOutputStream(appDataPath() + Sep + "EmailTray" + Sep + "datajm.asc");

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(hash);

            oos.close();
