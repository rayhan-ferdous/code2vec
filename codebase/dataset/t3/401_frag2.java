        ItemSkuResult res = new ItemSkuResult();

        TaobaoSkuRequest itemSkuReq = (TaobaoSkuRequest) itemRequest;

        if (itemSkuReq == null || itemSkuReq.getShopId() == null || itemSkuReq.getNumIid() == null || StringUtil.isBlank(itemSkuReq.getProperties())) {

            logger.error("skuUpdate param error:itemSkuReq=" + itemSkuReq);

            res.setError(ResultConstants.RESULT_PARAM_NULL, ResultConstants.RESULT_PARAM_NULL_INFO);

            return res;

        }
