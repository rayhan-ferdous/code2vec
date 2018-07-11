    @SuppressWarnings("unchecked")

    public void addColumnModel(MetaRelation2Many column) {

        UjoProperty property = column.getProperty();

        MetaRelation2Many oldColumn = findColumnModel(property);

        if (oldColumn == null) {

            propertyMap.put(property, column);

        } else {

            final Class oldType = oldColumn.getTableClass();

            final Class newType = column.getTableClass();

            if (newType.isAssignableFrom(oldType)) {

                propertyMap.put(property, column);

            }

        }

    }
