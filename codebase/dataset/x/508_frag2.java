    public void testIndexedPropertyDescriptorStringClassStringStringStringString_IndexedRWNull() throws IntrospectionException {

        String propertyName = "PropertyFour";

        Class<MockJavaBean> beanClass = MockJavaBean.class;

        try {

            new IndexedPropertyDescriptor(propertyName, beanClass, "get" + propertyName, "set" + propertyName, null, null);

            fail("Should throw IntrospectionException.");

        } catch (IntrospectionException e) {

        }

    }
