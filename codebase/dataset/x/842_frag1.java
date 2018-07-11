import java.io.OutputStream;

import java.io.Reader;

import java.io.Writer;

import org.restlet.data.MediaType;

import org.restlet.engine.io.BioUtils;

import org.restlet.engine.io.NioUtils;



/**

 * Representation based on a NIO byte channel.

 * 

 * @author Jerome Louvel

 */

public abstract class ChannelRepresentation extends Representation {



    /**

     * Constructor.

     * 

     * @param mediaType

     *            The media type.

     */

    public ChannelRepresentation(MediaType mediaType) {
