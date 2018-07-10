            out.write("  var surl = '/xwiki/bin/view/Sandbox/?xpage=watch&do=' + action;\n");

            out.write("  var myAjax = new Ajax.Request(\n");

            out.write("    surl,\n");

            out.write("    {\n");

            out.write("      method: 'get',\n");

            out.write("      onComplete: reloadActionMenu()\n");

            out.write("    });\n");

            out.write("}\n");
