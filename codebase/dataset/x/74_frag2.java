    public static List FindAgentsInContext(String contextPath, String sqlQuery, Object functionHandler) {

        try {

            org.josql.Query model = new org.josql.Query();

            model.setClassLoader(RepastEssentials.class.getClassLoader());

            model.addFunctionHandler(functionHandler);

            model.parse(sqlQuery);

            Context context = FindContext(contextPath);

            Class fromClass = model.getFromObjectClass();

            ArrayList list = new ArrayList();

            for (Object obj : context.getObjects(fromClass)) {

                if ((obj != null) && (fromClass.isAssignableFrom(obj.getClass()))) {

                    list.add(obj);

                }

            }

            QueryResults results = model.execute(list);

            return list;

        } catch (QueryParseException e) {

            e.printStackTrace();

        } catch (QueryExecutionException e) {

            e.printStackTrace();

        }

        return null;

    }
