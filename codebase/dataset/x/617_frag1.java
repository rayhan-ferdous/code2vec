                scanDir(files, dir, getRootDir(dir), Class.forName(superClass));

            } catch (ClassNotFoundException e) {

                e.printStackTrace(System.err);

            }

        }

        return files;

    }



    /**

	 * Returns first directory part (ending with fileSep) - without package subdirectories.

	 * Is needed only for -super option.

	 * @param dir - full path

	 */

    protected final String getRootDir(String dir) {

        String last;

        do {

            if (dir.endsWith(fileSep)) dir = dir.substring(0, dir.length() - 1);
