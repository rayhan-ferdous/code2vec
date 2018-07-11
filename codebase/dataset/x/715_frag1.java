            String address = InetAddress.getLocalHost().getHostAddress();

            char[] password = user.toCharArray();

            CerberoAuthRequest request = new CerberoAuthRequest(user, address);

            CerberoAuthReply reply = (CerberoAuthReply) connector.login(request);

            PasswordEncryptedObject ticket = reply.getLoginTicket();
