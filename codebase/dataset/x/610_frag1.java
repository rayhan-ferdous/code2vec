    public void subscribe() {

        Group g = null;

        if (allGroupList.isShowing()) {

            g = (Group) allGroupList.getSelectedValue();

        } else if (newGroupList.isShowing()) {

            g = (Group) newGroupList.getSelectedValue();

        } else if (subscribedGroupList.isShowing()) {

            g = (Group) subscribedGroupList.getSelectedValue();

        }

        if (g != null) {

            HibernateUtils.subscribe(g, true);

            ((GroupListModel) subscribedGroupList.getModel()).addElement(g);

        }

    }
