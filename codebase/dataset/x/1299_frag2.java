    public static void createDefaultPerson() {

        Organization organization = null;

        for (IElementWrapper elementWrapper : ElementUtil.getAllElementsOfType(UIType.organization)) {

            if (elementWrapper.getLabel().equals(License.getLicense().getOrganization())) {

                organization = (Organization) elementWrapper.getElement();

            }

        }

        if (organization == null) {

            organization = UIPlugin.getRootPortfolio().createOrganization(License.getLicense().getOrganization());

        }

        IElementWrapper organizationWrapper = DataCacheManager.getWrapperByElement(organization);

        License lic = License.getLicense();

        Person person = organization.createPerson(lic.getAccountName());

        person.setEMail(lic.getEmail());

        person.setFirstName(lic.getFirstName());

        person.setLastName(lic.getLastName());

        person.setLicenseId(lic.getCheckSum());

        ChangeEventFactory.startChangeRecording(RootWrapper.getInstance());

        ChangeEventFactory.addChange(RootWrapper.getInstance(), ChangeEvent.NEW_CHILD);

        ChangeEventFactory.startChangeRecording(organizationWrapper);

        ChangeEventFactory.addChange(organizationWrapper, ChangeEvent.NEW_CHILD);

        IElementWrapper personWrapper = DataCacheManager.getWrapperByElement(person);

        ChangeEventFactory.startChangeRecording(personWrapper);

        ChangeEventFactory.addChange(personWrapper, ChangeEvent.NEW_CHILD);

        try {

            assert (UIPlugin.getDataSource().getAuthenticatedPerson() != null);

        } catch (AuthenticationException e) {

            e.printStackTrace();

            return;

        }

    }
