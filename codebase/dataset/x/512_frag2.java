            logger.error("templatesGet param error:itemReq=" + itemReq);

            res.setError(ResultConstants.RESULT_PARAM_NULL, ResultConstants.RESULT_PARAM_NULL_INFO);

            return res;

        }

        ItemTemplatesGetRequest req = new ItemTemplatesGetRequest();

        String sessionKey = commonCache.getSessionKey(itemReq.getShopId());

        if (StringUtil.isBlank(sessionKey)) {
