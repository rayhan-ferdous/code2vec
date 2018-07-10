                if (element instanceof CounterTimer) {

                    ReferenceType xmlCTExpChannelRef = xmlMeasurementGroup.addNewCTExpChannelRef();

                    xmlCTExpChannelRef.setName(element.getName());

                } else if (element instanceof ZeroDExpChannel) {

                    ReferenceType xmlZeroDExpChannelRef = xmlMeasurementGroup.addNewZeroDExpChannelRef();

                    xmlZeroDExpChannelRef.setName(element.getName());

                }
