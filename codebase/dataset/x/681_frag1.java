                    if (f.exists()) throw new IOException("file exists");

                    ((MimeBodyPart) p).saveFile(f);

                } catch (IOException ex) {

                    pr("Failed to save attachment: " + ex);

                }

                pr("---------------------------");

            }

        }

    }



    public static void dumpEnvelope(Message m) throws Exception {

        pr("This is the message envelope");

        pr("---------------------------");

        Address[] a;
