        Object addedObject = parentController.getChildPropertyControllerMap().addPropertyInstance(key, null, retVal);

        AssertUtility.assertSame(retVal, addedObject);

        return retVal;

    }



    public PropertyCollectionController createController(PropertyCollectionController parentController, PropertyCollection model) {

        if (model == null) {

            throw new NullPointerException();

        }

        Class controllerClass = (Class) _controllerCreationMap.get(model.getType());
