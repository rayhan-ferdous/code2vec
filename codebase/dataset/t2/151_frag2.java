        fTableData = (ResourceVO[]) data;

        fSortOrder = new int[(fTableData != null) ? fTableData.length : 0];

        for (int i = 0; i < fSortOrder.length; i++) {

            fSortOrder[i] = i;

        }

    }
