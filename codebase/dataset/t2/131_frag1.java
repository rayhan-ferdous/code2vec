                this.scanner.resetTo(end + 1, previousStart);

                try {

                    int token = this.scanner.getNextToken();

                    if (token != TerminalTokens.TokenNameWHITESPACE || this.scanner.currentPosition != previousStart) {

                        if (idx == endIdx) {

                            return nodeStart;

                        }

                        break;

                    }

                } catch (InvalidInputException e) {

                    return nodeStart;

                }

                char[] gap = this.scanner.getCurrentIdentifierSource();

                int nbrLine = 0;

                int pos = -1;

                while ((pos = CharOperation.indexOf('\n', gap, pos + 1)) >= 0) {

                    nbrLine++;

                }

                if (nbrLine > 1) {

                    break;

                }

            }
