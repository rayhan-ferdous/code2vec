    public void _setFileNamePrefix(String rPrefix) {

        if (null == rPrefix) {

            return;

        }

        writer__iFileNamePrefix = rPrefix;

    }



    /** Get prefix of files to be written. */

    public String _getFileNamePrefix() {

        return writer__iFileNamePrefix;

    }



    /** Set the suffix of the files to be written.

  *  @param rSuffix Written files suffix.

  */

    public void _setFileNameSuffix(String rSuffix) {

        if (null == rSuffix) {

            return;

        }

        writer__iFileNameSuffix = rSuffix;

    }



    /** Get suffix of files to be written. */

    public String _getFileNameSuffix() {

        return writer__iFileNameSuffix;

    }



    /** Set the full name of the file to be written.

  *  Prefix and Suffix are set to empty

  *  @param rName Full name of the file to write.

  */

    public void _setFullFileName(String rName) {

        _setFileNamePrefix("");

        _setFileNameRoot(rName);

        _setFileNameSuffix("");

    }



    /** Get the full name of current file being generated. */

    public String _getFullFileName() {

        return _getFileNamePrefix() + _getFileNameRoot() + _getFileNameSuffix();

    }



    /** Set the names of the files to be written.

  *  Prefix and Suffix are set to empty

  *  @param rName Full name of the file to write.

  */

    public void _setFullFileNames(String[] rNames) {

        _setFileNamePrefix("");

        _setFileNameRoots(rNames);

        _setFileNameSuffix("");

    }



    /** Get the full names of the files to be written. */

    public String[] _getFullFileNames() {

        String[] fileNameRoots = _getFileNameRoots();

        int numFiles = fileNameRoots.length;

        String[] fullFileNames = new String[numFiles];

        String fileNamePrefix = _getFileNamePrefix();

        String fileNameSuffix = _getFileNameSuffix();

        for (int fileI = 0; fileI < numFiles; fileI++) {

            fullFileNames[fileI] = fileNamePrefix + fileNameRoots[fileI] + fileNameSuffix;

        }

        return fullFileNames;

    }



    /** Set the root of the name of the file to be written.

  *  @param rFileNameRoot Root of the name of file to be written.

  */

    public void _setFileNameRoot(String rFileNameRoot) {

        if (null == rFileNameRoot) {

            return;

        }

        _setFileNameRoots(new String[] { rFileNameRoot });

    }



    /** Get the root of the name of current file being generated. */

    public String _getFileNameRoot() {

        if (0 < writer__iFileNameRoots.length) {

            return writer__iFileNameRoots[writer__iCurrentFileIndex];

        }

        return "";

    }



    /** Set the roots of the names of the files to be written.

  *  @param rFileNameRoots Roots of names of files to be written.

  */

    public void _setFileNameRoots(String[] rFileNameRoots) {

        if (null == rFileNameRoots) {

            return;

        }

        String[] roots = (String[]) rFileNameRoots.clone();

        int numRoots = roots.length;

        for (int rootI = 0; rootI < numRoots; rootI++) {

            if (null == roots[rootI]) {

                roots[rootI] = "";

            }

        }

        writer__iFileNameRoots = roots;

        writer__iNumFiles = numRoots;

    }



    /** Get roots of the names of files to be written. */

    public String[] _getFileNameRoots() {

        return writer__iFileNameRoots;

    }



    /** Get index of file currently being generated. */

    public int _getFileIndex() {

        return writer__iCurrentFileIndex;

    }



    /** Get number of generated files. */

    public int _getNumFiles() {

        return writer__iNumFiles;

    }



    /** Set output folder.

  *  @param rOutputFolder Folder to output generated code to.

  */

    public void _setOutputFolder(String rOutputFolder) {

        writer__iOutputFolder = rOutputFolder;

    }



    /** Get output folder. */

    public String _getOutputFolder() {

        return writer__iOutputFolder;

    }



    /** Set backup folder.

  *  @param rBackupFolder Folder to backup overwritten files to.

  */

    public void _setBackupFolder(String rBackupFolder) {

        writer__iBackupFolder = writer__iOutputFolder + "\\" + rBackupFolder;

    }



    /** Get backup folder. */

    public String _getBackupFolder() {

        return writer__iBackupFolder;

    }



    /** Set the suffix of backup files.

  *  @param rSuffix Backup files suffix.

  */

    public void _setBackupSuffix(String rSuffix) {

        if (null == rSuffix) {

            return;

        }

        writer__iBackupSuffix = rSuffix;

    }



    /** Set the prefix of backup files.

  *  @param rPrefix Backup files prefix.

  */

    public void _setBackupPrefix(String rPrefix) {

        if (null == rPrefix) {

            return;

        }

        writer__iBackupPrefix = rPrefix;

    }



    /** Set to true if written files are to be backed up to disk automatically.

  *  @param rBackup True => Backup files to disk.

  */

    public void _backup(boolean rBackup) {

        writer__iBackup = rBackup;

    }



    /** Set to true if written files are to be saved to disk automatically.

  *  @param rSave True => Save written files to disk.

  */

    public void _save(boolean rSave) {

        writer__iSave = rSave;

    }



    /** Get compile time property

  *  @param rName Name of property to get.

  */

    public String _getProperty(String rName) {

        String result = "";

        if (!writer__iPropertiesInitialised) {

            writer__initProperties();

        }

        if (writer__iProperties.containsKey(rName)) {

            result = (String) writer__iProperties.get(rName);

        }

        return result;

    }



    /** Get first user arg - that is, first arg with no writer__ARGUMENT_CONTROL_PREFIX. */

    public String _getFirstUserArg() {

        return _getUserArg(0);

    }



    /** Get second user arg - that is, second arg with no writer__ARGUMENT_CONTROL_PREFIX. */

    public String _getSecondUserArg() {

        return _getUserArg(1);

    }



    /** Get third user arg - that is, third arg with no writer__ARGUMENT_CONTROL_PREFIX. */

    public String _getThirdUserArg() {

        return _getUserArg(2);

    }



    /** Get user arg at specified ordinal.

  *  @param rOrdinal ordinal of user arg to get.

  */

    public String _getUserArg(int rOrdinal) {

        if (null == writer__iArgs) {

            return "";

        }

        int ordinal = 0;

        int numArgs = writer__iArgs.length;

        next_arg: for (int argI = 0; argI < numArgs; argI++) {

            if (writer__iArgs[argI].startsWith(writer__ARGUMENT_CONTROL_PREFIX)) {

                continue next_arg;

            } else {

                if (ordinal == rOrdinal) {

                    return writer__iArgs[argI];

                } else {

                    ordinal++;

                }

            }

        }

        return "";

    }



    /** Get command line arguments to CodeWriter. */

    public String[] _getArgs() {

        return writer__iArgs;

    }



    /** Get number of command line arguments to CodeWriter. */

    public int _getNumArgs() {

        return writer__iNumArgs;

    }



    /** Insert text into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rText Text to insert.

  */

    public void _insert(String rText) {

        writer__iCurrentText.append(rText);

    }



    /** Insert string representation of object into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rObject Object to insert.

  */

    public void _insert(Object rObject) {

        writer__iCurrentText.append("" + rObject);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rInt int to insert

  */

    public void _insert(int rInt) {

        writer__iCurrentText.append(rInt);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rLong long to insert

  */

    public void _insert(long rLong) {

        writer__iCurrentText.append(rLong);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rShort short to insert

  */

    public void _insert(short rShort) {

        writer__iCurrentText.append(rShort);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rByte byte to insert

  */

    public void _insert(byte rByte) {

        writer__iCurrentText.append(rByte);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rDouble double to insert

  */

    public void _insert(double rDouble) {

        writer__iCurrentText.append(rDouble);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rFloat float to insert

  */

    public void _insert(float rFloat) {

        writer__iCurrentText.append(rFloat);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rChar char to insert

  */

    public void _insert(char rChar) {

        writer__iCurrentText.append(rChar);

    }



    /** Insert string representation of primitive data type into written file.

  *  Abbreviated by <%=foobar%>.

  *  @param rBoolean boolean to insert

  */

    public void _insert(boolean rBoolean) {

        writer__iCurrentText.append(rBoolean);

    }



    /** Create a String containing specified number of spaces.

  *  @param rNumSpaces Number of spaces to place in String

  */

    public String _spaces(int rNumSpaces) {

        int numSpaces = rNumSpaces;

        if (0 > numSpaces) {

            numSpaces *= -1;

        }

        StringBuffer spaces = new StringBuffer(numSpaces);

        for (int spaceI = 0; spaceI < numSpaces; spaceI++) {

            spaces.append(" ");

        }

        return spaces.toString();

    }



    /** Left align String with spaces. */

    public String _left(String rText, int rColWidth) {

        return _align(rText, " ", rColWidth, 'l');

    }



    /** Right align String with spaces. */

    public String _right(String rText, int rColWidth) {

        return _align(rText, " ", rColWidth, 'r');

    }



    /** Center align String with spaces. */

    public String _center(String rText, int rColWidth) {

        return _align(rText, " ", rColWidth, 'c');

    }



    /** Align text within background text to specified column width.

  *  Alignment can be 'l': left, 'c': center, 'r': right

  */

    public String _align(String rText, String rBackText, int rColWidth, char rAlignment) {

        String result = rText;

        if (null == rText) {

            result = "";

        } else if (null != rBackText) {

            try {

                int textLen = rText.length();

                if (rColWidth > textLen) {

                    int backTextLen = rBackText.length();

                    int remainWidth = rColWidth - textLen;

                    int backTextRepeats = remainWidth / backTextLen;

                    int backTextRemain = remainWidth % backTextLen;

                    String back = "";

                    for (int backTextI = 0; backTextI < backTextRepeats; backTextI++) {

                        back = back + rBackText;

                    }

                    back = back + rBackText.substring(0, backTextRemain);

                    switch(rAlignment) {

                        case 'l':

                            result = result + back;

                            break;

                        case 'c':

                            result = back.substring(0, (back.length() / 2)) + result + back.substring((back.length() / 2));

                            break;

                        case 'r':

                            result = back + result;

                            break;

                    }

                }

            } catch (Exception e) {

                result = rText;

            }

        }

        return result;

    }



    /** Set current text of file currently being generated. */

    public void _setText(String rText) {

        writer__iCurrentText = new StringBuffer(rText);

    }



    /** Get current text of file currently being generated. */

    public String _getText() {

        return writer__iCurrentText.toString();

    }

}
