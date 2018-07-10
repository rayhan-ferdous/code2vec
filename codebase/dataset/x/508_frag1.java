    public void testIndexedPropertyDescriptorStringClass_PropertyNameInvalid() throws IntrospectionException {

        String propertyName = "Not a property";

        Class<MockJavaBean> beanClass = MockJavaBean.class;

        try {

            new IndexedPropertyDescriptor(propertyName, beanClass);

            fail("Should throw IntrospectionException");

        } catch (IntrospectionException e) {

        }

    }
