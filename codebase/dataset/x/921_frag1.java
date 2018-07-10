    public void onEvent(Event event) {

        Component comp = event.getTarget();

        String eventName = event.getName();

        if (eventName.equals(Events.ON_CLICK)) {

            if (comp instanceof ToolBarButton) {

                ToolBarButton btn = (ToolBarButton) comp;

                int menuId = 0;

                try {

                    menuId = Integer.valueOf(btn.getName());

                } catch (Exception e) {

                }

                if (menuId > 0) onMenuSelected(menuId);

            }

        } else if (eventName.equals(Events.ON_DROP)) {

            DropEvent de = (DropEvent) event;

            Component dragged = de.getDragged();

            if (dragged instanceof Treerow) {

                Treerow treerow = (Treerow) dragged;

                Treeitem treeitem = (Treeitem) treerow.getParent();

                favPanel.addItem(treeitem);

            }

        }

    }
