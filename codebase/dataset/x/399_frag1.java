                if ((tokenizer.ttype == EnhancedStreamTokenizer.TT_WORD) || (tokenizer.ttype == DBConstants.quoteChar)) str = tokenizer.sval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) tx = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) ty = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));
