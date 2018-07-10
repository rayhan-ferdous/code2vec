    public Element requestDeleteItem(String id, boolean soft) throws IOException, SoapRequestException {

        if (!init()) {

            return null;

        }

        Document request = createZimbraCall(ZConst.ITEM_ACTION_REQUEST, ZConst.URN_ZIMBRA_MAIL);

        Element deleteRequest = (Element) soapHelper.getBody(request).elements().get(0);

        {

            Element action = documentFactory.createElement(ZConst.E_ACTION);

            {

                action.addAttribute(ZConst.A_ID, id);

                action.addAttribute(ZConst.A_OPERATION, soft ? ZConst.OP_TRASH : ZConst.OP_DELETE);

            }

            deleteRequest.add(action);

        }

        Document response = sendRequest(request);

        Element contactsResponse = soapHelper.getBody(response).element(ZConst.ITEM_ACTION_RESPONSE);

        ccontext.processContext(soapHelper.getContext(response));

        return contactsResponse;

    }
