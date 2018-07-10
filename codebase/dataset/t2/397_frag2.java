public class DiscountModel implements ITableModel {



    protected DiscountVO fTotal;



    protected DiscountVO[] fTableData;



    protected String fSortedColumn;



    protected int[] fSortOrder;



    protected Comparator fComparator;



    public DiscountModel() {

        super();

    }



    public void setData(Object[] data) {

        fTableData = (DiscountVO[]) data;

        fSortOrder = new int[(fTableData != null) ? fTableData.length : 0];

        for (int i = 0; i < fSortOrder.length; i++) {

            fSortOrder[i] = i;

        }

    }



    public Object getValueAt(int pRow, String pColumn) {

        DiscountVO vo = fTableData[fSortOrder[pRow]];

        if ("id".equals(pColumn)) return new Long(vo.getId());
