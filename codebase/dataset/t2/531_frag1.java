            for (Iterator it = parentView.getSourceEdges().iterator(); it.hasNext(); ) {

                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);

                if (elements.size() == element2ViewMap.size()) return element2ViewMap;

            }

            return element2ViewMap;