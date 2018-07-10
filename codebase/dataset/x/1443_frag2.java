                    for (int k = 0; k < rowNumber; k++) {

                        input[k] = st.nextToken().trim();

                        if (!input[k].equalsIgnoreCase(MISSING_MARK)) {

                            xList[k].add(input[k]);

                        }

                    }

                } catch (NoSuchElementException e) {

                    System.out.println(Utility.getErrorMessage("Chi Square Contingency Table"));
