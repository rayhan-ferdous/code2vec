    public static void selectUnread() {

        items = new ArrayList<Item>();

        for (Channel channel : visibleChannels) {

            if (channel.getUnreadItemsCount() > 0) {

                for (Item item : channels.getItems(channel.getId())) {

                    if (!item.isRead()) {

                        items.add(item);

                    }

                }

            }

        }

        Collections.sort(items, itemComparator);

        currentFilter = "unread";

    }
