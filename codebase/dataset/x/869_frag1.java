                while (iterator.hasNext()) {

                    Element currentElement = (Element) iterator.next();

                    VersionInfo versionInfo = getVersionInfo(currentElement);

                    versionInfos.put(new Integer(count), versionInfo);

                    count++;

                }

                if (identificationElem != null) {
