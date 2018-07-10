        if (special != null) {

            _stk.push(special);

        } else {

        }

    }



    /**

	 * Evalulate the string and return the object after evaluation, typical

	 * objects returned include Boolean, Pair, or Symbol.

	 * 

	 * @param _inscheme String to evaluate

	 * @see #getStringInput(String)

	 * @see #runEval(String)     

	 */

    public Object objectStringInput(String _inscheme) throws SchemeException {

        incomingDataLen += _inscheme.length();

        incomingLinesIndex++;

        InputPort inp = new InputPort(new StringReader(_inscheme));

        StringWriter _strOut = new StringWriter();

        PrintWriter _outWriter = new PrintWriter(new BufferedWriter(_strOut));
