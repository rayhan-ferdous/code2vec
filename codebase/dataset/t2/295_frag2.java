                        for (j = 0; j < pN.size(); j++) {

                            Security s = (Security) pN.get(j);

                            String security = nextLine[3];

                            String secName = s.getName();

                            if (security.equals(secName)) {

                                found = true;

                                s.subMore(nextLine[3], nextLine[4], nextLine[5], nextLine[0]);

                            }

                        }

                        if (found == false) {

                            pN.add(new Security(nextLine[3], nextLine[4], nextLine[5], nextLine[0]));

                        }

                    }
