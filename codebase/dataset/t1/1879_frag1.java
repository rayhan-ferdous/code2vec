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
