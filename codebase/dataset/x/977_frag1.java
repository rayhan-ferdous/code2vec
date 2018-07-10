    public void testAdd_Jar_Directory_File() throws IOException {

        createAdditionalDirectory();

        _nextContentFile = File.createTempFile("nextContent.file", "");

        _nextContentFile.createNewFile();

        assertTrue(_nextContentFile.exists());

        StandalonePackager packager = new StandalonePackager(_targetJarFile);

        try {

            packager.addJarFile(_sourceJarFile);

            packager.addFullDirectory(_additionalDir);

            packager.addFile(_nextContentFile, null);

        } finally {

            packager.close();

        }

        assertTrue(_targetJarFile.exists());

        Map<String, String> mandatoryEntries = new HashMap<String, String>(8);

        mandatoryEntries.put(_contentDir.getName(), "dir");

        mandatoryEntries.put(_contentFile.getName(), "file");

        mandatoryEntries.put(_nextContentFile.getName(), "file");

        mandatoryEntries.put(_additionalDir.getName(), "dir");

        mandatoryEntries.put(_additionalFile.getName(), "file");

        mandatoryEntries.put("META-INF", "dir");

        mandatoryEntries.put("MANIFEST.MF", "file");

        JarFile targetJarFile = new JarFile(_targetJarFile);

        try {

            Enumeration<JarEntry> entries = targetJarFile.entries();

            while (entries.hasMoreElements()) {

                JarEntry entry = (JarEntry) entries.nextElement();

                String name;

                String entryName = entry.getName();

                int slashIndex = entryName.indexOf("/");

                if (slashIndex >= 0) {

                    name = entryName.substring(slashIndex + 1);

                    String dirName = entryName.substring(0, slashIndex);

                    assertTrue(mandatoryEntries.containsKey(dirName));

                    assertEquals("dir", mandatoryEntries.get(dirName));

                    mandatoryEntries.remove(dirName);

                } else {

                    name = entryName;

                }

                if (mandatoryEntries.containsKey(name)) {

                    assertEquals("file", (String) mandatoryEntries.get(name));

                    assertFalse(entry.isDirectory());

                    mandatoryEntries.remove(name);

                }

            }

            assertTrue(mandatoryEntries.size() == 0);

            assertNotNull(targetJarFile.getManifest());

        } finally {

            targetJarFile.close();

        }

    }
