    public ZipInputStreamZipEntrySource(ZipInputStream inp) throws IOException {

        zipEntries = new ArrayList<FakeZipEntry>();

        boolean going = true;

        while (going) {

            ZipEntry zipEntry = inp.getNextEntry();

            if (zipEntry == null) {

                going = false;

            } else {

                FakeZipEntry entry = new FakeZipEntry(zipEntry, inp);

                inp.closeEntry();

                zipEntries.add(entry);

            }

        }

        inp.close();

    }
