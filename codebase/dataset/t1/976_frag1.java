            if (false) {

                throw new Exception(writer__UITEXT_PlaceHolderException);

            }

            String noFirstArgMsg = "Please specify Enumeration name and package or enumeration-spec-file.txt.";

            String[] specObjectNames = new String[] {};

            String specPackage = "";

            String objectSpecFileName = _getFirstUserArg();

            if (0 == objectSpecFileName.length()) {

                usageAndExit(noFirstArgMsg);

            }

            java.io.File objectSpecFile = new java.io.File(objectSpecFileName);

            if (!objectSpecFile.exists()) {

                specObjectNames = new String[] { objectSpecFileName };

                String packageFromArg = _getSecondUserArg();

                if (0 < packageFromArg.length()) {

                    specPackage = "package " + packageFromArg + ";";

                }

            } else {

                Vector specObjectNamesVector = new Vector();

                String line = null;

                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(objectSpecFile));

                while (null != (line = br.readLine())) {

                    if (line.startsWith("package")) {

                        specPackage = line;

                    } else {

                        line = line.trim();

                        if (0 < line.length()) {

                            specObjectNamesVector.addElement(line);

                        }

                    }

                }

                specObjectNames = new String[specObjectNamesVector.size()];

                specObjectNamesVector.copyInto(specObjectNames);

            }

            _setFileNameRoots(specObjectNames);

            _setFileNameSuffix("Enumeration.java");

            int writer__numFiles = _getNumFiles();

            int writer__fileI = 0;

            writer__next_file: for (writer__fileI = 0; writer__fileI < writer__numFiles; writer__fileI++) {
