                PropertyValueXMLFactory.newInstance().addType(name.getSimpleName(), (PropertyValueXML) name.newInstance());

            } catch (InstantiationException ex) {

                Logger.getLogger(DynamicLoader.class.getName()).log(Level.SEVERE, "Failed to create a '" + name.getSimpleName() + "' object", ex);

            } catch (IllegalAccessException ex) {

                Logger.getLogger(DynamicLoader.class.getName()).log(Level.SEVERE, "Failed to create a '" + name.getSimpleName() + "' object", ex);

            }

        }
