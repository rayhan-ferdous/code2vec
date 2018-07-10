        writer.println("		}{");

        writer.println("			dup length 4 sub 0 exch 4 exch{");

        writer.println("				1 index exch 4 getinterval aload pop");

        writer.println("				BuildRectPath");

        writer.println("			}for");

        writer.println("			pop");

        writer.println("		}ifelse");

        writer.println("} bind def\n");

        writer.println("/*RC { gsave newpath BuildRectPath fill grestore } bind def\n");

        writer.println("% install Level 2 emulations, or substitute built-in Level 2 operators");

        writer.println("/languagelevel where");

        writer.println("  {pop languagelevel}{1} ifelse");

        writer.println("2 lt {");

        writer.println("  /RC /*RC load def");

        writer.println("  /SF /*SF load def");

        writer.println("}{");

        writer.println("  /RC /rectclip load def      % use RC instead of rectclip");

        writer.println("  /SF /selectfont load def    % use SF instead of selectfont");

        writer.println("} ifelse\n");

        writer.println("%Coordinate conversion utilities");

        writer.println("/polar { %(ang rad) -> (x y)");

        writer.println("	/rad exch def		/ang exch def");

        writer.println("	/x rad ang cos mul def		/y rad ang sin mul def");

        writer.println("	x y");

        writer.println("} def\n");

        writer.println("/midang {");

        writer.println("	/inf exch def");

        writer.println("	inf 1 eq {360 2 maxlevel exp div mul -90.0 add}           %for first level male, go counter clockwise from bottom");

        writer.println("				{360 2 maxlevel exp div mul 90.0 add} ifelse     %for first level female, go clockwise from bottom");
