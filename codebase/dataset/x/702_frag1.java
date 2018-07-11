    public RaciInfoLine getRaci(Integer raciObjectId, Integer userId) throws Exception {

        Call call = (Call) SoapTestCase.service.createCall();

        call.setTargetEndpointAddress(soapEndPoint + "Raci");

        call.setOperationName("getRaci");

        call.addParameter("raciObjectId", XMLType.XSD_INT, ParameterMode.IN);

        call.addParameter("userId", XMLType.XSD_INT, ParameterMode.IN);

        registerTypeMappingForRaciException(call);

        registerEnum(call);

        QName qn = getRaciQn();

        call.registerTypeMapping(com.entelience.objects.raci.RaciInfoLine.class, qn, new BeanSerializerFactory(com.entelience.objects.raci.RaciInfoLine.class, qn), new BeanDeserializerFactory(com.entelience.objects.raci.RaciInfoLine.class, qn));

        call.setReturnType(qn);

        return (RaciInfoLine) call.invoke(new Object[] { raciObjectId, userId });

    }
