            out.write("No comments for this document\n");

            out.write("      <form action=\"/xwiki/bin/commentadd/Sandbox/WebHome\" method=\"post\">\n");

            out.write("        <fieldset class=\"expanded\" id=\"commentform\">\n");

            out.write("        <legend onclick=\"toggleForm(this.form)\">Add Comment<span class=\"expands\">...</span></legend>\n");

            out.write("        <input type=\"hidden\" name=\"xredirect\" value=\"/xwiki/bin/view/Sandbox/\" />\n");

            out.write("        <input type=\"hidden\" name=\"XWiki.XWikiComments_author\" value=\"XWiki.dieisii\"/>\n");

            out.write("        <input type=\"hidden\" name=\"XWiki.XWikiComments_date\" value=\"\"/>\n");

            out.write("        <div><textarea id='XWiki.XWikiComments_comment' rows='5' cols=\"80\" name='XWiki.XWikiComments_comment' style=\"width: 100%;\"></textarea></div>\n");

            out.write("        <div>\n");

            out.write("        <span class=\"buttonwrapper\"><input type=\"submit\" value=\"Add Comment\" class=\"button\"/></span>\n");

            out.write("        <span class=\"buttonwrapper\"><input type=\"reset\" value=\"Cancel\" onclick=\"hideForm(this.form);\" class=\"button\"/></span>\n");

            out.write("        </div>\n");

            out.write("        </fieldset>\n");

            out.write("      </form>\n");

            out.write("    </div>   </div>       <script type=\"text/javascript\">\n");

            out.write("//<![CDATA[\n");

            out.write("if(document.getElementById(\"commentform\")) {\n");

            out.write("  document.getElementById(\"commentform\").className = \"collapsed\";\n");

            out.write("}\n");

            out.write("document.getElementById(\"comments\").className += \" hidden\";\n");

            out.write("//]]>\n");

            out.write("      </script>\n");

            out.write("\n");

            out.write("    <div id=\"attachmentscontent\" class=\"xwikiintracontent\">\n");

            out.write("    <div id=\"attw\">\n");

            out.write("    <div class=\"xwikititlewrapper\" onclick=\"toggleClass($('attachments'), 'hidden'); toggleClass($('attachmentscontent'), 'exp')\"><h3 class=\"xwikiintratitle\">Attachments<span class=\"hidden\">:</span> <span class=\"attachmentsno\">0</span></h3></div>\n");

            out.write("      <div id=\"attachments\">\n");

            out.write("<div>No attachments for this document</div>\n");

            out.write("      <form action=\"/xwiki/bin/upload/Sandbox/WebHome\" enctype=\"multipart/form-data\" method=\"post\">\n");

            out.write("<div>\n");

            out.write("<input type=\"hidden\" name=\"xredirect\" value=\"http://172.16.6.129:8180/xwiki/bin/view/Sandbox/\" />\n");

            out.write("<fieldset class=\"expanded\" id=\"attachform\">\n");

            out.write("        <legend onclick=\"toggleForm(this.form)\">Add an attachment<span class=\"expands\">...</span></legend>\n");

            out.write("     <!--  <div><label id=\"xwikiuploadnamelabel\" for=\"xwikiuploadname\">Choose the target file name:</label></div> -->\n");

            out.write("        <div><input id=\"xwikiuploadname\" type=\"hidden\" name=\"filename\" value=\"\" size=\"40\"/></div>\n");

            out.write("        <div><label id=\"xwikiuploadfilelabel\" for=\"xwikiuploadfile\">Choose file to upload:</label></div>\n");

            out.write("        <div><input id=\"xwikiuploadfile\" type=\"file\" name=\"filepath\" value=\"\" size=\"40\"/></div>\n");

            out.write("        <div>\n");

            out.write("        <span class=\"buttonwrapper\"><input type=\"submit\" value=\"Attach this file\" onclick=\"return updateAttachName(this.form, 'Do you want to replace the filename with')\" class=\"button\"/></span>\n");

            out.write("        <span class=\"buttonwrapper\"><input type=\"reset\" value=\"Cancel\" onclick=\"hideForm(this.form);\" class=\"button\"/></span>\n");

            out.write("        </div>\n");

            out.write("        </fieldset>\n");

            out.write("</div>\n");

            out.write("      </form>\n");

            out.write("      </div>       </div>     </div> <script type=\"text/javascript\">\n");

            out.write("//<![CDATA[\n");

            out.write("document.getElementById(\"attachform\").className = \"collapsed\";\n");

            out.write("document.getElementById(\"attachments\").className = \"hidden\";\n");

            out.write("//]]>\n");

            out.write("</script>\n");

            out.write("\n");

            out.write("</div> </div>  \n");

            out.write("<div class=\"clearfloats\"></div>\n");

            out.write("</div>    </div>\n");

            out.write("  </div><div id=\"rightPanels\" class=\"panels right\">\n");

            out.write("                  <div class=\"panel expanded Search\">\n");

            out.write("<h5 class=\"xwikipaneltitle hidden\" onclick=\"togglePanelVisibility(this.parentNode);\">Search</h5>\n");

            out.write("<div class=\"xwikipanelcontents\">\n");

            out.write("  <form action=\"/xwiki/bin/view/Main/WebSearch\">\n");

            out.write("    <div id=\"globalsearch\">\n");

            out.write("      <input id=\"globalsearchinput\" type=\"text\" name=\"text\" value=\"search...\" size=\"15\" onfocus=\"if (this.value == 'search...') value=''; this.select();\" onblur=\"if (this.value == '') value='search...'; this.blur()\"/>\n");

            out.write("&nbsp;\n");

            out.write("      <input class=\"button\" value=\"Go\" type=\"image\" src=\"/xwiki/skins/albatross/go.png\"/>\n");

            out.write("    </div>\n");

            out.write("  </form>\n");

            out.write("</div>\n");

            out.write("</div>\n");

            out.write("                        <div class=\"panel expanded QuickLinks\">\n");

            out.write("<h5 class=\"xwikipaneltitle\" onclick=\"if(eltHasClass(this.parentNode, 'expanded')) createCookie('XWiki.dieisii_Panels.QuickLinks','collapsed', ''); else eraseCookie('XWiki.dieisii_Panels.QuickLinks'); togglePanelVisibility(this.parentNode);\">Quick Links</h5>\n");

            out.write("<div class=\"xwikipanelcontents\">\n");

            out.write("<ul class=\"star\">\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Main/\">Home</a></span></li>\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Main/AllDocs\">Index</a></span></li>\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Main/Dashboard\">What's New</a></span> <a href=\"/xwiki/bin/view/Main/RssFeeds\"><img src=\"/xwiki/skins/albatross/icons/black%2Drss%2Dmini3.png\" title=\"RSS Feeds\" alt=\"RSS Feeds\"/></a></li>\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Blog/\">Blog</a></span></li>\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Main/EventCalendar\">Calendar</a></span></li>\n");

            out.write("<li><span class=\"wikilink\"><a href=\"/xwiki/bin/view/Photos/\">Photo Albums</a></span></li>\n");
