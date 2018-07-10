            } catch (Exception e) {

                throw new IllegalArgumentException("Not an HTTP URL");

            } finally {

                if (is != null) is.close();

                if (dstream != null) dstream.close();

                if (c != null) c.close();

            }
