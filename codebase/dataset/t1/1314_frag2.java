            final String cName = t.getCanonicalName();

            final boolean isTimestamp = "java.sql.Timestamp".equals(cName);

            final boolean isDouble = "java.lang.Double".equals(cName);

            final boolean isInteger = "java.lang.Integer".equals(cName);

            final boolean isFloat = "java.lang.Float".equals(cName);

            final boolean isShot = "java.lang.Short".equals(cName);

            final boolean isLong = "java.lang.Long".equals(cName);

            final boolean isBigDecimal = "class java.math.BigDecimal".equals(cName);

            final boolean isString = "java.lang.String".equals(cName);
