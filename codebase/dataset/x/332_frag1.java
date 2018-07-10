            systemClassMap.put(aName, aName);

        }

    }



    /**

	 * Returns a string containing the contents of the given file.  Returns an empty string if there

	 * were any errors reading the file.

	 * @param file

	 * 

	 * @return

	 */

    protected static String getText(IFile file) {

        try {

            InputStream in = file.getContents();

            return streamToString(in);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "";
