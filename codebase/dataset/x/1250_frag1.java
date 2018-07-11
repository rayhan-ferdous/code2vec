    @SuppressWarnings("unchecked")

    public ArrayList<String> getTransactionIdentifierList() {

        ArrayList<String> resultList = new ArrayList<String>();

        try {

            ZipFile zipFile = new ZipFile(new File(this.zipFilePath), ZipFile.OPEN_READ);

            Enumeration zipFileEntries = zipFile.entries();

            while (zipFileEntries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

                String name = entry.getName();

                resultList.add(ArchiveFileSupport.extractIdentifier(name));

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return resultList;

    }
