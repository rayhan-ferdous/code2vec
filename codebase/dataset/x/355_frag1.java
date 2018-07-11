                } else if (element instanceof ZeroDExpChannel) {

                    ReferenceType xmlZeroDExpChannelRef = xmlMeasurementGroup.addNewZeroDExpChannelRef();

                    xmlZeroDExpChannelRef.setName(element.getName());

                } else if (element instanceof OneDExpChannel) {

                    ReferenceType xmlOneDExpChannelRef = xmlMeasurementGroup.addNewOneDExpChannelRef();

                    xmlOneDExpChannelRef.setName(element.getName());

                } else if (element instanceof TwoDExpChannel) {
