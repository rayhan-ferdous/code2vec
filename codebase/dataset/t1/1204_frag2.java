    public Object getAdapter(Class type) {

        if (type == IShowInTargetList.class) {

            return new IShowInTargetList() {



                public String[] getShowInTargetIds() {

                    return new String[] { ProjectExplorer.VIEW_ID };

                }

            };

        }

        return super.getAdapter(type);

    }
