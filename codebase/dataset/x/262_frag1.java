    public boolean join(String channel) {

        methods.game.openTab(Game.TAB_CLAN);

        if (isInChannel()) {

            if (!leave()) {

                return false;

            }

        }

        methods.interfaces.getComponent(INTERFACE_CLAN_CHAT, INTERFACE_CLAN_CHAT_BUTTON_JOIN).doClick();

        sleep(random(200, 400));

        if (methods.interfaces.get(INTERFACE_JOIN_CLAN_CHAT).isValid()) {

            lastChannel = methods.interfaces.getComponent(INTERFACE_JOIN_CLAN_CHAT, INTERFACE_JOIN_CLAN_CHAT_LAST_CHANNEL).getText();

            methods.keyboard.sendText(channel, true);

            sleep(random(1550, 1800));

            if (isInChannel()) {

                lastChannel = channel;

                return true;

            }

        }

        return false;

    }
