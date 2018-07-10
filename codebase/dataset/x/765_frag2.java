    public void refresh() {

        if (triggerFilter != null) {

            triggerFilter.updateFilter();

        }

        setTriggerFilter(triggerFilter);

    }
