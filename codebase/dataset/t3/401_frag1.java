        ItemResult res = new ItemResult();

        TaobaoSkuRequest itemSkuReq = (TaobaoSkuRequest) itemRequest;

        if (itemSkuReq == null || itemSkuReq.getShopId() == null || itemSkuReq.getNumIid() == null || itemSkuReq.getQuantity() == null) {

            logger.error("quantityUpdate param error:itemSkuReq=" + itemSkuReq);

            res.setError(ResultConstants.RESULT_PARAM_NULL, ResultConstants.RESULT_PARAM_NULL_INFO);

            return res;

        }
