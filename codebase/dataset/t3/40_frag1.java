                    final List paramList = propertiesElement.getChildren(XAwareConstants.ELEMENT_PROPERTY, ns);

                    if (paramList.size() > 0) {

                        final Vector<Vector<String>> dataVec = new Vector<Vector<String>>();

                        for (int i = 0; i < paramList.size(); i++) {

                            final Vector<String> rowVec = new Vector<String>();

                            final Element paramElem = (Element) paramList.get(i);

                            rowVec.add(paramElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, ns));

                            rowVec.add(paramElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_VALUE, ns));

                            dataVec.add(rowVec);

                        }

                        createJNDIPropsPan.setParametersData(dataVec, useExistingBtn.getSelection());

                        useExistJNDIPropsPan.setParametersData(dataVec, useExistingBtn.getSelection());

                    } else {

                        final Vector dataVec = new Vector();

                        createJNDIPropsPan.setParametersData(dataVec, useExistingBtn.getSelection());

                        useExistJNDIPropsPan.setParametersData(dataVec, useExistingBtn.getSelection());

                    }

                }
