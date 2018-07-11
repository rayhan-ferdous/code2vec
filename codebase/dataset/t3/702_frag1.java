        } catch (XmlException e) {

            try {

                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());

            } catch (ModuleNotFoundException m) {

                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());

            }

        } catch (XPathExpressionException e) {

            try {

                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());

            } catch (ModuleNotFoundException m) {

                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
