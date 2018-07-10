        protected void removeTag(Tag tag) {

            LangElement el;

            String name = tag.name;

            Locale newLocale = null;

            for (; ; ) {

                if (tagStack.empty()) unbalanced(name);

                el = (LangElement) tagStack.pop();

                if (el.getTag().name.equals(name)) {

                    if (tagStack.empty()) {

                        newLocale = defaultLocale;

                    } else {

                        el = (LangElement) tagStack.peek();

                        newLocale = el.getLocale();

                    }

                    break;

                }

            }

            if (lastLocale == null) {

                lastLocale = newLocale;

                return;

            }

            if (newLocale == null) {

                lastLocale = newLocale;

                return;

            }

            if (!lastLocale.equals(newLocale)) {

                lastLocale = newLocale;

            }

        }
