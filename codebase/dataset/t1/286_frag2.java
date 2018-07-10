    protected PageCounter countModuleReports(Integer moduleId) throws Exception {

        Logs.logMethodName();

        Call call = (Call) SoapTestCase.service.createCall();

        call.setTargetEndpointAddress(getEndPoint() + "Module");

        call.setOperationName("countModuleReports");

        call.setOperationStyle("rpc");

        call.addParameter("moduleId", XMLType.XSD_INT, ParameterMode.IN);

        QName qn = new QName("urn:com.entelience.soap.soapModule", "PageCounter");

        call.registerTypeMapping(com.entelience.objects.PageCounter.class, qn, new BeanSerializerFactory(com.entelience.objects.PageCounter.class, qn), new BeanDeserializerFactory(com.entelience.objects.PageCounter.class, qn));

        call.setReturnType(qn);

        PageCounter pc = (PageCounter) call.invoke(new Object[] { moduleId });

        return pc;

    }
