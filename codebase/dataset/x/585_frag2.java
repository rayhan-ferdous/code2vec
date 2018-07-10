    private static void registerOtherFields(DynamicJasperDesign jd, List fields) {

        for (Iterator iter = fields.iterator(); iter.hasNext(); ) {

            ColumnProperty element = (ColumnProperty) iter.next();

            JRDesignField field = new JRDesignField();

            field.setValueClassName(element.getValueClassName());

            field.setName(element.getProperty());

            try {

                jd.addField(field);

            } catch (JRException e) {

                log.warn(e.getMessage(), e);

            }

        }

    }
