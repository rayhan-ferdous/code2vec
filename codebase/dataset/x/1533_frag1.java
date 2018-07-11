        } catch (Exception e) {

            System.err.println(e);

        }

        bis.close();

        outStream.close();

        js.close();

        return sb.toString();

    }



    public static String zir(String zips) throws Exception {

        String s = "", ss = "", sz = "";

        int n = 0;

        File f = new File(zips);
