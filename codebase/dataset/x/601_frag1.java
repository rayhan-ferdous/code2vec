    private void initialNcml(InputStream in) throws ParserConfigurationException, IOException, SAXException {

        try {

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputSource source = new InputSource(in);

            initialDocument = builder.parse(source);

            in.close();

            XPathFactory factory = XPathFactory.newInstance();

            XPath xpath = factory.newXPath();

            String sStartTime = xpath.evaluate("//netcdf/StartDate/text()", initialDocument);

            String sEndTime = xpath.evaluate("//netcdf/EndDate/text()", initialDocument);

            String sppd = xpath.evaluate("//netcdf/IntervalsPerDay/text()", initialDocument);

            int ppd = Integer.parseInt(sppd);

            parameterPpd = ppd;

            DatumRange dr0 = DatumRangeUtil.parseTimeRangeValid(sStartTime);

            DatumRange dr1 = DatumRangeUtil.parseTimeRangeValid(sEndTime);

            parameterRange = new DatumRange(dr0.min(), dr1.max());

        } catch (XPathExpressionException ex) {

            Logger.getLogger(TsdsDataSource.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
