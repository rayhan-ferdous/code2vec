    public void run() {

        try {

            if (matrix == null) {

                loadBlock();

            } else {

                serializeBlock();

            }

        } catch (Exception e) {

            e.printStackTrace();

            savedException = e;

        } catch (Throwable t) {

            savedException = new Exception(t);

        }

        System.out.println(LanguageTraslator.traslate("487"));

        System.out.println(LanguageTraslator.traslate("488") + blockFileName);

        System.gc();

        System.gc();

        System.gc();

        System.gc();

        System.gc();

        System.out.println("5 gc()" + blockFileName);

    }
