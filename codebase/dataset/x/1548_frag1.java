    protected void handleAttributeDefinition(final String name, final String attribName, final String handlerClass) throws ObjectDescriptionException {

        final PropertyInfo propertyInfo = ModelBuilder.getInstance().createSimplePropertyInfo(getPropertyDescriptor(name));

        if (propertyInfo == null) {

            throw new ObjectDescriptionException("Unable to load property " + name);

        }

        propertyInfo.setComments(new Comments(getOpenComment(), getCloseComment()));

        propertyInfo.setPropertyType(PropertyType.ATTRIBUTE);

        propertyInfo.setXmlName(attribName);

        propertyInfo.setXmlHandler(handlerClass);

        this.propertyList.add(propertyInfo);

    }
