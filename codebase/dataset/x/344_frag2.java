                    htmlViews = createHTMLVector();

                } else {

                    View v = BasicHTML.createHTMLView(tp, title);

                    htmlViews.insertElementAt(v, index);

                }

            } else {

                if (htmlViews != null) {

                    htmlViews.insertElementAt(null, index);
