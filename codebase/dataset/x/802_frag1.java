                        if (loc.equals("")) addGlosEntry(src); else addGlosEntry(src, loc);

                    }

                } else {

                    System.out.println(OStrings.CT_DONT_RECOGNIZE_GLOS_FILE + fname);

                }

            }

        } else {

            throw new IOException("can't access glossary directory");

        }

        m_ignoreList = new ArrayList(32);

        String ignoreName = m_config.getInternal() + OConsts.IGNORE_LIST;
