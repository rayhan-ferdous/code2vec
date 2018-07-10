                    break;

                default:

                    throw new RuntimeException();

            }

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            exit(e.getMessage(), true);

        }

    }



    private static void putKey(Properties cfg, String s) {

        int pos = s.indexOf(':');

        if (pos == -1) {

            cfg.put("key." + s, "");
