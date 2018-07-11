            for (Iterator iter = featureToInfo[fi].keySet().iterator(); iter.hasNext(); ) {

                Object key = iter.next();

                Point2D.Double pt = (Point2D.Double) featureToInfo[fi].get(key);

                double splitPoint = ((Double) key).doubleValue();
