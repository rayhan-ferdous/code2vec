    public static synchronized StockDAO getSingletonStockManager() {

        if (singletonStockDAO == null) {

            singletonStockDAO = new StockDAO();

        }

        return singletonStockDAO;

    }
