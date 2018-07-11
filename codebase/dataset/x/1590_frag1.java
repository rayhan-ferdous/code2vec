import org.processmining.framework.ui.ComponentFrame;

import org.processmining.framework.ui.MainUI;

import org.processmining.framework.ui.Message;



/**

 * The UI that provides the possibility to create redesigns for a given

 * high-level Petri net. The redesign is done step by step by transforming a

 * certain process part with a certain redesign type. In this way, gradually a

 * tree of redesign alternatives is created. The performance of the original and

 * the alternative models can be evaluated with simulation.

 * 

 * @see HLPetriNet

 * 

 * @author Mariska Netjes

 */

public class RedesignAnalysisUI extends JPanel implements Provider {
