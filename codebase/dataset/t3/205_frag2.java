    private static UpdateResult update_minutiae_V2(MINUTIAE minutiae, MINUTIA minutia, final RectilinearScanDirection scan_dir, final int dmapval, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {

        final int qtr_ndirs = lfsparms.num_directions / 4;

        final int full_ndirs = lfsparms.num_directions * 2;

        if (minutiae.getNum() > 0) {

            for (int i = minutiae.getNum() - 1; i >= 0; i--) {

                final int dx = Math.abs(minutiae.get(i).x - minutia.x);

                if (dx < lfsparms.max_minutia_delta) {

                    final int dy = Math.abs(minutiae.get(i).y - minutia.y);

                    if (dy < lfsparms.max_minutia_delta) {

                        if (minutiae.get(i).type == minutia.type) {

                            final int delta_dir_tmp = Math.abs(minutiae.get(i).direction - minutia.direction);

                            final int delta_dir = Math.min(delta_dir_tmp, full_ndirs - delta_dir_tmp);

                            if (delta_dir <= qtr_ndirs) {

                                if ((dx == 0) && (dy == 0)) {

                                    return (UpdateResult.UPDATE_IGNORE);

                                }

                                if (Contour.search_contour(minutia.x, minutia.y, lfsparms.max_minutia_delta, minutiae.get(i).x, minutiae.get(i).y, minutiae.get(i).ex, minutiae.get(i).ey, RotationalScanDirection.SCAN_CLOCKWISE, bdata, iw, ih) || Contour.search_contour(minutia.x, minutia.y, lfsparms.max_minutia_delta, minutiae.get(i).x, minutiae.get(i).y, minutiae.get(i).ex, minutiae.get(i).ey, RotationalScanDirection.SCAN_COUNTERCLOCKWISE, bdata, iw, ih)) {

                                    if (dmapval >= 0) {

                                        final RectilinearScanDirection map_scan_dir = choose_scan_direction(dmapval, lfsparms.num_directions);

                                        if (map_scan_dir == scan_dir) {

                                            minutiae.remove(i);

                                            log.debug("removing minutia at index i = " + i);

                                        } else return (UpdateResult.UPDATE_IGNORE);

                                    } else {

                                        return (UpdateResult.UPDATE_IGNORE);

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        minutiae.add(minutia);

        return UpdateResult.UPDATE_SUCCESS;

    }
