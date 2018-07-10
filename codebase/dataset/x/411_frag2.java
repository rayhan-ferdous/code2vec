                        if (isVoice != null && isVoice.trim().equals("true")) {

                            voiceJarURLs.add(jarURL);

                        }

                    }

                }

            }

            return voiceJarURLs;

        } catch (java.net.URISyntaxException e) {

            throw new Error("Error reading directory name '" + dirName + "'.");

        } catch (MalformedURLException e) {

            throw new Error("Error reading jars from directory " + dirName + ". ");
