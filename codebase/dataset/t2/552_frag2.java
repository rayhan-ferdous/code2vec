            double[] temp = new double[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPercentage();

            }

            fSortOrder = sorter.sortDouble(temp, fSortOrder, up);

        }
