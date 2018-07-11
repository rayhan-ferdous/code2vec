                for (int j = 0; j < li.size(); j++) {

                    if (j == 0) str = ((Element) li.get(j)).getChild("Name").getText(); else {

                        str = str + ", " + ((Element) li.get(j)).getText();

                    }

                }

                retobj[5] = str;

                li = ((Element) datalist.get(i)).getChildren("ChronologicalSD");

                str = "";

                for (int j = 0; j < li.size(); j++) {

                    if (j == 0) str = ((Element) li.get(j)).getChild("Name").getText(); else {

                        str = str + ", " + ((Element) li.get(j)).getText();

                    }

                }

                retobj[6] = str;
