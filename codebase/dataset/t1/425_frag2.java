                    Object o = unmarshaller.unmarshal(source);

                    if (o instanceof JAXBElement<?>) {

                        o = ((JAXBElement<?>) o).getValue();

                    }

                    if (o instanceof Module) {

                        modules.getModule().add((Module) o);

                    } else {
