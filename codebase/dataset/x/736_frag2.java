                    } catch (Exception e2) {

                    }

                }

                if (path_url != null) classpath_urls.addElement(path_url);

            }

        }

        URL[] paths = null;

        if (!classpath_urls.isEmpty()) {

            paths = new URL[classpath_urls.size()];

            for (int i = 0; i < paths.length; i++) paths[i] = (URL) classpath_urls.elementAt(i);
