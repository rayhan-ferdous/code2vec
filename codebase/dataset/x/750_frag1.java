            } else lmse.printStackTrace();

        }

        return (default_source);

    }



    /**

 * Add a source of locations to this marker

 */

    public void addLocationMarkerSource(LocationMarkerSource source) {

        if (location_sources_ == null) location_sources_ = new Vector();

        location_sources_.add(source);

    }



    /**

 * Returns the LocationMarkerSource that is able to store new

 * LocationMarker objects (make them persistent).

 *

 * @return the LocationMarkerSource that will be used to store

 * new LocationMarkers.

 */

    public LocationMarkerSource getTargetLocationMarkerSource() {

        return (location_target_);
