        String productModel;

        String productName;

        String productCategory;

        String productNet;

        String productGross;

        String productVatPercent;

        String productVatName;

        String productDescription;

        String productImage;

        String pictureName;

        String productQuantity;

        String productEAN;

        String productQUnit;

        int productID;

        NamedNodeMap attributes = productNode.getAttributes();

        productNet = getAttributeAsString(attributes, "net");

        productGross = getAttributeAsString(attributes, "gross");
